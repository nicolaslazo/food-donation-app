document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector('.triggerea-toast');
    const toast = document.getElementById('toast');

    function mostrarToast(mensaje) {
        if (mensaje) {
            toast.innerHTML = mensaje;
            toast.classList.add('show-toast');

            setTimeout(() => {
                toast.classList.remove('show-toast');
            }, 3000);
        }
    }

    if (form) {
        form.addEventListener('submit', function (event) {
            event.preventDefault();  // Previene el submit de form tradicional

            // Hace la request POST AJAX
            const datosForm = new FormData(form);
            fetch(window.location.pathname, {
                method: 'POST',
                body: datosForm
            })
                .then(response => response.json())
                .then(data => {
                    mostrarToast(data.message);

                    if (data.urlRedireccion) {
                        setTimeout(() => {
                            window.location.href = data.urlRedireccion;
                        }, data.demoraRedireccionEnSegundos * 1000 || 0);
                    }
                })
                .catch(error => console.error('Error:', error));
        });
    }
});
