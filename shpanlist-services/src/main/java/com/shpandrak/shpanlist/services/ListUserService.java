package com.shpandrak.shpanlist.services;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.persistence.PersistenceLayerManager;
import com.shpandrak.shpanlist.gae.datastore.ListTemplateItemManager;
import com.shpandrak.shpanlist.gae.datastore.ListUserManager;
import com.shpandrak.shpanlist.model.*;

import java.util.Date;

/**
 * Created with love
 * User: shpandrak
 * Date: 5/13/13
 * Time: 23:25
 */
public abstract class ListUserService {
    public static ListUser createUser(String userName, String password, String firstName, String lastName, String email) throws PersistenceException {
        if (userName == null || userName.isEmpty()) throw new IllegalArgumentException("userName can't be empty");
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListUserManager listUserManager = new ListUserManager();
            ListTemplateItemManager listTemplateItemManager = new ListTemplateItemManager();

            // Checking if this userName already exists
            ListUser existingUser = listUserManager.getByField(ListUser.DESCRIPTOR.userNameFieldDescriptor, userName);
            if (existingUser != null){
                throw new IllegalStateException("User " + userName + " already exists");
            }

            ListUser newUser = new ListUser(userName, email, firstName, lastName, null, password, null, null, new Date());

            listUserManager.create(newUser);

            // Creating default list Template
            ListTemplate listTemplate = ListTemplateService.createListTemplate(newUser.getId(), newUser.getUserName() + "'s Default List Template");
            listTemplateItemManager.create(new ListTemplateItem(listTemplate, "Milk", 1, "Creamy 3% fat milk", 1));

            return newUser;


        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }

    public static ListUser findByUserName(String userName) throws PersistenceException {
        PersistenceLayerManager.beginOrJoinConnectionSession();
        try{
            ListUserManager listUserManager = new ListUserManager();
            return listUserManager.getByField(ListUser.DESCRIPTOR.userNameFieldDescriptor, userName);
        }finally {
            PersistenceLayerManager.endJointConnectionSession();
        }
    }
}
