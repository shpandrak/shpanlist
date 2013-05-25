var ListGroupView = {
    data:null,
    listGroupId:null,

    refresh: function(){
        this.data = null;
        this.show();
    },

    show:function() {

        var listGroupXml = this.data;

        if (listGroupXml == null){
            ShpanlistController.getListGroup(localStorage['listGroupId'], function(responseXml){
                ListGroupView.load(responseXml)
            });

        }else{
            ListGroupView.load(listGroupXml);
        }
    },

    load:function(listGroupXml) {

        var listGroupName = $(listGroupXml).find("name").first().text();
        document.getElementById('pageListGroupHeaderDiv').innerHTML = listGroupName;


        var theHtml = '<li data-role="list-divider">Active Lists</li>';
        $(listGroupXml).find("listInstanceRelationshipEntries").first().find("listInstance").each(function () {
            var currEntity = jQuery(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<li><a href="javascript:ShpanlistController.menuListInstance(\'' + currEntityId + '\')\">' + currEntity.find("name").first().text() + '</a>' +
                '<a href="javascript:ListGroupView.removeListInstance(\'' + currEntityId + '\')\">Remove</a></li>';
        });


        theHtml += '<li data-role="list-divider">List Templates</li>';
        $(listGroupXml).find("listTemplateRelationshipEntries").first().find("listTemplate").each(function () {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<li>' +
                       '<a HREF=\"javascript:ShpanlistController.menuListTemplate(\'' + currEntityId + '\')\">' + currEntity.find("name").first().text() + '</a></li>';
        });

        //$('#lstLists').html(theHtml);
        document.getElementById('lstLists').innerHTML = theHtml;


        // Build group members table
        theHtml = '<h2>List Group members</h2><table id="tabGroupMembers">';
        $(listGroupXml).find("memberRelationshipEntries").first().find("listUser").each(function () {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<tr><td>' + currEntity.find("firstName").first().text() + ' ' + currEntity.find("lastName").first().text() + '</td>';
            theHtml += '<td><a HREF=\"javascript:ShpanlistController.removeGroupMember(\'' + currEntityId + '\')\">Remove</a></td></tr>';
        });

        theHtml += '</table><br/>';
        theHtml += 'UserName:<input type="text" id="txtNewMemberUserName"/><a HREF="javascript:ListGroupView.addNewMember()">Add Member</a><br/>';

        $("#lstLists").listview('refresh');
        //document.getElementById('pageListGroupContentDiv').innerHTML = theHtml;
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
    addNewMember: function(){
        var memberUserName = $('txtNewMemberUserName').value;
        if (memberUserName == ""){
            alert("Please Type user name to add");
        }else{
            ShpanlistController.addNewGroupMember(ListGroupView.listGroupId, memberUserName);
        }
    },

    removeListInstance: function removeListInstance(listInstanceId){
        var conf = confirm("Are you sure you want to remove this list?");
        if (conf){
            ShpanlistController.removeListInstance(listInstanceId, function(){
                ListGroupView.refresh();
            });
        }
    }

}
