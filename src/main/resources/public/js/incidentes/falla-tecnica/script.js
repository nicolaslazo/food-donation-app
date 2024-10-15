// Nuestro código relacionado a Leaflet
const map = L.map('map', {
    center: [-34.6037425,-58.381669],
    zoom: 17
});

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

// Verificación de la Imagen Ingresada
document.addEventListener('DOMContentLoaded', function() {
    const fileInput = document.querySelector('input[name="imagen"]');
    const form = document.querySelector('form');
    const submitButton = document.querySelector('.submit-button input[type="submit"]');

    fileInput.addEventListener('change', function(event) {
        const file = event.target.files[0];
        if (file) {
            const fileType = file.type;
            if (fileType !== 'image/jpeg') {
                alert('Por favor, sube una imagen en formato JPG.');
                fileInput.value = ''; // Limpiar el input
                submitButton.disabled = true; // Deshabilitar botón de enviar
            } else {
                alert('Imagen aceptada.');
                submitButton.disabled = false; // Habilitar botón de enviar
            }
        }
    });

    form.addEventListener('submit', function(event) {
        const file = fileInput.files[0];
        if (!file || file.type !== 'image/jpeg') {
            event.preventDefault();
            alert('Por favor, asegúrate de subir una imagen en formato JPG antes de enviar.');
        }
    });
});
