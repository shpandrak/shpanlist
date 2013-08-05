var ListInstanceEditPageView = {

    listInstanceId:null,

    refresh: function(){
    },

    show: function(){
    },

    load: function(listInstanceXml){
    },


    getListInstanceId: function(){
        return document.getElementById('listInstanceId').value;
    },

    createNewItem: function(listInstanceId, itemName, itemDescription, itemAmount){
        ShpanlistController.addNewListInstanceItem(
            listInstanceId,
            itemName, itemDescription, itemAmount, function(responseXml){
                ShpanlistController.menuEditListInstance(ListInstanceEditPageView.getListInstanceId());
            });
    },

    removeItem: function(listInstanceItemId){
        var conf = confirm("Are you sure you want to remove this item?");
        if (conf){
            ShpanlistController.removeListInstanceItem(ListInstanceEditPageView.getListInstanceId(), listInstanceItemId);
        }
    },

    pushItemUp: function(listInstanceItemId){
        ShpanlistController.pushListInstanceItemUp(ListInstanceEditPageView.getListInstanceId(), listInstanceItemId);
    },
    pushItemDown: function(listInstanceItemId){
        ShpanlistController.pushListInstanceItemDown(ListInstanceEditPageView.getListInstanceId(), listInstanceItemId);
    },

    updateName: function(listName){
        if (listName == null || listName.length == 0){
            alert('list Name must not be empty');
        }else{
            ShpanlistController.updateListInstanceName(ListInstanceEditPageView.getListInstanceId(), listName);
        }
    }




};
