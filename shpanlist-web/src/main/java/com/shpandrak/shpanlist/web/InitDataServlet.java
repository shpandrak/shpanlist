package com.shpandrak.shpanlist.web;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.shpanlist.gae.datastore.ListGroupManager;
import com.shpandrak.shpanlist.gae.datastore.ListTemplateManager;
import com.shpandrak.shpanlist.gae.datastore.ListUserManager;
import com.shpandrak.shpanlist.model.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            createInitData();
        } catch (PersistenceException e) {
            e.printStackTrace(httpServletResponse.getWriter());
        }
    }

    private void createInitData() throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListUserManager listUserManager = new ListUserManager();
            ListGroupManager listGroupManager = new ListGroupManager();
            ListTemplateManager listTemplateManager = new ListTemplateManager();

            ListUser shpandrakUser = listUserManager.getByField(ListUser.DESCRIPTOR.userNameFieldDescriptor, "shpandrak");
            if (shpandrakUser == null){
                // Creating default user
                shpandrakUser = new ListUser("shpandrak", "shpandrak@gmail.com", "Amit", "Lieberman", "amit.lieberman", "shpanlist", Gender.M, new GregorianCalendar(1981, 3, 5).getTime(), new Date());
                listUserManager.create(shpandrakUser);
            }

//            List<ListGroup> shpandrakListGroups = listGroupManager.listByOwnerUserRelationship(shpandrakUser.getId());
            ListGroup listGroup = listGroupManager.getByField(ListGroup.DESCRIPTOR.nameFieldDescriptor, "My List Group");
            if (listGroup == null){
                // Creating default list group
                listGroup = new ListGroup("My List Group", new Date(), shpandrakUser);
                listGroup.getMemberRelationship().addNewRelation(new ListGroupMemberRelationshipEntry(shpandrakUser, listGroup.getCreationDate()));
                listGroupManager.create(listGroup);
            }

            List<ListTemplate> listTemplates = listTemplateManager.listByListGroupRelationship(listGroup.getId());
            if (listTemplates.isEmpty()){
                ListTemplate listTemplate = new ListTemplate(listGroup, "My first list", new Date(), shpandrakUser);
                listTemplateManager.create(listTemplate);
            }


        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }
}
