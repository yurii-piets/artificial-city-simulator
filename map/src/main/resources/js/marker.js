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
    agents.forEach(createMarkerForAgent);
}

function createMarkerForAgent(agent){
    var marker = new google.maps.Marker({
        position: {lat: agent.location.latitude, lng: agent.location.longitude},
        map: map,
        icon: getIconForAgentType(agent.type),
        label: agent.id + ": " + agent.type.toLowerCase()
    });
    markers.push(marker);
}

function getIconForAgentType(agentType) {
    switch(agentType){
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