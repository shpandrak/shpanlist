package com.shpandrak.shpanlist.web;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/13/13
 * Time: 18:36
 */
public class HtmlRefreshResponse {
    private final String id;
    private final String type;

    public HtmlRefreshResponse(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
