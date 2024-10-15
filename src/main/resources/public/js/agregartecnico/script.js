// Inicializar el mapa en el div con id 'map' y centrarlo en unas coordenadas (por ejemplo, Buenos Aires)
var map = L.map('map').setView([-34.6037, -58.3816], 13); // Coordenadas de Buenos Aires con zoom nivel 13

// Cargar y añadir una capa de mapa desde OpenStreetMap
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 18
}).addTo(map);

// Variables para almacenar el marcador y el círculo actual
var currentMarker = null;
var currentCircle = null;

// Cuando se hace clic en el mapa, actualizar los campos de latitud y longitud
map.on('click', function(e) {
    var lat = e.latlng.lat;
    var lng = e.latlng.lng;

    // Actualizar los campos ocultos con la latitud y longitud seleccionadas
    document.getElementById('latitude').value = lat;
    document.getElementById('longitude').value = lng;

    // Habilitar el campo de radio
    document.getElementById('radius').disabled = false;

    // Si ya existe un marcador, eliminarlo
    if (currentMarker) {
        map.removeLayer(currentMarker);
    }

    // Si ya existe un círculo, eliminarlo
    if (currentCircle) {
        map.removeLayer(currentCircle);
    }

    // Añadir un nuevo marcador en la ubicación clicada y guardarlo en la variable
    currentMarker = L.marker([lat, lng]).addTo(map);
});

// Función para actualizar el radio cuando el usuario ingresa el valor
function actualizarRadio() {
    var radiusInKm = document.getElementById('radius').value;

    if (!radiusInKm || !currentMarker) {
        alert("Por favor, coloca un marcador en el mapa y selecciona un radio.");
        return;
    }

    var radiusInMeters = radiusInKm * 1000; // Convertir de kilómetros a metros

    // Si ya existe un círculo, eliminarlo
    if (currentCircle) {
        map.removeLayer(currentCircle);
    }

    // Obtener las coordenadas del marcador
    var markerLatLng = currentMarker.getLatLng();

    // Añadir un círculo alrededor del marcador con el radio dado en metros
    currentCircle = L.circle([markerLatLng.lat, markerLatLng.lng], {
        color: 'blue',        // Color del borde
        fillColor: '#a0d6ff', // Color de relleno
        fillOpacity: 0.3,     // Opacidad del relleno
        radius: radiusInMeters // Radio en metros
    }).addTo(map);
}