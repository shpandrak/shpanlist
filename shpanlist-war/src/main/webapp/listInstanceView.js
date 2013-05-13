var ListInstanceView = {

    mainFrame:undefined,
    listInstanceId:null,

    show: function(listInstanceXml){
        this.listInstanceId = $(listInstanceXml).find("listInstance").first().attr("id");
        var listInstanceName = $(listInstanceXml).find("name").first().text();
        var theHtml = '<h2>' + listInstanceName + '</h2><table id="tabListInstanceItems">';
        var listGroupId = $(listInstanceXml).find("listGroup").first().attr("id");

        $(listInstanceXml).find("listInstanceItemRelationshipEntries").find("listInstanceItem") .each(function() {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            var currRowHtml = '';
            currRowHtml += '<td>' + currEntity.find("name").first().text() + '</td>';
            currRowHtml += '<td>' + currEntity.find("description").first().text() + '</td>';
            currRowHtml += '<td>' + currEntity.find("amount").first().text() + '</td>';
            if (currEntity.find("gotIt").first().text() == 'true'){
                currRowHtml += '<td><a HREF=\"javascript:ListInstanceView.bringBackItem(\'' + currEntityId + '\')\">Oops</a></td></tr>';
                theHtml += '<tr style="text-decoration: line-through">' + currRowHtml + '</del></tr>';
            }else{
                currRowHtml += '<td><a HREF=\"javascript:ListInstanceView.gotItem(\'' + currEntityId + '\')\">Got It!</a></td></tr>';
                theHtml += '<tr>' + currRowHtml + '</tr>';
            }



        });

        theHtml += '</table><br/><a HREF="javascript:ShpanlistController.menuListGroup(\'' + listGroupId +'\')">Back to group page</a>' ;


        this.mainFrame.innerHTML = theHtml;
    },

    createNewItem: function(){
        ShpanlistController.addNewListTemplateItem(ListInstanceView.listInstanceId, txtNewItemName.value, txtNewItemDescription.value, txtNewItemDefaultAmount.value)
    },

    gotItem: function(listInstanceItemId){
        ShpanlistController.gotListInstanceItem(ListInstanceView.listInstanceId, listInstanceItemId);
    },

    bringBackItem: function(listInstanceItemId){
        ShpanlistController.bringBackItem(ListInstanceView.listInstanceId, listInstanceItemId);
    }


}
