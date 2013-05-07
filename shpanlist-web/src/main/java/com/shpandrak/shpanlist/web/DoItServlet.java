package com.shpandrak.shpanlist.web;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.shpanlist.model.ListGroup;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;
import com.shpandrak.shpanlist.services.ListGroupService;
import com.shpandrak.shpanlist.services.ShpanlistAuthService;
import com.shpandrak.xml.EntityXMLConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created with love
 * User: shpandrak
 * Date: 5/6/13
 * Time: 20:26
 */
@WebServlet("/doIt")
public class DoItServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoggedInUser loggedInUser = (LoggedInUser)request.getSession().getAttribute("user");
        String what = request.getParameter("what");
        try {

            if ("signIn".equals(what)){
                signIn(loggedInUser, request, response);
            }else if ("listListGroups".equals(what)){
                listListGroups(loggedInUser, request, response);
            }
        } catch (PersistenceException e) {
            log("Failed " + what, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    private void listListGroups(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException, IOException {
        List<ListGroup> listGroups = ListGroupService.getListGroups(loggedInUser);
        response.getWriter().print(new EntityXMLConverter<ListGroup>(ListGroup.DESCRIPTOR).toXML(listGroups));
    }

    private void signIn(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        try {
            LoggedInUser user = ShpanlistAuthService.signIn(userName, password);
            if (user == null){
                response.getWriter().println("Boooo!");
            }else {
                request.getSession().setAttribute("user", user);
                //response.sendRedirect("/main.html");
                response.getWriter().println("Yey!");
            }
        } catch (PersistenceException e) {
            response.getWriter().println("Baahhhh" + e.getMessage());
        }
    }

}