var HomePageView = {

    refresh: function(){
    },

    show:function() {
    },

    load:function(userDataXml) {
    },


    removeItem: function(listTemplateItemId){
        ShpanlistController.removeListTemplateItem(ListTemplatePageView.listTemplateId, listTemplateItemId);
    },

    createListFromTemplate: function(){
        ShpanlistController.createListFromTemplate(ListTemplatePageView.listTemplateId);
    },

    removeListInstance: function removeListInstance(listInstanceId){
        var conf = confirm("Are you sure you want to remove this list?");
        if (conf){
            ShpanlistController.removeListInstance(listInstanceId);
        }
    }

};
