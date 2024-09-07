document.addEventListener('DOMContentLoaded', function() {
const fileInput = document.getElementById('file-upload');
const fileNameDisplay = document.getElementById('file-name');
const uploadForm = document.getElementById('upload-form');
const successMessage = document.getElementById('success-message');

// Mostrar el nombre del archivo seleccionado
fileInput.addEventListener('change', function () {
    if (fileInput.files.length > 0) {
        fileNameDisplay.textContent = fileInput.files[0].name;
    } else {
        fileNameDisplay.textContent = "No se ha seleccionado archivo";
    }
});

// Mostrar el mensaje de éxito después de la carga del archivo
uploadForm.addEventListener('submit', function (e) {
    e.preventDefault(); // Prevenir el envío real del formulario para la demostración
    // Simular un proceso de carga de archivo
    setTimeout(() => {
        successMessage.style.display = 'block';
    }, 500);
});
});