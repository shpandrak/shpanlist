package com.shpandrak.shpanlist.web;

import com.shpandrak.shpanlist.model.auth.LoggedInUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created with love
 * User: shpandrak
 * Date: 5/10/13
 * Time: 15:21
 */
public class WelcomeServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoggedInUser loggedInUser = (LoggedInUser)request.getSession().getAttribute("user");

        PrintWriter rw = response.getWriter();
        rw.append("" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Hi Dude</title>\n" +
                "    <script src=\"webmanage/jquery-1.8.3.js\"></script>\n" +
                "    <script src=\"ShpanlistView.js\"></script>\n" +
                "    <script src=\"ShpanlistController.js\"></script>\n" +
                "    <script src=\"listTemplateView.js\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "\n" +
                "        function onLoad() {\n");

        if (loggedInUser != null){
             rw.append(
                 "            ShpanlistController.signedInUser = \"").append(loggedInUser.getUserName()).append("\";\n");
        }

        rw.append(
                "            ShpanlistView.mainFrame = divMain;\n" +
                "            ListTemplateView.mainFrame = divMain;\n" +
                "            ShpanlistController.loadApp();\n" +
                "        }\n" +
                "\n" +
                "    </script>\n" +
                "</head>\n" +
                "\n" +
                "<body onload=\"onLoad()\">\n" +
                "<div id=\"divMain\"></div>\n" +
                "</body>\n" +
                "\n" +
                "</html>");
    }
}
