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
    })
});

// Definir la variable marker fuera del evento
var marker;

// Variable para almacenar el marcador seleccionado
var selectedMarker = null;

// Evento al hacer clic en el mapa
map.on('click', function(e) {
    var lat = e.latlng.lat;
    var lng = e.latlng.lng;

    // Obtener el nombre de la heladera desde el input
    var heladeraName = document.getElementById('heladera-name').value || 'Nueva heladera';

    // Si ya hay un marcador seleccionado, actualiza su posición
    if (selectedMarker) {
        selectedMarker.setLatLng(e.latlng);
        selectedMarker.bindPopup(heladeraName).openPopup();

        // Actualizar los inputs de latitud y longitud
        document.getElementById('latitude').value = lat;
        document.getElementById('longitude').value = lng;

        // Deseleccionar el marcador después de moverlo
        selectedMarker = null;
    } else {
        // Crear un nuevo marcador si no hay uno seleccionado
        var marker = L.marker(e.latlng, {
            draggable: true // Permitir arrastrar el marcador
        }).addTo(map);

        marker.bindPopup(heladeraName).openPopup();

        // Actualizar los inputs de latitud y longitud
        document.getElementById('latitude').value = lat;
        document.getElementById('longitude').value = lng;

        // Evento al hacer clic en el marcador
        marker.on('click', function() {
            // Seleccionar este marcador para moverlo
            selectedMarker = marker;

            // Actualizar los inputs con la posición actual del marcador
            var position = marker.getLatLng();
            document.getElementById('latitude').value = position.lat;
            document.getElementById('longitude').value = position.lng;
        });

        // Evento al arrastrar el marcador para actualizar las coordenadas
        marker.on('dragend', function(e) {
            var newLatLng = e.target.getLatLng();
            document.getElementById('latitude').value = newLatLng.lat;
            document.getElementById('longitude').value = newLatLng.lng;
        });
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