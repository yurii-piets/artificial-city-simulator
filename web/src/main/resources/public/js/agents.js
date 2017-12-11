function initAgents() {
    $.ajax({
        url: REST_URL + 'agent/all/objects'
    }).then(function (agents) {
        agents.forEach(createMarkerForAgent);
    });
}

function processDeadAgents() {
    $.ajax({
        url: REST_URL + 'agent/all/dead/ids'
    }).then(function (ids) {
        ids.forEach(deleteMarker);
    });
}