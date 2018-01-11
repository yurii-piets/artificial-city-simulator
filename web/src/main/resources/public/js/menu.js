var CHECKBOX_PATTERN = "<li style=\"list-style: none;\">" +
    "<input type=\"checkbox\" onclick=\"onMenuCheckBoxAction(this);\" class=\"checkTypes\" value=\"${value}\" id =\"${value}\">${value}" +
    "</li>";
var TYPE_REPLACE_PATTERN = /\${value}/g;
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
        url: REST_URL + 'ways/types',
        error: ajaxErrorHandler
    }).then(function (wayTypes) {
        var ul = $('#road-type-enumeration');
        wayTypes.forEach(function (type) {
            var html = CHECKBOX_PATTERN.replace(TYPE_REPLACE_PATTERN, type);
            ul.html(ul.html() + html);
        });
    });
}

function createListOfStaticsTypes() {
    $.ajax({
        url: REST_URL + 'statics/types',
        error: ajaxErrorHandler
    }).then(function (staticTypes) {
        var ul = $('#static-type-enumeration');
        staticTypes.forEach(function (type) {
            var html = CHECKBOX_PATTERN.replace(TYPE_REPLACE_PATTERN, type);
            ul.html(ul.html() + html);
        });
    });
}

function onMenuCheckBoxAction(box) {
    if (cachedTypesMap[box.value] === undefined) {
        console.log(box.value + " contains 0 values");
        return;
    }

    if (box.value === 'lights' && box.checked) {
        updateLights(box);
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

function onMenuGraphAction(box) {
    if (box.checked) {
        switch (box.value) {
            case "vertices":
                if (vertexMarkers.length === 0) {
                    //todo add alert with confirmation
                    initVertices();
                } else {
                    vertexMarkers.forEach(function (marker) {
                        marker.setMap(map);
                    })
                }
                break;

            case "edges":
                if (edgesPolyLines.length === 0) {
                    //todo add alert with confirmation
                    initEdges();
                } else {
                    edgesPolyLines.forEach(function (line) {
                        line.setMap(map);
                    })
                }
                break;

            case "startVertices":
                if (startVerticesPolyLines.length === 0) {
                    //todo add alert with confirmation
                    initStartVertices();
                } else {
                    startVerticesPolyLines.forEach(function (line) {
                        line.setMap(map);
                    })
                }
                break;
        }
    } else {
        switch (box.value) {
            case "vertices":
                vertexMarkers.forEach(function (marker) {
                    marker.setMap(null);
                });
                break;

            case "edges":
                edgesPolyLines.forEach(function (edgeLine) {
                    edgeLine.setMap(null);
                });
                break;

            case "startVertices":
                startVerticesPolyLines.forEach(function (marker) {
                    marker.setMap(null);
                });
                break;
        }
    }
}