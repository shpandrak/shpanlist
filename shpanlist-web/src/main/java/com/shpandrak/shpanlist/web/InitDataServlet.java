package com.shpandrak.shpanlist.web;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.shpanlist.gae.datastore.ListTemplateItemManager;
import com.shpandrak.shpanlist.gae.datastore.ListTemplateManager;
import com.shpandrak.shpanlist.gae.datastore.ListUserManager;
import com.shpandrak.shpanlist.model.Gender;
import com.shpandrak.shpanlist.model.ListTemplate;
import com.shpandrak.shpanlist.model.ListTemplateItem;
import com.shpandrak.shpanlist.model.ListUser;
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
            ListTemplateManager listTemplateManager = new ListTemplateManager();
            ListTemplateItemManager listTemplateItemManager = new ListTemplateItemManager();

            ListUser shpandrakUser = listUserManager.getByField(ListUser.DESCRIPTOR.userNameFieldDescriptor, "shpandrak");
            if (shpandrakUser == null){
                // Creating default user
                shpandrakUser = new ListUser("shpandrak", "shpandrak@gmail.com", "Amit", "Lieberman", "amit.lieberman", "shpanlist", Gender.M, new GregorianCalendar(1981, 3, 5).getTime(), new Date());
                listUserManager.create(shpandrakUser);
            }

            ListTemplate listTemplate;
            List<ListTemplate> listTemplates = listTemplateManager.listByField(
                    ListTemplate.DESCRIPTOR.nameFieldDescriptor, "My first list");
            if (listTemplates.isEmpty()){
                listTemplate = new ListTemplate("My first list", new Date(), shpandrakUser);
                listTemplateManager.create(listTemplate);
            }else {
                listTemplate = listTemplates.get(0);
            }

            List<ListTemplateItem> listTemplateItems = listTemplateItemManager.listByListTemplateRelationship(listTemplate.getId());
            if (listTemplateItems.isEmpty()){
                ListTemplateItem eggs = new ListTemplateItem(listTemplate, "Eggs", 1, "Size L Eggs", 12);
                ListTemplateItem milk = new ListTemplateItem(listTemplate, "Milk", 2, "3% fat milk", null);
                listTemplateItemManager.create(Arrays.asList(eggs, milk));
            }

            ListUser user = ShpanlistAuthService.signIn("shpandrak", "shpanlist");
            DoItServlet.attachUserToSession(request, new LoggedInUser(user.getId(), user.getUserName()));



        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }
}
