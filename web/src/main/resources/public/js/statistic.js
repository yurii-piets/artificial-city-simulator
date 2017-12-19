function showStatistic(stat) {

    var statisticMarker = new google.maps.Marker({
        position: {lat: stat.location.latitude, lng: stat.location.longitude},
        label: stat.count + 'c/h',
        map: map
    });

    statisticMarker.addListener('click', function () {
        statisticMarker.setMap(null);
    })
}