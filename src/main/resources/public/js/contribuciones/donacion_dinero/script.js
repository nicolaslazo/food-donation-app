document.getElementById('donacion-periodica').addEventListener('change', function () {
    var frecuenciaContainer = document.getElementById('frecuencia-container');
    if (this.value === 'si') {
        frecuenciaContainer.style.display = 'flex';
        frecuenciaContainer.style.flexDirection = 'column';
    } else {
        frecuenciaContainer.style.display = 'none';
    }
});