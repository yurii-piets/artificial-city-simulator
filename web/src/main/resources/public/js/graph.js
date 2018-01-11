var edgesPolyLines = [];
var vertexMarkers = [];
var startVerticesPolyLines = [];


function initEdges() {
    $.ajax({
        url: REST_URL + 'graph/edges/ids',
        error: ajaxErrorHandler
    }).then(function (ids) {
        ids.forEach(getAndShowEdge);
    });
}

function initVertices() {
    $.ajax({
        url: REST_URL + 'graph/vertices/ids',
        error: ajaxErrorHandler
    }).then(function (ids) {
        ids.forEach(getVertexAndMarker);
    });
}

function initStartVertices() {
    $.ajax({
        url: REST_URL + 'graph/startVertices',
        error: ajaxErrorHandler
    }).then(function (startVertices) {
        startVertices.forEach(showStartVertex);
    });
}

function getAndShowEdge(id) {
    $.ajax({
        url: REST_URL + 'graph/edge/' + id,
        error: ajaxErrorHandler
    }).then(showEdge);
}

function getVertexAndMarker(id) {
    $.ajax({
        url: REST_URL + 'graph/vertex/' + id,
        error: ajaxErrorHandler
    }).then(showVertex);
}

function showEdge(edge) {
    var polyLine = new google.maps.Polyline({
        path: convertPoints([edge.source.location, edge.destination.location]),
        geodesic: true,
        strokeColor: getRandomColor(),
        strokeOpacity: 1.0,
        strokeWeight: 2,
        map: map
    });

    edgesPolyLines.push(polyLine);
}

function showVertex(vertex) {
    var marker = new google.maps.Marker({
        position: {lat: vertex.location.latitude, lng: vertex.location.longitude},
        label: vertex.id.toString(),
        map: map
    });

    vertexMarkers.push(marker)
}

function showStartVertex(startVertex) {
    var marker = new google.maps.Marker({
        position: {lat: startVertex.location.latitude, lng: startVertex.location.longitude},
        label: startVertex.id.toString(),
        map: map
    });

    startVerticesPolyLines.push(marker);
}