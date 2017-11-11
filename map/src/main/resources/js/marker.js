var markers = [];

function processAgent(agent){
    calculateRoute(agent, createMarkerForAgent(agent));
}

function createMarkerForAgent(agent) {
    var marker = new google.maps.Marker({
        position: {lat: agent.location.latitude, lng: agent.location.longitude},
        map: map,
        icon: getIconForAgentType(agent.type),
        label: agent.id + ": " + agent.type.toLowerCase()
    });
    markers.push(marker);
    return marker;
}

function calculateRoute(agent, marker) {
    var directionsService = new google.maps.DirectionsService;

    directionsService.route({
        origin: {lat: agent.location.latitude, lng: agent.location.longitude},
        destination: getDestination(agent),
        travelMode: getTravelMode(agent.type)
    }, function (response, status) {
        if (status === 'OK') {
            autoRefresh(response.routes[0].overview_path, marker, agent.speed);
        } else {
            //Fail silently
            //window.alert('Directions request failed due to ' + status);
            console.log('Directions request failed due to ' + status);
        }
    });
}

function autoRefresh(pathCoords, marker, speed) {
    for (var i = 0; i < pathCoords.length; i++) {
        setTimeout(function (coords) {marker.setPosition(coords);}, speed * 6 * i, pathCoords[i]);
    }
}

function getDestination(agent) {
    if (agent.destinations.length > 0) {
        var firstDestPoint = agent.destinations[0];
        var dest = {lat: firstDestPoint.latitude, lng: firstDestPoint.longitude};
        return dest;
    }
}

function getIconForAgentType(agentType) {
    switch (agentType) {
        case "HUMAN":
            return "http://maps.google.com/mapfiles/ms/icons/yellow-dot.png";
        case "CYCLE":
            return "http://maps.google.com/mapfiles/ms/icons/yellow-dot.png";
        case "CAR":
            return "http://maps.google.com/mapfiles/ms/icons/blue-dot.png";
        case "BUS":
            return "http://maps.google.com/mapfiles/ms/icons/red-dot.png";
        case "TRAM":
            return "http://maps.google.com/mapfiles/ms/icons/red-dot.png";
        case "TRUCK":
            return "http://maps.google.com/mapfiles/ms/icons/green-dot.png";
        case "UNKNOWN":
            return "http://maps.google.com/mapfiles/ms/icons/red-dot.png";
    }
}

function getTravelMode(agentType) {
    switch (agentType) {
        case "HUMAN":
            return 'WALKING';
        case "CYCLE":
            return 'BICYCLING';
        case "CAR":
            return 'DRIVING';
        case "BUS":
            return 'TRANSIT';
        case "TRAM":
            return 'TRANSIT';
        case "TRUCK":
            return 'DRIVING';
        case "UNKNOWN":
            return 'DRIVING';
    }
}

function clearMarkers() {
    setMapOnAll(null);

    function setMapOnAll(map) {
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(map);
        }
    }

    markers = [];
}