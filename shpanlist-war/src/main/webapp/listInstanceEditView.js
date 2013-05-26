var ListInstanceEditView = {

    data:null,
    listInstanceId:null,


    refresh: function(){
        this.data = null;
        this.show();
    },

    show: function(){
        var listInstanceXml = this.data;

        if (listInstanceXml == null){
            ShpanlistController.getListInstanceFull(localStorage['listInstanceId'], function(responseXml){
                ListInstanceEditView.load(responseXml)
            });

        }else{
            ListInstanceEditView.load(listInstanceXml);
        }
    },

    load: function(listInstanceXml){
        this.listInstanceId = $(listInstanceXml).find("listInstance").first().attr("id");
        document.getElementById('pageEditListInstanceHeaderDiv').innerHTML = $(listInstanceXml).find("name").first().text();
        var theHtml = '';

        var listInstanceItems = $(listInstanceXml).find("listInstanceItemRelationshipEntries").find("listInstanceItem");
        var lastListElement = listInstanceItems.size() - 1;
        var i = 0;
        listInstanceItems .each(function() {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<tr><td>' + currEntity.find("name").first().text() + '</td>';
            theHtml += '<td>' + currEntity.find("description").first().text() + '</td>';
            theHtml += '<td>' + currEntity.find("defaultAmount").first().text() + '</td>';
            theHtml += '<td><a HREF=\"javascript:ListInstanceEditView.removeItem(\'' + currEntityId + '\')\">Remove</a>';

            if (i != lastListElement){
                theHtml += '&nbsp;<img src="/images/down.png" onclick="ListInstanceEditView.pushItemDown(\'' + currEntityId + '\')"/>';
            }

            if (i != 0){
                theHtml += '&nbsp;<img src="/images/up.png" onclick="ListInstanceEditView.pushItemUp(\'' + currEntityId + '\')"/>';
            }

            theHtml += '</td></tr>';
            ++i;


        });

        //theHtml += '<tr><td>Item:<input type="text" id="txtNewItemName"/></td><td>description:<input type="text" id="txtNewItemDescription"/></td><td>Default Amount:<input type="number" min="1" id="txtNewItemDefaultAmount"/></td><td><a HREF="javascript:ListInstanceEditView.createNewItem()">Add</a></td></tr>';

        document.getElementById('tabListInstanceItems').tBodies[0].innerHTML = theHtml;
        $("#tabListInstanceItems").table('refresh');
    },

    createNewItem: function(itemName, itemDescription, itemAmount){
        ShpanlistController.addNewListInstanceItem(
            ListInstanceEditView.listInstanceId,
            itemName, itemDescription, itemAmount, function(responseXml){
                ShpanlistController.menuListInstance(ListInstanceEditView.listInstanceId);
            });
    },

    removeItem: function(listInstanceItemId){
        ShpanlistController.removeListInstanceItem(ListInstanceEditView.listInstanceId, listInstanceItemId, function(){
            ListInstanceEditView.refresh();
        });
    },

    pushItemUp: function(listInstanceItemId){
        ShpanlistController.pushListInstanceItemUp(ListInstanceEditView.listInstanceId, listInstanceItemId, function(){
            ListInstanceEditView.refresh();
        });
    },
    pushItemDown: function(listInstanceItemId){
        ShpanlistController.pushListInstanceItemDown(ListInstanceEditView.listInstanceId, listInstanceItemId, function(){
            ListInstanceEditView.refresh();
        });
    },

    createListFromInstance: function(){
        ShpanlistController.createListFromInstance(ListInstanceEditView.listInstanceId);
    }

};
