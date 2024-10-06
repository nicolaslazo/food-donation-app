// Nuestro código relacionado a Leaflet
const map = L.map('map', {
    center: [-34.6037425,-58.381669],
    zoom: 17
});


L.tileLayer(
    'https://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png',
    { attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors' }
).addTo(map);

// Marcadores para las heladeras
heladeras.forEach(function (heladera) {
   L.marker([heladera.lat, heladera.long], {title: heladera.nombre}).bindPopup(heladera.nombre).openPopup().addTo(map);
});
var marker;

map.on('click', function(e) {
    var lat = e.latlng.lat;
    var lng = e.latlng.lng;

    if (marker) {
        marker.setLatLng(e.latlng);
    }

    document.getElementById('latitude').value = lat;
    document.getElementById('longitude').value = lng;
});

// Integración de Leaflet.PinSearch
L.control.pinSearch({
    placeholder: 'Buscar heladera...',
    onSearch: function(query) {
        console.log('Buscando:', query);
    },
    focusOnMarker: true,
    maxSearchResults: 6
}).addTo(map);
