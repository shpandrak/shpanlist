package com.shpandrak.shpanlist.web.pages;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.shpanlist.model.ListTemplate;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;
import com.shpandrak.shpanlist.services.ListTemplateService;
import com.shpandrak.shpanlist.web.EmptyResponsePrinter;
import com.shpandrak.shpanlist.web.HtmlResponsePrinter;
import com.shpandrak.shpanlist.web.ResponsePrinter;
import com.shpandrak.shpanlist.web.UserMustSignInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/12/13
 * Time: 15:06
 */
public class AddListTemplateItemPageServlet extends BasePageServlet {

    @Override
    public HtmlResponsePrinter doItPlease(HttpServletRequest request, HttpServletResponse response, LoggedInUser loggedInUser, String what) throws IOException, PersistenceException, UserMustSignInException {

        if ("addListTemplateItem".equals(what)) {
            return addListTemplateItem(loggedInUser, request, response);
        }else{
            throw new IllegalArgumentException("Unsupported operation by page: " + what);
        }
    }

    private HtmlResponsePrinter addListTemplateItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listTemplateId = request.getParameter("listTemplateId");
        String listTemplateItemName = request.getParameter("listTemplateItemName");
        String listTemplateItemDescription = request.getParameter("listTemplateItemDescription");
        String listTemplateItemDefaultAmount = request.getParameter("listTemplateItemDefaultAmount");
        Integer defaultAmount = null;
        if (listTemplateItemDefaultAmount != null && !listTemplateItemDefaultAmount.isEmpty()){
            defaultAmount = Integer.valueOf(listTemplateItemDefaultAmount);
        }
        ListTemplateService.addListTemplateItem(ListTemplate.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateId), listTemplateItemName, listTemplateItemDescription, defaultAmount);
        return HtmlResponsePrinter.emptyResponse();
    }

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

        sb.append("<head>\n" +
                "\t<title>").append("Add List Template Item").append("</title>\n").append(
                getHeaderConstants()).append("\n");

        sb.append("\t<script type=\"text/javascript\">\n" +
                "\n" +
                "\tfunction loadMe() {\n" +
                "\tListTemplatePageView.listTemplateId = '").append(listTemplateIdString).append("';\n" +
                "\t}\n" +
                "\t</script>\n" +
                "</head>");


        sb.append(
                "<body onload=\"loadMe()\">\n" +
                        "<div id=\"pageAddListTemplateItem\" data-role=\"page\">\n" +
                        "    <div data-role=\"header\">\n" +
                        "        <h2>Add List Template Item</h2>\n" +
                        "    </div>\n" +
                        "    <div data-role=\"content\">\n" +
                        "        <table style=\"border-spacing:0;padding:0\">\n" +
                        "            <tr>\n" +
                        "                <td>Item:</td>\n" +
                        "                <td>\n" +
                        "                    <input id=\"txtListTemplateItemName\" type=\"text\"/>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td>Amount:</td>\n" +
                        "                <td>\n" +
                        "                    <input id=\"txtListTemplateItemAmount\" type=\"number\"/>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td>Description:</td>\n" +
                        "                <td>\n" +
                        "                    <input id=\"txtListTemplateItemDescription\" type=\"text\"/>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td colspan=\"2\">\n" +
                        "                    <input type=\"button\" onClick=\"ListTemplatePageView.createNewItem()\"\n" +
                        "                           value=\"Add\"/>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </table>\n" +
                        "\n" +
                        "    </div>\n" +
                        "    <div data-role=\"footer\">\n" +
                        "        <a data-icon=\"arrow-l\" href=\"javascript:$.mobile.back();\">Back</a>\n" +
                        "        <a href=\"javascript:ShpanlistController.signOut()\">Sign Out</a>\n" +
                        "    </div>\n" +
                        "</div>\n" +
                        "\n" +
                        "\n" +
                        "</body>");
        return true;
    }
}
