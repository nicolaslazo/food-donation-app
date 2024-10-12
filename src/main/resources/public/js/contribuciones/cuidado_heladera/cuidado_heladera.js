var map = L.map('map').setView([-34.6037, -58.3816], 13); // Coordenadas iniciales centradas en Buenos Aires

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 18,
}).addTo(map);

// Marcadores para las heladeras
heladeras.forEach(function (heladera) {
    var marcador = L.marker([heladera.lat, heladera.long], {title: heladera.nombre, id: heladera.idHeladera}).bindPopup(heladera.nombre).openPopup().addTo(map);
    marcador.on('click', function (e){
        document.getElementById("idHeladera").value = marcador.options.id;
        document.getElementById("isNewLocation").value = "false"; // Marcamos que es una heladera existente
        alert("Esta ubicación ya está ocupada por una heladera existente. Por favor, elija una nueva.");
    });
});

// Definir la variable marker fuera del evento
var marker;

map.on('click', function(e) {
    var lat = e.latlng.lat;
    var lng = e.latlng.lng;

    // Verificamos si el colaborador está seleccionando una nueva ubicación
    document.getElementById("idHeladera").value = ""; // Limpiamos el valor de idHeladera
    document.getElementById("isNewLocation").value = "true"; // Marcamos que es una nueva locación

    // Obtener el nombre de la heladera desde el input
    var heladeraName = document.getElementById('heladera-name').value || 'Nueva heladera';

    if (marker) {
        // Actualizar la posición del marcador existente
        marker.setLatLng(e.latlng);
    } else {
        // Crear un nuevo marcador
        marker = L.marker(e.latlng).addTo(map);
    }

    // Actualizar el popup del marcador con el nombre de la heladera
    marker.bindPopup(heladeraName).openPopup();

    // Actualizar los inputs de latitud y longitud
    document.getElementById('latitude').value = lat;
    document.getElementById('longitude').value = lng;
});

// Integración de Leaflet.PinSearch
let pinSearchControl = L.control.pinSearch({
    placeholder: 'Buscar heladera...',
    onSearch: function(query) {
        console.log('Buscando:', query);
        var markerFound = pinSearchControl._findMarkerByTitle(query);
        if (markerFound) {
            document.getElementById("idHeladera").value = markerFound.options.id;
            document.getElementById("isNewLocation").value = "false"; // Marcamos que es una heladera existente
            alert("Está seleccionando una heladera existente. Debe elegir una nueva ubicación.");
        }
    },
    focusOnMarker: true,
    maxSearchResults: 3
}).addTo(map);

// Validación antes del envío del formulario
document.querySelector('form').addEventListener('submit', function(e) {
    var isNewLocation = document.getElementById("isNewLocation").value;

    if (isNewLocation === "false") {
        e.preventDefault(); // Bloqueamos el envío
        alert("Debe seleccionar una nueva ubicación para la heladera. No puede usar una existente.");
    }
});
