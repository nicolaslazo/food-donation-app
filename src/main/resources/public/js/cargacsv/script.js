document.addEventListener('DOMContentLoaded', function () {
    const fileInput = document.getElementById('file-upload');
    const fileNameDisplay = document.getElementById('file-name');

    // Mostrar el nombre del archivo seleccionado
    fileInput.addEventListener('change', function () {
        if (fileInput.files.length > 0) {
            fileNameDisplay.textContent = fileInput.files[0].name;
        } else {
            fileNameDisplay.textContent = "No se ha seleccionado archivo";
        }
    });
});