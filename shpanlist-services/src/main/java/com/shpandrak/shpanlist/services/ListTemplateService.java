package com.shpandrak.shpanlist.services;

import com.shpandrak.datamodel.OrderByClauseEntry;
import com.shpandrak.datamodel.field.Key;
import com.shpandrak.datamodel.relationship.RelationshipLoadLevel;
import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.persistence.query.filter.QueryFilter;
import com.shpandrak.persistence.query.filter.RelationshipFilterCondition;
import com.shpandrak.shpanlist.gae.datastore.ListTemplateItemManager;
import com.shpandrak.shpanlist.gae.datastore.ListTemplateManager;
import com.shpandrak.shpanlist.model.ListTemplate;
import com.shpandrak.shpanlist.model.ListTemplateItem;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    public static void addListTemplateItem(Key listTemplateId, String listTemplateItemName, String listTemplateItemDescription, Integer defaultAmount) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            //todo:transaciton
            ListTemplateItemManager listTemplateItemManager = new ListTemplateItemManager();
            List<ListTemplateItem> existingItems = listTemplateItemManager.list(new QueryFilter(new RelationshipFilterCondition(ListTemplateItem.DESCRIPTOR.listTemplateRelationshipDescriptor, listTemplateId), null, null, Arrays.asList(new OrderByClauseEntry(ListTemplateItem.DESCRIPTOR.itemOrderFieldDescriptor, false))));
            int itemOrdinal = 1;
            if (!existingItems.isEmpty()){
                itemOrdinal = existingItems.get(0).getItemOrder() + 1;
            }
            listTemplateItemManager.create(new ListTemplateItem(listTemplateId, listTemplateItemName, itemOrdinal, listTemplateItemDescription, defaultAmount));
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
