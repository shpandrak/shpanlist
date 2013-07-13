package com.shpandrak.shpanlist.web.pages;

import com.shpandrak.datamodel.field.EntityKey;
import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.shpanlist.model.ListInstance;
import com.shpandrak.shpanlist.model.ListInstanceItem;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;
import com.shpandrak.shpanlist.services.ListInstanceService;
import com.shpandrak.shpanlist.web.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/12/13
 * Time: 15:06
 */
public class ListInstancePageServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter rw = response.getWriter();

        try{
            LoggedInUser loggedInUser = (LoggedInUser)request.getSession().getAttribute("user");
            if (loggedInUser == null){
                response.sendRedirect("/");
                return;
            }

            String pathInfo = request.getPathInfo();
            if (!pathInfo.startsWith("/")){
                response.sendRedirect("/");
            }
            String listInstanceIdString = pathInfo.substring(1);
            if (listInstanceIdString == null || listInstanceIdString.isEmpty()){
                response.sendRedirect("/");
            }

            EntityKey listInstanceId = ListInstance.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceIdString);

            ListInstance listInstanceFull = ListInstanceService.getListInstanceFull(listInstanceId);


            rw.append(
                    "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "\t<title>Hi Dude</title>\n").append(
                    WelcomeServlet.getHeaderConstants()).append(
                    "\t<script type=\"text/javascript\">\n" +
                            "\n" +
                            "\tfunction onLoad() {\n" +
                                "\tListInstancePageView.listInstanceId = '").append(listInstanceIdString).append("';\n" +
                            "\t}\n" +
                    "\t</script>\n" +
                    "</head>\n"
            );


            rw.append(
                            "\n" +
                            "<body onload=\"onLoad()\">\n" +
                            "<div id=\"pageListInstance2\" data-role=\"page\">\n" +
                                    "    <div id=\"listInstanceHeader\" data-role=\"header\">\n<h2>").append(
                                    listInstanceFull.getName()).append("</h2>\n" +
                                    "    </div>\n" +
                                    "    <div data-role=\"content\">\n" +
                                    "        <ul id=\"lstItems\" data-role=\"listview\">\n");

                                for (ListInstanceItem currListInstanceItem : listInstanceFull.getListInstanceItemRelationship().getTargetEntities()){

                                    appendListInstanceItemListElement(rw, currListInstanceItem);
                                }



                            rw.append(
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
                            "\n" +
                            "</html>");
        } catch (Exception e) {
            log("oops", e);
            //todo
            rw.append("Oops.. error on page");
        }

    }

    private void appendListInstanceItemListElement(Appendable writer, ListInstanceItem currListInstanceItem) throws IOException {
        if (currListInstanceItem.isGotIt()){
            writer.append("<li id=\"").append(currListInstanceItem.getId().toString()).append("\" style=\"text-decoration: line-through\"><a HREF=\"javascript:ListInstancePageView.bringBackItem('").append(currListInstanceItem.getId().toString()).append("')\">");
        }else {
            writer.append("<li id=\"").append(currListInstanceItem.getId().toString()).append("\"><a HREF=\"javascript:ListInstancePageView.gotItem('").append(currListInstanceItem.getId().toString()).append("')\">");
        }

        writer.append(currListInstanceItem.getName());
        if (currListInstanceItem.getDescription() != null && !currListInstanceItem.getDescription().isEmpty()){
            writer.append(" - ").append(currListInstanceItem.getDescription());
        }

        if (currListInstanceItem.getAmount() != null){
            writer.append(" - ").append(currListInstanceItem.getAmount().toString());
        }

        writer.append("</a></li>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoggedInUser loggedInUser = (LoggedInUser)request.getSession().getAttribute("user");
        String what = request.getParameter("what");
        try {

            response.setContentType("application/json; charset=UTF-8");
            HtmlResponsePrinter responsePrinter = doItPlease(request, response, loggedInUser, what);


            PrintWriter writer = response.getWriter();
            writer.print("{\"status\":0, \"message\":\"Cool\"");
            responsePrinter.printResponse(writer);
            writer.print("}");

        } catch (UserMustSignInException e){
            response.getWriter().print("{\"status\":1, \"message\":\"User Must Sign In\"}");

        } catch (Exception e) {
            response.getWriter().print("{\"status\":1, \"message\":\"" + e.getMessage() + "\"}");
            log("Failed " + what, e);
        }

    }

    private HtmlResponsePrinter doItPlease(HttpServletRequest request, HttpServletResponse response, LoggedInUser loggedInUser, String what) throws IOException, PersistenceException, UserMustSignInException {
        if ("gotListInstanceItem".equals(what)){
            return gotListInstanceItem(loggedInUser, request, response);
        }else if ("bringBackItem".equals(what)){
            return bringBackInstanceItem(loggedInUser, request, response);
        }else {
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
