// Inicializar el mapa centrado en Buenos Aires
var map = L.map('map').setView([-34.6037, -58.3816], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 18,
}).addTo(map);

// Variables para marcadores y la línea entre origen y destino
let markerOrigen, markerDestino, polyline;

// Función para seleccionar heladera como origen o destino
function seleccionarPunto(marker, nombreHeladera, idHeladera, capacidadDisponible, viandasDepositadas) {
    const cantidadViandas = parseInt(document.getElementById('cantidadViandas').value);

    // Verificar que se haya ingresado una cantidad válida de viandas antes de seleccionar el origen o destino
    if (!cantidadViandas || isNaN(parseInt(cantidadViandas)) || parseInt(cantidadViandas) <= 0) {
        alert('Por favor, ingresa la cantidad de viandas a redistribuir antes de seleccionar las heladeras.');
        return;
    }

    if (!markerOrigen) {
        // Asignar origen
        if (cantidadViandas > viandasDepositadas) {
            alert(`La heladera de origen no tiene suficientes viandas. Tiene ${viandasDepositadas} viandas disponibles.`);
            return;
        }

        markerOrigen = marker;
        markerOrigen.bindPopup(`Origen: ${nombreHeladera} <br> Capacidad: ${capacidadDisponible} viandas <br> Viandas Depositadas: ${viandasDepositadas}`).openPopup();
        document.getElementById('latitudeOrigen').value = marker.getLatLng().lat;
        document.getElementById('longitudeOrigen').value = marker.getLatLng().lng;
        document.getElementById('idHeladeraOrigen').value = idHeladera;

    } else if (!markerDestino) {
        // Validar que el destino no sea el mismo que el origen
        if (markerOrigen.getLatLng().equals(marker.getLatLng())) {
            alert('El destino no puede ser el mismo que el origen. Selecciona otra heladera.');
            return;
        }

        // Validar distancia máxima de 2.5 km
        const distancia = calcularDistancia(markerOrigen.getLatLng(), marker.getLatLng());
        if (distancia > 2.5) {
            alert('La distancia entre las heladeras es mayor a 2.5 km. Selecciona otra heladera.');
            return;
        }

        // Verificar capacidad de la heladera destino
        if (cantidadViandas > capacidadDisponible) {
            alert(`La heladera de destino no tiene suficiente espacio. Puede recibir un máximo de ${capacidadDisponible} viandas.`);
            return;
        }

        // Asignar destino
        markerDestino = marker;
        markerDestino.bindPopup(`Destino: ${nombreHeladera} <br> Capacidad: ${capacidadDisponible} viandas <br> Viandas Depositadas: ${viandasDepositadas}`).openPopup();
        document.getElementById('latitudeDestino').value = marker.getLatLng().lat;
        document.getElementById('longitudeDestino').value = marker.getLatLng().lng;
        document.getElementById('idHeladeraDestino').value = idHeladera;

        // Dibujar la línea entre origen y destino
        if (polyline) {
            polyline.setLatLngs([markerOrigen.getLatLng(), markerDestino.getLatLng()]);
        } else {
            polyline = L.polyline([markerOrigen.getLatLng(), markerDestino.getLatLng()], { color: 'blue' }).addTo(map);
        }
    }
}

// Evento de envío para verificar que el formulario esté completo
document.getElementById('submitBtn').addEventListener('click', function(event) {
    const idHeladeraOrigen = document.getElementById('idHeladeraOrigen').value;
    const idHeladeraDestino = document.getElementById('idHeladeraDestino').value;

    if (!idHeladeraOrigen || !idHeladeraDestino) {
        alert('Por favor, selecciona una heladera de origen y destino.');
        event.preventDefault();
    }
});

// Evento para deseleccionar origen y destino
document.getElementById('clear').addEventListener('click', function() {
    if (markerOrigen) {
        markerOrigen.closePopup();
        markerOrigen = null;
        markerDestino = null;
        document.getElementById('latitudeOrigen').value = '';
        document.getElementById('longitudeOrigen').value = '';
        document.getElementById('latitudeDestino').value = '';
        document.getElementById('longitudeDestino').value = '';
        document.getElementById('idHeladeraOrigen').value = '';
        document.getElementById('idHeladeraDestino').value = '';

        if (polyline) {
            map.removeLayer(polyline);
            polyline = null;
        }
    }
});

// Añadir marcadores de heladeras con datos de capacidad
heladeras.forEach(function(heladera) {
    const marker = L.marker([heladera.lat, heladera.long], { title: heladera.nombre })
        .bindPopup(`<strong>${heladera.nombre}</strong><br>
        Capacidad: ${heladera.capacidadDisponible} viandas<br>
        Viandas Depositadas: ${heladera.viandasDepositadas}`)
        .addTo(map);

    // Evento para seleccionar la heladera al hacer clic
    marker.on('click', function() {
        seleccionarPunto(marker, heladera.nombre, heladera.idHeladera, heladera.capacidadDisponible, heladera.viandasDepositadas);
    });
});

// Función para calcular la distancia entre dos puntos geográficos en kilómetros
function calcularDistancia(latlng1, latlng2) {
    const R = 6371; // Radio de la Tierra en km
    const dLat = (latlng2.lat - latlng1.lat) * Math.PI / 180;
    const dLon = (latlng2.lng - latlng1.lng) * Math.PI / 180;
    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(latlng1.lat * Math.PI / 180) * Math.cos(latlng2.lat * Math.PI / 180) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);
    return R * 2 * Math.asin(Math.sqrt(a));
}

// Integración de Leaflet.PinSearch para buscar heladeras
L.control.pinSearch({
    placeholder: 'Buscar heladera...',
    autoActive: false,
    onSearch: function(query) {
        const resultado = heladeras.find(heladera => heladera.nombre.toLowerCase().includes(query.toLowerCase()));
        if (resultado) {
            const latlng = L.latLng(resultado.lat, resultado.long);
            map.setView(latlng, 15);
            seleccionarPunto(L.marker(latlng), resultado.nombre, resultado.idHeladera, resultado.capacidadDisponible);
        } else {
            alert('No se encontró la heladera');
        }
    },
    focusOnMarker: false,
    maxSearchResults: 3
}).addTo(map);



