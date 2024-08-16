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

window.onload = function() {
    mostrarModal();
};

function canjearServicio(serviceId) {
    console.log("Mostrando el modal y aplicando blur");

    // Muestra el modal
    document.getElementById('modal').style.display = 'block';

    // Agrega la clase blur al contenedor de puntos
    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.add('blur-background');

    // Desactiva la interacción con el fondo
    pointsContainer.style.pointerEvents = 'none';
}

function cerrarModal() {
    console.log("Cerrando el modal y quitando blur");

    // Oculta el modal
    document.getElementById('modal').style.display = 'none';

    // Elimina la clase blur del contenedor de puntos
    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.remove('blur-background');

    // Habilita la interacción con el fondo
    pointsContainer.style.pointerEvents = 'auto';
}

function confirmarCanje() {
    alert('¡Canje realizado con éxito!');
    cerrarModal(); // Cierra el modal y elimina el desenfoque después del canje
}





