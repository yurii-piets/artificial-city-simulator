var lastRequestSuccessful = true;

function initAgents() {
    if (lastRequestSuccessful) {
        lastRequestSuccessful = false;
        $.ajax({
            url: REST_URL + 'agents/objects',
            error: onAgentLoadError
        }).then(function (agents) {
            agents.forEach(createMarkerForAgent);
            lastRequestSuccessful = true;
        });
    }
}

function processDeadAgents() {
    $.ajax({
        url: REST_URL + 'agents/dead/ids',
        error: ajaxErrorHandler
    }).then(function (ids) {
        ids.forEach(deleteMarker);
    });
}

function onAgentLoadError(jqXHR, exception){
    lastRequestSuccessful = true;
    ajaxErrorHandler(jqXHR, exception);
}