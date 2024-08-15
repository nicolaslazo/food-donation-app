function canjearServicio(serviceId) {
    window.location.href = `canje.html?service=${serviceId}`;
}

// Función para filtrar los productos por categoría
function filtrarPorCategoria() {
    const categoriaSeleccionada = document.getElementById("category-filter").value;
    const servicios = document.querySelectorAll(".service-item");

    servicios.forEach(servicio => {
        const categoria = servicio.getAttribute("data-categoria");
        if (categoriaSeleccionada === "todos" || categoria === categoriaSeleccionada) {
            servicio.style.display = "flex";
        } else {
            servicio.style.display = "none";
        }
    });
}

