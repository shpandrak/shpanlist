package com.shpandrak.shpanlist.web.pages;

import com.shpandrak.datamodel.field.EntityKey;
import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.shpanlist.model.ListTemplate;
import com.shpandrak.shpanlist.model.ListTemplateItem;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;
import com.shpandrak.shpanlist.services.ListTemplateService;
import com.shpandrak.shpanlist.web.HtmlBindingResponse;
import com.shpandrak.shpanlist.web.HtmlRefreshResponse;
import com.shpandrak.shpanlist.web.HtmlResponsePrinter;
import com.shpandrak.shpanlist.web.UserMustSignInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/12/13
 * Time: 15:06
 */
public class ListTemplatePageServlet extends BasePageServlet {

    @Override
    public boolean drawPage(HttpServletRequest request, HttpServletResponse response, StringBuilder sb, LoggedInUser loggedInUser) throws IOException, PersistenceException {
        String pathInfo = request.getPathInfo();
        if (!pathInfo.startsWith("/")) {
            response.sendRedirect("/");
            return false;
        }
        String listTemplateIdString = pathInfo.substring(1);
        if (listTemplateIdString == null || listTemplateIdString.isEmpty()) {
            response.sendRedirect("/");
            return false;
        }

        EntityKey listTemplateId = ListTemplate.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateIdString);

        ListTemplate listTemplateFull = ListTemplateService.getListTemplateFull(listTemplateId);


        sb.append("<head>\n" +
                "\t<title>Shpanlist - Edit List Template</title>\n").append(
                getHeaderConstants());

        sb.append("\t<script type=\"text/javascript\">\n" +
                "\n" +
                "\tfunction onLoad() {\n" +
                "\tListTemplatePageView.listTemplateId = '").append(listTemplateIdString).append("';\n" +
                "\t}\n" +
                "\t</script>\n" +
                "</head>");


        sb.append(
                "<body onload=\"onLoad()\">\n" +
                        "<div id=\"pageListTemplate\" data-role=\"page\">\n" +
                        "    <div data-role=\"header\"><h2 id =\"pageListTemplateHeader\">").append(
                listTemplateFull.getName()).append("</h2>\n" +
                "    </div>\n" +
                "    <div data-role=\"content\">\n" +
                "        List Name:<input id=\"txtListName\" type=\"text\" value=\"").append(listTemplateFull.getName()).append("\">\n" +
                "        <input type=\"button\" value=\"Update List Name\" onclick=\"ListTemplatePageView.updateName(txtListName.value)\" />\n" +
                "        <table id=\"tablistTemplateItems\" data-role=\"table\" class=\"ui-responsive table-stroke\">\n");

        appendItemsTableBody(sb, listTemplateFull);

        sb.append(
                "        </table>\n" +
                        "    <br/>\n" +
                        "    <input value=\"Add Item\" type=\"button\" data-icon=\"plus\" onclick=\"ShpanlistController.menuAddNewListTemplateItem('").append(listTemplateIdString).append("')\" />\n" +
                        "    <br/>\n" +
                        "    <a href=\"javascript:ListTemplatePageView.createListFromTemplate()\">Create list from this template</a>\n" +
                "    </div>\n" +
                "    <div data-role=\"footer\">\n" +
                "        <a data-icon=\"arrow-l\" href=\"javascript:$.mobile.back();\"> Back</a>\n" +
                "        <a href='javascript:ShpanlistController.menuHome()' data-icon=\"home\">Home</a>\n" +
                "        <a href='javascript:ShpanlistController.signOut()'>Sign Out</a>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>");
        return true;
    }

    private void appendItemsTableBody(StringBuilder sb, ListTemplate listTemplateFull) throws IOException {
        sb.append(
                "            <thead>\n" +
                        "            <tr>\n" +
                        "                <th>Item</th><th>Description</th><th>Amount</th><th>Actions</th>\n" +
                        "            </tr>\n" +
                        "            </thead>\n" +
                        "            <tbody>\n");

        int idx = 0;
        List<ListTemplateItem> listTemplateItems = listTemplateFull.getListTemplateItemRelationship().getTargetEntities();
        for (ListTemplateItem currlistTemplateItem : listTemplateItems) {

            appendlistTemplateItemListElement(sb, currlistTemplateItem, idx == 0, idx == listTemplateItems.size() - 1);
            ++idx;
        }


        sb.append(
                "</tbody>\n");
    }


    private void appendlistTemplateItemListElement(Appendable writer, ListTemplateItem currlistTemplateItem, boolean first, boolean lastItem) throws IOException {
        writer.append(
                "<tr><td>").append(currlistTemplateItem.getName()).append("</td>\n" +
                "<td>").append(currlistTemplateItem.getDescription()).append("</td>\n" +
                "<td>").append(currlistTemplateItem.getDefaultAmount() == null ? "" : String.valueOf(currlistTemplateItem.getDefaultAmount())).append("</td>\n" +
                "<td><a HREF=\"javascript:ListTemplatePageView.removeItem('").append(currlistTemplateItem.getId().toString()).append("')\">Remove</a>");

        if (!lastItem) {
            writer.append("&nbsp;<img src=\"/images/down.png\" onclick=\"ListTemplatePageView.pushItemDown('").append(currlistTemplateItem.getId().toString()).append("')\"/>");
        }

        if (!first) {
            writer.append("&nbsp;<img src=\"/images/up.png\" onclick=\"ListTemplatePageView.pushItemUp('").append(currlistTemplateItem.getId().toString()).append("')\"/>");
        }

        writer.append("</td></tr>");
    }


    public HtmlResponsePrinter doItPlease(HttpServletRequest request, HttpServletResponse response, LoggedInUser loggedInUser, String what) throws IOException, PersistenceException, UserMustSignInException {
        if ("updateListTemplateName".equals(what)) {
            return updatelistTemplateName(loggedInUser, request, response);
        } else if ("pushListTemplateItemUp".equals(what)) {
            return pushlistTemplateItemUp(loggedInUser, request, response);
        } else if ("pushListTemplateItemDown".equals(what)) {
            return pushlistTemplateItemDown(loggedInUser, request, response);
        } else if ("removeListTemplateItem".equals(what)) {
            return removelistTemplateItem(loggedInUser, request, response);
        } else {
            throw new IllegalArgumentException("Invalid action " + what);
        }
    }

    private HtmlResponsePrinter removelistTemplateItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException, IOException {
        String listTemplateId = request.getParameter("listTemplateId");
        String listTemplateItemId = request.getParameter("listTemplateItemId");
        ListTemplateService.removeListTemplateItem(ListTemplateItem.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateItemId));

        //todo: return a removeFromDOM html refresh command...
        return getTableRefreshResponsePrinter(listTemplateId);
    }

    private HtmlResponsePrinter updatelistTemplateName(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException, IOException {
        String listTemplateId = request.getParameter("listTemplateId");
        String listTemplateName = request.getParameter("listTemplateName");

        ListTemplateService.updateListTemplateName(ListTemplate.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateId), listTemplateName);

        return new HtmlResponsePrinter(
                Arrays.asList(new HtmlBindingResponse("#pageListTemplateHeader", listTemplateName)),
                Collections.<HtmlRefreshResponse>emptyList());
    }

    private HtmlResponsePrinter pushlistTemplateItemUp(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException, IOException {
        String listTemplateId = request.getParameter("listTemplateId");
        String listTemplateItemId = request.getParameter("listTemplateItemId");
        ListTemplateService.pushListTemplateItemUp(ListTemplateItem.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateItemId));

        return getTableRefreshResponsePrinter(listTemplateId);
    }

    private HtmlResponsePrinter pushlistTemplateItemDown(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException, IOException {
        String listTemplateId = request.getParameter("listTemplateId");
        String listTemplateItemId = request.getParameter("listTemplateItemId");
        ListTemplateService.pushListTemplateItemDown(ListTemplateItem.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateItemId));

        return getTableRefreshResponsePrinter(listTemplateId);
    }

    private HtmlResponsePrinter getTableRefreshResponsePrinter(String listTemplateId) throws PersistenceException, IOException {
        ListTemplate listTemplateFull = ListTemplateService.getListTemplateFull(ListTemplate.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateId));
        StringBuilder sb = new StringBuilder();
        appendItemsTableBody(sb, listTemplateFull);
        //todo:proper encoding

        return new HtmlResponsePrinter(
                Arrays.asList(new HtmlBindingResponse("#tablistTemplateItems", encodeHtmlForJSONTransport(sb))),
                Arrays.asList(new HtmlRefreshResponse("#tablistTemplateItems", "table")));
    }


}
