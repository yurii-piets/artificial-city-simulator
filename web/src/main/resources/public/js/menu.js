var checkboxPattern = "<li style=\"list-style: none;\"><input type=\"checkbox\">${value}</li>";

function onMenuBarClick(){
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "", true);
    request.send();

    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            var agents = JSON.parse(request.response);

            agents.forEach(createMarkerForStatic);
        }
    }
}