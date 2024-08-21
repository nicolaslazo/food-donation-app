// Nuestro código relacionado a Leaflet
const map = L.map('map', {
    center: [-34.6037425,-58.381669],
    zoom: 17
});

L.tileLayer(
    'https://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png',
    { attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors' }
).addTo(map);

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

// Lista de heladeras con su información
const heladeras = [
    { id: 1, lat: -34.6025246, lng: -58.3843585, alerta: 'conexion', nombre: 'Heladera 1', fechaInstalacion: '01-01-2023', encargado: 'Juan Pérez', capacidad: 20, direccion: 'Calle Falsa 123', alertaFecha: '20-08-2024', alertaDescripcion: 'Problema de conexión.' },
    { id: 2, lat: -34.6024605, lng: -58.3812774, alerta: 'reporte', nombre: 'Heladera 2', fechaInstalacion: '15-03-2023', encargado: 'Maria López', capacidad: 25, direccion: 'Avenida Siempreviva 742', alertaFecha: '18-08-2024', alertaDescripcion: 'Reporte de mal uso.' },
    { id: 3, lat: -34.6047476, lng: -58.3795161, alerta: 'fraude', nombre: 'Heladera 3', fechaInstalacion: '10-05-2023', encargado: 'Carlos García', capacidad: 15, direccion: 'Calle Verdadera 456', alertaFecha: '19-08-2024', alertaDescripcion: 'Posible fraude detectado.' },
    { id: 4, lat: -34.598607968791924, lng: -58.420107873800205, alerta: 'temperatura', nombre: 'Heladera Medrano', fechaInstalacion: '05-07-2023', encargado: 'Ana Rodríguez', capacidad: 18, direccion: 'Calle Medrano 789', alertaFecha: '21-08-2024', alertaDescripcion: 'Alerta de temperatura elevada.' }
];

// Función para actualizar la información de la heladera en el panel
function actualizarInfoHeladera(heladera) {
    document.getElementById('nombre').innerText= heladera.nombre;
    document.getElementById('installDate').innerText = heladera.fechaInstalacion;
    document.getElementById('managerName').innerText = heladera.encargado;
    document.getElementById('capacity').innerText = heladera.capacidad;
    document.getElementById('address').innerText = heladera.direccion;
    document.getElementById('alertType').innerText = heladera.alerta;
    document.getElementById('alertDate').innerText = heladera.alertaFecha;
    document.getElementById('alertDescription').innerText = heladera.alertaDescripcion;
}

// Añadir los marcadores de las heladeras al mapa y asignar eventos
heladeras.forEach(heladera => {
    const marker = L.marker([heladera.lat, heladera.lng], {
        icon: obtenerIconoPorAlerta(heladera.alerta),
        title: heladera.nombre
    }).addTo(map);

    marker.on('click', function () {
        actualizarInfoHeladera(heladera);
    });
});
