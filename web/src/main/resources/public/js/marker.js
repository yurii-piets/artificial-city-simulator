var agentMarkers = [];

function createMarkerForAgent(agent) {
    if (agentMarkers[agent.id] === undefined) {
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

function createMarkerForStatic(staticObject) {
    new google.maps.Marker({
        position: {lat: staticObject.location.latitude, lng: staticObject.location.longitude},
        map: map,
        icon: getIconForStaticType(staticObject.type),
        label: staticObject.id + ": " + staticObject.type.toLowerCase()
    });
}

function createPolylineForWay(way) {
    new google.maps.Polyline({
        path: convertPoints(way.points),
        geodesic: true,
        strokeColor: getColorForWayType(way.roadType),
        strokeOpacity: 1.0,
        strokeWeight: 2,
        map: map
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

function getColorForWayType(roadType) {
    switch (roadType) {
        case "PATH":
            return '#0b00ff';

        case "FOOTWAY":
            return '#daff00';

        case "SERVICE":
            return '#00ff46';

        case "PEDESTRIAN":
            return '#00fffd';

        case "RESIDENTIAL":
            return '#ff00ff';

        case "PRIMARY":
            return '#ff3568';

        case "SECONDARY":
            return '#507b4b';

        case "TERTIARY":
            return '#f5ffc9';

        case "LIVING_STREET":
            return '#a896ff';

        case "STEPS":
            return '#1effd0';

        case "PRIMARY_LINK":
            return '#74554a';

        case "SECONDARY_LINK":
            return '#679fff';

        case "TRACK":
            return '#ffbad0';

        case "OTHER":
            return '#8f8f8f';

        case "UNKNOWN":
            return '#ff0800';
    }
}

function getRandomColor() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

function convertPoints(points) {
    var cumulated = [];
    for (var i = 0; i < points.length; i++) {
        cumulated[i] = {lat: points[i].latitude, lng: points[i].longitude};
    }
    return cumulated;
}
