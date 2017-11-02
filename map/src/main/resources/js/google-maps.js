var map;
function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 50.062150, lng: 19.937315},
        zoom: 15
    });

    gs();
}

