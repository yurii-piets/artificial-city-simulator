var map;

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 50.065431, lng: 19.923276},
        zoom: 17,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    });

    setInterval(function () {
        initAgents();
    }, 200);
}
