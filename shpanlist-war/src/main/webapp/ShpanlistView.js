var ShpanlistView = {
    mainFrame:undefined,

    doNavigate: function(url){
        jQuery(this.mainFrame).load(url);
    },

    showSignIn: function(){
        window.location.replace("/login.html");
    },


    listSuccess: function (xmlData){
        var lstGroups = document.getElementById("lstGroups");
        //tabGroups.innerHTML = "<thead><td><b>Name</b></td><td>&nbsp;</td></thead>";
        var theHtml = '';
        jQuery(xmlData).find("listGroup").each(function () {
            var currEntity = jQuery(this);
            var currEntityId = currEntity.attr("id");

            //var row = tabGroups.insertRow(tabGroups.rows.length);
            //row.innerHTML = "<td><input type=\"checkbox\" /><input type=\"button\" value=\"Delete\" onClick=\"ListGroupLibrary.deleteEntity('" + currEntityId + "')\"/></td>";
            //var cell;
            //cell = row.insertCell(row.cells.length);
            //cell.innerHTML = currEntity.find("name").text();
            //theHtml += '<li>' + ;

            //cell = row.insertCell(row.cells.length);
            theHtml += '<li><a data-transition="slide" href="javascript:ShpanlistController.menuListGroup(\'' + currEntityId + '\')">' + currEntity.find("name").text() + '</a></li>';
            //cell.innerHTML = '<a HREF=\"javascript:ShpanlistController.menuListGroup(\'' + currEntityId + '\')\">Open</a>';
        });

        lstGroups.innerHTML = theHtml;
        jQuery("#lstGroups").listview();
    },


    showListGroups: function(){
        this.mainFrame.innerHTML =
            '<h1>' + ShpanlistController.signedInUser + '\'s Groups</h1>' +
            '<ul id="lstGroups"></ul>';

        ShpanlistController.listListGroups(this.listSuccess);
    }
}

