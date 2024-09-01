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

    const dniSiRadio = document.getElementById('tiene-dni-si');
    const dniNoRadio = document.getElementById('tiene-dni-no');
    const dniSection = document.getElementById('posee-documento');

    const domicilioSiRadio = document.getElementById('tiene-domicilio-si');
    const domicilioNoRadio = document.getElementById('tiene-domicilio-no');
    const addressSection = document.getElementById('direccion'); // El div que contiene los campos de la dirección

    // Manejar la visibilidad de los campos de Domicilio cuando se selecciona una opción
    domicilioSiRadio.addEventListener('change', function() {
        if (this.checked) {
            addressSection.classList.add('show'); // Muestra el div
            addressSection.classList.remove('hidden'); // Asegura que la clase 'hidden' no esté presente
        }
    });

    domicilioNoRadio.addEventListener('change', function() {
        if (this.checked) {
            addressSection.classList.add('hidden'); // Oculta el div
            addressSection.classList.remove('show'); // Asegura que la clase 'show' no esté presente
        }
    });

 // Manejar la visibilidad de los campos de DNI cuando se selecciona una opción
    dniSiRadio.addEventListener('change', function() {
        if (this.checked) {
            dniSection.classList.add('show'); // Muestra el div
            dniSection.classList.remove('hidden'); // Asegura que la clase 'hidden' no esté presente
        }
    });

    dniNoRadio.addEventListener('change', function() {
        if (this.checked) {
            dniSection.classList.add('hidden'); // Oculta el div
            dniSection.classList.remove('show'); // Asegura que la clase 'show' no esté presente
        }
    });

});
