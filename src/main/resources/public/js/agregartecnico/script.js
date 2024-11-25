
// Manejo del formulario
document.querySelector('.form-container').addEventListener('submit', async function (event) {
    event.preventDefault(); // Prevenir el envío por defecto

    const form = event.target;
    let formIsValid = true;

    // Obtener la fecha de nacimiento
    const dob = document.getElementById('dob').value;
    const dobError = document.getElementById('dob-error');
    const currentDate = new Date();
    const birthDate = new Date(dob);

    const age = currentDate.getFullYear() - birthDate.getFullYear();
    const isBirthdayPassed =
        currentDate.getMonth() > birthDate.getMonth() ||
        (currentDate.getMonth() === birthDate.getMonth() && currentDate.getDate() >= birthDate.getDate());

    if (age < 18 || (age === 18 && !isBirthdayPassed)) {
        dobError.textContent = 'Debes tener al menos 18 años para registrarte.';
        dobError.style.display = 'block'; // Mostrar mensaje de error
        formIsValid = false;
    } else {
        dobError.style.display = 'none'; // Ocultar mensaje de error
    }

    // Validar formato de DNI
    const documento = document.getElementById('numero-documento').value;
    const documentoError = document.getElementById('documento-error');
    const dniRegex = /^[\d]{1,3}\.?[\d]{3}\.?[\d]{3}$/;

    if (!dniRegex.test(documento)) {
        documentoError.textContent = 'El número de documento no es válido. Debe seguir el formato argentino.';
        documentoError.style.display = 'block'; // Mostrar error
        formIsValid = false;
    } else {
        documentoError.style.display = 'none'; // Ocultar error
    }

    // Validar CUIT
    const cuil = document.getElementById('cuil').value;
    const cuilError = document.getElementById('cuil-error');
    const cuitRegex = /^([0-9]{11}|[0-9]{2}-[0-9]{8}-[0-9]{1})$/;

    if (!cuitRegex.test(cuil)) {
        cuilError.textContent = 'El CUIT no es válido. Debe seguir el formato correcto.';
        cuilError.style.display = 'block'; // Mostrar error
        formIsValid = false;
    } else {
        cuilError.style.display = 'none'; // Ocultar error
    }

    if (!formIsValid) {
        return; // No enviar el formulario si hay errores
    }

    // Si todo está correcto, enviar el formulario
    try {
        const formData = new FormData(form);
        const response = await fetch(form.action, {
            method: 'POST',
            body: formData,
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            if (errorMessage.includes("El correo ya está registrado.")) {
                mostrarModalError("El correo ingresado ya está registrado.");
            }
        } else {
            window.location.href = "/quiero-ayudar"; // Redirigir si todo está correcto
        }
    } catch (error) {
        console.error("Error al enviar el formulario:", error);
    }
});

function mostrarModalError(mensaje) {
    const modal = document.getElementById('errorModal');
    const modalMessage = document.getElementById('modalMessage');
    modalMessage.textContent = mensaje;
    modal.style.display = 'block';
}

document.getElementById('closeModal').addEventListener('click', function () {
    const modal = document.getElementById('errorModal');
    modal.style.display = 'none';
});