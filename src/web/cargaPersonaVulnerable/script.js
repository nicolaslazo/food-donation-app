document.addEventListener('DOMContentLoaded', function() {
    // Validar número de menores a cargo
    const menoresCargoInput = document.getElementById('menores-cargo');
    menoresCargoInput.addEventListener('input', function() {
        if (menoresCargoInput.value < 0) {
            alert('La cantidad de menores a cargo no puede ser negativa.');
            menoresCargoInput.value = ''; // Resetea el valor
        }
    });

    // Validar que el ID de la tarjeta sea un UUID cuando el campo pierde el foco
    const tarjetaInput = document.getElementById('tarjeta');
    tarjetaInput.addEventListener('blur', function() {
        const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;
        if (tarjetaInput.value && !uuidRegex.test(tarjetaInput.value)) {
            alert('El código de la tarjeta debe ser un UUID válido.');
            tarjetaInput.value = ''; // Resetea el valor
        }
    });

    const addressSection = document.getElementById('direccion');
    const tieneDomicilioCheckbox = document.getElementById('tiene-domicilio');

    tieneDomicilioCheckbox.addEventListener('change', function() {
        if (this.checked) {
            addressSection.classList.add('show');
            addressSection.classList.remove('hidden');
        } else {
            addressSection.classList.add('hidden');
            addressSection.classList.remove('show');
        }
    });

    const documentSection = document.getElementById('posee-documento');
    const tieneDocumentoCheckbox = document.getElementById('tiene-dni');

    tieneDocumentoCheckbox.addEventListener('change', function() {
        if (this.checked) {
            documentSection.classList.add('show');
            documentSection.classList.remove('hidden');
        } else {
            documentSection.classList.add('hidden');
            documentSection.classList.remove('show');
        }
    });

});
