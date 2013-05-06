package com.shpandrak.shpanlist.web;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.shpanlist.gae.datastore.ListUserManager;
import com.shpandrak.shpanlist.model.ListUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String userName = httpServletRequest.getParameter("txtUserName");
        String password = httpServletRequest.getParameter("txtPassword");

        try {
            ListUser user = login(userName, password);
            if (user == null){
                httpServletResponse.getWriter().println("Boooo!");
            }else {
                httpServletResponse.getWriter().println("Yey!");
            }
        } catch (PersistenceException e) {
            httpServletResponse.getWriter().println("Baahhhh" + e.getMessage());
        }
    }

    private ListUser login(String userName, String password) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListUserManager listUserManager = new ListUserManager();
            ListUser byField = listUserManager.getByField(ListUser.DESCRIPTOR.userNameFieldDescriptor, userName);
            if (byField != null && byField.getShpanPassword().equals(password)){
                return byField;
            }
            return null;
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }
}
