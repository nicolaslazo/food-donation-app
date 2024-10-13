// Inicializar el mapa en el div con id 'map' y centrarlo en unas coordenadas (por ejemplo, Buenos Aires)
var map = L.map('map').setView([-34.6037, -58.3816], 13); // Coordenadas de Buenos Aires con zoom nivel 13

// Cargar y añadir una capa de mapa desde OpenStreetMap
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 18
}).addTo(map);

// Cuando se hace clic en el mapa, actualizar los campos de latitud y longitud
map.on('click', function(e) {
    var lat = e.latlng.lat;
    var lng = e.latlng.lng;

    // Actualizar los campos ocultos con la latitud y longitud seleccionadas
    document.getElementById('latitude').value = lat;
    document.getElementById('longitude').value = lng;

    // Añadir un marcador en la ubicación clicada
    var marker = L.marker([lat, lng]).addTo(map);
});