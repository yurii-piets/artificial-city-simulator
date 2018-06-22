var es = [];
var MAX_REQUEST_AMOUNT = 25;

function initAgents() {

    if (es.length >= MAX_REQUEST_AMOUNT) {
        for (var i = 0; i < es.length; ++i) {
            if (es[i].readyState === EventSource.CLOSED) {
                es = es.slice(i, 1);
            }
        }
    }

    while (es.length >= MAX_REQUEST_AMOUNT) {
        es.pop().close();
    }

    if (es.length <= MAX_REQUEST_AMOUNT) {
        var stockEventSource = new EventSource("/acs/agents/objects");
        stockEventSource.onmessage = function (e) {
            createMarkerForAgent(JSON.parse(e.data));
        };
        es.add(stockEventSource);
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

function onAgentLoadError(jqXHR, exception) {
    lastRequestSuccessful = true;
    ajaxErrorHandler(jqXHR, exception);
}