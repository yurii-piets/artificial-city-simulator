function initAgents() {
    $.ajax({
        url: REST_URL + 'agent/all/objects'
    }).then(function (agents) {
        agents.forEach(createMarkerForAgent);
    });
}

function initAgentsWithPagination() {
    $.ajax({
        url: REST_URL + 'agent/all/count'
    }).then(function (count) {
        if (count < 100) {
            getAndMarkByObject();
        } else if (count < 5000) {
            getAndMarkByIds();
        } else {
            getAndMarkByRange();
        }
    });
}

function getAndMarkByObject() {
    $.ajax({
        url: REST_URL + 'agent/all/objects'
    }).then(function (agents) {
        agents.forEach(createMarkerForAgent);
    });
}

function getAndMarkByIds() {
    $.ajax({
        url: REST_URL + 'agent/all/ids'
    }).then(function (ids) {
        ids.forEach(processById);
    });
}

function getAndMarkByRange() {
    $.ajax({
        url: REST_URL + 'agent/all/range'
    }).then(function (range) {
        for (var i = range.min; i <= range.max; ++i) {
            processById(i);
        }
    });
}

function processById(id) {
    $.ajax({
        url: REST_URL + 'agent/' + id
    }).then(function (agent) {
        createMarkerForAgent(agent);
    });
}

function processDeadAgents() {
    $.ajax({
        url: REST_URL + 'agent/all/dead/ids'
    }).then(function (ids) {
        ids.forEach(deleteMarker);
    });
}