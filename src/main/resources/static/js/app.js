(() => {
    const modalElement = document.getElementById('confirmarExclusao');
    let pendingForm;
    document.querySelectorAll('form[data-confirmar]').forEach(form => {
        form.addEventListener('submit', event => {
            event.preventDefault();
            pendingForm = form;
            document.getElementById('exclusaoTexto').textContent = form.dataset.confirmar;
            bootstrap.Modal.getOrCreateInstance(modalElement).show();
        });
    });
    document.getElementById('exclusaoConfirmar')?.addEventListener('click', event => {
        if (pendingForm) {
            event.currentTarget.disabled = true;
            HTMLFormElement.prototype.submit.call(pendingForm);
        }
    });
    const profile = document.getElementById('incluirPerfil');
    const profileFields = document.getElementById('camposPerfil');
    if (profile && profileFields) {
        const update = () => {
            profileFields.hidden = !profile.checked;
            profileFields.querySelectorAll('input, select, textarea').forEach(field => {
                field.disabled = !profile.checked;
            });
        };
        profile.addEventListener('change', update);
        update();
    }
})();
