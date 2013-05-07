package com.shpandrak.shpanlist.services;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.shpanlist.gae.datastore.ListGroupManager;
import com.shpandrak.shpanlist.model.ListGroup;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;

import java.util.List;

/**
 * Created with love
 * User: shpandrak
 * Date: 5/7/13
 * Time: 23:46
 */
public abstract class ListGroupService {
    public static List<ListGroup> getListGroups(LoggedInUser loggedInUser) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListGroupManager listGroupManager = new ListGroupManager();
            return listGroupManager.listByMemberRelationship(loggedInUser.getUserId());
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }
}
