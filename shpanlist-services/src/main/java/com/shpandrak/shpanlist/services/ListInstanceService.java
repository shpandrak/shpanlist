package com.shpandrak.shpanlist.services;

import com.shpandrak.datamodel.field.EntityKey;
import com.shpandrak.datamodel.field.Key;
import com.shpandrak.datamodel.relationship.RelationshipLoadLevel;
import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.shpanlist.gae.datastore.ListInstanceItemManager;
import com.shpandrak.shpanlist.gae.datastore.ListInstanceManager;
import com.shpandrak.shpanlist.gae.datastore.ListTemplateManager;
import com.shpandrak.shpanlist.model.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created with love
 * User: shpandrak
 * Date: 5/12/13
 * Time: 16:29
 */
public abstract class ListInstanceService {
    private static final SimpleDateFormat shortDayDate = new SimpleDateFormat("MMM-dd");

    public static ListInstance getListInstanceFull(Key listTemplateId) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListInstanceManager listInstanceManager = new ListInstanceManager();
            return listInstanceManager.getById(listTemplateId, RelationshipLoadLevel.FULL);
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }

    public static ListInstance createFromTemplate(Key listTemplateId, Key creatingUserId) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListTemplateManager listTemplateManager = new ListTemplateManager();
            ListInstanceManager listInstanceManager = new ListInstanceManager();
            ListInstanceItemManager listInstanceItemManager = new ListInstanceItemManager();
            ListTemplate listTemplate = listTemplateManager.getById(listTemplateId, RelationshipLoadLevel.FULL);

            Date listExpirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
            String listName = listTemplate.getName() + "_" + shortDayDate.format(listExpirationDate);

            // Creating the list object
            ListInstance listInstance = new ListInstance(
                    listTemplate.getListGroupId(),
                    listName,
                    listExpirationDate,
                    new Date(),
                    creatingUserId);

            // Relating it to the template
            listInstance.setListTemplateId(listTemplate.getId());

            PersistenceLayerManager.getConnectionProvider().beginTransaction();

            listInstanceManager.create(listInstance);



            // Copying the list template items
            Collection<ListTemplateItem> listTemplateItems = listTemplate.getListTemplateItemRelationship().getTargetEntities().values();
            if (!listTemplateItems.isEmpty()){
                List<ListInstanceItem> listInstanceItems = new ArrayList<ListInstanceItem>(listTemplateItems.size());
                for (ListTemplateItem currItemTemplate : listTemplateItems){
                    listInstanceItems.add(new ListInstanceItem(listInstance, currItemTemplate.getName(), currItemTemplate.getDescription(), currItemTemplate.getDefaultAmount(), false));
                }
                listInstanceItemManager.create(listInstanceItems);
            }

            PersistenceLayerManager.getConnectionProvider().commitTransaction();


            return listInstance;

        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }

    public static void gotItem(Key listInstanceItemId) throws PersistenceException {
        gotItem(listInstanceItemId, true);
    }

    public static void gotItem(Key listInstanceItemId, boolean gotIt) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListInstanceItemManager listInstanceItemManager = new ListInstanceItemManager();
            listInstanceItemManager.updateFieldValueById(ListInstanceItem.DESCRIPTOR.gotItFieldDescriptor, gotIt, listInstanceItemId);
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }

    }
}
