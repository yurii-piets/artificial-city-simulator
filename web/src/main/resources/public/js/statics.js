function initStatics() {
    $.ajax({
        url: REST_URL + 'statics'
    }).then(function (statics) {
        statics.forEach(cacheMarkerForStatic)
    })
}

function initWays() {
    $.ajax({
        url: REST_URL + 'ways/ids'
    }).then(function (wayIds) {
        wayIds.forEach(getAndDrawWay)
    })
}

function getAndDrawWay(id) {
    $.ajax({
        url: REST_URL + 'ways/' + id
    }).then(cachePolylineForWay)
}