document.addEventListener('DOMContentLoaded', function() {

    // Validar número de menores a cargo
    const menoresCargoInput = document.getElementById('menores-cargo');
    menoresCargoInput.addEventListener('input', function() {
        if (menoresCargoInput.value < 0) {
            alert('La cantidad de menores a cargo no puede ser negativa.');
            menoresCargoInput.value = ''; // Resetea el valor
        }
    });

    // Validar que el ID de la tarjeta sea un UUID
    const tarjetaInput = document.getElementById('tarjeta');
    tarjetaInput.addEventListener('input', function() {
        const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;
        if (!uuidRegex.test(tarjetaInput.value)) {
            alert('El código de la tarjeta debe ser un UUID válido.');
            tarjetaInput.value = ''; // Resetea el valor
        }
    });

    const addressSection = document.getElementById('direccion'); // El div que contiene los campos de la dirección
    const tieneDomicilioCheckbox = document.getElementById('tiene-domicilio'); // Nueva checkbox

    // Manejar la visibilidad de los campos de Domicilio cuando se selecciona la nueva checkbox
    tieneDomicilioCheckbox.addEventListener('change', function() {
        if (this.checked) {
            addressSection.classList.add('show'); // Muestra el div de la dirección
            addressSection.classList.remove('hidden'); // Asegura que la clase 'hidden' no esté presente
        } else {
            addressSection.classList.add('hidden'); // Oculta el div de la dirección
            addressSection.classList.remove('show'); // Asegura que la clase 'show' no esté presente
        }
    });


    const documentSection = document.getElementById('posee-documento'); // El div que contiene los campos de la dirección
    const tieneDocumentoCheckbox = document.getElementById('tiene-dni'); // Nueva checkbox

    // Manejar la visibilidad de los campos de Domicilio cuando se selecciona la nueva checkbox
    tieneDocumentoCheckbox.addEventListener('change', function() {
        if (this.checked) {
            documentSection.classList.add('show'); // Muestra el div de la dirección
            documentSection.classList.remove('hidden'); // Asegura que la clase 'hidden' no esté presente
        } else {
            documentSection.classList.add('hidden'); // Oculta el div de la dirección
            documentSection.classList.remove('show'); // Asegura que la clase 'show' no esté presente
        }
    });

});

