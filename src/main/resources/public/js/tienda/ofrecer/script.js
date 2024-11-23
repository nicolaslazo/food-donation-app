function cerrarModalAgregarRecompensa() {
    document.getElementById('modal-agregar-servicio').style.display = 'none';
    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.remove('blur-background');
    pointsContainer.style.pointerEvents = 'auto';
}


function abrirModalAgregarRecompensa() {
    document.getElementById('modal-agregar-servicio').style.display = 'block';
    const pointsContainer = document.querySelector('.points-container');
    pointsContainer.classList.add('blur-background');
    pointsContainer.style.pointerEvents = 'none';
}

document.getElementById('form-agregar-servicio').onsubmit = function(event) {
    event.preventDefault(); // Evitar que el formulario se envíe de forma tradicional

    const formData = new FormData(this); // Obtener los datos del formulario

    fetch('/tienda/recompensas/admin', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                alert("¡Servicio agregado con éxito!");
                cerrarModalAgregarRecompensa();
                location.reload(); // Recargar la página para ver la nueva recompensa
            } else {
                alert("Hubo un error al agregar el servicio");
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("Hubo un error al agregar el servicio");
        });
};
