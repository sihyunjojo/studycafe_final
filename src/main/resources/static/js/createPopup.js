function createPopupByJsonMessage(message){
    var errorPopup = window.open("/popup.html?message=" + message, "errorPopup",
        "left=300, top=150," +
        "width=500, height=150," +
        "status=no, scrollbars=no ,toolbar=no ,location=no ,menubar=no," +
        "relative=yes");


    // var easyPopup = window.open("/popup/easyPopup?message=" + responseObject.message, "easyPopUp",
    //     "left=300, top=150," +
    //     "width=500, height=170, " +
    //     "status=no, scrollbars=no, toolbar=no, location=no, menubar=no"+
    //     "relative=yes");
}