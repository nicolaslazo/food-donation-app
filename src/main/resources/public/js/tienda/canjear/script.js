

function filtrarPorCategoria() {
    const categoriaSeleccionada = document.getElementById("category-filter").value;
    const servicios = document.querySelectorAll(".service-item");
    var noHayServicios = true;

    servicios.forEach(servicio => {
        const categoria = servicio.getAttribute("data-categoria");
        if (categoriaSeleccionada === "todos" || categoria === categoriaSeleccionada) {
            servicio.style.display = "flex";
            noHayServicios = false;
        } else {
            servicio.style.display = "none";
        }
    });

    var mensajeNoServicios = document.getElementById('no-services-message');
    if (noHayServicios) {
        mensajeNoServicios.style.display = 'block';
    } else {
        mensajeNoServicios.style.display = 'none';
    }
}

window.onload = function() {
};

let serviceId;

function abrirModalCanjeo(id) {
    serviceId = id;

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
    // Recargar la página actual
    location.reload();
}

function confirmarCanje() {
    fetch('/tienda/canjearProductos/' + serviceId, {
        method: 'POST',
    })
    alert('¡Canje realizado con éxito!');
    cerrarModal();
}





