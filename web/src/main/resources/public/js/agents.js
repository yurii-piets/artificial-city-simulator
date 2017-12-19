function initAgents() {
    $.ajax({
        url: REST_URL + 'agents/objects',
        error: ajaxErrorHandler
    }).then(function (agents) {
        agents.forEach(createMarkerForAgent);
    });
}

function processDeadAgents() {
    $.ajax({
        url: REST_URL + 'agents/dead/ids',
        error: ajaxErrorHandler
    }).then(function (ids) {
        ids.forEach(deleteMarker);
    });
}