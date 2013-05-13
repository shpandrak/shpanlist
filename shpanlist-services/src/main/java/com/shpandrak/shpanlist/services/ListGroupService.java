package com.shpandrak.shpanlist.services;

import com.shpandrak.datamodel.field.Key;
import com.shpandrak.datamodel.relationship.RelationshipLoadLevel;
import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.shpanlist.gae.datastore.ListGroupManager;
import com.shpandrak.shpanlist.model.ListGroup;
import com.shpandrak.shpanlist.model.ListGroupMemberRelationshipEntry;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;

import java.util.Date;
import java.util.List;

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
            return listGroupManager.listByMemberRelationship(loggedInUser.getUserId());
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
