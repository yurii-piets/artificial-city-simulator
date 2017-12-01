var checkboxPattern = "<li style=\"list-style: none;\"><input type=\"checkbox\" onclick=\"onMenuCheckBoxAction(this);\" class=\"checkTypes\" value=\"${value}\">${value}</li>";

function onMenuBarClick() {
    createListOfWayTypes();
    createListOfStaticsTypes();
}

function createListOfWayTypes() {
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
            var ul = document.getElementById("road-type-enumeration");
            types.forEach(function (type) {
                ul.innerHTML += checkboxPattern.replace("${value}", type).replace("${value}", type)
            });
        }
    }
}

function createListOfStaticsTypes(){
    var request = getAjaxRequest();

    if (!request) {
        console.log("Ajax request error.");
        return;
    }

    request.open("GET", REST_URL + "statics/types", true);
    request.send();

    request.onreadystatechange = function () {
        if (request.readyState === 3 && request.status === 200) {
            var types = JSON.parse(request.response);
            var ul = document.getElementById("static-type-enumeration");
            types.forEach(function (type) {
                ul.innerHTML += checkboxPattern.replace("${value}", type).replace("${value}", type)
            });
        }
    }
}

function onMenuCheckBoxAction(box) {
    if(box.checked){
        cachedTypesMap[box.value].forEach(function (type) { type.setMap(map) })
    } else {
        cachedTypesMap[box.value].forEach(function (type) { type.setMap(null) })
    }
}