
var ShpanlistController = {

    doInstructions: function (instructions) {
        var htmlBindings = instructions.bindings;
        if (htmlBindings != undefined && htmlBindings != null) {
            for (var i = 0; i < htmlBindings.length; i++) {

                var currHtmlBinding = htmlBindings[i];
                if (currHtmlBinding.hasOwnProperty('outer')) {
                    jQuery(currHtmlBinding.id).replaceWith(currHtmlBinding.html);
                } else {
                    jQuery(currHtmlBinding.id).html(currHtmlBinding.html);
                }
            }
        }

        var refreshResponses = instructions.refreshResponses;
        if (refreshResponses != undefined && refreshResponses != null) {
            for (var j = 0; j < htmlBindings.length; j++) {

                var currRefreshResponse = refreshResponses[j];
                switch (currRefreshResponse.type) {
                    case "listview":
                        jQuery(currRefreshResponse.id).listview('refresh');
                        break;
                    case "table":
                        jQuery(currRefreshResponse.id).table('refresh');
                        break;
                }
            }

        }

    },


    menuSignIn: function () {
        $.mobile.changePage("/signIn")
    },

    menuAddNewListTemplateItem: function (listTemplateId) {
            $.mobile.changePage("/addListTemplateItem/" + listTemplateId, {reloadPage:true, role:'dialog'})
    },

    menuAddNewListInstanceItem: function (listInstanceId) {
            $.mobile.changePage("/addListInstanceItem/" + listInstanceId, {reloadPage:true, role:'dialog'})
    },

    menuHome: function(){
        $.mobile.changePage('/home', {transition:'slide', reloadPage:true});
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

    doItPage:function(page, data, successFunction){
        $.mobile.loading('show');
        jQuery.post(
            page,
            data
        )
            .done(
            function(responseJson){
                $.mobile.loading('hide');
                var status = responseJson.status;

                switch (status){
                    case 0:
                        successFunction(responseJson);
                        break;
                    case 1:
                        ShpanlistController.menuSignIn();
                        break;
                    default :
                        var message = responseJson.message;
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
                    ShpanlistController.menuHome();
                }else{
                    alert("Nope... " + responseText);
                }
            });
    },

    menuListTemplate: function(listTemplateId){
        ListTemplatePageView.listTemplateId = listTemplateId;
        $.mobile.changePage('/listTemplate/' + listTemplateId, {reloadPage:true, transition:'slide'});
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
        $.mobile.changePage('/listInstance/' + listInstanceId, {transition:'slide', reloadPage:true});
    },

    menuEditListInstance: function(listInstanceId){
        ListInstanceEditPageView.listInstanceId = listInstanceId;
        $.mobile.changePage('/listInstanceEdit/' + listInstanceId, {transition:'slide', reloadPage:true});
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

    removeListTemplateItem: function(listTemplateId, listTemplateItemId){
        ShpanlistController.doItPage('/listTemplate',
            { what: "removeListTemplateItem", listTemplateId:listTemplateId, listTemplateItemId: listTemplateItemId},
            function(resplonse){
                ShpanlistController.doInstructions(resplonse);
            });

    },

    removeListInstanceItem: function(listInstanceId, listInstanceItemId){
        ShpanlistController.doItPage('/listInstanceEdit',
            { what: "removeListInstanceItem", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId},
            function(response){
                ShpanlistController.doInstructions(response);
            });

    },

    pushListTemplateItemUp: function(listTemplateId, listTemplateItemId){
        ShpanlistController.doItPage('/listTemplate',
            { what: "pushListTemplateItemUp", listTemplateId:listTemplateId, listTemplateItemId: listTemplateItemId},
            function(response){
                ShpanlistController.doInstructions(response);
            });
    },

    pushListTemplateItemDown: function(listTemplateId, listTemplateItemId){
        ShpanlistController.doItPage('/listTemplate',
            { what: "pushListTemplateItemDown", listTemplateId:listTemplateId, listTemplateItemId: listTemplateItemId},
            function(response){
                ShpanlistController.doInstructions(response);
            });
    },

    pushListInstanceItemUp: function(listInstanceId, listInstanceItemId){
        ShpanlistController.doItPage('/listInstanceEdit',
            { what: "pushListInstanceItemUp", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId},
            function(response){
                ShpanlistController.doInstructions(response);
            });
    },

    pushListInstanceItemDown: function(listInstanceId, listInstanceItemId){
        ShpanlistController.doItPage('/listInstanceEdit',
            { what: "pushListInstanceItemDown", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId},
            function(response){
                ShpanlistController.doInstructions(response);
            });
    },

    gotListInstanceItem: function(listInstanceId, listInstanceItemId, callback){
        ShpanlistController.doIt(
            { what: "gotListInstanceItem", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId},
            function(responseText){
                callback(responseText);
            });

    },

    gotListInstanceItemNew: function(listInstanceId, listInstanceItemId){
        ShpanlistController.doItPage("/listInstance",
            { what: "gotListInstanceItem", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId},
            function(response){
                ShpanlistController.doInstructions(response)
            });

    },

    bringBackItem: function(listInstanceId, listInstanceItemId){
        ShpanlistController.doItPage('/listInstance',
            { what: "bringBackItem", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId},
            function(response){
                ShpanlistController.doInstructions(response);
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
        $.mobile.changePage("/createUser", {transition:'slide'})
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
        ShpanlistController.doItPage('/home',
            { what: "removeListInstance", listInstanceId: listInstanceId},
            function(response){
                ShpanlistController.doInstructions(response);
            });
    },

    updateListInstanceName: function(listInstanceId, listInstanceName){
        ShpanlistController.doItPage("/listInstanceEdit",
            { what: "updateListInstanceName", listInstanceId: listInstanceId, listInstanceName:listInstanceName},
            function(response){
                ShpanlistController.doInstructions (response);
            });
    },

    updateListTemplateName: function(listTemplateId, listTemplateName){
        ShpanlistController.doItPage("/listTemplate",
            { what: "updateListTemplateName", listTemplateId: listTemplateId, listTemplateName:listTemplateName},
            function(response){
                ShpanlistController.doInstructions (response);
            });
    },

    menuNewListInstance: function(){
        ShpanlistController.doIt(
            { what: "createNewListInstance"},
            function(responseXml){
                var listInstanceId = $(responseXml).attr('id');
                ShpanlistController.menuEditListInstance(listInstanceId);
            });
    }

};
