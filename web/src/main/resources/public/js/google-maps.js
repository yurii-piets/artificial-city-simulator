var map;

function initMap() {
    $.ajax({
        url: REST_URL + 'map/center',
        error: ajaxErrorHandler
    }).then(initGoogleMap);
}

function initGoogleMap(centerLocation) {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: centerLocation.latitude, lng: centerLocation.longitude},
        zoom: 17,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    });

    initStatics();
    initWays();

    // initEdges(); // can be to heavy for your browser
    // initVertices();

    setInterval(function () {
        initAgents();
    }, 200);

    setInterval(function () {
        processDeadAgents();
    }, 1000);
}