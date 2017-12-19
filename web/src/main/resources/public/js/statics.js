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