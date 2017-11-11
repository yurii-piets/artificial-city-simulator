function initAgents() {
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "agent/all/count", true);
    request.send();
    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            var count = request.response;
            if (count < 100) {
                getAndMarkByObject();
            } else if (count < 5000) {
                getAndMarkByIds();
            } else {
                getAndMarkByRange();
            }
        }
    };
}

function getAndMarkByObject() {
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "agent/all/objects", true);
    request.send();
    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            var agents = JSON.parse(request.response);
            agents.forEach(processAgent);
        }
    }
}

function getAndMarkByIds() {
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "agent/all/ids", true);
    request.send();
    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            var ids = JSON.parse(request.response);
            ids.forEach(processById);
        }
    }
}

function getAndMarkByRange() {
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "agent/all/range", true);
    request.send();
    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            var range = JSON.parse(request.response);
            for (var i = range.min; i <= range.max; ++i) {
                processById(i);
            }
        }
    };
}

function processById(id) {
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "agent/" + id, true);
    request.send();
    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            var agent = JSON.parse(request.response);
            processAgent(agent);
        }
    }
}

function getAjaxRequest() {
    try {
        var request = new XMLHttpRequest();
    } catch (e1) {
        try {
            request = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e2) {
            try {
                request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e3) {
                request = false;
            }
        }
    }
    return request;
}
