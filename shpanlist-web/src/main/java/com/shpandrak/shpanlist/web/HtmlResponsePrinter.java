package com.shpandrak.shpanlist.web;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/13/13
 * Time: 11:11
 */
public class HtmlResponsePrinter {
    private final List<HtmlBindingResponse> bindings;
    private final List<HtmlRefreshResponse> refreshResponses;
    private final HtmlRedirectResponse redirectResponse;

    public static HtmlResponsePrinter redirectResponse(String targetPage){
        return new HtmlResponsePrinter(Collections.<HtmlBindingResponse>emptyList(), Collections.<HtmlRefreshResponse>emptyList(), new HtmlRedirectResponse(targetPage));
    }
    public static HtmlResponsePrinter emptyResponse(){
        return new HtmlResponsePrinter(Collections.<HtmlBindingResponse>emptyList(), Collections.<HtmlRefreshResponse>emptyList(), null);
    }

    public HtmlResponsePrinter(List<HtmlBindingResponse> bindings, List<HtmlRefreshResponse> refreshResponses) {
        this(bindings, refreshResponses, null);
    }
    public HtmlResponsePrinter(List<HtmlBindingResponse> bindings, List<HtmlRefreshResponse> refreshResponses, HtmlRedirectResponse redirectResponse) {
        this.bindings = bindings;
        this.refreshResponses = refreshResponses;
        this.redirectResponse = redirectResponse;
    }

    public List<HtmlBindingResponse> getBindings() {
        return bindings;
    }

    public List<HtmlRefreshResponse> getRefreshResponses() {
        return refreshResponses;
    }

    public HtmlRedirectResponse getRedirectResponse() {
        return redirectResponse;
    }

    public void printResponse(PrintWriter writer) {
        if (bindings != null && !bindings.isEmpty()){
            boolean first = true;
            writer.println(",\"bindings\":[");
            for (HtmlBindingResponse currBinding : bindings){
                if (first){
                    first = false;
                }else {
                    writer.println(",");
                }
                writer.append("{\"id\":\"").append(currBinding.getId()).append("\",\"html\":\"").append(currBinding.getHtml()).append("\"");
                if (currBinding.isOuter()){
                    writer.append(",\"outer\":true");
                }
                writer.append("}");
            }
            writer.println("]");
        }

        if (refreshResponses != null && !refreshResponses.isEmpty()){
            writer.println(",\"refreshResponses\":[");
            boolean first = true;
            for (HtmlRefreshResponse currRefreshResponse : refreshResponses){
                if (first){
                    first = false;
                }else {
                    writer.println(",");
                }
                writer.append("{\"id\":\"").append(currRefreshResponse.getId()).append("\",\"type\":\"").append(currRefreshResponse.getType()).append("\"}");
            }
            writer.println("]");

        }

        if (redirectResponse != null){
            writer.append(",\"redirectResponse\":{\"target\":\"").append(redirectResponse.getTargetPage()).append("\"}");
        }

    }
}
