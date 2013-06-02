$( document ).on( 'pagebeforeshow', '#pageListInstance', function(event, data){
    ListInstanceView.show();
});

$( document ).on( 'pagebeforeshow', '#pageListTemplate', function(event, data){
    ListTemplateView.show();
});

$( document ).on( 'pagebeforeshow', '#pageMain', function(event, data){
    ShpanlistController.loadApp();
});

$( document ).on( 'pagebeforeshow', '#pageHome', function(event, data){
    HomeView.show();
});

$( document ).on( 'pagebeforeshow', '#pageEditListInstance', function(event, data){
    ListInstanceEditView.show();
});

