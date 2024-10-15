// Inicializar el mapa centrado en Buenos Aires
var map = L.map('map').setView([-34.6037, -58.3816], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 18,
}).addTo(map);

// Variables para los marcadores y la línea que conecta origen y destino
var markerOrigen, markerDestino, polyline;

// Función para seleccionar punto de origen y destino
function seleccionarPunto(marker, nombreHeladera, idHeladera) {
    if (!markerOrigen) {
        // Asignar origen
        markerOrigen = marker;
        markerOrigen.bindPopup(`Origen: ${nombreHeladera}`).openPopup();
        document.getElementById('latitudeOrigen').value = marker.getLatLng().lat;
        document.getElementById('longitudeOrigen').value = marker.getLatLng().lng;
        document.getElementById('idHeladeraOrigen').value = idHeladera; // Guardar el id de la heladera de origen
    } else if (!markerDestino) {
        // Verificar si el destino es el mismo que el origen
        if (markerOrigen.getLatLng().equals(marker.getLatLng())) {
            alert('El destino no puede ser el mismo que el origen. Selecciona otro destino.');
            return;
        }

        // Verificar la distancia
        var distancia = calcularDistancia(markerOrigen.getLatLng().lat, markerOrigen.getLatLng().lng, marker.getLatLng().lat, marker.getLatLng().lng);
        if (distancia > 2.5) {
            alert('La distancia entre las heladeras es mayor a 2.5 km. Selecciona otro destino.');
        } else {
            // Asignar destino
            markerDestino = marker;
            markerDestino.bindPopup(`Destino: ${nombreHeladera}`).openPopup();
            document.getElementById('latitudeDestino').value = marker.getLatLng().lat;
            document.getElementById('longitudeDestino').value = marker.getLatLng().lng;
            document.getElementById('idHeladeraDestino').value = idHeladera;
            // Dibujar la trayectoria entre origen y destino
            if (polyline) {
                polyline.setLatLngs([markerOrigen.getLatLng(), markerDestino.getLatLng()]);
            } else {
                polyline = L.polyline([markerOrigen.getLatLng(), markerDestino.getLatLng()], { color: 'blue' }).addTo(map);
            }
        }
    }
}

// Evento para deseleccionar el origen y destino
document.getElementById('clear').addEventListener('click', function() {
    if (markerOrigen) {
        markerOrigen.closePopup();
        markerOrigen = null;
        markerDestino = null;
        document.getElementById('latitudeOrigen').value = '';
        document.getElementById('longitudeOrigen').value = '';
        document.getElementById('latitudeDestino').value = '';
        document.getElementById('longitudeDestino').value = '';
        document.getElementById('idHeladeraOrigen').value = ''; // Resetear el id de la heladera
        document.getElementById('idHeladeraDestino').value = '';
        // Eliminar la línea de conexión si existe
        if (polyline) {
            map.removeLayer(polyline);
            polyline = null;
        }
    }
});

// Añadir los marcadores de heladeras recuperadas de la base de datos
heladeras.forEach(function(heladera) {
    var marker = L.marker([heladera.lat, heladera.long]).bindPopup(heladera.nombre).addTo(map);
    // Asignar evento al hacer clic en el marcador
    marker.on('click', function() {
        seleccionarPunto(marker, heladera.nombre, heladera.idHeladera);
    });
});

// Cálculo de distancia en kilómetros entre dos puntos geográficos
function calcularDistancia(lat1, lon1, lat2, lon2) {
    var R = 6371; // Radio de la Tierra en km
    var dLat = (lat2 - lat1) * Math.PI / 180;
    var dLon = (lon2 - lon1) * Math.PI / 180;
    var a =
        0.5 - Math.cos(dLat)/2 +
        Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
        (1 - Math.cos(dLon))/2;
    return R * 2 * Math.asin(Math.sqrt(a));
}

// Integración de Leaflet.PinSearch para buscar heladeras
L.control.pinSearch({
    placeholder: 'Buscar heladera...',
    onSearch: function(query) {
        var resultado = heladeras.find(heladera => heladera.nombre.toLowerCase().includes(query.toLowerCase()));
        if (resultado) {
            var latlng = L.latLng(resultado.lat, resultado.long);
            map.setView(latlng, 15);  // Centramos el mapa en la heladera encontrada
            seleccionarPunto(L.marker(latlng), resultado.nombre, resultado.idHeladera);
        } else {
            alert('No se encontró la heladera');
        }
    },
    focusOnMarker: false,
    maxSearchResults: 3
}).addTo(map);
