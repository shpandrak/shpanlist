$( document ).on( 'pageinit', '#pageListGroups', function(event, data){
    ListGroupsView.show();
});

$( document ).on( 'pageinit', '#pageListInstance', function(event, data){
    ListInstanceView.show();
});

$( document ).on( 'pageinit', '#pageListGroup', function(event, data){
    ListGroupView.show();
});

$( document ).on( 'pageinit', '#pageListTemplate', function(event, data){
    ListTemplateView.show();
});

$( document ).on( 'pagebeforeshow', '#pageMain', function(event, data){
    ShpanlistController.loadApp();
});

