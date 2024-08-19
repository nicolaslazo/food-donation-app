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

window.onload = function() {
    mostrarModal();
};

//ADMINISTRAR SERVICIO

function confirmarModificacion() {
    alert('¡Modificación realizada con éxito!');
    cerrarModal();
}

document.getElementById('form-modificar-valores').onsubmit = function(event) {
    event.preventDefault();

    const puntos = document.getElementById('edit-points').value;
    const stock = document.getElementById('edit-stocks').value;

    if (puntos && stock) {
        confirmarModificacion();
    } else {
    }
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

//AGREGAR SERVICIO

function cerrarModalAgregarServicio() {
    document.getElementById('modal-agregar-servicio').style.display = 'none';
    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.remove('blur-background');
    pointsContainer.style.pointerEvents = 'auto';
}


function abrirModalAgregarServicio() {
    document.getElementById('modal-agregar-servicio').style.display = 'block';
    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.add('blur-background');
    pointsContainer.style.pointerEvents = 'none';
}

document.getElementById('form-agregar-servicio').onsubmit = function(event) {
    event.preventDefault();

    const nombreServicio = document.getElementById('nombre-servicio').value;
    const puntosServicio = document.getElementById('puntos-servicio').value;
    const categoriaServicio = document.getElementById('categoria-servicio').value;

    alert("¡Servicio agregado con éxito!");

    cerrarModalAgregarServicio();
};

//ELIMINAR SERVICIO
let servicioAEliminar;

function mostrarModalEliminar(button) {
    servicioAEliminar = button.closest('.service-item');
    console.log(servicioAEliminar);
    document.getElementById('modalEliminar').style.display = 'block';

    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.add('blur-background');
    pointsContainer.style.pointerEvents = 'none';
}

function cerrarModalEliminar() {
    document.getElementById('modalEliminar').style.display = 'none';

    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.remove('blur-background');
    pointsContainer.style.pointerEvents = 'auto';
}

function eliminarConfirmado() {
    if (servicioAEliminar) {
        servicioAEliminar.remove();
        alert('Servicio eliminado con éxito');
        cerrarModalEliminar();
    }
}







