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

    setInterval(
        // initAgents,
        initAgentsWithPagination,
        200);

    setInterval(processDeadAgents, 1000);
}