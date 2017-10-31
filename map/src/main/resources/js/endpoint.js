function gs() {
    var request = getAjaxRequest();
    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "agent/all", true);
    request.send();
    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            createMarkerForAgents(JSON.parse(request.response));
        }
    };
}

function getAjaxRequest() {
    try {
        var request = new XMLHttpRequest();
    } catch (e1) {
        try {
            request = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e2) {
            try {
                request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e3) {
                request = false;
            }
        }
    }
    return request;
}
