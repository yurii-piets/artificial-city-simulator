var lastRequestSuccessful = true;

function initAgents() {
    var stockEventSource = new EventSource("/acs/agents/objects");
    stockEventSource.onmessage = function (e) {
        createMarkerForAgent(JSON.parse(e.data));
    };
}

function processDeadAgents() {
    $.ajax({
        url: REST_URL + 'agents/dead/ids',
        error: ajaxErrorHandler
    }).then(function (ids) {
        ids.forEach(deleteMarker);
    });
}

function onAgentLoadError(jqXHR, exception) {
    lastRequestSuccessful = true;
    ajaxErrorHandler(jqXHR, exception);
}