document.addEventListener('DOMContentLoaded', function() {
    const fileInput = document.querySelector('input[name="imagen"]');
    const form = document.querySelector('form');
    const submitButton = document.querySelector('.submit-button input[type="submit"]');

    fileInput.addEventListener('change', function(event) {
        const file = event.target.files[0];
        if (file) {
            const fileType = file.type;
            if (fileType !== 'image/jpeg') {
                alert('Por favor, sube una imagen en formato JPG.');
                fileInput.value = ''; // Limpiar el input
                submitButton.disabled = true; // Deshabilitar botón de enviar
            } else {
                alert('Imagen aceptada.');
                submitButton.disabled = false; // Habilitar botón de enviar
            }
        }
    });

    form.addEventListener('submit', function(event) {
        const file = fileInput.files[0];
        if (!file || file.type !== 'image/jpeg') {
            event.preventDefault();
            alert('Por favor, asegúrate de subir una imagen en formato JPG antes de enviar.');
        }
    });
});
