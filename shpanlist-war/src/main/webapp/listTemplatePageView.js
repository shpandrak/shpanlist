var ListTemplatePageView = {
    listTemplateId:null,

    refresh: function(){
    },

    show: function(){
    },

    load: function(listTemplateXml){
    },

    createNewItem: function(){
        ShpanlistController.addNewListTemplateItem(
            ListTemplatePageView.listTemplateId,
            document.getElementById('txtListTemplateItemName').value,
            document.getElementById('txtListTemplateItemDescription').value,
            document.getElementById('txtListTemplateItemAmount').value, function(){
                ShpanlistController.menuListTemplate(ListTemplatePageView.listTemplateId);
        });
    },

    removeItem: function(listTemplateItemId){
        var conf = confirm("Are you sure you want to remove this item?");
        if (conf){
            ShpanlistController.removeListTemplateItem(ListTemplatePageView.listTemplateId, listTemplateItemId);
        }
    },

    pushItemUp: function(listTemplateItemId){
        ShpanlistController.pushListTemplateItemUp(ListTemplatePageView.listTemplateId, listTemplateItemId);
    },
    pushItemDown: function(listTemplateItemId){
        ShpanlistController.pushListTemplateItemDown(ListTemplatePageView.listTemplateId, listTemplateItemId);
    },

    createListFromTemplate: function(){
        ShpanlistController.createListFromTemplate(ListTemplatePageView.listTemplateId);
    },

    updateName: function(listName){
        if (listName == null || listName.length == 0){
            alert('list Name must not be empty');
        }else{
            ShpanlistController.updateListTemplateName(ListTemplatePageView.listTemplateId, listName);
        }

    }


}
