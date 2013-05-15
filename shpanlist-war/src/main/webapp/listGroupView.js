var ListGroupView = {

    mainFrame:undefined,
    listGroupId:null,

    show: function(listGroupXml){
        this.listGroupId = $(listGroupXml).find("listGroup").first().attr("id");
        var listGroupName = $(listGroupXml).find("name").first().text();
        var theHtml = '<h1>' + listGroupName + '</h1><br/><br/>';


        theHtml += '<h2>Active Lists</h2> <table id="tabListInstances">';
        $(listGroupXml).find("listInstanceRelationshipEntries").first().find("listInstance").each(function () {
            var currEntity = jQuery(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<tr><td>' + currEntity.find("name").first().text() + '</td>';
            theHtml += '<td><a HREF=\"javascript:ShpanlistController.menuListInstance(\'' + currEntityId + '\')\">Show List</a></td></tr>';
        });
        theHtml += '</table><br/><br/>';


        theHtml += '<h2>List Templates</h2> <table id="tabListInstances">';
        theHtml += '<table id="tabListTemplates">';
        $(listGroupXml).find("listTemplateRelationshipEntries").first().find("listTemplate").each(function () {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<tr><td>' + currEntity.find("name").first().text() + '</td>';
            theHtml += '<td><a HREF=\"javascript:ShpanlistController.menuListTemplate(\'' + currEntityId + '\')\">Open Template</a></td></tr>';
        });
        theHtml += '</table>';

        // Build group members table
        theHtml += '<h2>List Group members</h2><table id="tabGroupMembers">';
        $(listGroupXml).find("memberRelationshipEntries").first().find("listUser").each(function () {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<tr><td>' + currEntity.find("firstName").first().text() + ' ' + currEntity.find("lastName").first().text() +'</td>';
            theHtml += '<td><a HREF=\"javascript:ShpanlistController.removeGroupMember(\'' + currEntityId + '\')\">Remove</a></td></tr>';
        });

        theHtml += '</table><br/>';
        theHtml += 'UserName:<input type="text" id="txtNewMemberUserName"/><a HREF="javascript:ListGroupView.addNewMember()">Add Member</a><br/>';

        this.mainFrame.innerHTML = theHtml;
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
        var memberUserName = txtNewMemberUserName.value;
        if (memberUserName == ""){
            alert("Please Type user name to add");
        }else{
            ShpanlistController.addNewGroupMember(ListGroupView.listGroupId, memberUserName);
        }
    }

}
