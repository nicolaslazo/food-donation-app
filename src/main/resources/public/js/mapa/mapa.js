// Inicializar el mapa centrado en Buenos Aires
var map = L.map('map').setView([-34.6037, -58.3816], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 18,
}).addTo(map);

// Variables para marcadores, la línea entre origen y destino, y la selección de viandas
let markerOrigen;
const modal = document.getElementById('modalViandas');

// Variable para detectar el rol del usuario
const userRol = document.getElementById('userRol');

// Añadir marcadores de heladeras con datos de capacidad
heladeras.forEach(function(heladera) {
    const marker = L.marker([heladera.lat, heladera.long], { title: heladera.nombre })
        .bindTooltip(
            `<strong>${heladera.nombre}</strong><br>
             Viandas Disponibles: ${heladera.viandasDepositadas}`,
            { permanent: false, direction: 'top', opacity: 0.8 }
        )
        .addTo(map);

    // Evento para seleccionar la heladera al hacer clic
    marker.on('click', function() {
        seleccionarPunto(marker, heladera.nombre, heladera.idHeladera, heladera.viandasDepositadas);
    });
});

let totalViandasSeleccionadas = 0;

// Mostrar el modal cuando se selecciona el Origen o Destino
function seleccionarPunto(marker, nombreHeladera, idHeladera, viandasDepositadas) {
    // Asignar heladera como origen
    markerOrigen = marker;
    document.getElementById('idHeladera').value = idHeladera;

    // Filtrar viandas que pertenecen a la heladera seleccionada
    const viandasHeladera = viandas.filter(vianda => vianda.idHeladera === idHeladera);
    
    // Llenar la lista de viandas en el modal
    const listaViandas = document.getElementById('listaViandas');
    listaViandas.innerHTML = ''; // Limpiar la lista anterior
    viandasHeladera.forEach(vianda => {
        const li = document.createElement('li');
        if (userRol.value === "PERSONAVULNERABLE") {
            li.innerHTML = `
            <label class="modal-input-box">
                <h3>${vianda.descripcion}</h3>
                <div>
                    <strong>Caducidad: </strong>${vianda.fechaCaducidad}<br>
                    <strong>Peso: </strong>${vianda.pesoVianda}g
                </div>
                <input name="viandasIds" type="checkbox" value="${vianda.idVianda}" data-id="${vianda.idHeladera}">
            </label>
        `;
        } else {
            li.innerHTML = `
            <label class="modal-input-box">
                <h3>${vianda.descripcion}</h3>
                <div>
                    <strong>Caducidad: </strong>${vianda.fechaCaducidad}<br>
                    <strong>Peso: </strong>${vianda.pesoVianda}g
                </div>
            </label>
        `;
        }
        listaViandas.appendChild(li);
    });

    // Mostrar modal de selección de viandas
    modal.classList.remove('hidden');
}

document.addEventListener('DOMContentLoaded', () => {
    const usosDisponibles = parseInt(document.getElementById('usosDisponibles').value);
    let totalViandasSeleccionadas = 0;
    const listaViandas = document.getElementById('listaViandas');
    const confirmarViandasBtn = document.getElementById('confirmarViandas');
    const cancelarViandasBtn = document.getElementById('cancelarViandas');

    // Lógica para mostrar la ventana modal en función del rol del usuario
    if (userRol.value === "PERSONAVULNERABLE") {
        confirmarViandasBtn.addEventListener('click', () => {
            const selectedViandas = [];
            const checkboxes = document.querySelectorAll('#listaViandas input[type="checkbox"]:checked');

            checkboxes.forEach(checkbox => {
                selectedViandas.push(checkbox.value);
                totalViandasSeleccionadas += 1;
            });

            if (totalViandasSeleccionadas > usosDisponibles) {
                alert('No puedes retirar más viandas de las disponibles en tu tarjeta.');
                totalViandasSeleccionadas -= selectedViandas.length; // Reset total viandas seleccionadas
                return; // No se realiza la confirmación
            }

            // Crear un input oculto para enviar los IDs de las viandas
            const inputViandas = document.createElement('input');
            inputViandas.type = 'hidden';
            inputViandas.name = 'viandasIds'; // Nombre para recuperar en el back
            inputViandas.value = selectedViandas.join(','); // Cadena separada por comas

            // Agregar el input oculto al formulario
            const form = document.querySelector('.form__body');
            form.appendChild(inputViandas);

            console.log("Viandas Seleccionadas: " + inputViandas.value);

            // Cerrar el modal después de confirmar
            modal.classList.add('hidden');
        });

        cancelarViandasBtn.addEventListener('click', () => {
            limpiarInputsHeladera();
            modal.classList.add('hidden');
        });
    } else {
        // Lógica para otros roles que solo pueden ver las viandas
        confirmarViandasBtn.style.display = 'none'; // Oculta el botón de confirmar

        cancelarViandasBtn.addEventListener('click', () => {
            modal.classList.add('hidden');
        });
    }
});


document.getElementById('submitBtn').addEventListener('click', function(event) {
    const idHeladera = document.getElementById('idHeladera').value;

    if (!idHeladera) {
        alert('Por favor, selecciona una heladera donde retirar una o más viandas.');
        event.preventDefault();
    }
});

function limpiarInputsHeladera() {
    if (markerOrigen) {
        totalViandasSeleccionadas = 0;
        markerOrigen.closePopup();
        markerOrigen = null;
        document.getElementById('idHeladera').value = '';
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