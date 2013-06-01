
var ShpanlistController = {
    loadApp: function(){
        if (localStorage["signedInUser"] == null){
            this.menuSignIn();
        }else{
            this.menuHome();
        }
    },

    menuSignIn: function () {
        $.mobile.changePage("#pageSignIn")
    },

    menuAddNewListTemplateItem: function (listTemplateId) {
            $.mobile.changePage("listTemplateItem.html", {transition:'flip'})
    },

    menuAddNewListInstanceItem: function (listInstanceId) {
            $.mobile.changePage("listInstanceItem.html", {transition:'flip'})
    },

    menuHome: function(){
        ShpanlistController.listUserData(function(responseXml){
            HomeView.data = responseXml;
            $.mobile.changePage('home.html');

        });
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
                var status = $(responseText).find("shpanlistResponse").first().attr("status");

                switch (status){
                    case "0":
                        successFunction($(responseText).find("shpanlistResponse").first().children());
                        break;
                    case "1":
                        ShpanlistController.menuSignIn();
                        break;
                    case "2":
                        var message = $(responseText).find("shpanlistResponse").first().attr("message");
                        alert("Oops, sorry about that.. " + message);
                        break;

                }


            })
            .fail(function(jqXHR, textStatus) {
                $.mobile.loading('hide');
                alert("Failed oh no no!" + jqXHR.statusText+ ": " + textStatus + ": " + jqXHR.responseText);
            });
    },

    listUserData: function(callback){
        ShpanlistController.doIt({ what: "listUserData"},
            function(responseText){
                callback(responseText);
            });

    },

    signIn: function (userName, password) {
        ShpanlistController.doIt(
            { what: "signIn", userName: userName, password: password },
            function(responseText){
                if ($(responseText)[0].nodeName == "listUser"){
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

    getListInstanceFull: function (listInstanceId, callback) {
        ShpanlistController.doIt(
            { what: "getListInstanceFull", listInstanceId: listInstanceId },
            function (responseText) {
                callback(responseText)
            });
    },

    menuListInstance: function(listInstanceId){
        localStorage['listInstanceId'] = listInstanceId;
        this.getListInstanceFull(listInstanceId, function(responseText){

            ListInstanceView.data = responseText;
            ListInstanceView.listInstanceId = listInstanceId;
            $.mobile.changePage('listInstance.html', 'slide');
        });
    },

    menuEditListInstance: function(listInstanceId){
        localStorage['listInstanceId'] = listInstanceId;
        this.getListInstanceFull(listInstanceId, function(responseText){

            ListInstanceEditView.data = responseText;
            ListInstanceEditView.listInstanceId = listInstanceId;
            $.mobile.changePage('listInstanceEdit.html', 'slide');
        });

    },

    addNewListTemplateItem: function(listTemplateId, listTemplateItemName, listTemplateItemDescription, listTemplateItemDefaultAmount, callback){
        ShpanlistController.doIt(
            { what: "addListTemplateItem", listTemplateId:listTemplateId, listTemplateItemName: listTemplateItemName, listTemplateItemDescription:listTemplateItemDescription, listTemplateItemDefaultAmount:listTemplateItemDefaultAmount},
            function(responseText){
                callback(responseText);
            });
    },

    addNewListInstanceItem: function(listInstanceId, listInstanceItemName, listInstanceItemDescription, listInstanceItemAmount, callback){
        ShpanlistController.doIt(
            { what: "addListInstanceItem", listInstanceId:listInstanceId, listInstanceItemName: listInstanceItemName, listInstanceItemDescription:listInstanceItemDescription, listInstanceItemAmount:listInstanceItemAmount},
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

    removeListInstanceItem: function(listInstanceId, listInstanceItemId, callback){
        ShpanlistController.doIt(
            { what: "removeListInstanceItem", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId},
            function(responseText){
                callback(listInstanceId);
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

    pushListInstanceItemUp: function(listInstanceId, listInstanceItemId, callback){
        ShpanlistController.doIt(
            { what: "pushListInstanceItemUp", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId},
            function(responseText){
                callback(responseText);
            });
    },

    pushListInstanceItemDown: function(listInstanceId, listInstanceItemId, callback){
        ShpanlistController.doIt(
            { what: "pushListInstanceItemDown", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId},
            function(responseText){
                callback(responseText);
            });
    },

    gotListInstanceItem: function(listInstanceId, listInstanceItemId, callback){
        ShpanlistController.doIt(
            { what: "gotListInstanceItem", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId},
            function(responseText){
                callback(responseText);
            });

    },

    bringBackItem: function(listInstanceId, listInstanceItemId, callback){
        ShpanlistController.doIt(
            { what: "bringBackItem", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId},
            function(responseText){
                callback(responseText);
            });

    },

    createListFromTemplate:function(listTemplateId){
        ShpanlistController.doIt(
            { what: "createListFromTemplate", listTemplateId:listTemplateId},
            function(responseText){
                ShpanlistController.menuListInstance($(responseText).attr("id"));
            });
    },

    menuCreateUser:function(){
        window.location.replace("/createUser.html");
    },

    createUser: function(userName, password, firstName, lastName, email){
        ShpanlistController.doIt(
            { what: "createUser", userName:userName, password:password, firstName:firstName, lastName:lastName, email:email},
            function(responseText){
                ShpanlistController.menuHome();
            });
    },

    signOut: function(){
        ShpanlistController.doIt(
            { what: "signOut"},
            function(responseText){
                ShpanlistController.signedInUser = null;
                localStorage.removeItem("signedInUser");
                window.location.replace("/");
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
