document.addEventListener('DOMContentLoaded', function() {
    const situationSelect = document.getElementById('situacion');
    const addressSection = document.getElementById('direccion');

    const hasDniSelect = document.getElementById('tiene-dni');
    const dniSection = document.getElementById('posee-documento');

// Manejar la visibilidad del campo de dirección
    situationSelect.addEventListener('change', function() {
        if (this.value === 'domicilio') {
            addressSection.classList.add('show'); // Muestra el div
            addressSection.classList.remove('hidden'); // Asegura que la clase 'hidden' no esté presente
        } else {
            addressSection.classList.add('hidden'); // Oculta el div
            addressSection.classList.remove('show'); // Asegura que la clase 'show' no esté presente
        }
    });

    // Manejar la visibilidad de los campos de DNI
    hasDniSelect.addEventListener('change', function() {
        if (this.value === 'yes') {
            dniSection.classList.add('show'); // Muestra el div
            dniSection.classList.remove('hidden'); // Asegura que la clase 'hidden' no esté presente
        } else {
            dniSection.classList.add('hidden'); // Oculta el div
            dniSection.classList.remove('show'); // Asegura que la clase 'show' no esté presente
        }
    });
});
