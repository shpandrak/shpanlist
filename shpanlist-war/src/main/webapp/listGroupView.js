var ListGroupView = {

    mainFrame:undefined,
    listGroupId:null,

    show: function(listGroupXml){
        this.listGroupIdId = $(listGroupXml).find("listGroup").first().attr("id");
        var listGroupName = $(listGroupXml).find("name").first().text();
        var theHtml = '<h1>' + listGroupName + '</h1><br/><br/>';


        theHtml += '<h2>Active Lists</h2> <table id="tabListInstances">';
        $(listGroupXml).find("listInstanceRelationshipEntries").first().find("listInstance").each(function () {
            var currEntity = jQuery(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<tr><td>' + currEntity.find("name").first().text() + '</td>';
            theHtml += '<td><a HREF=\"javascript:ShpanlistController.menuListInstance(\'' + currEntityId + '\')\">Show List</a></td></tr>';
        });
        theHtml += '</table><br/><br/>';


        theHtml += '<h2>List Templates</h2> <table id="tabListInstances">';
        theHtml += '<table id="tabListTemplates">';
        $(listGroupXml).find("listTemplateRelationshipEntries").first().find("listTemplate").each(function () {
            var currEntity = jQuery(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<tr><td>' + currEntity.find("name").first().text() + '</td>';
            theHtml += '<td><a HREF=\"javascript:ShpanlistController.menuListTemplate(\'' + currEntityId + '\')\">Open Template</a></td></tr>';
        });
        theHtml += '</table>';






        this.mainFrame.innerHTML = theHtml;
    },

    createNewItem: function(){
        ShpanlistController.addNewListTemplateItem(ListTemplateView.listTemplateId, txtNewItemName.value, txtNewItemDescription.value, txtNewItemDefaultAmount.value)
    },

    removeItem: function(listTemplateItemId){
        ShpanlistController.removeListTemplateItem(ListTemplateView.listTemplateId, listTemplateItemId);
    },

    createListFromTemplate: function(){
        ShpanlistController.createListFromTemplate(ListTemplateView.listTemplateId);
    }

}
