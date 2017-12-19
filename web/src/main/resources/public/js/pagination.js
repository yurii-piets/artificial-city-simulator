var loadSize = 100;

function initAgentsWithPagination() {
    $.ajax({
        url: REST_URL + 'agents/ids',
        error: ajaxErrorHandler
    }).then(function (ids) {
        if (ids.length <= loadSize) {
            getAndMarkByObject();
        } else {
            var accum = [];
            for (var i = 0; i < ids.length; ++i) {
                accum.push(ids[i]);

                if (accum.length === loadSize || i === accum.length - 1) {
                    $.ajax({
                        url: REST_URL + 'agents/objects',
                        type: 'POST',
                        data: JSON.stringify(accum),
                        dataType: 'json',
                        contentType: 'application/json',
                        error: ajaxErrorHandler
                    }).then(function (agents) {
                        agents.forEach(createMarkerForAgent);
                    });
                    accum = [];
                }

            }
        }
    });
}

function getAndMarkByObject() {
    $.ajax({
        url: REST_URL + 'agents/objects'
    }).then(function (agents) {
        agents.forEach(createMarkerForAgent);
    });
}