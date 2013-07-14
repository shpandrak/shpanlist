$( document ).on( 'pagebeforeshow', '#pageListTemplate', function(event, data){
    ListTemplateView.show();
});

$( document ).on( 'pagebeforeshow', '#pageMain', function(event, data){
    ShpanlistController.loadApp();
});

$( document ).on( 'pagebeforeshow', '#pageEditListInstance', function(event, data){
    ListInstanceEditView.show();
});

