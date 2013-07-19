var ListInstancePageView = {

    data: null,
    listInstanceId: null,

    refresh: function () {
    },

    show: function () {
    },

    load: function (listInstanceXml) {
    },


    gotItem: function (listInstanceItemId) {
        ShpanlistController.gotListInstanceItemNew(ListInstancePageView.listInstanceId, listInstanceItemId);
    },

    bringBackItem: function (listInstanceItemId) {
        ShpanlistController.bringBackItem(ListInstancePageView.listInstanceId, listInstanceItemId);
    }


}
