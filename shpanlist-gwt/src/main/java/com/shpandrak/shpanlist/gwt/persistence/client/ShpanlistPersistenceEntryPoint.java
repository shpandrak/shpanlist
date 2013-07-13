package com.shpandrak.shpanlist.gwt.persistence.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.shpandrak.shpanlist.gwt.persistence.shared.ListUserGWTServiceAsync;
import com.shpandrak.shpanlist.model.ListUser;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/5/13
 * Time: 15:18
 */
public class ShpanlistPersistenceEntryPoint implements EntryPoint {
    @Override
    public void onModuleLoad() {
        Window.alert("Ok, I'm here...");
        ListUserGWTServiceAsync.Util.getInstance().findByUserName("shpandrak", new AsyncCallback<ListUser>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert("Bahhh..");
            }

            @Override
            public void onSuccess(ListUser listUser) {
                Window.alert("Nice!: " + listUser.getFirstName() + " " + listUser.getLastName());
                refreshMe("<li><a href='javascript::window.alert(''a'');'>dodo</a></li><li><a href='javascript::window.alert(''a'');'>dodo2</a></li>");
            }


        });
    }

    public static native void refreshMe(String theHtml)/*-{
        var lstItems = $wnd.jQuery("#lstItems");
        lstItems.html(theHtml);
        lstItems.listview('refresh');
    }-*/;

}
