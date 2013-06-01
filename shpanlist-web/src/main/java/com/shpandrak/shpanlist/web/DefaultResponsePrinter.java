package com.shpandrak.shpanlist.web;

import com.shpandrak.xml.XMLConverter;

import java.io.PrintWriter;
import java.util.Collection;

/**
 * Created with love
 * User: shpandrak
 * Date: 6/1/13
 * Time: 10:16
 */
public class DefaultResponsePrinter<T> implements ResponsePrinter{

    private XMLConverter<T> xmlConverter;
    private T entity;
    private Collection<T> entities;

    public DefaultResponsePrinter(XMLConverter<T> xmlConverter, T entity) {
        this.xmlConverter = xmlConverter;
        this.entity = entity;
    }

    public DefaultResponsePrinter(XMLConverter<T> xmlConverter, Collection<T> entities) {
        this.xmlConverter = xmlConverter;
        this.entities = entities;
    }

    @Override
    public void printResponse(PrintWriter writer) {
        if (entity != null){
            writer.print(xmlConverter.toXML(entity));
        }else if (entities != null){
            writer.print(xmlConverter.toXML(entities));
        }
    }
}
