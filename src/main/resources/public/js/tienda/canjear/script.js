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

function confirmarCanje() {
    const puntosNecesarios = parseInt(document.getElementById('required-points').textContent);
    const puntosActuales = parseInt(document.getElementById('points').textContent);

    if (puntosActuales >= puntosNecesarios) {
        fetch('/tienda/recompensas/' + idRecompensa, {
            method: 'POST',
        })
        .then(response => {
            if (response.ok) {
                alert('¡Canje realizado con éxito!');
                location.reload();
            } else {
                alert('Hubo un problema al realizar el canje. Inténtalo de nuevo.');
            }
        })
        .catch(error => {
            alert('Hubo un problema al realizar el canje. Inténtalo de nuevo.');
        });
    } else {
        alert('No tienes suficientes puntos para realizar el canje.');
    }
}
