// Inicializar el mapa centrado en Buenos Aires
var map = L.map('map').setView([-34.6037, -58.3816], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 18,
}).addTo(map);

// Añadir los marcadores de heladeras desde la base de datos
heladeras.forEach(function (heladera) {
    var marker = L.marker([heladera.lat, heladera.long], {title: heladera.nombre, id: heladera.idHeladera})
        .bindPopup(heladera.nombre)
        .addTo(map);

    // Asignar evento al hacer clic en el marcador
    marker.on('click', function (e) {
        document.getElementById('latitude').value = heladera.lat;
        document.getElementById('longitude').value = heladera.long;
        document.getElementById('idHeladera').value = heladera.idHeladera;
    });
});

// Integración de Leaflet.PinSearch para buscar heladeras
L.control.pinSearch({
    placeholder: 'Buscar heladera...',
    onSearch: function(query) {
        var resultado = heladeras.find(heladera => heladera.nombre.toLowerCase().includes(query.toLowerCase()));
        if (resultado) {
            var latlng = L.latLng(resultado.lat, resultado.long);
            map.setView(latlng, 15);  // Centramos el mapa en la heladera encontrada
            var marker = L.marker(latlng, {title: resultado.nombre, id: resultado.idHeladera}).addTo(map);
            marker.bindPopup(resultado.nombre).openPopup();
            document.getElementById('latitude').value = resultado.lat;
            document.getElementById('longitude').value = resultado.long;
            document.getElementById('idHeladera').value = resultado.idHeladera;
        } else {
            alert('No se encontró la heladera');
        }
    },
    focusOnMarker: false,
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



