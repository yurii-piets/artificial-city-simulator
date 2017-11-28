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

function initWays() {
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "ways/ids", true);
    request.send();

    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            //todo behaves strange when array is empty, check for each usage like this
            var wayIds = JSON.parse(request.response);

            wayIds.forEach(getAndDrawWay);

            // agents.forEach(createPolylineForWay);
        }
    }
}

function getAndDrawWay(id) {
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "ways/" + id, true);
    request.send();

    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            //todo behaves strange when array is empty, check for each usage like this
            var way = JSON.parse(request.response);

            createPolylineForWay(way);
        }
    }
}