// Inicializar el mapa en el div con id 'map' y centrarlo en unas coordenadas (por ejemplo, Buenos Aires)
var map = L.map('map').setView([-34.6037, -58.3816], 13); // Coordenadas de Buenos Aires con zoom nivel 13

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
    var radiusInMeters = radiusInKm * 1000; // Convertir de kilómetros a metros

    // Si ya existe un círculo, eliminarlo
    if (currentCircle) {
        map.removeLayer(currentCircle);
    }

    // Obtener las coordenadas del marcador
    var markerLatLng = currentMarker.getLatLng();

    // Añadir un círculo alrededor del marcador con el radio dado en metros
    currentCircle = L.circle([markerLatLng.lat, markerLatLng.lng], {
        color: 'blue',       // Color del borde
        fillColor: '#a0d6ff', // Color de relleno
        fillOpacity: 0.3,     // Opacidad del relleno
        radius: radiusInMeters // Radio en metros
    }).addTo(map);
}

// Función para verificar la capacidad de la heladera
function verificarCapacidadHeladera(cantidadViandas, heladera) {
    return cantidadViandas <= heladera.capacidadDisponible;
}

// Iterar sobre cada heladera para agregarla al mapa
heladeras.forEach(function(heladera) {
    // Crear un contenido de popup personalizado con el nombre y la capacidad disponible
    var popupContent = `
        <strong>${heladera.nombre}</strong><br>
        Capacidad Disponible: ${heladera.capacidadDisponible} viandas
    `;

    // Crear el marcador con el título y contenido de popup
    var marker = L.marker([heladera.lat, heladera.long], {title: heladera.nombre, idHeladera: heladera.idHeladera})
        .bindPopup(popupContent)
        .addTo(map);

    // Asignar evento al hacer clic en el marcador
    marker.on('click', function (e) {
        // Obtener la cantidad de viandas seleccionada
        const cantidadViandas = parseInt(document.getElementById('cantidad').value, 10);

        // Verificar si la heladera tiene suficiente capacidad
        if (!verificarCapacidadHeladera(cantidadViandas, heladera)) {
            alert("La heladera no tiene suficiente capacidad.");
            return; // Si no hay suficiente capacidad, salir de la función
        }

        // Si la capacidad es suficiente, asignar los valores al formulario
        document.getElementById('latitude').value = heladera.lat;
        document.getElementById('longitude').value = heladera.long;
        document.getElementById('idHeladera').value = heladera.idHeladera;
    });
});

// Función para verificar si la persona es mayor de 18 años
function esMayorDe18(fechaNacimiento) {
    const hoy = new Date();
    const nacimiento = new Date(fechaNacimiento);
    const edad = hoy.getFullYear() - nacimiento.getFullYear();
    const mes = hoy.getMonth() - nacimiento.getMonth();
    const dia = hoy.getDate() - nacimiento.getDate();

    return (edad > 18 || (edad === 18 && mes > 0) || (edad === 18 && mes === 0 && dia >= 0));
}

// Agregar validación al formulario
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('formulario').addEventListener('submit', function(event) {
        const fechaNacimiento = document.getElementById('donb').value;
        const resultado = esMayorDe18(fechaNacimiento);

        if (!resultado) {
            event.preventDefault(); // Prevenir el envío del formulario si no es mayor de 18
            alert("Debe ser mayor de 18 años para registrarse.");
        }
    });
});

// Agregar evento de cambio al input de radio
document.getElementById('radius').addEventListener('input', actualizarRadio);

