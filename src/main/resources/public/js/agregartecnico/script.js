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


document.getElementById('submitBtn').addEventListener('click', function(event) {
    let formIsValid = true;  // Variable para controlar si el formulario es válido

    // Limpiar mensajes de error previos
    document.querySelectorAll('.error-message').forEach(function(element) {
        element.textContent = '';  // Limpiar mensaje
        element.style.display = 'none';  // Ocultar mensaje
    });

    // Obtener la fecha de nacimiento
    const dob = document.getElementById('dob').value;
    const currentDate = new Date();
    const birthDate = new Date(dob);
    const age = currentDate.getFullYear() - birthDate.getFullYear();
    const month = currentDate.getMonth() - birthDate.getMonth();

    if (age < 18 || (age === 18 && month < 0)) {
        document.getElementById('dob-error').textContent = 'Debes tener al menos 18 años para registrarte.';
        document.getElementById('dob-error').style.display = 'block';  // Mostrar mensaje
        formIsValid = false;
    }

    // Validar formato de DNI, Libreta o Cédula Argentina
    const documento = document.getElementById('numero-documento').value;
    const dniRegex = /^[\d]{1,3}\.?[\d]{3,3}\.?[\d]{3,3}$/;
    if (!dniRegex.test(documento)) {
        document.getElementById('documento-error').textContent = 'El número de documento no es válido. Debe seguir el formato argentino.';
        document.getElementById('documento-error').style.display = 'block';
        formIsValid = false;
    }

    // Validar CUIT
    const cuil = document.getElementById('cuil').value;
    const cuitRegex = /^([0-9]{11}|[0-9]{2}-[0-9]{8}-[0-9]{1})$/g;
    if (!cuitRegex.test(cuil)) {
        document.getElementById('cuil-error').textContent = 'El CUIT no es válido. Debe seguir el formato correcto.';
        document.getElementById('cuil-error').style.display = 'block';
        formIsValid = false;
    }

    if (!formIsValid) {
        event.preventDefault();
    }
});
