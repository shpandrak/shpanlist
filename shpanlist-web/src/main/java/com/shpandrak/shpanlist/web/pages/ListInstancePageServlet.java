package com.shpandrak.shpanlist.web.pages;

import com.shpandrak.datamodel.field.EntityKey;
import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.shpanlist.model.ListInstance;
import com.shpandrak.shpanlist.model.ListInstanceItem;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;
import com.shpandrak.shpanlist.services.ListInstanceService;
import com.shpandrak.shpanlist.web.HtmlBindingResponse;
import com.shpandrak.shpanlist.web.HtmlRefreshResponse;
import com.shpandrak.shpanlist.web.HtmlResponsePrinter;
import com.shpandrak.shpanlist.web.UserMustSignInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/12/13
 * Time: 15:06
 */
public class ListInstancePageServlet extends BasePageServlet {

    @Override
    public boolean drawPage(HttpServletRequest request, HttpServletResponse response, StringBuilder sb, LoggedInUser loggedInUser) throws IOException, PersistenceException {
        String pathInfo = request.getPathInfo();
        if (!pathInfo.startsWith("/")) {
            response.sendRedirect("/");
            return false;
        }
        String listInstanceIdString = pathInfo.substring(1);
        if (listInstanceIdString == null || listInstanceIdString.isEmpty()) {
            response.sendRedirect("/");
            return false;
        }

        EntityKey listInstanceId = ListInstance.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceIdString);

        ListInstance listInstanceFull = ListInstanceService.getListInstanceFull(listInstanceId);


        sb.append("<head>\n" +
                "\t<title>").append("Hi Dude").append("</title>\n").append(
                getHeaderConstants());

        sb.append("\t<script type=\"text/javascript\">\n" +
                "\n" +
                "\tfunction onLoad() {\n" +
                "\tListInstancePageView.listInstanceId = '").append(listInstanceIdString).append("';\n" +
                "\t}\n" +
                "\t</script>\n");


        sb.append(
                "<body onload=\"onLoad()\">\n" +
                        "<div id=\"pageListInstance2\" data-role=\"page\">\n" +
                        "    <div id=\"listInstanceHeader\" data-role=\"header\">\n<h2>").append(
                listInstanceFull.getName()).append("</h2>\n" +
                "    </div>\n" +
                "    <div data-role=\"content\">\n" +
                "        <ul id=\"lstItems\" data-role=\"listview\">\n");

        for (ListInstanceItem currListInstanceItem : listInstanceFull.getListInstanceItemRelationship().getTargetEntities()) {

            appendListInstanceItemListElement(sb, currListInstanceItem);
        }


        sb.append(
                "        </ul>\n" +
                        "        <br/>\n" +
                        "        <input value=\"Edit\" type=\"button\" data-icon=\"edit\" onclick=\"ShpanlistController.menuEditListInstance(ListInstancePageView.listInstanceId)\"/>\n" +
                        "        <br/>\n" +
                        "\n" +
                        "    </div>\n" +
                        "    <div data-role=\"footer\">\n" +
                        "        <a data-icon=\"home\" href=\"javascript:ShpanlistController.menuHome()\">Home</a>\n" +
                        "        <a href='javascript:ShpanlistController.signOut()'>Sign Out</a>\n" +
                        "    </div>\n" +
                        "</div>\n" +
                        "</body>\n" +
                        "</html>");
        return true;
    }


    private void appendListInstanceItemListElement(Appendable writer, ListInstanceItem currListInstanceItem) throws IOException {
        if (currListInstanceItem.isGotIt()) {
            writer.append("<li id=\"").append(currListInstanceItem.getId().toString()).append("\" style=\"text-decoration: line-through\"><a HREF=\"javascript:ListInstancePageView.bringBackItem('").append(currListInstanceItem.getId().toString()).append("')\">");
        } else {
            writer.append("<li id=\"").append(currListInstanceItem.getId().toString()).append("\"><a HREF=\"javascript:ListInstancePageView.gotItem('").append(currListInstanceItem.getId().toString()).append("')\">");
        }

        writer.append(currListInstanceItem.getName());
        if (currListInstanceItem.getDescription() != null && !currListInstanceItem.getDescription().isEmpty()) {
            writer.append(" - ").append(currListInstanceItem.getDescription());
        }

        if (currListInstanceItem.getAmount() != null) {
            writer.append(" - ").append(currListInstanceItem.getAmount().toString());
        }

        writer.append("</a></li>");
    }


    public HtmlResponsePrinter doItPlease(HttpServletRequest request, HttpServletResponse response, LoggedInUser loggedInUser, String what) throws IOException, PersistenceException, UserMustSignInException {
        if ("gotListInstanceItem".equals(what)) {
            return gotListInstanceItem(loggedInUser, request, response);
        } else if ("bringBackItem".equals(what)) {
            return bringBackInstanceItem(loggedInUser, request, response);
        } else {
            throw new IllegalArgumentException("Invalid action " + what);
        }
    }

    private HtmlResponsePrinter gotListInstanceItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException, IOException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemId = request.getParameter("listInstanceItemId");
        EntityKey listInstanceItemKey = ListInstanceItem.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceItemId);
        ListInstanceService.gotItem(listInstanceItemKey);

        return new HtmlResponsePrinter(
                Arrays.asList(new HtmlBindingResponse("#" + listInstanceItemId, getListInstanceItemOutput(listInstanceItemKey), true)),
                Arrays.asList(new HtmlRefreshResponse("#lstItems", "listview")));
    }

    private HtmlResponsePrinter bringBackInstanceItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException, IOException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemId = request.getParameter("listInstanceItemId");
        EntityKey listInstanceItemKey = ListInstanceItem.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceItemId);
        ListInstanceService.gotItem(listInstanceItemKey, false);

        return new HtmlResponsePrinter(
                Arrays.asList(new HtmlBindingResponse("#" + listInstanceItemId, getListInstanceItemOutput(listInstanceItemKey), true)),
                Arrays.asList(new HtmlRefreshResponse("#lstItems", "listview")));
    }

    private String getListInstanceItemOutput(EntityKey listInstanceItemKey) throws PersistenceException, IOException {
        ListInstanceItem item = ListInstanceService.getListInstanceItem(listInstanceItemKey);
        StringBuilder sb = new StringBuilder();
        appendListInstanceItemListElement(sb, item);

        //todo:proper encoding

        return sb.toString().replaceAll("\\\"", "\\\\\"");
    }


}
