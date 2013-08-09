package com.shpandrak.shpanlist.web;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.shpanlist.gae.datastore.*;
import com.shpandrak.shpanlist.model.*;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;
import com.shpandrak.shpanlist.services.ShpanlistAuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created with love
 * User: shpandrak
 * Date: 5/7/13
 * Time: 00:15
 */
public class InitDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            createInitData(request, response);
            response.sendRedirect("/");
        } catch (PersistenceException e) {
            e.printStackTrace(response.getWriter());
            throw new ServletException("Oops", e);
        }
    }

    private void createInitData(HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListUserManager listUserManager = new ListUserManager();
            ListInstanceManager listInstanceManager = new ListInstanceManager();
            ListInstanceItemManager listInstanceItemManager = new ListInstanceItemManager();

            ListUser shpandrakUser = listUserManager.getByField(ListUser.DESCRIPTOR.userNameFieldDescriptor, "shpandrak");
            if (shpandrakUser == null){
                // Creating default user
                shpandrakUser = new ListUser("shpandrak", "shpandrak@gmail.com", "Amit", "Lieberman", "amit.lieberman", "shpanlist", Gender.M, new GregorianCalendar(1981, 3, 5).getTime(), new Date());
                listUserManager.create(shpandrakUser);
            }

            ListInstance listInstance;
            List<ListInstance> listTemplates = listInstanceManager.listByField(
                    ListInstance.DESCRIPTOR.nameFieldDescriptor, "My first list");
            if (listTemplates.isEmpty()){
                listInstance = new ListInstance("My first list", new Date(), null, shpandrakUser);
                listInstanceManager.create(listInstance);
            }else {
                listInstance = listTemplates.get(0);
            }

            List<ListInstanceItem> listInstanceItems = listInstanceItemManager.listByListInstanceRelationship(listInstance.getId());
            if (listInstanceItems.isEmpty()){
                ListInstanceItem eggs = new ListInstanceItem(listInstance, "Eggs", 1, "Size L Eggs", 12, false, "http://farm8.staticflickr.com/7382/buddyicons/7797673@N02_r.jpg?1369432289#7797673@N02");
                ListInstanceItem milk = new ListInstanceItem(listInstance, "Milk", 2, "3% fat milk", null, false, null);
                listInstanceItemManager.create(Arrays.asList(eggs, milk));
            }

            ListUser user = ShpanlistAuthService.signIn("shpandrak", "shpanlist");
            DoItServlet.attachUserToSession(request, new LoggedInUser(user.getId(), user.getUserName()));



        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }
}
