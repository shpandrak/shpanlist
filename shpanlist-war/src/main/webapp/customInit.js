$( document ).on( 'pagebeforeshow', '#pageListGroups', function(event, data){
    ListGroupsView.show(event, data);
});

$( document ).on( 'pagebeforeshow', '#pageListGroup', function(event, data){
    ListGroupView.show(event, data);
});

$( document ).on( 'pagebeforeshow', '#pageListTemplate', function(event, data){
    ListTemplateView.show(event, data);
});

$( document ).on( 'pagebeforeshow', '#pageMain', function(event, data){
    ShpanlistController.loadApp();
});

