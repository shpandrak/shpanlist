package com.shpandrak.shpanlist.services;

import com.shpandrak.datamodel.field.Key;
import com.shpandrak.datamodel.relationship.RelationshipLoadLevel;
import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.shpanlist.gae.datastore.ListTemplateItemManager;
import com.shpandrak.shpanlist.gae.datastore.ListTemplateManager;
import com.shpandrak.shpanlist.model.ListTemplate;
import com.shpandrak.shpanlist.model.ListTemplateItem;

import java.util.Date;

/**
 * Created with love
 * User: shpandrak
 * Date: 5/7/13
 * Time: 23:46
 */
public abstract class ListTemplateService {

    public static ListTemplate createListTemplate(Key userKey, Key listGroupId, String listName) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListTemplateManager listTemplateManager = new ListTemplateManager();
            ListTemplate listTemplate = new ListTemplate(listGroupId, listName, new Date(), userKey);
            listTemplateManager.create(listTemplate);
            return listTemplate;
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }


    }

    public static ListTemplate getListTemplateFull(Key listTemplateId) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListTemplateManager listTemplateManager = new ListTemplateManager();
            return listTemplateManager.getById(listTemplateId, RelationshipLoadLevel.FULL);
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }

    public static void addListTemplateItem(Key listTemplateId, String listTemplateItemName, String listTemplateItemDescription, Long defaultAmount) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListTemplateItemManager listTemplateItemManager = new ListTemplateItemManager();
            listTemplateItemManager.create(new ListTemplateItem(listTemplateId, listTemplateItemName, listTemplateItemDescription, defaultAmount));
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }

    public static void removeListTemplateItem(Key listTemplateItemId) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListTemplateItemManager listTemplateItemManager = new ListTemplateItemManager();
            listTemplateItemManager.delete(listTemplateItemId);
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }
}
