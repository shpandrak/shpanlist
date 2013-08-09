package com.shpandrak.shpanlist.web.pages;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.shpanlist.model.ListInstance;
import com.shpandrak.shpanlist.model.ListInstanceItem;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;
import com.shpandrak.shpanlist.services.ListInstanceService;
import com.shpandrak.shpanlist.web.HtmlResponsePrinter;
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
public class ListInstanceItemInfoPageServlet extends BasePageServlet {

    @Override
    public HtmlResponsePrinter doItPlease(HttpServletRequest request, HttpServletResponse response, LoggedInUser loggedInUser, String what) throws IOException, PersistenceException, UserMustSignInException {
        if ("uploadImage".equals(what)){
            return addListInstanceItem(loggedInUser, request, response);
        }else {
            throw new IllegalArgumentException("Unsupported operation by page: " + what);
        }
    }

    @Override
    public boolean drawPage(HttpServletRequest request, HttpServletResponse response, StringBuilder sb, LoggedInUser loggedInUser) throws IOException, PersistenceException {
        String pathInfo = request.getPathInfo();
        if (!pathInfo.startsWith("/")) {
            response.sendRedirect("/");
            return false;
        }
        String listInstanceItemIdString = pathInfo.substring(1);
        if (listInstanceItemIdString == null || listInstanceItemIdString.isEmpty()) {
            response.sendRedirect("/");
            return false;
        }

        ListInstanceItem listInstanceItem = ListInstanceService.getListInstanceItem(ListInstanceItem.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceItemIdString));

        sb.append("<head>\n" +
                "\t<title>").append(listInstanceItem.getName()).append("</title>\n").append(
                getHeaderConstants()).append("\n").append(
                "</head>");
        sb.append(
                "<body>\n" +
                        "<div id=\"pageAddListInstanceItem\" data-role=\"page\">\n" +
                        "    <div data-role=\"header\">\n" +
                        "        <h2>").append(listInstanceItem.getName()).append("</h2>\n" +
                        "    </div>\n" +
                        "    <div data-role=\"content\">\n" +
                        "        <input id =\"listInstanceId\" type=\"hidden\" value=\"").append(listInstanceItem.getListInstanceId().toString()).append("\"/>" +
                "        <table style=\"border-spacing:0;padding:0\">\n" +
                "            <tr>\n" +
                "                <td>Item:</td>\n" +
                "                <td>\n" +
                "                    ").append(listInstanceItem.getName()).append("\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>Amount:</td>\n" +
                "                <td>\n" +
                "                    ").append (listInstanceItem.getAmount()).append("\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>Description:</td>\n" +
                "                <td>\n" +
                "                    ").append (listInstanceItem.getDescription()).append("\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td colspan=\"2\">\n" +
                "                   <img src=\"").append(listInstanceItem.getImageURL()).append("\">" +
                "                </td>\n" +
                "            </tr>\n" +
                "        </table>\n" +
                "\n" +
                "    </div>\n" +
                "    <div data-role=\"footer\">\n" +
                "        <a data-icon=\"arrow-l\" href=\"javascript:$.mobile.back();\"> Back</a>\n" +
                "        <a href='javascript:ShpanlistController.signOut()'>Sign Out</a>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "</body>\n");
        return true;
    }

    private HtmlResponsePrinter addListInstanceItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemName = request.getParameter("listInstanceItemName");
        String listInstanceItemDescription = request.getParameter("listInstanceItemDescription");
        String listInstanceItemDefaultAmount = request.getParameter("listInstanceItemAmount");
        Integer amount = null;
        if (listInstanceItemDefaultAmount != null && !listInstanceItemDefaultAmount.isEmpty()){
            amount = Integer.valueOf(listInstanceItemDefaultAmount);
        }
        ListInstanceService.addListInstanceItem(ListInstance.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceId), listInstanceItemName, listInstanceItemDescription, amount, null);
        return HtmlResponsePrinter.emptyResponse();
    }

}
