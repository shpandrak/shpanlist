
var ShpanlistController = {
    loadApp: function(){
        if (localStorage["signedInUser"] == null){
            this.menuSignIn();
        }else{
            this.menuListGroups();
        }
    },

    menuSignIn: function () {
        $.mobile.changePage("#pageSignIn")
    },

    menuAddNewListTemplateItem: function (listTemplateId) {
            $.mobile.changePage("listTemplateItem.html", {transition:'flip'})
    },

    menuListGroups: function(){
        $.mobile.changePage('listGroups.html', {transition: 'slide'});
    },


    doIt:function(data, successFunction){
        $.mobile.loading('show');
        jQuery.post(
            "/doIt",
            data
        )
            .done(
            function(responseText){
                $.mobile.loading('hide');
                successFunction(responseText);
            })
            .fail(function(jqXHR, textStatus) {
                $.mobile.loading('hide');
                alert("Failed oh no!" + jqXHR.statusText+ ": " + textStatus + ": " + jqXHR.responseText);
            });
    },

    menuListGroup: function(listGroupId){
        localStorage['listGroupId'] = listGroupId;
        ShpanlistController.getListGroup(listGroupId, function(responseXml){
            ListGroupView.data = responseXml;
            ListGroupView.listGroupId = listGroupId;
            $.mobile.changePage('listGroup.html', {transition:'slide'});
        });
    },

    getListGroup: function(listGroupId, callback){
        ShpanlistController.doIt(
            { what: "getListGroup", listGroupId: listGroupId},
            function(responseText){
                callback(responseText);
            }
        )
    },

    listListGroups: function(successFunction){
        ShpanlistController.doIt({ what: "listListGroups"},
            function(responseText){
                successFunction(responseText);
            });

    },

    signIn: function (userName, password) {
        ShpanlistController.doIt(
            { what: "signIn", userName: userName, password: password },
            function(responseText){
                if ($(responseText).find("listUser").size() > 0){
                    var userName = $(responseText).find("listUser").first().find("name").first().text();
                    ShpanlistController.signedInUser = userName;
                    localStorage["signedInUser"] = userName;
                    ShpanlistController.loadApp();
                }else{
                    alert("Nope... " + responseText);
                }
            });
    },

    menuListTemplate: function(listTemplateId){
        localStorage['listTemplateId'] = listTemplateId;
        ShpanlistController.getListTemplateFull(listTemplateId, function(responseXml){
            ListTemplateView.data = responseXml;
            ListTemplateView.listTemplateId = listTemplateId;
            $.mobile.changePage('listTemplate.html', {transition:'slide'});
        });
    },

    getListTemplateFull: function(listTemplateId, callback){
        ShpanlistController.doIt(
            { what: "getListTemplateFull", listTemplateId: listTemplateId },
            function(responseText){
                callback(responseText);
            });
    },

    menuListInstance: function(listInstanceId){
        ShpanlistController.doIt(
            { what: "getListInstanceFull", listInstanceId: listInstanceId },
            function(responseText){
                ListInstanceView.show(responseText);
            });
    },

    menuEditListInstance: function(listInstanceId){
        ShpanlistController.doIt(
            { what: "getListInstanceFull", listInstanceId: listInstanceId },
            function(responseText){
                ListInstanceEditView.show(responseText);
            });
    },

    addNewListTemplateItem: function(listTemplateId, listTemplateItemName, listTemplateItemDescription, listTemplateItemDefaultAmount, callback){
        ShpanlistController.doIt(
            { what: "addListTemplateItem", listTemplateId:listTemplateId, listTemplateItemName: listTemplateItemName, listTemplateItemDescription:listTemplateItemDescription, listTemplateItemDefaultAmount:listTemplateItemDefaultAmount},
            function(responseText){
                callback(responseText);
            });
    },

    removeListTemplateItem: function(listTemplateId, listTemplateItemId, callback){
        ShpanlistController.doIt(
            { what: "removeListTemplateItem", listTemplateId:listTemplateId, listTemplateItemId: listTemplateItemId},
            function(responseText){
                callback(responseText);
            });

    },

    removeListInstanceItem: function(listInstanceId, listInstanceItemId){
        ShpanlistController.doIt(
            { what: "removeListInstanceItem", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId},
            function(responseText){
                ShpanlistController.menuEditListInstance(listInstanceId);
            });

    },

    pushListTemplateItemUp: function(listTemplateId, listTemplateItemId, callback){
        ShpanlistController.doIt(
            { what: "pushListTemplateItemUp", listTemplateId:listTemplateId, listTemplateItemId: listTemplateItemId},
            function(responseText){
                callback(responseText);
            });
    },

    pushListTemplateItemDown: function(listTemplateId, listTemplateItemId, callback){
        ShpanlistController.doIt(
            { what: "pushListTemplateItemDown", listTemplateId:listTemplateId, listTemplateItemId: listTemplateItemId},
            function(responseText){
                callback(responseText);
            });
    },

    gotListInstanceItem: function(listInstanceId, listInstanceItemId){
        ShpanlistController.doIt(
            { what: "gotListInstanceItem", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId},
            function(responseText){
                ShpanlistController.menuListInstance(listInstanceId);
            });

    },

    bringBackItem: function(listInstanceId, listInstanceItemId){
        ShpanlistController.doIt(
            { what: "bringBackItem", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId},
            function(responseText){
                ShpanlistController.menuListInstance(listInstanceId);
            });

    },

    createListFromTemplate:function(listTemplateId){
        ShpanlistController.doIt(
            { what: "createListFromTemplate", listTemplateId:listTemplateId},
            function(responseText){
                ShpanlistController.menuListInstance($(responseText).find("listInstance").first().attr("id"));
            });
    },

    menuCreateUser:function(){
        window.location.replace("/createUser.html");
    },

    createUser: function(userName, password, firstName, lastName, email){
        ShpanlistController.doIt(
            { what: "createUser", userName:userName, password:password, firstName:firstName, lastName:lastName, email:email},
            function(responseText){
                window.location.replace("/");
            });
    },

    signOut: function(){
        ShpanlistController.doIt(
            { what: "signOut"},
            function(responseText){
                ShpanlistController.signedInUser = null;
                localStorage.removeItem("signedInUser");
                window.location.replace("");
            });
    },

    addNewGroupMember: function(listGroupId, memberUserName){
        ShpanlistController.doIt(
            { what: "addNewGroupMember", listGroupId: listGroupId, memberUserName:memberUserName},
            function(responseText){
                ShpanlistController.menuListGroup(listGroupId);
            });
    },

    removeListInstance: function(listInstanceId, successCallback){
        ShpanlistController.doIt(
            { what: "removeListInstance", listInstanceId: listInstanceId},
            function(responseText){
                successCallback();
            });
    }
}
