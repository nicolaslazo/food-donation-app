// Inicializar el mapa centrado en Buenos Aires
var map = L.map('map').setView([-34.6037, -58.3816], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 18,
}).addTo(map);

// Variables para marcadores, la línea entre origen y destino, y la selección de viandas
let markerOrigen, markerDestino, polyline, selectedViandas = [];
const modal = document.getElementById('modalViandas');

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

let totalViandasSeleccionadas;

// Mostrar el modal cuando se selecciona el Origen o Destino
function seleccionarPunto(marker, nombreHeladera, idHeladera, capacidadDisponible, viandasDepositadas) {
    if (!markerOrigen) {
        // Asignar heladera como origen
        markerOrigen = marker;
        document.getElementById('latitudeOrigen').value = marker.getLatLng().lat;
        document.getElementById('longitudeOrigen').value = marker.getLatLng().lng;
        document.getElementById('idHeladeraOrigen').value = idHeladera;

        // Filtrar viandas que pertenecen a la heladera seleccionada
        const viandasHeladera = viandas.filter(vianda => vianda.idHeladera === idHeladera);

        // Si esta vacia, es que no tiene viandas
        if (viandasHeladera.length === 0) {
            alert(`La heladera ${nombreHeladera} se encuentra actualmente vacia. Por favor seleccione una heladera que posea viandas.`)
            limpiarInputsHeladera();
            return;
        }

        // Llenar la lista de viandas en el modal
        const listaViandas = document.getElementById('listaViandas');
        listaViandas.innerHTML = ''; // Limpiar la lista anterior
        viandasHeladera.forEach(vianda => {
            const li = document.createElement('li');
            li.innerHTML = `
                <label class="modal-input-box">
                    <h3>${vianda.descripcion}</h3>
                    <div>
                        <strong>Caducidad: </strong>${vianda.fechaCaducidad}<br>
                        <strong>Peso: </strong>${vianda.pesoVianda}g
                    </div>
                    <input type="checkbox" value="${vianda.idVianda}" data-id="${vianda.idHeladera}">
                </label>
            `;
            listaViandas.appendChild(li);
        });

        // Mostrar modal de selección de viandas
        modal.classList.remove('hidden');
    } else if (!markerDestino) {
        if (markerOrigen.getLatLng().equals(marker.getLatLng())) {
            alert('El destino no puede ser el mismo que el origen. Selecciona otra heladera.');
            limpiarInputsHeladera();
            return;
        }

        // Verificar distancia máxima de 2.5 km
        const distancia = calcularDistancia(markerOrigen.getLatLng(), marker.getLatLng());
        if (distancia > 2.5) {
            alert('La distancia entre las heladeras es mayor a 2.5 km. Selecciona otra heladera.');
            limpiarInputsHeladera();
            return;
        }

        if (totalViandasSeleccionadas > capacidadDisponible) {
            alert(`La heladera de destino no tiene suficiente espacio. Puede recibir un máximo de ${capacidadDisponible} viandas.`);
            limpiarInputsHeladera();
            return;
        }

        markerDestino = marker;
        document.getElementById('latitudeDestino').value = marker.getLatLng().lat;
        document.getElementById('longitudeDestino').value = marker.getLatLng().lng;
        document.getElementById('idHeladeraDestino').value = idHeladera;

        if (polyline) {
            polyline.setLatLngs([markerOrigen.getLatLng(), markerDestino.getLatLng()]);
        } else {
            polyline = L.polyline([markerOrigen.getLatLng(), markerDestino.getLatLng()], { color: 'blue' }).addTo(map);
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const listaViandas = document.getElementById('listaViandas');
    const confirmarViandasBtn = document.getElementById('confirmarViandas');
    const cancelarViandasBtn = document.getElementById('cancelarViandas');

    // Confirmar selección de viandas
    document.getElementById('confirmarViandas').addEventListener('click', () => {
        const selectedViandas = [];
        const checkboxes = document.querySelectorAll('#listaViandas input[type="checkbox"]:checked');

        checkboxes.forEach(checkbox => {
            selectedViandas.push(checkbox.value); // Agrega el ID de la vianda seleccionada
        });

        totalViandasSeleccionadas = checkboxes.length

        // Crear un input oculto para enviar los IDs de las viandas
        const inputViandas = document.createElement('input');
        inputViandas.type = 'hidden';
        inputViandas.name = 'viandaIds'; // Nombre para recuperar en el back
        inputViandas.value = selectedViandas.join(',');

        // Agregar el input oculto al formulario
        const form = document.querySelector('.form__body');
        form.appendChild(inputViandas);

        // Cerrar el modal después de confirmar
        document.getElementById('modalViandas').classList.add('hidden');
    });


    cancelarViandasBtn.addEventListener('click', () => {
        if (markerOrigen) {
            limpiarInputsHeladera();
        }
        modal.classList.add('hidden');
    });
});

// Función para calcular la distancia entre dos puntos geográficos en kilómetros
function calcularDistancia(latlng1, latlng2) {
    const R = 6371;
    const dLat = (latlng2.lat - latlng1.lat) * Math.PI / 180;
    const dLon = (latlng2.lng - latlng1.lng) * Math.PI / 180;
    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(latlng1.lat * Math.PI / 180) * Math.cos(latlng2.lat * Math.PI / 180) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);
    return R * 2 * Math.asin(Math.sqrt(a));
}

document.getElementById('submitBtn').addEventListener('click', function(event) {
    const idHeladeraOrigen = document.getElementById('idHeladeraOrigen').value;
    const idHeladeraDestino = document.getElementById('idHeladeraDestino').value;

    if (!idHeladeraOrigen || !idHeladeraDestino) {
        alert('Por favor, selecciona una heladera de origen y destino.');
        event.preventDefault();
    }
});

function limpiarInputsHeladera() {
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
}

document.getElementById('clear').addEventListener('click', function() {
    limpiarInputsHeladera();
});

L.control.pinSearch({
    placeholder: 'Buscar heladera...',
    buttonText: 'Buscar',
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
