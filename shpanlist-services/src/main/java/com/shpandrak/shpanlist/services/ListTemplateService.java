package com.shpandrak.shpanlist.services;

import com.shpandrak.datamodel.OrderByClauseEntry;
import com.shpandrak.datamodel.field.Key;
import com.shpandrak.datamodel.relationship.RelationshipLoadLevel;
import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.persistence.query.filter.*;
import com.shpandrak.shpanlist.gae.datastore.ListTemplateItemManager;
import com.shpandrak.shpanlist.gae.datastore.ListTemplateManager;
import com.shpandrak.shpanlist.model.ListTemplate;
import com.shpandrak.shpanlist.model.ListTemplateItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(ListTemplateService.class);
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

    public static void pushListTemplateItemDown(Key listTemplateItemId) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListTemplateItemManager listTemplateItemManager = new ListTemplateItemManager();
            ListTemplateItem byId = listTemplateItemManager.getById(listTemplateItemId);
            Integer itemOrder = byId.getItemOrder();
            // Searching if there is an item after current
            ListTemplateItem nextItem = listTemplateItemManager.findObject(new QueryFilter(
                    new CompoundFieldFilterCondition(
                            FieldFilterLogicalOperatorType.AND,
                            new RelationshipFilterCondition(ListTemplateItem.DESCRIPTOR.listTemplateRelationshipDescriptor, byId.getListTemplateId()),
                            BasicFieldFilterCondition.build(ListTemplateItem.DESCRIPTOR.itemOrderFieldDescriptor, FilterConditionOperatorType.EQUALS, itemOrder + 1))));
            if (nextItem == null){
                logger.warn("Skip pushing down last item {}", byId);
            }else{
                // Updating both fields
                PersistenceLayerManager.getConnectionProvider().beginTransaction();
                try{
                    listTemplateItemManager.updateFieldValueById(ListTemplateItem.DESCRIPTOR.itemOrderFieldDescriptor, itemOrder + 1, byId.getId());
                    listTemplateItemManager.updateFieldValueById(ListTemplateItem.DESCRIPTOR.itemOrderFieldDescriptor, itemOrder, nextItem.getId());
                    PersistenceLayerManager.getConnectionProvider().commitTransaction();
                }
                catch (PersistenceException pex){
                    //todo:better handle tx exception
                    PersistenceLayerManager.getConnectionProvider().rollbackTransaction();
                    throw pex;
                }
                catch (Exception ex){
                    PersistenceLayerManager.getConnectionProvider().rollbackTransaction();
                    throw new RuntimeException(ex);
                }
            }
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }

    public static void pushListTemplateItemUp(Key listTemplateItemId) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListTemplateItemManager listTemplateItemManager = new ListTemplateItemManager();
            ListTemplateItem byId = listTemplateItemManager.getById(listTemplateItemId);
            Integer itemOrder = byId.getItemOrder();
            if (itemOrder == 1){
                logger.warn("Skip pushing up item on top order {}", byId);
            }else{
                // Searching if there is an item before current
                ListTemplateItem previousItem = listTemplateItemManager.findObject(new QueryFilter(
                        new CompoundFieldFilterCondition(
                                FieldFilterLogicalOperatorType.AND,
                                new RelationshipFilterCondition(ListTemplateItem.DESCRIPTOR.listTemplateRelationshipDescriptor, byId.getListTemplateId()),
                                BasicFieldFilterCondition.build(ListTemplateItem.DESCRIPTOR.itemOrderFieldDescriptor, FilterConditionOperatorType.EQUALS, itemOrder - 1))));
                if (previousItem == null){
                    throw new IllegalStateException("Failed pushing up item - could not find previous item " + byId);
                }else{
                    // Updating both fields
                    PersistenceLayerManager.getConnectionProvider().beginTransaction();
                    try{
                        listTemplateItemManager.updateFieldValueById(ListTemplateItem.DESCRIPTOR.itemOrderFieldDescriptor, itemOrder - 1, byId.getId());
                        listTemplateItemManager.updateFieldValueById(ListTemplateItem.DESCRIPTOR.itemOrderFieldDescriptor, itemOrder, previousItem.getId());
                        PersistenceLayerManager.getConnectionProvider().commitTransaction();
                    }
                    catch (PersistenceException pex){
                        //todo:better handle tx exception
                        PersistenceLayerManager.getConnectionProvider().rollbackTransaction();
                        throw pex;
                    }
                    catch (Exception ex){
                        PersistenceLayerManager.getConnectionProvider().rollbackTransaction();
                        throw new RuntimeException(ex);
                    }
                }
            }
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }
}
