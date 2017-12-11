var checkboxPattern = "<li style=\"list-style: none;\"><input type=\"checkbox\" onclick=\"onMenuCheckBoxAction(this);\" class=\"checkTypes\" value=\"${value}\">${value}</li>";
var menuOpen = false;

function onMenuBarClick() {
    showMenu();
}

function showMenu() {
    var menuBar = document.getElementById("menu-bar");
    var menuBarBtn = document.getElementById("menu-bar-button");
    if (menuOpen) {
        menuBar.style.display = "none";
        menuBarBtn.style.left = "0";
    } else {
        menuBar.style.display = "block";
        menuBarBtn.style.left = "14.75%";
    }
    menuOpen = !menuOpen;
}

function createListOfWayTypes() {
    $.ajax({
        url: REST_URL + 'ways/types'
    }).then(function (wayTypes) {
        var ul = $('#road-type-enumeration');
        wayTypes.forEach(function (type) {
            var html = checkboxPattern.replace("${value}", type).replace("${value}", type);
            ul.html(ul.html() + html);
        });
    });
}

function createListOfStaticsTypes() {
    $.ajax({
        url: REST_URL + 'statics/types'
    }).then(function (staticTypes) {
        var ul = $('#static-type-enumeration');
        staticTypes.forEach(function (type) {
            var html = checkboxPattern.replace("${value}", type).replace("${value}", type);
            ul.html(ul.html() + html);
        });
    });
}

function onMenuCheckBoxAction(box) {
    if (cachedTypesMap[box.value] === undefined) {
        console.log(box.value + " contains 0 values");
        return;
    }

    if (box.checked) {
        cachedTypesMap[box.value].forEach(function (type) {
            type.setMap(map)
        })
    } else {
        cachedTypesMap[box.value].forEach(function (type) {
            type.setMap(null)
        })
    }
}