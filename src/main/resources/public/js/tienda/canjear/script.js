let idRecompensa;

function abrirModalCanjeo(id) {
    idRecompensa = id;

    const servicioSeleccionado = document.querySelector(`.service-item[data-id="${id}"]`);

    const nombreProducto = servicioSeleccionado.querySelector('h3').textContent;
    const puntosNecesarios = parseInt(servicioSeleccionado.querySelector('.service-info p').textContent.match(/\d+/)[0]);
    const puntosActuales = parseInt(document.getElementById('points').textContent);
    const puntosRestantes = puntosActuales - puntosNecesarios;

    document.getElementById('required-points').textContent = puntosNecesarios;
    document.getElementById('remaining-points').textContent = puntosRestantes;
    document.getElementById('product-info').textContent = nombreProducto;

    document.getElementById('modal').style.display = 'block';

    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.add('blur-background');
    pointsContainer.style.pointerEvents = 'none';
}

function cerrarModal() {
    document.getElementById('modal').style.display = 'none';

    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.remove('blur-background');

    pointsContainer.style.pointerEvents = 'auto';
}

function confirmarCanje() {
    fetch('/tienda/recompensas/' + idRecompensa, {
        method: 'POST',
    })
    alert('¡Canje realizado con éxito!');
    location.reload();
}