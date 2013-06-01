var ListInstanceView = {

    data:null,
    listInstanceId:null,

    refresh: function(){
        this.data = null;
        this.show();
    },

    show: function(){
        var listInstanceXml = this.data;

        if (listInstanceXml == null){
            var listInstanceId = localStorage['listInstanceId'];
            if (listInstanceId){
                ShpanlistController.getListInstanceFull(listInstanceId, function(responseXml){
                    ListInstanceView.load(responseXml)
                });
            }else{
                ShpanlistController.signOut();
            }

        }else{
            ListInstanceView.load(listInstanceXml);
        }
    },

    load: function(listInstanceXml){
        this.listInstanceId = $(listInstanceXml).attr("id");

        var listInstanceName = $(listInstanceXml).find("name").first().text();
        $('#pageListInstanceHeaderDiv').html(listInstanceName);

        var theHtml = '';
        $(listInstanceXml).find("listInstanceItemRelationshipEntries").find("listInstanceItem") .each(function() {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            var currRowHtml = currEntity.find("name").first().text();
            var desc = currEntity.find("description").first().text();
            if (desc != null && desc.length > 0){
                currRowHtml += '- ' + desc;
            }
            var amount = currEntity.find("amount").first().text();
            if (amount != null && amount.length > 0){
                currRowHtml += '(' + amount + ')';
            }

            if (currEntity.find("gotIt").first().text() == 'true'){

                theHtml += '<li style="text-decoration: line-through">' +
                    '<a HREF=\"javascript:ListInstanceView.bringBackItem(\'' + currEntityId + '\')\">' +
                    currRowHtml +
                    '</a></del></li>';
            }else{
                theHtml += '<li><a HREF=\"javascript:ListInstanceView.gotItem(\'' + currEntityId + '\')\">' +
                    currRowHtml +
                    '</a></li>';
            }
        });

        var lstItems = $("#lstItems");

        lstItems.html(theHtml);
        lstItems.listview('refresh');
    },

    createNewItem: function(){
        ShpanlistController.addNewListTemplateItem(ListInstanceView.listInstanceId, txtNewItemName.value, txtNewItemDescription.value, txtNewItemDefaultAmount.value)
    },

    gotItem: function(listInstanceItemId){
        ShpanlistController.gotListInstanceItem(ListInstanceView.listInstanceId, listInstanceItemId, function(responseXml){
            ListInstanceView.refresh();
        });
    },

    bringBackItem: function(listInstanceItemId){
        ShpanlistController.bringBackItem(ListInstanceView.listInstanceId, listInstanceItemId, function(responseXml){
            ListInstanceView.refresh();
        });
    }


}
