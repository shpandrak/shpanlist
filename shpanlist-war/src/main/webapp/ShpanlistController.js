
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



    menuListGroup: function(listGroupId){
        jQuery.post(
            "/doIt",
            { what: "getListGroup", listGroupId: listGroupId}
        )
            .done(
            function(responseText){
                ListGroupView.show(responseText);
            })
            .fail(function(jqXHR, textStatus) {
                alert("Failed oh no!" + jqXHR.status+ ": " + textStatus);
            });

    },

    listListGroups: function(successFunction){
        jQuery.post(
            "/doIt",
            { what: "listListGroups"}
        )
            .done(
            function(responseText){
                successFunction(responseText);
            })
            .fail(function(jqXHR, textStatus) {
                alert("Failed oh no!" + jqXHR.status+ ": " + textStatus);
            });

    },

    signIn: function (userName, password) {
            jQuery.post(
                "/doIt",
                { what: "signIn", userName: userName, password: password }
            )
        .done(
            function(responseText){
                if (responseText == "Yey!\n"){
                    window.location.reload();
                }else{
                    alert("Nope... " + responseText);
                }
            })
        .fail(function(jqXHR, textStatus) {
            alert("Failed oh no!" + jqXHR.status+ ": " + textStatus);
        });


    },

    menuListTemplate: function(listTemplateId){
        jQuery.post(
            "/doIt",
            { what: "getListTemplateFull", listTemplateId: listTemplateId }
        )
            .done(
            function(responseText){
                ListTemplateView.show(responseText);
            })
            .fail(function(jqXHR, textStatus) {
                alert("Failed oh no!" + jqXHR.status+ ": " + textStatus);
            });
    },

    menuListInstance: function(listInstanceId){
        jQuery.post(
            "/doIt",
            { what: "getListInstanceFull", listInstanceId: listInstanceId }
        )
            .done(
            function(responseText){
                ListInstanceView.show(responseText);
            })
            .fail(function(jqXHR, textStatus) {
                alert("Failed oh no!" + jqXHR.status+ ": " + textStatus);
            });
    },

    addNewListTemplateItem: function(listTemplateId, listTemplateItemName, listTemplateItemDescription, listTemplateItemDefaultAmount){
        jQuery.post(
            "/doIt",
            { what: "addListTemplateItem", listTemplateId:listTemplateId, listTemplateItemName: listTemplateItemName, listTemplateItemDescription:listTemplateItemDescription, listTemplateItemDefaultAmount:listTemplateItemDefaultAmount}
        )
            .done(
            function(responseText){
                ShpanlistController.menuListTemplate(listTemplateId);
            })
            .fail(function(jqXHR, textStatus) {
                alert("Failed oh no!" + jqXHR.status+ ": " + textStatus);
            });

    },

    removeListTemplateItem: function(listTemplateId, listTemplateItemId){
        jQuery.post(
            "/doIt",
            { what: "removeListTemplateItem", listTemplateId:listTemplateId, listTemplateItemId: listTemplateItemId}
        )
            .done(
            function(responseText){
                ShpanlistController.menuListTemplate(listTemplateId);
            })
            .fail(function(jqXHR, textStatus) {
                alert("Failed oh no!" + jqXHR.status+ ": " + textStatus);
            });

    },

    gotListInstanceItem: function(listInstanceId, listInstanceItemId){
        jQuery.post(
            "/doIt",
            { what: "gotListInstanceItem", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId}
        )
            .done(
            function(responseText){
                ShpanlistController.menuListInstance(listInstanceId);
            })
            .fail(function(jqXHR, textStatus) {
                alert("Failed oh no!" + jqXHR.status+ ": " + textStatus);
            });

    },

    bringBackItem: function(listInstanceId, listInstanceItemId){
        jQuery.post(
            "/doIt",
            { what: "bringBackItem", listInstanceId:listInstanceId, listInstanceItemId: listInstanceItemId}
        )
            .done(
            function(responseText){
                ShpanlistController.menuListInstance(listInstanceId);
            })
            .fail(function(jqXHR, textStatus) {
                alert("Failed oh no!" + jqXHR.status+ ": " + textStatus);
            });

    },

    createListFromTemplate:function(listTemplateId){
        jQuery.post(
            "/doIt",
            { what: "createListFromTemplate", listTemplateId:listTemplateId}
        )
            .done(
            function(responseText){
                ShpanlistController.menuListInstance($(responseText).find("listInstance").first().attr("id"));
            })
            .fail(function(jqXHR, textStatus) {
                alert("Failed oh no!" + jqXHR.status+ ": " + textStatus);
            });


    },

    menuCreateUser:function(){
        window.location.replace("/createUser.html");
    },

    createUser: function(userName, password, firstName, lastName, email){
        jQuery.post(
            "/doIt",
            { what: "createUser", userName:userName, password:password, firstName:firstName, lastName:lastName, email:email}
        )
            .done(
            function(responseText){
                window.location.replace("/");
            })
            .fail(function(jqXHR, textStatus) {
                alert("Failed oh no!" + jqXHR.status+ ": " + textStatus);
            });
    },

    signOut: function(){
        jQuery.post(
            "/doIt",
            { what: "signOut"}
        )
            .done(
            function(responseText){
                window.location.replace("/");
            })
            .fail(function(jqXHR, textStatus) {
                alert("Failed oh no!" + jqXHR.status+ ": " + textStatus);
            });

    }
}
