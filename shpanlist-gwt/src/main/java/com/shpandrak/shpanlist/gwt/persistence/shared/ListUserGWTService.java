package com.shpandrak.shpanlist.gwt.persistence.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.shpanlist.model.ListUser;

import java.util.List;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/5/13
 * Time: 15:35
 */
@RemoteServiceRelativePath("ListUserGWTService")
public interface ListUserGWTService extends RemoteService{

    String getFirstUserName();

    ListUser findByUserName(String userName) throws PersistenceException;
}
