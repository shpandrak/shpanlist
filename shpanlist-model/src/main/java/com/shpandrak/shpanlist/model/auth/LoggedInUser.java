package com.shpandrak.shpanlist.model.auth;

import java.io.Serializable;

/**
 * Created with love
 * User: shpandrak
 * Date: 5/7/13
 * Time: 17:45
 */
public class LoggedInUser implements Serializable {
    private final String userName;

    public LoggedInUser(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
