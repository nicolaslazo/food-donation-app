//FILTRAR//
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

//
window.onload = function() {
    mostrarModal();
};

function administrarServicio(serviceId) {

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

function cerrarModalAgregarServicio() {
    document.getElementById('modal-agregar-servicio').style.display = 'none';
    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.remove('blur-background');
    pointsContainer.style.pointerEvents = 'auto';
}


function confirmarModificacion() {
    alert('¡Modificación realizado con éxito!');
    cerrarModal();
}

function abrirModalAgregarServicio() {
    document.getElementById('modal-agregar-servicio').style.display = 'block';
    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.add('blur-background');
    pointsContainer.style.pointerEvents = 'none';
}

document.getElementById('form-agregar-servicio').onsubmit = function(event) {
    event.preventDefault();

    // Aquí puedes añadir lógica para agregar el nuevo servicio a la lista
    const nombreServicio = document.getElementById('nombre-servicio').value;
    const puntosServicio = document.getElementById('puntos-servicio').value;
    const categoriaServicio = document.getElementById('categoria-servicio').value;

    // Puedes insertar la lógica para añadir el servicio a la lista de servicios aquí

    // Mostrar mensaje de éxito
    alert("¡Servicio agregado con éxito!");

    // Cerrar el modal después de agregar el servicio
    cerrarModalAgregarServicio();
};

let servicioAEliminar; // Variable global para almacenar el servicio a eliminar

function mostrarModalEliminar(button) {
    servicioAEliminar = button.closest('.service-item'); // Almacenar el servicio que se desea eliminar
    console.log(servicioAEliminar); // Verifica si se está seleccionando el servicio correcto
    document.getElementById('modalEliminar').style.display = 'block';

    // Aplicar el blur al fondo
    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.add('blur-background');
    pointsContainer.style.pointerEvents = 'none';
}

function cerrarModalEliminar() {
    document.getElementById('modalEliminar').style.display = 'none';

    // Remover el blur del fondo
    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.remove('blur-background');
    pointsContainer.style.pointerEvents = 'auto';
}

function eliminarConfirmado() {
    if (servicioAEliminar) {
        servicioAEliminar.remove(); // Eliminar el servicio del DOM
        alert('Servicio eliminado con éxito');
        cerrarModalEliminar(); // Cerrar el modal después de eliminar
    }
}







