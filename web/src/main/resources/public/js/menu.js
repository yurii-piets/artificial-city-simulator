var checkboxPattern = "<li style=\"list-style: none;\"><input type=\"checkbox\" onclick=\"onMenuCheckBoxAction();\" class=\"checkTypes\" value=\"${value}\">${value}</li>";

function onMenuBarClick() {
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "ways/types", true);
    request.send();

    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            var types = JSON.parse(request.response);
            var ul = document.getElementById("menu-bar-enumeration");
            types.forEach(function (type) {
                ul.innerHTML += checkboxPattern.replace("${value}", type).replace("${value}", type)
            });
        }
    }
}

var paramPattern = "type=${type}";

function onMenuCheckBoxAction() {
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    var boxes = document.getElementsByClassName("checkTypes");

    var params = "";
    for (var i = 0; i < boxes.length; ++i){
        var box = boxes[i];
        if(box.checked){
            var paramValue = "";
            var param = paramPattern.replace("${type}", box.value);
            params += param + "&"
        }
    }

    clearPolylines();
    if(params === ""){
        return;
    }

    request.open("GET", REST_URL + "ways?" + params, true);
    request.send();

    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            var wayIds = JSON.parse(request.response);
            wayIds.forEach(getAndDrawWay);
        }
    };
}