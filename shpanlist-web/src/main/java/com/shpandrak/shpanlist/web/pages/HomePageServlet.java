package com.shpandrak.shpanlist.web.pages;

import com.shpandrak.datamodel.field.EntityKey;
import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.shpanlist.model.ListInstance;
import com.shpandrak.shpanlist.model.ListInstanceItem;
import com.shpandrak.shpanlist.model.ListTemplate;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;
import com.shpandrak.shpanlist.services.ListInstanceService;
import com.shpandrak.shpanlist.services.ListTemplateService;
import com.shpandrak.shpanlist.web.HtmlResponsePrinter;
import com.shpandrak.shpanlist.web.UserMustSignInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/14/13
 * Time: 19:29
 */
public class HomePageServlet extends BasePageServlet {

    @Override
    public HtmlResponsePrinter doItPlease(HttpServletRequest request, HttpServletResponse response, LoggedInUser loggedInUser, String what) throws IOException, PersistenceException, UserMustSignInException {
        return null;
    }

    @Override
    public boolean drawPage(HttpServletRequest request, HttpServletResponse response, StringBuilder sb, LoggedInUser loggedInUser) throws IOException, PersistenceException {
        sb.append("<head>\n" +
                "\t<title>").append("Hi Dude").append("</title>\n").append(
                getHeaderConstants());

        sb.append("</head>\n" +
            "<body>\n" +
                    "<div id=\"pageHome\" data-role=\"page\">\n" +
                    "    <div data-role=\"header\">\n" +
                    "        Welcome to Shpanlist\n" +
                    "    </div>\n" +
                    "    <div data-role=\"content\">\n" +
                    "        <ul id=\"lstLists\" data-role=\"listview\" data-split-icon=\"delete\" data-split-theme=\"c\">");



        List<ListInstance> userListInstances = ListInstanceService.getUserLists(loggedInUser.getUserId());
        List<ListTemplate> userListTemplates = ListTemplateService.getUserLists(loggedInUser.getUserId());

        // List instances divider
        sb.append("<li data-role=\"list-divider\">Active Lists</li>");

        // List Instances items
        for (ListInstance currListInstance : userListInstances) {
            String listInstanceIdString = currListInstance.getId().toString();
            sb.append("<li><a href=\"javascript:ShpanlistController.menuListInstance('").append(
                    listInstanceIdString).append("')\">").append(
                    currListInstance.getName()).append("</a>");
        }

        // List Templates divider
        sb.append("<li data-role=\"list-divider\">List Templates</li>");

        // List template list items
        for (ListTemplate currListTemplate : userListTemplates){
            sb.append("<li><a HREF=\"javascript:ShpanlistController.menuListTemplate('").append(currListTemplate.getId().toString()).append("')\">").append(currListTemplate.getName()).append("</a></li>");
        }

        sb.append(
            "</ul>\n" +
                    "        <br/>\n" +
                    "    </div>\n" +
                    "    <div data-role=\"footer\">\n" +
                    "        <a data-icon=\"plus\" href='javascript:ShpanlistController.menuNewListInstance()'>New List</a>\n" +
                    "        <a href='javascript:ShpanlistController.signOut()'>Sign Out</a>\n" +
                    "    </div>\n" +
                    "</div>\n");


        return true;
    }
}
