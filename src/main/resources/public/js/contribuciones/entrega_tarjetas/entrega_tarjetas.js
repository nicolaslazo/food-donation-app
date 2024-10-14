document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('.form__body');
    const direccionContainer = document.getElementById('direccion-container');
    const botonAgregarDireccion = document.getElementById('agregar-direccion');
    const camposDireccionObligatorios = ['pais', 'provincia', 'ciudad', 'codigoPostal', 'calle', 'altura'];

    // Mostrar/Ocultar los campos de dirección cuando se haga clic en el botón
    botonAgregarDireccion.addEventListener('click', function () {
        if (direccionContainer.style.display === 'none') {
            direccionContainer.style.display = 'block';
            botonAgregarDireccion.textContent = 'Ocultar Dirección de Entrega';
        } else {
            direccionContainer.style.display = 'none';
            botonAgregarDireccion.textContent = 'Agregar Dirección de Entrega';
            // Limpiar los campos y remover los requeridos si se oculta
            camposDireccionObligatorios.forEach(campo => {
                const elemento = document.getElementById(campo);
                elemento.value = ''; // Limpiar los valores
                elemento.removeAttribute('required'); // Quitar el atributo 'required'
            });
        }
    });

    // Función para hacer que los campos de dirección sean requeridos si se completa uno de ellos
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

    // Agregar event listeners a los campos de dirección
    camposDireccionObligatorios.forEach(campo => {
        document.getElementById(campo).addEventListener('input', validarCamposDireccion);
    });

    // Validar el formulario antes de enviarlo
    form.addEventListener('submit', function (event) {
        let formValido = true;

        // Validar campos de dirección
        validarCamposDireccion();

        // Validar que los campos obligatorios estén completos
        const inputsRequeridos = form.querySelectorAll('input[required]');
        inputsRequeridos.forEach(input => {
            if (!input.value.trim()) {
                formValido = false;
                input.classList.add('error');  // Agrega clase 'error' si el campo está vacío
            } else {
                input.classList.remove('error');
            }
        });

        if (!formValido) {
            event.preventDefault();
            alert('Por favor, complete todos los campos requeridos.');
        } else {
            alert('Solicitud enviada exitosamente');
        }
    });
});
