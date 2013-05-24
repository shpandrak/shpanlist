var ListTemplateView = {

    data:null,
    listTemplateId:null,


    refresh: function(){
        this.data = null;
        this.show();
    },

    show: function(){
        var listTemplateXml = this.data;

        if (listTemplateXml == null){
            ShpanlistController.getListTemplateFull(localStorage['listTemplateId'], function(responseXml){
                ListTemplateView.load(responseXml)
            });

        }else{
            ListTemplateView.load(listTemplateXml);
        }
    },

    load: function(listTemplateXml){
        //this.listTemplateId = $(listTemplateXml).find("listTemplate").first().attr("id");
        var listTemplateName = $(listTemplateXml).find("name").first().text();
        document.getElementById('pageListTemplateHeaderDiv').innerHTML = listTemplateName;
        var theHtml = '';
        var listGroupId = $(listTemplateXml).find("listGroup").first().attr("id");

        var firstIteration = true;
        $(listTemplateXml).find("listTemplateItemRelationshipEntries").find("listTemplateItem") .each(function() {
            var currEntity = $(this);
            var currEntityId = currEntity.attr("id");
            theHtml += '<tr><td>' + currEntity.find("name").first().text() + '</td>';
            theHtml += '<td>' + currEntity.find("description").first().text() + '</td>';
            theHtml += '<td>' + currEntity.find("defaultAmount").first().text() + '</td>';
            theHtml += '<td><a HREF=\"javascript:ListTemplateView.removeItem(\'' + currEntityId + '\')\">Remove</a>';
            theHtml += '&nbsp;<img src="/images/down.png" onclick="ListTemplateView.pushItemDown(\'' + currEntityId + '\')"/>';
            if (firstIteration){
                firstIteration = false;
            }else{
                theHtml += '&nbsp;<img src="/images/up.png" onclick="ListTemplateView.pushItemUp(\'' + currEntityId + '\')"/>';

            }

            theHtml += '</td></tr>';


        });

        //theHtml += '<tr><td>Item:<input type="text" id="txtNewItemName"/></td><td>description:<input type="text" id="txtNewItemDescription"/></td><td>Default Amount:<input type="number" min="1" id="txtNewItemDefaultAmount"/></td><td><a HREF="javascript:ListTemplateView.createNewItem()">Add</a></td></tr>';

        document.getElementById('tabListTemplateItems').tBodies[0].innerHTML = theHtml;
        $("#tabListTemplateItems").table('refresh');
    },

    createNewItem: function(){
        ShpanlistController.addNewListTemplateItem(
            ListTemplateView.listTemplateId,
            document.getElementById('txtListTemplateItemName').value,
            document.getElementById('txtListTemplateItemDescription').value,
            document.getElementById('txtListTemplateItemAmount').value, function(){
                ShpanlistController.menuListTemplate(localStorage['listTemplateId']);
        });
    },

    removeItem: function(listTemplateItemId){
        ShpanlistController.removeListTemplateItem(ListTemplateView.listTemplateId, listTemplateItemId, function(){
            ListTemplateView.refresh();
        });
    },

    pushItemUp: function(listTemplateItemId){
        ShpanlistController.pushListTemplateItemUp(ListTemplateView.listTemplateId, listTemplateItemId, function(){
            ListTemplateView.refresh();
        });
    },
    pushItemDown: function(listTemplateItemId){
        ShpanlistController.pushListTemplateItemDown(ListTemplateView.listTemplateId, listTemplateItemId, function(){
            ListTemplateView.refresh();
        });
    },

    createListFromTemplate: function(){
        ShpanlistController.createListFromTemplate(ListTemplateView.listTemplateId);
    }

}
