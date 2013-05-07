package com.shpandrak.shpanlist.services.auth;

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

    public static LoggedInUser signIn(String userName, String password) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListUserManager listUserManager = new ListUserManager();
            ListUser byField = listUserManager.getByField(ListUser.DESCRIPTOR.userNameFieldDescriptor, userName);
            if (byField != null && byField.getShpanPassword().equals(password)){
                return new LoggedInUser(byField.getUserName());
            }
            return null;
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }
}
