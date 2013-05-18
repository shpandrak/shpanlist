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
                "\t<title>Hi Dude</title>\n" +
                "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "\t<link rel=\"stylesheet\" href=\"include/jquery.mobile-1.3.1/jquery.mobile-1.3.1.min.css\" />\n" +
                "\t<script src=\"include/jquery-1.9.1.min.js\"></script>\n" +
                "\t<script src=\"customInit.js\"></script>\n" +
                "\t<script src=\"include/jquery.mobile-1.3.1/jquery.mobile-1.3.1.min.js\"></script>\n" +
                "\t<script src=\"ShpanlistView.js\"></script>\n" +
                "\t<script src=\"ShpanlistController.js\"></script>\n" +
                "\t<script src=\"listTemplateView.js\"></script>\n" +
                "\t<script src=\"listInstanceView.js\"></script>\n" +
                "\t<script src=\"listInstanceEditView.js\"></script>\n" +
                "\t<script src=\"listGroupView.js\"></script>\n" +
                "\t<script type=\"text/javascript\">\n" +
                "\n" +
                "        function onLoad() {\n");

        if (loggedInUser != null){
             rw.append(
                 "            ShpanlistController.signedInUser = \"").append(loggedInUser.getUserName()).append("\";\n");
        }

        rw.append(
                "            ShpanlistView.mainFrame = divMain;\n" +
                "            ListTemplateView.mainFrame = divMain;\n" +
                "            ListGroupView.mainFrame = divMain;\n" +
                "            ListInstanceView.mainFrame = divMain;\n" +
                "            ListInstanceEditView.mainFrame = divMain;\n" +
                "            ShpanlistController.loadApp();\n" +
                "        }\n" +
                "\n" +
                "    </script>\n" +
                "</head>\n" +
                "\n" +
                "<body onload=\"onLoad()\">\n" +
                "<a href=\"javascript:ShpanlistController.signOut()\">Sign Out</a>\n" +
                "<div id=\"divMain\"></div>\n" +
                "</body>\n" +
                "\n" +
                "</html>");
    }
}
