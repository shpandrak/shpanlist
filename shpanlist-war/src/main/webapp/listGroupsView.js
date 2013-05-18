var ListGroupsView = {

    show: function (event, data) {
        ShpanlistController.listListGroups(ListGroupsView.load);
    },

    load: function (listGroupsXml) {
        var lstGroups = document.getElementById("lstGroups");
        var theHtml = '';
        $(listGroupsXml).find("listGroup").each(function () {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<li><a href="javascript:ShpanlistController.menuListGroup(\'' + currEntityId + '\')">' + currEntity.find("name").text() + '</a></li>';
        });

        lstGroups.innerHTML = theHtml;
        $("#lstGroups").listview('refresh');
    }
}



