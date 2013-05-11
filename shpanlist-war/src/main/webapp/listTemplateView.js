var ListTemplateView = {

    mainFrame:undefined,
    listTemplateId:null,

    show: function(listTemplateXml){
        var listTemplateDoc = $(listTemplateXml).find("listTemplate").first();
        this.listTemplateId = $(listTemplateXml).find("listTemplate").first().attr("id");
        var listTemplateName = $(listTemplateXml).find("name").first().text();
        var theHtml = '<h2>' + listTemplateName + '</h2><table id="tabListTemplateItems">';

        $(listTemplateXml).find("listTemplateItemRelationshipEntries").find("listTemplateItem") .each(function() {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<tr><td>' + currEntity.find("name").first().text() + '</td>';
            theHtml += '<td>' + currEntity.find("description").first().text() + '</td>';
            theHtml += '<td><a HREF=\"javascript:ListTemplateView.removeItem(\'' + currEntityId + '\')\">Remove</a></td></tr>';

        });

        theHtml += '<tr><td>Item:<input type="text" id="txtNewItemName"/></td><td>description:<input type="text" id="txtNewItemDescription"/></td><td><a HREF="javascript:ListTemplateView.createNewItem()">Add</a></td></tr>';

        this.mainFrame.innerHTML = theHtml + '</table>';
    },

    createNewItem: function(){
        ShpanlistController.addNewListTemplateItem(ListTemplateView.listTemplateId, txtNewItemName.value, txtNewItemDescription.value)
    },

    removeItem: function(listTemplateItemId){
        ShpanlistController.removeListTemplateItem(ListTemplateView.listTemplateId, listTemplateItemId);
    }
}
