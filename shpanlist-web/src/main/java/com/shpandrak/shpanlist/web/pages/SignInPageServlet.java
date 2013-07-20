package com.shpandrak.shpanlist.web.pages;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;
import com.shpandrak.shpanlist.web.HtmlResponsePrinter;
import com.shpandrak.shpanlist.web.UserMustSignInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/14/13
 * Time: 19:29
 */
public class SignInPageServlet extends BasePageServlet {


    @Override
    public boolean requiresLogin() {
        return false;
    }

    @Override
    public HtmlResponsePrinter doItPlease(HttpServletRequest request, HttpServletResponse response, LoggedInUser loggedInUser, String what) throws IOException, PersistenceException, UserMustSignInException {
        return null;
    }

    @Override
    public boolean drawPage(HttpServletRequest request, HttpServletResponse response, StringBuilder sb, LoggedInUser loggedInUser) throws IOException, PersistenceException {
        if (loggedInUser != null){
            response.sendRedirect("/home");
            return false;
        }

        sb.append("<head>\n" +
                "\t<title>").append("Shpanlist Sign In").append("</title>\n").append(
                getHeaderConstants());

        sb.append("</head>\n" +
            "<body>\n" +
                "    <div data-role=\"page\" id=\"pageSignIn\">\n" +
                "\n" +
                "        <div data-role=\"header\">\n" +
                "            Sign In\n" +
                "        </div>\n" +
                "        <div data-role=\"content\">\n" +
                "            <table style=\"border-spacing:0;padding:0\">\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        Username<br/>\n" +
                "                        <input id=\"txtUserName\" type=\"text\"/>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        Password<br/>\n" +
                "                        <input id=\"txtPassword\" type=\"password\"/>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <input type=\"button\" onClick=\"ShpanlistController.signIn(txtUserName.value, txtPassword.value)\"\n" +
                "                               value=\"Login\"/>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <a href=\"javascript:ShpanlistController.menuCreateUser()\" data-transition=\"slide\">Sign Up</a>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>");
        return true;
    }
}
