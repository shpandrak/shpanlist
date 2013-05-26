$( document ).on( 'pageinit', '#pageListInstance', function(event, data){
    ListInstanceView.show();
});

$( document ).on( 'pageinit', '#pageListTemplate', function(event, data){
    ListTemplateView.show();
});

$( document ).on( 'pageinit', '#pageMain', function(event, data){
    ShpanlistController.loadApp();
});

$( document ).on( 'pageinit', '#pageHome', function(event, data){
    HomeView.show();
});

$( document ).on( 'pageinit', '#pageEditListInstance', function(event, data){
    ListInstanceEditView.show();
});

