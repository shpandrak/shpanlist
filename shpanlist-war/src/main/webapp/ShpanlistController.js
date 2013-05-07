var ShpanlistController = {
    showSignIn: function () {
        window.location.replace("/login.html");
    },

    showListGroups: function(){
        window.location.replace("/main.html")
    },

    signIn: function (userName, password) {
            jQuery.post(
                "/doIt",
                { what: "signIn", userName: userName, password: password }
            )
        .done(
            function(responseText){
                if (responseText == "Yey!\n"){
                    ShpanlistController.showListGroups();
                }else{
                    alert("Nope... " + responseText);
                }
            })
        .fail(function(jqXHR, textStatus) {
            alert("Failed oh no!" + jqXHR.status+ ": " + textStatus);
        });


/*
        new Ajax.Request("/auth/login", {
            method: 'post',
            evalJS: false,
            headers: {
                "userName":userName,
                "password":password
            },
            onSuccess: function (transport) {
                alert(transport.responseText);
            },
            onFailure: function (transport) {
                alert("Failed oh no!" + transport.status + ": " + transport.statusText);
            },
            onException: function (request, e) {
                alert("Exception occurred boo " + e);
            }
        });
*/
    }


}
