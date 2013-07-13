package com.shpandrak.shpanlist.web;

import java.io.PrintWriter;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/13/13
 * Time: 11:11
 */
public class HtmlBindingResponse {
    private final String id;
    private final String html;
    private final boolean outer;

    public HtmlBindingResponse(String id, String html, boolean outer) {
        this.id = id;
        this.html = html;
        this.outer = outer;
    }

    public HtmlBindingResponse(String id, String html) {
        this(id, html, false);
    }

    public String getId() {
        return id;
    }

    public String getHtml() {
        return html;
    }

    public boolean isOuter() {
        return outer;
    }
}
