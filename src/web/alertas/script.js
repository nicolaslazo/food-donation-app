// Nuestro código relacionado a Leaflet
const map = L.map('map', {
    center: [-34.6037425,-58.381669],
    zoom: 17
});

L.tileLayer(
    'https://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png',
    { attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors' }
).addTo(map);

var marker;

map.on('click', function(e) {
    var lat = e.latlng.lat;
    var lng = e.latlng.lng;

    if (marker) {
        marker.setLatLng(e.latlng);
    }

    document.getElementById('latitude').value = lat;
    document.getElementById('longitude').value = lng;
});

// Integración de Leaflet.PinSearch
L.control.pinSearch({
    placeholder: 'Buscar heladera...',
    onSearch: function(query) {
        console.log('Buscando:', query);
    },
    focusOnMarker: true,
    maxSearchResults: 3
}).addTo(map);

// Función para mostrar la información de la heladera en el panel
function mostrarInfoHeladera(heladera) {
    const infoPanel = document.getElementById('infoPanel');
    infoPanel.innerHTML = `
        <h2>Heladera ID: ${heladera.id}</h2>
        <p>Alerta: ${heladera.alerta}</p>
        <p>Detalles: ${heladera.detalles}</p>
    `;
}

document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById("modal");
    const modalImg = document.getElementById("imgModal");
    const closeBtn = document.getElementById("close");

    document.querySelectorAll('.alert img, .previous-alert img').forEach(img => {
        img.addEventListener('click', function () {
            modal.style.display = "block";
            modalImg.src = this.src;
        });
    });

    closeBtn.onclick = function () {
        modal.style.display = "none";
    }

    modal.onclick = function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }
});

document.addEventListener('DOMContentLoaded', () => {
    // Definir iconos personalizados
    const conexionIcon = L.icon({
        iconUrl: '../mapa/images/iconConexion.png',
        iconSize: [32, 32],
        iconAnchor: [16, 35],
        popupAnchor: [0, -32]
    });

    const fraudeIcon = L.icon({
        iconUrl: '../mapa/images/iconFraude.png',
        iconSize: [32, 32],
        iconAnchor: [16, 35],
        popupAnchor: [0, -32]
    });

    const temperturaIcon = L.icon({
        iconUrl: '../mapa/images/iconTemperatura.png',
        iconSize: [32, 32],
        iconAnchor: [16, 35],
        popupAnchor: [0, -32]
    });

    const reporteIcon = L.icon({
        iconUrl: '../mapa/images/iconReporteColaborador.png',
        iconSize: [32, 32],
        iconAnchor: [16, 35],
        popupAnchor: [0, -32]
    });

    // Función para actualizar ícono basado en el tipo de alerta
    function obtenerIconoPorAlerta(tipoAlerta) {
        switch(tipoAlerta.toLowerCase()) {
            case 'conexion':
                return conexionIcon;
            case 'fraude':
                return fraudeIcon;
            case 'reporte':
                return reporteIcon;
            case 'temperatura':
                return temperturaIcon;
            default:
                return bajaIcon;  // Icono por defecto si no se reconoce el tipo
        }
    }

    // Ejemplo: Crear marcadores con diferentes tipos de alertas
    const heladeras = [
        { lat: -34.6025246, lng: -58.3843585, alerta: 'conexion', nombre: 'Heladera 1' },
        { lat: -34.6024605, lng: -58.3812774, alerta: 'reporte', nombre: 'Heladera 2' },
        { lat: -34.6047476, lng: -58.3795161, alerta: 'fraude', nombre: 'Heladera 3' },
        { lat: -34.598607968791924, lng: -58.420107873800205, alerta: 'temperatura', nombre: 'Heladera Medrano' }
    ];

    heladeras.forEach(heladera => {
        const marker = L.marker([heladera.lat, heladera.lng], {
            icon: obtenerIconoPorAlerta(heladera.alerta),
            title: heladera.nombre
        }).bindPopup(`<b>${heladera.nombre}</b><br>Alerta: ${heladera.alerta}`).addTo(map);
    });
});