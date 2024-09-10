// Coordenadas iniciales centradas en Buenos Aires
var map = L.map('map').setView([-34.6037, -58.3816], 13); 

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 18,
}).addTo(map);

// Añadir los marcadores de las heladeras predefinidas y asignarles eventos de clic
var heladeras = [
    { coords: [-34.6025246,-58.3843585], nombre: 'Primera heladera' },
    { coords: [-34.6024605,-58.3812774], nombre: 'Segunda heladera' },
    { coords: [-34.6047476,-58.3795161], nombre: 'Tercera heladera' },
    { coords: [-34.598607968791924, -58.420107873800205], nombre: 'Heladera Medrano' }
];

// Integración de Leaflet.PinSearch
L.control.pinSearch({
    placeholder: 'Buscar heladera...',
    onSearch: function(query) {
        console.log('Buscando:', query);
    },
    focusOnMarker: true,
    maxSearchResults: 3
}).addTo(map);

var markerOrigen, markerDestino, polyline;

function seleccionarPunto(marker, nombreHeladera) {
    if (!markerOrigen) {
        markerOrigen = marker;
        markerOrigen.bindPopup(`Origen: ${nombreHeladera}`).openPopup();
        document.getElementById('latitudeOrigen').value = marker.getLatLng().lat;
        document.getElementById('longitudeOrigen').value = marker.getLatLng().lng;
    } else if (!markerDestino) {
        var distancia = calcularDistancia(markerOrigen.getLatLng().lat, markerOrigen.getLatLng().lng, marker.getLatLng().lat, marker.getLatLng().lng);
        
        if (distancia > 2.5) {
            alert('La distancia entre las heladeras es mayor a 2.5 km. Selecciona otro destino.');
        } else {
            markerDestino = marker;
            markerDestino.bindPopup(`Destino: ${nombreHeladera}`).openPopup();
            document.getElementById('latitudeDestino').value = marker.getLatLng().lat;
            document.getElementById('longitudeDestino').value = marker.getLatLng().lng;

            // Dibujar la trayectoria entre origen y destino
            if (polyline) {
                polyline.setLatLngs([markerOrigen.getLatLng(), markerDestino.getLatLng()]);
            } else {
                polyline = L.polyline([markerOrigen.getLatLng(), markerDestino.getLatLng()], { color: 'blue' }).addTo(map);
            }
        }
    }
}

// Deseleccionar el origen (sin eliminar el marcador)
document.getElementById('clear').addEventListener('click', function() {
    if (markerOrigen) {
        markerOrigen.closePopup();  // Solo cierra el popup sin eliminar el marcador
        markerOrigen = null;
        markerDestino = null;
        document.getElementById('latitudeOrigen').value = '';
        document.getElementById('longitudeOrigen').value = '';
        document.getElementById('latitudeDestino').value = '';
        document.getElementById('longitudeDestino').value = '';
        
        // Eliminar la línea si solo el origen es deseleccionado
        if (polyline) {
            map.removeLayer(polyline);
            polyline = null;
        }
    }
});

// Añadir los marcadores a las heladeras
heladeras.forEach(function(heladera) {
    var marker = L.marker(heladera.coords).bindPopup(heladera.nombre).addTo(map);

    marker.on('click', function() {
        seleccionarPunto(marker, heladera.nombre);
    });
});

// Cálculo de distancia en kilómetros
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
