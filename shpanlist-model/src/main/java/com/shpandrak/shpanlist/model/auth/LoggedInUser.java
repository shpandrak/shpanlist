package com.shpandrak.shpanlist.model.auth;

import com.shpandrak.datamodel.field.Key;

import java.io.Serializable;

/**
 * Created with love
 * User: shpandrak
 * Date: 5/7/13
 * Time: 17:45
 */
public class LoggedInUser implements Serializable {
    private final Key userId;
    private final String userName;

    public LoggedInUser(Key userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public Key getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
