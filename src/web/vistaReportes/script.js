document.addEventListener('DOMContentLoaded', function () {
    const accordions = document.querySelectorAll('.accordion');

    accordions.forEach(accordion => {
        const title = accordion.querySelector('.accordion-title');
        const content = accordion.querySelector('.accordion-content');

        title.addEventListener('click', function () {
            // Primero cerramos todos los demás
            accordions.forEach(a => {
                const otherContent = a.querySelector('.accordion-content');
                if (otherContent !== content) {
                    otherContent.style.maxHeight = null;
                }
            });

            // Alternamos el estado de la sección actual
            if (content.style.maxHeight) {
                content.style.maxHeight = null;
            } else {
                content.style.maxHeight = content.scrollHeight + "px";
            }
        });
    });
});
