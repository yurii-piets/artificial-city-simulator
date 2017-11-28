var agentMarkers = [];

function createMarkerForAgent(agent) {
    if(agentMarkers[agent.id] === undefined ) {
        agentMarkers[agent.id] = new google.maps.Marker({
            position: {lat: agent.location.latitude, lng: agent.location.longitude},
            map: map,
            icon: getIconForAgentType(agent.type),
            label: agent.id + ": " + agent.type.toLowerCase()
        });
    } else {
        agentMarkers[agent.id].setPosition({lat: agent.location.latitude, lng: agent.location.longitude});
    }
}

function createMarkerForStatic(staticObject){
    new google.maps.Marker({
        position: {lat: staticObject.location.latitude, lng: staticObject.location.longitude},
        map: map,
        icon: getIconForStaticType(staticObject.type),
        label: staticObject.id + ": " + staticObject.type.toLowerCase()
    });
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

function getIconForStaticType(staticType) {
    switch (staticType) {
        case "STOP":
            return "http://maps.google.com/mapfiles/ms/icons/green-dot.png";
        case "LIGHTS":
            return "http://maps.google.com/mapfiles/ms/icons/red-dot.png";
        case "CROSSING":
            return "http://maps.google.com/mapfiles/ms/icons/yellow-dot.png";
    }
}