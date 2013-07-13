package com.shpandrak.shpanlist.web;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/13/13
 * Time: 11:11
 */
public class HtmlResponsePrinter {
    private List<HtmlBindingResponse> bindings;
    private List<HtmlRefreshResponse> refreshResponses;

    public HtmlResponsePrinter(List<HtmlBindingResponse> bindings, List<HtmlRefreshResponse> refreshResponses) {
        this.bindings = bindings;
        this.refreshResponses = refreshResponses;
    }

    public List<HtmlBindingResponse> getBindings() {
        return bindings;
    }

    public List<HtmlRefreshResponse> getRefreshResponses() {
        return refreshResponses;
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

    }
}
