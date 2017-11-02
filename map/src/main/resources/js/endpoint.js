function initAgents() {
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "agent/all", true);
    request.send();
    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            var listOfLinks = JSON.parse(request.response);
            getAgentAndMakeMarker(listOfLinks);
        }
    };
}

function getAgentAndMakeMarker(links) {
    links.forEach(function (link) {
        var request = getAjaxRequest();

        if (!request) {
            console.log("Ajax request error.");
            return;
        }

        request.open("GET", link, true);
        request.send();
        request.onreadystatechange = function () {
            if (request.readyState === 3 && request.status === 200) {
                var agent = JSON.parse(request.response);
                processAgent(agent);
            }
        }
    });
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
