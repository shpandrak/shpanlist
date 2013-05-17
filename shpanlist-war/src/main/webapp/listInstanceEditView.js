var ListInstanceEditView = {

    mainFrame:undefined,
    listInstanceId:null,

    show: function(listInstanceXml){
        this.listInstanceId = $(listInstanceXml).find("listInstance").first().attr("id");
        var listInstanceName = $(listInstanceXml).find("name").first().text();
        var theHtml = '<h2> Edit ' + listInstanceName + '</h2><table id="tabListInstanceItems">';
        var listGroupId = $(listInstanceXml).find("listGroup").first().attr("id");

        $(listInstanceXml).find("listInstanceItemRelationshipEntries").find("listInstanceItem") .each(function() {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            var currRowHtml = '';
            currRowHtml += '<td>' + currEntity.find("name").first().text() + '</td>';
            currRowHtml += '<td>' + currEntity.find("description").first().text() + '</td>';
            currRowHtml += '<td>' + currEntity.find("amount").first().text() + '</td>';
            currRowHtml += '<td><a HREF=\"javascript:ListInstanceEditView.removeItem(\'' + currEntityId + '\')\">Remove</a>';

            theHtml += '<tr>' + currRowHtml + '</tr>';

        });

        theHtml += '</table><br/><a HREF="javascript:ShpanlistController.menuListGroup(\'' + listGroupId +'\')">Back to group page</a>' ;


        this.mainFrame.innerHTML = theHtml;
    },

    createNewItem: function(){
        //ShpanlistController.addNewListInstanceItem(ListInstanceEditView.listInstanceId, txtNewItemName.value, txtNewItemDescription.value, txtNewItemDefaultAmount.value)
    },

    removeItem: function(listInstanceItemId){
        ShpanlistController.removeListInstanceItem(ListInstanceEditView.listInstanceId, listInstanceItemId)
    }
}
