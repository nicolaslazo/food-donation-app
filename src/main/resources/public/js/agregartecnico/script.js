// Inicializar el mapa en el div con id 'map' y centrarlo en unas coordenadas
var map = L.map('map').setView([-34.6037, -58.3816], 13); // Coordenadas de Buenos Aires con zoom nivel 13

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 18,
}).addTo(map);

let currentMarker = null;
let currentCircle = null;

map.on('click', function (e) {
    const lat = e.latlng.lat;
    const lng = e.latlng.lng;

    document.getElementById('latitude').value = lat;
    document.getElementById('longitude').value = lng;
    document.getElementById('radius').disabled = false;

    if (currentMarker) map.removeLayer(currentMarker);
    if (currentCircle) map.removeLayer(currentCircle);

    currentMarker = L.marker([lat, lng]).addTo(map);
    actualizarRadio();
});

function actualizarRadio() {
    const radiusInKm = document.getElementById('radius').value;
    if (!radiusInKm || !currentMarker) return;

    const radiusInMeters = radiusInKm * 1000;
    if (currentCircle) map.removeLayer(currentCircle);

    const markerLatLng = currentMarker.getLatLng();
    currentCircle = L.circle([markerLatLng.lat, markerLatLng.lng], {
        color: 'blue',
        fillColor: '#a0d6ff',
        fillOpacity: 0.3,
        radius: radiusInMeters,
    }).addTo(map);
}

// Manejo del formulario
document.querySelector('.form-container').addEventListener('submit', async function (event) {
    event.preventDefault(); // Prevenir el envío del formulario por defecto

    const form = event.target;
    const formData = new FormData(form);

    try {
        const response = await fetch(form.action, {
            method: 'POST',
            body: formData,
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            if (errorMessage.includes("El correo ya está registrado.")) {
                mostrarModalError("El correo ingresado ya está registrado.");
            }
        } else {
            window.location.href = "/quiero-ayudar"; // Redirigir si todo está correcto
        }
    } catch (error) {
        console.error("Error al enviar el formulario:", error);
    }
});

function mostrarModalError(mensaje) {
    const modal = document.getElementById('errorModal');
    const modalMessage = document.getElementById('modalMessage');
    modalMessage.textContent = mensaje;
    modal.style.display = 'block';
}

document.getElementById('closeModal').addEventListener('click', function () {
    const modal = document.getElementById('errorModal');
    modal.style.display = 'none';
});

