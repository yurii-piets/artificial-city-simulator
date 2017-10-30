var markers = [];

function clearMarkers() {
    setMapOnAll(null);

function setMapOnAll(map) {
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(map);
        }
    }
}

function createMarkerForAgents(agents) {
    agents.forEach(createMarkerForAgent);
}

function createMarkerForAgent(agent){
    switch(agent.type){
        case "HUMAN":
            createHumanMarker(agent);
            break;
        case "CYCLE":
            createCycleMarker(agent);
            break;
        case "CAR":
            createCarMarket(agent);
            break;
        case "BUS":
            createBusMarker(agent);
            break;
        case "TRAM":
            createTramMarket(agent);
            break;
        case "TRUCK":
            createTruckMarket(agent);
            break;
        case "UNKOWN":
            createUnknownMarket(agent);
            break;
    }
}

function createHumanMarker(agent){

}

function createCycleMarker(agent){

}

function createCarMarket(agent) {
    var marker = new google.maps.Marker({
        position: {lat: agent.location.latitude, lng: agent.location.longitude},
        map: map
    });
    markers.push(marker);
}

function createBusMarker(agent) {

}

function createTramMarket(agent) {

}

function createTruckMarket(agent) {

}

function createUnknownMarket(agent){

}