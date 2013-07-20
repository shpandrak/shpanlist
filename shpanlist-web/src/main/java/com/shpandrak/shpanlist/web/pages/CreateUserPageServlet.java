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
public class CreateUserPageServlet extends BasePageServlet {


    @Override
    public boolean requiresLogin() {
        return false;
    }

    @Override
    public HtmlResponsePrinter doItPlease(HttpServletRequest request, HttpServletResponse response, LoggedInUser loggedInUser, String what) throws IOException, PersistenceException, UserMustSignInException {
        throw new IllegalArgumentException("Unsupported page action: " + what);
    }

    @Override
    public boolean drawPage(HttpServletRequest request, HttpServletResponse response, StringBuilder sb, LoggedInUser loggedInUser) throws IOException, PersistenceException {
        sb.append("<head>\n" +
                "\t<title>").append("Shpanlist - Create User").append("</title>\n").append(
                getHeaderConstants());

        sb.append("</head>\n" +
            "<body>\n" +
                "<div data-role=\"page\">\n" +
                "    <div data-role=\"header\">\n" +
                "    Create shpanlist User\n" +
                "    </div>\n" +
                "    <div data-role=\"content\">\n" +
                "        <table>\n" +
                "            <tr>\n" +
                "                <td>*Username:</td>\n" +
                "                <td><input type=\"text\" id=\"txtNewUsername\" required/></td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>*Password:</td>\n" +
                "                <td><input type=\"password\" id=\"txtNewPassword\" required/></td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>*First Name:</td>\n" +
                "                <td><input type=\"text\" id=\"txtNewFirstName\" required/></td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>*Last Name:</td>\n" +
                "                <td><input type=\"text\" id=\"txtNewLastName\" required/></td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>email:</td>\n" +
                "                <td><input type=\"email\" id=\"txtNewEmail\"/></td>\n" +
                "            </tr>\n" +
                "\n" +
                "        </table>\n" +
                "        <button onclick=\"ShpanlistController.createUser(txtNewUsername.value, txtNewPassword.value, txtNewFirstName.value, txtNewLastName.value, txtNewEmail.value)\">Create User</button>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n");
        return true;
    }
}
