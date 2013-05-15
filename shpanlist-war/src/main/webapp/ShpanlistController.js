
var ShpanlistController = {

    signedInUser:null,


    loadApp: function(){
        if (this.signedInUser == null){
            this.menuSignIn();
        }else{
            this.menuListGroups();
        }
    },

    menuSignIn: function () {
        ShpanlistView.showSignIn();
    },

    menuListGroups: function(){
        ShpanlistView.showListGroups();
    },


    doIt:function(data, successFunction){
        jQuery.post(
            "/doIt",
            data
        )
            .done(
            function(responseText){
                successFunction(responseText);
            })
            .fail(function(jqXHR, textStatus) {
                alert("Failed oh no!" + jqXHR.statusText+ ": " + textStatus + ": " + jqXHR.responseText);
            });
    },

    menuListGroup: function(listGroupId){
        ShpanlistController.doIt(
            { what: "getListGroup", listGroupId: listGroupId},
            function(responseText){
                ListGroupView.show(responseText);
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
                    window.location.reload();
                }else{
                    alert("Nope... " + responseText);
                }
            });
    },

    menuListTemplate: function(listTemplateId){
        ShpanlistController.doIt(
            { what: "getListTemplateFull", listTemplateId: listTemplateId },
            function(responseText){
                ListTemplateView.show(responseText);
            });
    },

    menuListInstance: function(listInstanceId){
        ShpanlistController.doIt(
            { what: "getListInstanceFull", listInstanceId: listInstanceId },
            function(responseText){
                ListInstanceView.show(responseText);
            });
    },

    addNewListTemplateItem: function(listTemplateId, listTemplateItemName, listTemplateItemDescription, listTemplateItemDefaultAmount){
        ShpanlistController.doIt(
            { what: "addListTemplateItem", listTemplateId:listTemplateId, listTemplateItemName: listTemplateItemName, listTemplateItemDescription:listTemplateItemDescription, listTemplateItemDefaultAmount:listTemplateItemDefaultAmount},
            function(responseText){
                ShpanlistController.menuListTemplate(listTemplateId);
            });
    },

    removeListTemplateItem: function(listTemplateId, listTemplateItemId){
        ShpanlistController.doIt(
            { what: "removeListTemplateItem", listTemplateId:listTemplateId, listTemplateItemId: listTemplateItemId},
            function(responseText){
                ShpanlistController.menuListTemplate(listTemplateId);
            });

    },

    pushListTemplateItemUp: function(listTemplateId, listTemplateItemId){
        ShpanlistController.doIt(
            { what: "pushListTemplateItemUp", listTemplateId:listTemplateId, listTemplateItemId: listTemplateItemId},
            function(responseText){
                ShpanlistController.menuListTemplate(listTemplateId);
            });
    },

    pushListTemplateItemDown: function(listTemplateId, listTemplateItemId){
        ShpanlistController.doIt(
            { what: "pushListTemplateItemDown", listTemplateId:listTemplateId, listTemplateItemId: listTemplateItemId},
            function(responseText){
                ShpanlistController.menuListTemplate(listTemplateId);
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
                window.location.replace("/");
            });
    },

    addNewGroupMember: function(listGroupId, memberUserName){
        ShpanlistController.doIt(
            { what: "addNewGroupMember", listGroupId: listGroupId, memberUserName:memberUserName},
            function(responseText){
                ShpanlistController.menuListGroup(listGroupId);
            });

    }
}
