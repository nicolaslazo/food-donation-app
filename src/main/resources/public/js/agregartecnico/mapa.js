var map = L.map('map').setView([-34.6037, -58.3816], 13);
// Cargar y añadir una capa de mapa desde OpenStreetMap
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 18,
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
    // Llamar a la función para actualizar el radio cuando se coloca un nuevo marcador
    actualizarRadio();
});
// Función para actualizar el radio cuando el usuario ingresa el valor
function actualizarRadio() {
    var radiusInKm = document.getElementById('radius').value;
    if (!radiusInKm || !currentMarker) {
        return;
    }
    var radiusInMeters = radiusInKm * 1000;
    // Si ya existe un círculo, eliminarlo
    if (currentCircle) {
        map.removeLayer(currentCircle);
    }
    // Obtener las coordenadas del marcado
    var markerLatLng = currentMarker.getLatLng();
    // Añadir un círculo alrededor del marcador con el radio dado en metros
    currentCircle = L.circle([markerLatLng.lat, markerLatLng.lng], {
        color: 'blue',
        fillColor: '#a0d6ff',
        fillOpacity: 0.3,
        radius: radiusInMeters
    }).addTo(map);
}


// Iterar sobre cada heladera para agregarla al mapa
heladeras.forEach(function(heladera) {
    // Crear un contenido de popup personalizado con el nombre y la capacidad disponible
    var popupContent = `
        <strong>${heladera.nombre}</strong><br>
    `;

    // Crear el marcador con el título y contenido de popup
    var marker = L.marker([heladera.lat, heladera.long], {title: heladera.nombre, idHeladera: heladera.idHeladera})
        .bindPopup(popupContent)
        .addTo(map);

});
