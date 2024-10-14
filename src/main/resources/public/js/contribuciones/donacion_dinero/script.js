document.addEventListener('DOMContentLoaded', function() {
    const donacionPeriodicaSelect = document.getElementById('donacion-periodica');
    const frecuenciaSelect = document.getElementById('frecuencia');
    const frecuenciaContainer = document.getElementById('frecuencia-container');

    function toggleFrecuencia() {
        if (donacionPeriodicaSelect.value === 'true') {
            frecuenciaContainer.style.display = 'block';
            frecuenciaSelect.setAttribute('required', '');
        } else {
            frecuenciaContainer.style.display = 'none';
            frecuenciaSelect.removeAttribute('required');
            frecuenciaSelect.value = ''; // Reset the value when hidden
        }
    }

    // Setup inicial
    toggleFrecuencia();

    // Listeneamos cambios
    donacionPeriodicaSelect.addEventListener('change', toggleFrecuencia);

    // Validación del form
    document.querySelector('form').addEventListener('submit', function(event) {
        if (donacionPeriodicaSelect.value === 'true' && !frecuenciaSelect.value) {
            event.preventDefault();
            alert('Por favor, seleccione una frecuencia para la donación periódica.');
        }
    });
});
