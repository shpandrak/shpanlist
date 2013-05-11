var ShpanlistView = {
    mainFrame:undefined,

    doNavigate: function(url){
        jQuery(this.mainFrame).load(url);
    },

    showSignIn: function(){
        this.doNavigate("/login.html");
    },


    listSuccess: function (xmlData){
        var tabGroups = document.getElementById("tabGroups");
        tabGroups.innerHTML = "<thead><td><b>Name</b></td><td>&nbsp;</td></thead>";
        jQuery(xmlData).find("listGroup").each(function () {
            var currEntity = jQuery(this);
            var currEntityId = currEntity.attr("id");

            var row = tabGroups.insertRow(tabGroups.rows.length);
            //row.innerHTML = "<td><input type=\"checkbox\" /><input type=\"button\" value=\"Delete\" onClick=\"ListGroupLibrary.deleteEntity('" + currEntityId + "')\"/></td>";
            var cell;
            cell = row.insertCell(row.cells.length);
            cell.innerHTML = currEntity.find("name").text();
            cell = row.insertCell(row.cells.length);
            cell.innerHTML = '<a HREF=\"javascript:ShpanlistController.menuListGroup(\'' + currEntityId + '\')\">Open</a>';
        });
    },


    showListGroups: function(){
        this.mainFrame.innerHTML =
            '<h1>' + ShpanlistController.signedInUser + '\'s Groups</h1>' +
            '<table id="tabGroups"></table>';

        ShpanlistController.listListGroups(this.listSuccess);
    },

    showListGroup: function(listGroupXml){

        //alert(jQuery(listGroupXml).first("listGroup").attr("id"));
        var groupName = jQuery(listGroupXml).find("name").first().text();

        var theHtml =
            '<h1>' + groupName + '\'s List templates</h1>' +
            '<table id="tabListTemplates">';


            jQuery(listGroupXml).find("listTemplateRelationshipEntries").first().find("listTemplate").each(function () {
                    var currEntity = jQuery(this);
                    var currEntityId = currEntity.attr("id");
                    theHtml += '<tr><td>' + currEntity.find("name").first().text() + '</td>';
                    theHtml += '<td><a HREF=\"javascript:ShpanlistController.menuListTemplate(\'' + currEntityId + '\')\">Open Template</a></td></tr>';
                });

        this.mainFrame.innerHTML = theHtml;



    }




}

