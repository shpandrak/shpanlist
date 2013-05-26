var homeView = {
    data:null,

    refresh: function(){
        this.data = null;
        this.show();
    },

    show:function() {

        var userDataXml = this.data;

        if (userDataXml == null){
            ShpanlistController.listUserData(function(responseXml){
                homeView.load(responseXml)
            });

        }else{
            homeView.load(userDataXml);
        }
    },

    load:function(userDataXml) {


        var theHtml = '<li data-role="list-divider">Active Lists</li>';
        $(userDataXml).find("listInstanceRelationshipEntries").first().find("listInstance").each(function () {
            var currEntity = jQuery(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<li><a href="javascript:ShpanlistController.menuListInstance(\'' + currEntityId + '\')\">' + currEntity.find("name").first().text() + '</a>' +
                '<a href="javascript:HomeView.removeListInstance(\'' + currEntityId + '\')\">Remove</a></li>';
        });


        theHtml += '<li data-role="list-divider">List Templates</li>';
        $(userDataXml).find("listTemplateRelationshipEntries").first().find("listTemplate").each(function () {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<li>' +
                       '<a HREF=\"javascript:ShpanlistController.menuListTemplate(\'' + currEntityId + '\')\">' + currEntity.find("name").first().text() + '</a></li>';
        });

        //$('#lstLists').html(theHtml);
        document.getElementById('lstLists').innerHTML = theHtml;


        // Build group members table
        theHtml = '<h2>List Group members</h2><table id="tabGroupMembers">';
        $(userDataXml).find("memberRelationshipEntries").first().find("listUser").each(function () {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<tr><td>' + currEntity.find("firstName").first().text() + ' ' + currEntity.find("lastName").first().text() + '</td>';
            theHtml += '<td><a HREF=\"javascript:ShpanlistController.removeGroupMember(\'' + currEntityId + '\')\">Remove</a></td></tr>';
        });

        theHtml += '</table><br/>';
        theHtml += 'UserName:<input type="text" id="txtNewMemberUserName"/><a HREF="javascript:HomeView.addNewMember()">Add Member</a><br/>';

        $("#lstLists").listview('refresh');
    },


    createNewItem: function(){
        ShpanlistController.addNewListTemplateItem(ListTemplateView.listTemplateId, txtNewItemName.value, txtNewItemDescription.value, txtNewItemDefaultAmount.value)
    },

    removeItem: function(listTemplateItemId){
        ShpanlistController.removeListTemplateItem(ListTemplateView.listTemplateId, listTemplateItemId);
    },

    createListFromTemplate: function(){
        ShpanlistController.createListFromTemplate(ListTemplateView.listTemplateId);
    },

    removeListInstance: function removeListInstance(listInstanceId){
        var conf = confirm("Are you sure you want to remove this list?");
        if (conf){
            ShpanlistController.removeListInstance(listInstanceId, function(){
                homeView.refresh();
            });
        }
    }

}
