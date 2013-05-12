package com.shpandrak.shpanlist.web;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.persistence.query.filter.*;
import com.shpandrak.shpanlist.gae.datastore.ListGroupManager;
import com.shpandrak.shpanlist.gae.datastore.ListTemplateItemManager;
import com.shpandrak.shpanlist.gae.datastore.ListTemplateManager;
import com.shpandrak.shpanlist.gae.datastore.ListUserManager;
import com.shpandrak.shpanlist.model.*;

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
            ListTemplateItemManager listTemplateItemManager = new ListTemplateItemManager();

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
            ListTemplate listTemplate;
            List<ListTemplate> listTemplates = listTemplateManager.list(new QueryFilter(
                    new CompoundFieldFilterCondition(FieldFilterLogicalOperatorType.AND,
                    new RelationshipFilterCondition(ListTemplate.DESCRIPTOR.listGroupRelationshipDescriptor, listGroup.getId()),
                            BasicFieldFilterCondition.build(ListTemplate.DESCRIPTOR.nameFieldDescriptor, FilterConditionOperatorType.EQUALS, "My first list")
                            )));
            if (listTemplates.isEmpty()){
                listTemplate = new ListTemplate(listGroup, "My first list", new Date(), shpandrakUser);
                listTemplateManager.create(listTemplate);
            }else {
                listTemplate = listTemplates.get(0);
            }

            List<ListTemplateItem> listTemplateItems = listTemplateItemManager.listByListTemplateRelationship(listTemplate.getId());
            if (listTemplateItems.isEmpty()){
                ListTemplateItem eggs = new ListTemplateItem(listTemplate, "Eggs", "Size L Eggs", 12L);
                ListTemplateItem milk = new ListTemplateItem(listTemplate, "Milk", "3% fat milk", null);
                listTemplateItemManager.create(Arrays.asList(eggs, milk));
            }


        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }
}
