var map = L.map('map').setView([-34.6037, -58.3816], 13); // Coordenadas iniciales centradas en Buenos Aires

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

// Definir la variable marker fuera del evento
var marker;

// Variable para almacenar el marcador seleccionado
var selectedMarker = null;

// Evento al hacer clic en el mapa
map.on('click', function(e) {
    var lat = e.latlng.lat;
    var lng = e.latlng.lng;

    // Obtener el nombre de la heladera desde el input
    var heladeraName = document.getElementById('heladera-name').value || 'Nueva heladera';

    // Si ya hay un marcador seleccionado, actualiza su posición
    if (selectedMarker) {
        selectedMarker.setLatLng(e.latlng);
        selectedMarker.bindPopup(heladeraName).openPopup();

        // Actualizar los inputs de latitud y longitud
        document.getElementById('latitude').value = lat;
        document.getElementById('longitude').value = lng;

        // Deseleccionar el marcador después de moverlo
        selectedMarker = null;
    } else {
        // Crear un nuevo marcador si no hay uno seleccionado
        var marker = L.marker(e.latlng, {
            draggable: true // Permitir arrastrar el marcador
        }).addTo(map);

        marker.bindPopup(heladeraName).openPopup();

        // Actualizar los inputs de latitud y longitud
        document.getElementById('latitude').value = lat;
        document.getElementById('longitude').value = lng;

        // Evento al hacer clic en el marcador
        marker.on('click', function() {
            // Seleccionar este marcador para moverlo
            selectedMarker = marker;

            // Actualizar los inputs con la posición actual del marcador
            var position = marker.getLatLng();
            document.getElementById('latitude').value = position.lat;
            document.getElementById('longitude').value = position.lng;
        });

        // Evento al arrastrar el marcador para actualizar las coordenadas
        marker.on('dragend', function(e) {
            var newLatLng = e.target.getLatLng();
            document.getElementById('latitude').value = newLatLng.lat;
            document.getElementById('longitude').value = newLatLng.lng;
        });
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

document.addEventListener('DOMContentLoaded', function () {
    updateViandas();
});

function updateViandas() {
    const cantidad = document.getElementById('cantidad').value;
    const viandasContainer = document.getElementById('viandas-container');
    viandasContainer.innerHTML = ''; // Limpiamos el contenido previo

    for (let i = 1; i <= cantidad; i++) {
        const viandaDiv = document.createElement('div');
        viandaDiv.classList.add('vianda-section');

        // Crear un título desplegable para cada vianda
        const title = document.createElement('button');
        title.setAttribute('type', 'button');
        title.classList.add('vianda-title');
        title.innerText = `Datos Vianda ${i}`;
        title.onclick = () => toggleSection(viandaDiv);

        // Contenedor de inputs
        const inputsDiv = document.createElement('div');
        inputsDiv.classList.add('vianda-inputs');
        inputsDiv.innerHTML = `
            <label class="input-box">
                Descripción de la Vianda *
                <textarea name="descripcionVianda_${i}" maxlength="255" required></textarea>
            </label>
            <label class="input-box">
                Fecha de Caducidad *
                <input name="fechaCaducidad_${i}" type="date" required>
            </label>
            <label class="input-box">
                Peso de la Vianda
                <input name="pesoVianda_${i}" type="number" min="1" placeholder="En Gramos">
            </label>
            <label class="input-box">
                Calorías
                <input name="calorias_${i}" type="number" min="1" placeholder="En Calorías">
            </label>
        `;

        viandaDiv.appendChild(title);
        viandaDiv.appendChild(inputsDiv);
        viandasContainer.appendChild(viandaDiv);
    }
}

function toggleSection(section) {
    const inputs = section.querySelector('.vianda-inputs');
    inputs.style.display = inputs.style.display === 'none' ? 'block' : 'none';
}



