var ListGroupsView = {
    data:null,

    show: function () {
        var listGroupsXml = this.data;

        if (listGroupsXml == null){
            ShpanlistController.listListGroups(function(responseXml){
                ListGroupsView.load(responseXml)
            });

        }else{
            ListGroupsView.load(listGroupsXml);
        }
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



