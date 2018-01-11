function initStatics() {
    $.ajax({
        url: REST_URL + 'statics',
        error: ajaxErrorHandler
    }).then(function (statics) {
        statics.forEach(cacheMarkerForStatic)
    });
    createListOfStaticsTypes();
}

function initWays() {
    $.ajax({
        url: REST_URL + 'ways/ids',
        error: ajaxErrorHandler
    }).then(function (wayIds) {
        wayIds.forEach(getAndDrawWay)
    });
    createListOfWayTypes();
}

function getAndDrawWay(id) {
    $.ajax({
        url: REST_URL + 'ways/' + id,
        error: ajaxErrorHandler
    }).then(cachePolylineForWay)
}

function updateLights(box) {
    if (box === null || box === undefined) {
        box = document.getElementById('lights');
    }

    if (box.checked) {
        $.ajax({
            url: REST_URL + 'statics/lights',
            error: ajaxErrorHandler
        }).then(function (lights) {
            cachedTypesMap['lights'].forEach(function (marker) {
                marker.setMap(null)
            });
            cachedTypesMap['lights'] = [];
            lights.forEach(function (light) {
                    var lightMarker = new google.maps.Marker({
                        position: {lat: light.location.latitude, lng: light.location.longitude},
                        icon: getIconForLight(light),
                        label: light.id + ": " + light.type.toLowerCase(),
                        map: map
                    });
                    addValueToLineMap('lights', lightMarker);
                }
            )
        });
    }
}