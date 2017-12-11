function initStatics() {
    $.ajax({
        url: REST_URL + 'statics'
    }).then(function (statics) {
        statics.forEach(cacheMarkerForStatic)
    });
    createListOfStaticsTypes();
}

function initWays() {
    $.ajax({
        url: REST_URL + 'ways/ids'
    }).then(function (wayIds) {
        wayIds.forEach(getAndDrawWay)
    });
    createListOfWayTypes();
}

function getAndDrawWay(id) {
    $.ajax({
        url: REST_URL + 'ways/' + id
    }).then(cachePolylineForWay)
}