var map;

function initMap() {
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "map/center", true);
    request.send();
    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            var centerLocation = JSON.parse(request.response);
            initGoogleMap(centerLocation);
        }
    }
}

function initGoogleMap(centerLocation) {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: centerLocation.latitude, lng: centerLocation.longitude},
        zoom: 17,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    });

    initStatics();
    initWays();

    createListOfWayTypes();
    createListOfStaticsTypes();

    // initEdges();
    // initVertices();

    setInterval(function () {
        initAgents();
    }, 200);
}