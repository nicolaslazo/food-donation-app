window.onload = function() {
    mostrarModal();
};

function mostrarModal() {
    // Muestra el modal y desenfoca el fondo
    document.getElementById('modal').style.display = 'block';
    document.getElementById('blur-background').style.filter = 'blur(5px)';
    document.getElementById('blur-background').style.pointerEvents = 'none'; // Evita la interacción con el fondo
}

function cerrarModal() {
    // Cierra el modal y quita el desenfoque del fondo
    document.getElementById('modal').style.display = 'none';
    document.getElementById('blur-background').style.filter = 'none';
    document.getElementById('blur-background').style.pointerEvents = 'auto';
}

function confirmarCanje() {
    alert('¡Canje realizado con éxito!');
    cerrarModal(); // Cierra el modal después del canje
}