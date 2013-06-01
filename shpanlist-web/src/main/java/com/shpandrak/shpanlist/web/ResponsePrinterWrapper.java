package com.shpandrak.shpanlist.web;

import java.io.PrintWriter;
import java.util.Collection;

/**
 * Created with love
 * User: shpandrak
 * Date: 6/1/13
 * Time: 10:22
 */
public class ResponsePrinterWrapper implements ResponsePrinter{
    private String wrapperTag;
    private Collection<ResponsePrinter> responsePrinters;

    public ResponsePrinterWrapper(String wrapperTag, Collection<ResponsePrinter> responsePrinters) {
        this.wrapperTag = wrapperTag;
        this.responsePrinters = responsePrinters;
    }

    @Override
    public void printResponse(PrintWriter writer) {
        writer.print("<" + wrapperTag + ">");
        for (ResponsePrinter currPrinter : responsePrinters){
            currPrinter.printResponse(writer);
        }
        writer.print("</" + wrapperTag + ">");
    }
}
