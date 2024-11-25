document.addEventListener('DOMContentLoaded', function () {
    const tabs = document.querySelectorAll('.tab-button');
    const contenidos = document.querySelectorAll('.tab-content');
    const form = document.getElementById('registro-form');
    const camposDireccionObligatorios = ['pais', 'provincia', 'ciudad', 'codigoPostal', 'calle', 'altura'];

    function togglearCamposRequeridos(tabContent, requerido) {
        const inputs = tabContent.querySelectorAll('input, select');
        inputs.forEach(input => {
            if (requerido) {
                input.setAttribute('required', '');
            } else {
                input.removeAttribute('required');
            }
        });
    }

    function validarCamposDireccion() {
        const algunCampoDireccionLleno = camposDireccionObligatorios.some(campo =>
            document.getElementById(campo).value.trim() !== ''
        );

        camposDireccionObligatorios.forEach(campo => {
            const elemento = document.getElementById(campo);
            if (algunCampoDireccionLleno) {
                elemento.setAttribute('required', '');
            } else {
                elemento.removeAttribute('required');
            }
        });
    }

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const target = tab.dataset.tab;

            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            contenidos.forEach(content => {
                if (content.id === target + '-content') {
                    content.style.display = 'block';
                    togglearCamposRequeridos(content, true);
                } else {
                    content.style.display = 'none';
                    togglearCamposRequeridos(content, false);
                }
            });
        });
    });

    // Mostrar la primera pestaña por defecto y hacer sus campos requeridos
    tabs[0].click();

    // Agregar event listeners a los campos de dirección
    camposDireccionObligatorios.forEach(campo => {
        document.getElementById(campo).addEventListener('input', validarCamposDireccion);
    });

    // Validar el formulario antes de enviarlo
    form.addEventListener('submit', function (event) {
        const tabActiva = document.querySelector('.tab-button.active');
        const contenidoActivo = document.getElementById(tabActiva.dataset.tab + '-content');

        const inputsActivos = contenidoActivo.querySelectorAll('input, select');
        let formValido = true;

        inputsActivos.forEach(input => {
            if (input.hasAttribute('required') && !input.value.trim()) {
                formValido = false;
                input.classList.add('error');
            } else {
                input.classList.remove('error');
            }
        });

        // Validar campos de dirección
        validarCamposDireccion();

        if (!formValido) {
            event.preventDefault();
            alert('Por favor, complete todos los campos requeridos.');
        }
    });
});
