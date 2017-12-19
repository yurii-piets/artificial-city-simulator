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

    initMapListener();

    initStatics();
    initWays();

    setInterval(
        // initAgents,
        initAgentsWithPagination,
        200);

    setInterval(processDeadAgents, 1000);
}

function initMapListener() {
    map.addListener('click', function (event) {
        var latitude = event.latLng.lat();
        var longitude = event.latLng.lng();

        $.ajax({
            url: REST_URL + 'statistic?' + 'longitude=' + longitude + '&' + 'latitude=' + latitude,
            error: ajaxErrorHandler
        }).then(showStatistic);
    })
}