package com.shpandrak.shpanlist.web;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.shpanlist.gae.datastore.ListUserManager;
import com.shpandrak.shpanlist.model.ListUser;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;
import com.shpandrak.shpanlist.services.auth.ShpanlistAuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        String what = request.getParameter("what");

        if ("signIn".equals(what)){
            signIn(request, response);
        }
    }

    private void signIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
