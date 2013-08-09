package com.shpandrak.shpanlist.services;

import com.shpandrak.datamodel.OrderByClauseEntry;
import com.shpandrak.datamodel.field.EntityKey;
import com.shpandrak.datamodel.field.Key;
import com.shpandrak.datamodel.relationship.RelationshipLoadLevel;
import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.persistence.query.filter.*;
import com.shpandrak.shpanlist.gae.datastore.ListInstanceItemManager;
import com.shpandrak.shpanlist.gae.datastore.ListInstanceManager;
import com.shpandrak.shpanlist.gae.datastore.ListTemplateManager;
import com.shpandrak.shpanlist.model.ListInstance;
import com.shpandrak.shpanlist.model.ListInstanceItem;
import com.shpandrak.shpanlist.model.ListTemplate;
import com.shpandrak.shpanlist.model.ListTemplateItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with love
 * User: shpandrak
 * Date: 5/12/13
 * Time: 16:29
 */
public abstract class ListInstanceService {
    private static final Logger logger = LoggerFactory.getLogger(ListInstanceService.class);
    private static final SimpleDateFormat shortDayDate = new SimpleDateFormat("MMM-dd");

    public static ListInstance getListInstanceFull(Key listInstanceId) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListInstanceManager listInstanceManager = new ListInstanceManager();
            return listInstanceManager.getById(listInstanceId, RelationshipLoadLevel.FULL);
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

            Date creationDate = new Date();
            String listName = listTemplate.getName() + " " + shortDayDate.format(creationDate);

            // Creating the list object
            ListInstance listInstance = new ListInstance(
                    listName,
                    creationDate,
                    null,
                    creatingUserId);

            PersistenceLayerManager.getConnectionProvider().beginTransaction();

            listInstanceManager.create(listInstance);



            // Copying the list template items
            Collection<ListTemplateItem> listTemplateItems = listTemplate.getListTemplateItemRelationship().getTargetEntities();
            if (!listTemplateItems.isEmpty()){
                List<ListInstanceItem> listInstanceItems = new ArrayList<ListInstanceItem>(listTemplateItems.size());
                for (ListTemplateItem currItemTemplate : listTemplateItems){
                    listInstanceItems.add(new ListInstanceItem(listInstance, currItemTemplate.getName(), currItemTemplate.getItemOrder(), currItemTemplate.getDescription(), currItemTemplate.getDefaultAmount(), false, null));
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

    public static void removeListInstanceItem(Key listInstanceItemId) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListInstanceItemManager listInstanceItemManager = new ListInstanceItemManager();
            listInstanceItemManager.delete(listInstanceItemId);
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }

    public static void removeListInstance(EntityKey listInstanceId) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListInstanceManager listInstanceManager = new ListInstanceManager();
            listInstanceManager.updateFieldValueById(ListInstance.DESCRIPTOR.deletionDateFieldDescriptor, new Date(), listInstanceId);
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }

    public static void pushListInstanceItemDown(Key listInstanceItemId) throws PersistenceException {
        //todo:limit fetch to return 1 item
        //todo:tx error handling
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try {
            ListInstanceItemManager listInstanceItemManager = new ListInstanceItemManager();
            PersistenceLayerManager.getConnectionProvider().beginTransaction();

            ListInstanceItem byId = listInstanceItemManager.getById(listInstanceItemId);
            Integer itemOrder = byId.getItemOrder();
            // Searching if there is an item after current

            List<ListInstanceItem> items = listInstanceItemManager.list(new QueryFilter(
                    new CompoundFieldFilterCondition(
                            FieldFilterLogicalOperatorType.AND,
                            new RelationshipFilterCondition(ListInstanceItem.DESCRIPTOR.listInstanceRelationshipDescriptor, byId.getListInstanceId()),
                            BasicFieldFilterCondition.build(ListInstanceItem.DESCRIPTOR.itemOrderFieldDescriptor, FilterConditionOperatorType.GREATER_THEN, itemOrder)), null, null,
                    Arrays.asList(new OrderByClauseEntry(ListInstanceItem.DESCRIPTOR.itemOrderFieldDescriptor, true))));

            if (items.isEmpty()) {
                logger.warn("Skip pushing down last item {}", byId);
            } else {
                ListInstanceItem nextItem = items.get(0);
                // Updating both fields
                listInstanceItemManager.updateFieldValueById(ListInstanceItem.DESCRIPTOR.itemOrderFieldDescriptor, nextItem.getItemOrder(), byId.getId());
                listInstanceItemManager.updateFieldValueById(ListInstanceItem.DESCRIPTOR.itemOrderFieldDescriptor, itemOrder, nextItem.getId());
            }
            PersistenceLayerManager.getConnectionProvider().commitTransaction();
        } catch (PersistenceException pex) {
            //todo:better handle tx exception
            PersistenceLayerManager.getConnectionProvider().rollbackTransaction();
            throw pex;
        } catch (Exception ex) {
            PersistenceLayerManager.getConnectionProvider().rollbackTransaction();
            throw new RuntimeException(
            );
        } finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }

    /**
     * moves list item up by one and replace with previous item on list
     *
     * @param listInstanceItemId item id to push up
     * @return list instance item id switched with current item position, or null if there was no switch
     * @throws PersistenceException
     */
    public static Key pushListInstanceItemUp(Key listInstanceItemId) throws PersistenceException {
        //todo:tx error handling
        //todo:limit fetch to return 1 item
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try {
            PersistenceLayerManager.getConnectionProvider().beginTransaction();
            ListInstanceItemManager listInstanceItemManager = new ListInstanceItemManager();
            ListInstanceItem byId = listInstanceItemManager.getById(listInstanceItemId);
            Integer itemOrder = byId.getItemOrder();
            if (itemOrder == 1) {
                logger.warn("Skip pushing up item on top order {}", byId);
                return null;
            } else {
                // Searching if there is an item before current
                List<ListInstanceItem> items = listInstanceItemManager.list(new QueryFilter(
                        new CompoundFieldFilterCondition(
                                FieldFilterLogicalOperatorType.AND,
                                new RelationshipFilterCondition(ListInstanceItem.DESCRIPTOR.listInstanceRelationshipDescriptor, byId.getListInstanceId()),
                                BasicFieldFilterCondition.build(ListInstanceItem.DESCRIPTOR.itemOrderFieldDescriptor, FilterConditionOperatorType.LESS_THEN, itemOrder)),
                        null,
                        null,
                        Arrays.asList(new OrderByClauseEntry(ListInstanceItem.DESCRIPTOR.itemOrderFieldDescriptor, false))));

                if (items.isEmpty()) {
                    throw new IllegalStateException("Failed pushing up item - could not find previous item " + byId);
                } else {
                    ListInstanceItem previousItem = items.get(0);
                    // Updating both fields

                    listInstanceItemManager.updateFieldValueById(ListInstanceItem.DESCRIPTOR.itemOrderFieldDescriptor, previousItem.getItemOrder(), byId.getId());
                    listInstanceItemManager.updateFieldValueById(ListInstanceItem.DESCRIPTOR.itemOrderFieldDescriptor, itemOrder, previousItem.getId());
                    PersistenceLayerManager.getConnectionProvider().commitTransaction();
                    return previousItem.getId();
                }
            }
        } catch (PersistenceException pex) {
            //todo:better handle tx exception
            PersistenceLayerManager.getConnectionProvider().rollbackTransaction();
            throw pex;
        } catch (Exception ex) {
            PersistenceLayerManager.getConnectionProvider().rollbackTransaction();
            throw new RuntimeException(ex);
        } finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }

    public static void addListInstanceItem(Key listInstanceId, String listInstanceItemName, String listInstanceItemDescription, Integer amount, String imageURL) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try {
            //todo:transaciton
            ListInstanceItemManager listInstanceItemManager = new ListInstanceItemManager();
            List<ListInstanceItem> existingItems = listInstanceItemManager.list(new QueryFilter(new RelationshipFilterCondition(ListInstanceItem.DESCRIPTOR.listInstanceRelationshipDescriptor, listInstanceId), null, null, Arrays.asList(new OrderByClauseEntry(ListInstanceItem.DESCRIPTOR.itemOrderFieldDescriptor, true))));
            int itemOrdinal = 1;
            if (!existingItems.isEmpty()) {
                itemOrdinal = existingItems.get(existingItems.size() - 1).getItemOrder() + 1;
            }
            listInstanceItemManager.create(new ListInstanceItem(listInstanceId, listInstanceItemName, itemOrdinal, listInstanceItemDescription, amount, false, imageURL));
        } finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }

    public static List<ListInstance> getUserLists(Key userId) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try {
            ListInstanceManager listInstanceManager = new ListInstanceManager();
            return listInstanceManager.list(
                    new QueryFilter(
                            FieldFilterCondition.concatenate(
                                    FieldFilterLogicalOperatorType.AND,
                                    new RelationshipFilterCondition(ListInstance.DESCRIPTOR.createdByUserRelationshipDescriptor, userId),
                                    new BasicFieldFilterCondition<Date>(ListInstance.DESCRIPTOR.deletionDateFieldDescriptor, FilterConditionOperatorType.EQUALS, null)),
                                    null, null, Arrays.asList(new OrderByClauseEntry(ListInstance.DESCRIPTOR.creationDateFieldDescriptor, false))));
        } finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }


    public static void updateListInstanceName(Key listInstanceId, String listInstanceName) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try {
            ListInstanceManager listInstanceManager = new ListInstanceManager();
            listInstanceManager.updateFieldValueById(ListInstance.DESCRIPTOR.nameFieldDescriptor, listInstanceName, listInstanceId);
        } finally {
            PersistenceLayerManager.endJointConnectionSession();
        }

    }

    public static ListInstance createNewListInstance(Key userId) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try {
            ListInstanceManager listInstanceManager = new ListInstanceManager();
            Date nowDate = new Date();
            ListInstance newListInstance = new ListInstance("New List " + shortDayDate.format(nowDate), nowDate, null, userId);
            listInstanceManager.create(newListInstance);
            return newListInstance;

        } finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }

    public static ListInstanceItem getListInstanceItem(EntityKey listInstanceItemKey) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try {
            ListInstanceItemManager listInstanceItemManager = new ListInstanceItemManager();
            return listInstanceItemManager.getById(listInstanceItemKey);

        } finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }
}
