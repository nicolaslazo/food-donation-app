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
   var marcador = L.marker([heladera.lat, heladera.long], {title: heladera.nombre, id: heladera.idHeladera}).bindPopup(heladera.nombre).openPopup().addTo(map);
   marcador.on('click', function (e){
       document.getElementById("idHeladera").value = marcador.options.id;
   })
});
var marker;

map.on('click', function(e) {
    var lat = e.latlng.lat;
    var lng = e.latlng.lng;
    if (marker) {
        marker.setLatLng(e.latlng);
    }
});

// Integración de Leaflet.PinSearch
let pinSearchControl = L.control.pinSearch({
    placeholder: 'Buscar heladera...',
    onSearch: function(query) {
        console.log('Buscando:', query);
        document.getElementById("idHeladera").value = pinSearchControl._findMarkerByTitle(query).options.id;
    },
    focusOnMarker: true,
    maxSearchResults: 3
}).addTo(map);
