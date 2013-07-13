package com.shpandrak.shpanlist.gwt.persistence.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.shpanlist.gwt.persistence.shared.ListUserGWTService;
import com.shpandrak.shpanlist.model.ListUser;
import com.shpandrak.shpanlist.services.ListUserService;

import java.util.List;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/5/13
 * Time: 15:38
 */
public class ListUserGWTServiceImpl extends RemoteServiceServlet implements ListUserGWTService {
    @Override
    public String getFirstUserName() {
        return "Moshe";
    }

    @Override
    public ListUser findByUserName(String userName) throws PersistenceException {
        return ListUserService.findByUserName(userName);
    }

}
