document.getElementById('persona-juridica').addEventListener('change', function () {
    var tipoJuridicaContainer = document.getElementById('container-tipo-persona-juridica');
    if (this.value === 'si') {
        tipoJuridicaContainer.style.display = 'flex';
        tipoJuridicaContainer.style.flexDirection = 'column';
    } else {
        tipoJuridicaContainer.style.display = 'none';
    }
});