var ListInstanceEditPageView = {

    data:null,
    listInstanceId:null,


    refresh: function(){
    },

    show: function(){
    },

    load: function(listInstanceXml){
        this.listInstanceId = $(listInstanceXml).attr("id");


        var listName = $(listInstanceXml).find("name").first().text();
        $('#pageEditListInstanceHeader').html(listName);
        $('#txtListName')[0].value = listName;
        var theHtml = '';

        var listInstanceItems = $(listInstanceXml).find("listInstanceItemRelationshipEntries").find("listInstanceItem");
        var lastListElement = listInstanceItems.size() - 1;
        var i = 0;
        listInstanceItems .each(function() {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<tr><td>' + currEntity.find("name").first().text() + '</td>';
            theHtml += '<td>' + currEntity.find("description").first().text() + '</td>';
            theHtml += '<td>' + currEntity.find("amount").first().text() + '</td>';
            theHtml += '<td><a HREF=\"javascript:ListInstanceEditPageView.removeItem(\'' + currEntityId + '\')\">Remove</a>';

            if (i != lastListElement){
                theHtml += '&nbsp;<img src="/images/down.png" onclick="ListInstanceEditPageView.pushItemDown(\'' + currEntityId + '\')"/>';
            }

            if (i != 0){
                theHtml += '&nbsp;<img src="/images/up.png" onclick="ListInstanceEditPageView.pushItemUp(\'' + currEntityId + '\')"/>';
            }

            theHtml += '</td></tr>';
            ++i;


        });

        //theHtml += '<tr><td>Item:<input type="text" id="txtNewItemName"/></td><td>description:<input type="text" id="txtNewItemDescription"/></td><td>Default Amount:<input type="number" min="1" id="txtNewItemDefaultAmount"/></td><td><a HREF="javascript:ListInstanceEditPageView.createNewItem()">Add</a></td></tr>';

        document.getElementById('tabListInstanceItems').tBodies[0].innerHTML = theHtml;
        $("#tabListInstanceItems").table('refresh');
        //$("#txtListName").text('refresh');
    },

    createNewItem: function(itemName, itemDescription, itemAmount){
        ShpanlistController.addNewListInstanceItem(
            ListInstanceEditPageView.listInstanceId,
            itemName, itemDescription, itemAmount, function(responseXml){
                ShpanlistController.menuEditListInstance(ListInstanceEditPageView.listInstanceId);
            });
    },

    removeItem: function(listInstanceItemId){
        var conf = confirm("Are you sure you want to remove this item?");
        if (conf){
            ShpanlistController.removeListInstanceItem(ListInstanceEditPageView.listInstanceId, listInstanceItemId);
        }
    },

    pushItemUp: function(listInstanceItemId){
        ShpanlistController.pushListInstanceItemUp(ListInstanceEditPageView.listInstanceId, listInstanceItemId);
    },
    pushItemDown: function(listInstanceItemId){
        ShpanlistController.pushListInstanceItemDown(ListInstanceEditPageView.listInstanceId, listInstanceItemId);
    },

    updateName: function(listName){
        if (listName == null || listName.length == 0){
            alert('list Name must not be empty');
        }else{
            ShpanlistController.updateListInstanceName(ListInstanceEditPageView.listInstanceId, listName);
        }

    }


};
