package com.shpandrak.shpanlist.services;

import com.shpandrak.datamodel.field.Key;
import com.shpandrak.datamodel.relationship.RelationshipLoadLevel;
import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.persistence.query.filter.QueryFilter;
import com.shpandrak.persistence.query.filter.RelationshipFilterCondition;
import com.shpandrak.shpanlist.gae.datastore.ListGroupManager;
import com.shpandrak.shpanlist.model.ListGroup;
import com.shpandrak.shpanlist.model.ListGroupMemberRelationshipEntry;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;

import java.util.*;

/**
 * Created with love
 * User: shpandrak
 * Date: 5/7/13
 * Time: 23:46
 */
public abstract class ListGroupService {
    public static ListGroup createListGroup(Key userKey, String groupName) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListGroupManager listGroupManager = new ListGroupManager();
            Date joinDate = new Date();
            ListGroup listGroup = new ListGroup(groupName, joinDate, userKey);
            listGroup.getMemberRelationship().addNewRelation(new ListGroupMemberRelationshipEntry(userKey, joinDate));
            listGroupManager.create(listGroup);
            return listGroup;

        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }

    public static List<ListGroup> getListGroups(LoggedInUser loggedInUser) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListGroupManager listGroupManager = new ListGroupManager();
            Map<Key,ListGroup> groupsUserOwns = listGroupManager.getMapById(new QueryFilter(new RelationshipFilterCondition(ListGroup.DESCRIPTOR.ownerUserRelationshipDescriptor, loggedInUser.getUserId())));
            Map<Key,ListGroup> groupsUserIsMemberOf = new HashMap<Key, ListGroup>(
                    listGroupManager.getMapById(new QueryFilter(new RelationshipFilterCondition(ListGroup.DESCRIPTOR.memberRelationshipDescriptor, loggedInUser.getUserId()))));

            groupsUserIsMemberOf.putAll(groupsUserOwns);
            return new ArrayList<ListGroup>(groupsUserIsMemberOf.values());



        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }

    public static ListGroup getListGroup(Key listGroupId) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListGroupManager listGroupManager = new ListGroupManager();
            return listGroupManager.getById(listGroupId, RelationshipLoadLevel.FULL);
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }
}
