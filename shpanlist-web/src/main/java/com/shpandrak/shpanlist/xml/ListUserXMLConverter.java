package com.shpandrak.shpanlist.xml;

import com.shpandrak.datamodel.BaseEntityDescriptor;
import com.shpandrak.datamodel.field.FieldInstance;
import com.shpandrak.shpanlist.model.ListUser;
import com.shpandrak.xml.EntityXMLConverter;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/21/13
 * Time: 23:39
 */
public class ListUserXMLConverter extends EntityXMLConverter<ListUser> {
    public ListUserXMLConverter(BaseEntityDescriptor<ListUser> entityDescriptor) {
        super(entityDescriptor);
    }

    @Override
    protected void appendField(StringBuilder sb, FieldInstance currField) {
        if (currField.getDescriptor().equals(ListUser.DESCRIPTOR.shpanPasswordFieldDescriptor)){
            sb
                    .append("<").append(currField.getName()).append(">")
                    .append("*************")
                    .append("</").append(currField.getName()).append(">");

        }else {
            super.appendField(sb, currField);
        }
    }
}
