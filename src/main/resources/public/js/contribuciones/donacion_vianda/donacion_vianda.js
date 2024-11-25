// Inicializar el mapa centrado en Buenos Aires
var map = L.map('map').setView([-34.6037, -58.3816], 13);

L.tileLayer('https://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 18,
}).addTo(map);

// Añadir los marcadores de heladeras desde la base de datos
heladeras.forEach(function (heladera) {
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
            return; // Si no hay suficiente capacidad, salir de la función y no seleccionar la heladera
        }

        // Si la capacidad es suficiente, asignar los valores al formulario
        document.getElementById('latitude').value = heladera.lat;
        document.getElementById('longitude').value = heladera.long;
        document.getElementById('idHeladera').value = heladera.idHeladera;
    });
});

L.control.pinSearch({
    placeholder: 'Buscar heladera...',
    onSearch: function (query) {
        // Verificar que el input no esté vacío antes de realizar la búsqueda
        if (query.trim() === "") {
            return; // Si el input está vacío, no hacer nada
        }

        var resultado = heladeras.find(heladera => heladera.nombre.toLowerCase().includes(query.toLowerCase()));
        if (resultado) {
            var latlng = L.latLng(resultado.lat, resultado.long);
            map.setView(latlng, 15);  // Centramos el mapa en la heladera encontrada

            // Crear un contenido de popup personalizado con el nombre y la capacidad disponible
            var popupContent = `
                <strong>${resultado.nombre}</strong><br>
                Capacidad Disponible: ${resultado.capacidadDisponible} viandas
            `;

            var marker = L.marker(latlng, {title: resultado.nombre, idHeladera: resultado.idHeladera}).addTo(map);
            marker.bindPopup(popupContent).openPopup();
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

// Esperar al envío del formulario
document.getElementById('formulario-viandas').addEventListener('submit', function (event) {
    // Obtener los valores de los inputs de heladera y cantidad de viandas
    const latitude = document.getElementById('latitude').value;
    const longitude = document.getElementById('longitude').value;
    const idHeladera = document.getElementById('idHeladera').value;
    const cantidadViandas = parseInt(document.getElementById('cantidad').value, 10);
    const form = document.getElementById('formulario-viandas');

    // Verificar si una heladera fue seleccionada
    if (!latitude || !longitude || !idHeladera) {
        event.preventDefault(); // Detener el envío del formulario
        alert('Debes seleccionar una heladera en el mapa antes de enviar el formulario.');
        return;
    }

    // Buscar la heladera seleccionada por su ID
    const heladeraSeleccionada = heladeras.find(h => h.idHeladera === idHeladera);

    // Verificar la capacidad de la heladera seleccionada
    if (!verificarCapacidadHeladera(cantidadViandas, heladeraSeleccionada)) {
        event.preventDefault(); // Detener el envío del formulario si la capacidad es insuficiente
        return;
    }
    // Aquí se permite el envío automático ya que todas las verificaciones pasaron
    form.addEventListener('submit', function (e) {
        e.preventDefault(); // Evita el envío inmediato del formulario

        // Verifica que el formulario sea válido (puedes añadir validaciones adicionales si es necesario)
        if (!form.checkValidity()) {
            alert('Por favor, revisa los campos y asegúrate de que todo esté completo.');
        }
    });
});

document.addEventListener('DOMContentLoaded', function () {
    updateViandas(); // Llamamos a la función para generar las viandas
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

        // Crear el ícono
        const icon = document.createElement('i');
        icon.classList.add('fas', 'fa-chevron-down'); // Icono de flecha hacia abajo inicialmente

        // Texto del título
        const titleText = document.createElement('span');
        titleText.innerText = `Datos Vianda ${i}`;

        title.appendChild(icon);
        title.appendChild(titleText);
        title.onclick = () => toggleSection(viandaDiv, icon); // Cambiamos para manejar el ícono

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
                <input name="fechaCaducidad_${i}" id="fechaCaducidad_${i}" type="date" required>
                <small class="error-message" id="errorFechaCaducidad_${i}" style="color: red; display: none;">La fecha debe ser al menos 30 días después del día de hoy.</small>
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

        // Inicialmente ocultamos el contenido
        inputsDiv.style.display = 'none';

        viandaDiv.appendChild(title);
        viandaDiv.appendChild(inputsDiv);
        viandasContainer.appendChild(viandaDiv);

        // Añadimos validación a la fecha de caducidad
        const fechaInput = document.getElementById(`fechaCaducidad_${i}`);
        fechaInput.addEventListener('input', function () {
            validarFechaCaducidad(fechaInput, i);
        });
    }
}

function toggleSection(section, icon, initialize = false) {
    const inputs = section.querySelector('.vianda-inputs');
    const isVisible = inputs.style.display === 'block';

    if (!initialize) {
        // Mostrar o esconder la sección solo si no es la inicialización
        inputs.style.display = isVisible ? 'none' : 'block';
    } else {
        // Durante la inicialización, aseguramos que esté oculto
        inputs.style.display = 'none';
    }

    // Cambiar el ícono según el estado (y mantenerlo consistente en la inicialización)
    icon.classList.toggle('fa-chevron-down', isVisible);
    icon.classList.toggle('fa-chevron-up', !isVisible); // Cambia a flecha hacia arriba si está expandido
}

// Registro la fecha en el que se completa el formulario, en el back seria la fecha de donación
document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector('.form__body');
    const fechaHoraInput = document.getElementById('fechaHora');

    form.addEventListener('submit', function () {
        const now = new Date();
        fechaHoraInput.value = now.toISOString();
    });
});

// Función para validar la fecha de caducidad
function validarFechaCaducidad(fechaInput, index) {
    const fechaSeleccionada = new Date(fechaInput.value);
    const fechaActual = new Date();

    // Ajustamos el tiempo de la fecha actual para que sea solo la fecha sin la hora
    fechaActual.setHours(0, 0, 0, 0);

    // Calculamos la fecha mínima permitida (30 días desde hoy)
    const fechaMinima = new Date();
    fechaMinima.setDate(fechaActual.getDate() + 30);

    // Seleccionamos el mensaje de error correspondiente
    const errorMessage = document.getElementById(`errorFechaCaducidad_${index}`);

    if (fechaSeleccionada < fechaMinima) {
        // Mostrar el mensaje de error si la fecha es inválida
        errorMessage.style.display = 'block';
        fechaInput.setCustomValidity('La fecha de caducidad debe ser al menos 30 días después de hoy.');
    } else {
        // Ocultar el mensaje de error si la fecha es válida
        errorMessage.style.display = 'none';
        fechaInput.setCustomValidity(''); // Vuelve a validar el campo como correcto
    }
}

// Función para verificar la capacidad de la heladera seleccionada
function verificarCapacidadHeladera(cantidadViandas, heladera) {
    if (heladera.capacidadDisponible === 1 && (cantidadViandas > heladera.capacidadDisponible)) {
        alert(`La heladera seleccionada solo tiene capacidad para una vianda. Por favor, elige una cantidad adecuada.`);
        return false;
    } else if (heladera.capacidadDisponible === 0) {
        alert(`La heladera seleccionada se encuentra llena en estos momentos. Por favor, elige otra heladera o espera que se vacie.`);
        return false;
    } else if (cantidadViandas > heladera.capacidadDisponible) {
        alert(`La heladera seleccionada solo tiene capacidad para ${heladera.capacidadDisponible} viandas. Por favor, elige una cantidad adecuada.`);
        return false;
    }
    return true;
}
