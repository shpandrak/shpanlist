var ListTemplateView = {

    mainFrame:undefined,

    show: function(listTemplateXml){

        var listTemplateName = jQuery(listTemplateXml).find("name").first().text();
        var theHtml = '<h2>' + listTemplateName + '</h2><table id="tabListTemplateItems">';

        $(listTemplateXml).find("listTemplateItemRelationshipEntries").find("listTemplateItem") .each(function() {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<tr><td>' + currEntity.find("name").first().text() + '</td>';
            theHtml += '<td>' + currEntity.find("description").first().text() + '</td>';
            theHtml += '<td><a HREF=\"javascript:ShpanlistController.menuRemoveListTemplateItem(\'' + currEntityId + '\')\">Remove</a></td></tr>';

        });

        this.mainFrame.innerHTML = theHtml + '</table>';
    }
}
