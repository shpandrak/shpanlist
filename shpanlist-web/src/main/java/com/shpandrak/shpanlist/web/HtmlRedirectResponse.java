package com.shpandrak.shpanlist.web;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/13/13
 * Time: 18:36
 */
public class HtmlRedirectResponse {
    private final String targetPage;

    public HtmlRedirectResponse(String targetPage) {
        this.targetPage = targetPage;
    }

    public String getTargetPage() {
        return targetPage;
    }
}
