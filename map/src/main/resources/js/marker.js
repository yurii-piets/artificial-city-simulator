var markers = [];

function clearMarkers() {
    setMapOnAll(null);

    function setMapOnAll(map) {
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(map);
        }
    }

    markers = [];
}

function createMarkerForAgents(agents) {
    agents.forEach(function (agent) {
        createMarkerForAgent(agent);
        calculateRoute(agent);
    });
}

function createMarkerForAgent(agent) {
    var marker = new google.maps.Marker({
        position: {lat: agent.location.latitude, lng: agent.location.longitude},
        map: map,
        icon: getIconForAgentType(agent.type),
        label: agent.id + ": " + agent.type.toLowerCase()
    });
    markers.push(marker);
}

function calculateRoute(agent) {
    var directionsService = new google.maps.DirectionsService;
    var directionsDisplay = new google.maps.DirectionsRenderer;
    directionsDisplay.setMap(map);

    directionsService.route({
        origin: {lat: agent.location.latitude, lng: agent.location.longitude},
        destination: getDestination(agent),
        travelMode: getTraveMode(agent.type)
    }, function (response, status) {
        if (status === 'OK') {
            directionsDisplay.setDirections(response);
        } else {
            window.alert('Directions request failed due to ' + status);
        }
    });
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

function getTraveMode(agentType) {
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