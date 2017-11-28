function initStatics(){
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "statics", true);
    request.send();

    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            var agents = JSON.parse(request.response);

            agents.forEach(createMarkerForStatic);
        }
    }
}