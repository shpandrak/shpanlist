
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
                ShpanlistView.showListGroup(responseText);
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
    }



}
