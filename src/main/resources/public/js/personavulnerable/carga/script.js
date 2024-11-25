document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('persona-vulnerable-form');
    const tieneDomicilioCheckbox = document.getElementById('tiene-domicilio');
    const seccionDireccion = document.getElementById('direccion');
    const camposDireccionObligatorios = ['pais', 'provincia', 'ciudad', 'calle', 'altura'];
    const tieneDocumentoCheckbox = document.getElementById('tiene-dni');
    const seccionDocumento = document.getElementById('posee-documento');
    const menoresCargoInput = document.getElementById('menores-cargo');
    const tarjetaInput = document.getElementById('tarjeta');

    function mostrarSeccion(section, mostrar) {
        if (mostrar) {
            section.style.display = 'block';
        } else {
            section.style.display = 'none';
        }
    }

    tieneDomicilioCheckbox.addEventListener('change', function () {
        mostrarSeccion(seccionDireccion, this.checked)
        camposDireccionObligatorios.forEach(campo => {
            const elemento = document.getElementById(campo);
            if (this.checked) {
                elemento.setAttribute('required', '');
            } else {
                elemento.removeAttribute('required');
            }
        });
    });

    tieneDocumentoCheckbox.addEventListener('change', function () {
        mostrarSeccion(seccionDocumento, this.checked)

        const tipoDocumento = document.getElementById('documento');
        const numeroDocumento = document.getElementById('numero-documento');
        if (this.checked) {
            tipoDocumento.setAttribute('required', '');
            numeroDocumento.setAttribute('required', '');
        } else {
            tipoDocumento.removeAttribute('required');
            numeroDocumento.removeAttribute('required');
        }
    });

    menoresCargoInput.addEventListener('input', function () {
        if (this.value < 0) {
            alert('La cantidad de menores a cargo no puede ser negativa.');
            this.value = ''; // Resetea el valor
        }
    });

    tarjetaInput.addEventListener('blur', function () {
        const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;
        if (this.value && !uuidRegex.test(this.value)) {
            alert('El código de la tarjeta debe ser un UUID válido.');
            this.value = ''; // Resetea el valor
        }
    });

    form.addEventListener('submit', function (event) {
        event.preventDefault();
        let formValido = true;

        // Validar campos requeridos
        form.querySelectorAll('input[required], select[required]').forEach(input => {
            if (!input.value.trim()) formValido = false;
        });

        // Validar campos de dirección si el checkbox está marcado
        if (tieneDomicilioCheckbox.checked) {
            camposDireccionObligatorios.forEach(campo => {
                const elemento = document.getElementById(campo);
                if (!elemento.value.trim()) formValido = false;
            });
        }

        // Validar campos de documento si el checkbox está marcado
        if (tieneDocumentoCheckbox.checked) {
            const tipoDocumento = document.getElementById('documento');
            const numeroDocumento = document.getElementById('numero-documento');
            if (!tipoDocumento.value || !numeroDocumento.value.trim()) formValido = false;
        }

        if (!formValido) {
            alert('Por favor, complete todos los campos requeridos.');
        }
    });
});
