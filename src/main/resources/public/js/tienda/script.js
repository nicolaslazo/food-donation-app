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

function cerrarModal() {

    document.getElementById('modal').style.display = 'none';

    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.remove('blur-background');

    pointsContainer.style.pointerEvents = 'auto';
}

function confirmarCanje() {
    alert('¡Canje realizado con éxito!');
    cerrarModal();
}





