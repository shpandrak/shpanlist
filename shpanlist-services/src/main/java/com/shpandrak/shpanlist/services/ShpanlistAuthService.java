package com.shpandrak.shpanlist.services;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.shpanlist.gae.datastore.ListUserManager;
import com.shpandrak.shpanlist.model.ListUser;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;

/**
 * Created with love
 * User: shpandrak
 * Date: 5/7/13
 * Time: 17:43
 */
public abstract class ShpanlistAuthService {

    public static ListUser signIn(String userName, String password) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListUserManager listUserManager = new ListUserManager();
            ListUser byField = listUserManager.getByField(ListUser.DESCRIPTOR.userNameFieldDescriptor, userName);
            if (byField != null && byField.getShpanPassword().equals(password)){
                return byField;
            }
            return null;
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }
}
