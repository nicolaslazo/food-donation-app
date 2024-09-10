document.getElementById('tipo-suscripcion').addEventListener('change', function () {
    var faltanViandasContainer = document.getElementById('faltan-viandas-container');
    var faltaEspacioContainer = document.getElementById('falta-espacio-container');

    // Ocultamos ambos contenedores antes de mostrar el seleccionado
    faltanViandasContainer.style.display = 'none';
    faltaEspacioContainer.style.display = 'none';

    switch (this.value) {
        case 'faltan_viandas':
            faltanViandasContainer.style.display = 'flex';
            faltanViandasContainer.style.flexDirection = 'column';
            break
        case 'falta_espacio':
            faltaEspacioContainer.style.display = 'flex';
            faltaEspacioContainer.style.flexDirection = 'column';
            break
        case 'falla_heladera':
            break
    }
});