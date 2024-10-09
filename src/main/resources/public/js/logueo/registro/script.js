document.addEventListener('DOMContentLoaded', function () {
    const tabs = document.querySelectorAll('.tab-button');
    const contenidos = document.querySelectorAll('.tab-content');
    const form = document.getElementById('registro-form');

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

    // Validar el formulario antes de enviarlo
    form.addEventListener('submit', function (event) {
        const tabActiva = document.querySelector('.tab-button.active');
        const contenidoActivo = document.getElementById(tabActiva.dataset.tab + '-content');

        const inputsActivos = contenidoActivo.querySelectorAll('input, select');
        let formValido = true;

        inputsActivos.forEach(input => {
            if (input.hasAttribute('required') && !input.value) {
                formValido = false;
                input.classList.add('error');
            } else {
                input.classList.remove('error');
            }
        });

        if (!formValido) {
            event.preventDefault();
            alert('Por favor, complete todos los campos requeridos.');
        }

        alert('Colaborador creado exitosamente');
    });
});
