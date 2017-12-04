var edgesPolyLines = [];

function initEdges() {
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "graph/edges/ids", true);
    request.send();

    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            var ids = JSON.parse(request.response);
            ids.forEach(getAndShowEdge);
        }
    }
}

function getAndShowEdge(id) {
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "graph/edge/" + id, true);
    request.send();

    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            var edge = JSON.parse(request.response);
            showEdge(edge);
        }
    }
}

function showEdge(edge) {
    var polyLine = new google.maps.Polyline({
        path: convertPoints([edge.source.location, edge.destination.location]),
        geodesic: true,
        strokeColor: '#ff1300',
        strokeOpacity: 1.0,
        strokeWeight: 2,
        map: map
    });

    edgesPolyLines.push(polyLine);
}