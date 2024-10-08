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
L.marker([-34.6025246,-58.3843585], { title: 'Primera heladera' }).bindPopup('Primera heladera').openPopup().addTo(map);
L.marker([-34.6024605,-58.3812774], { title: 'Segunda heladera' }).bindPopup('Segunda heladera').openPopup().addTo(map);
L.marker([-34.6047476,-58.3795161], { title: 'Tercera heladera' }).bindPopup('Tercera heladera').openPopup().addTo(map);
L.marker([-34.598607968791924, -58.420107873800205], {title: 'Heladera Medrano' }).bindPopup('Heladera medrano').openPopup().addTo(map);

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
    maxSearchResults: 3
}).addTo(map);
