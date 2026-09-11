document.addEventListener('DOMContentLoaded', function () {
    var toggle = document.getElementById('sidebarToggle');
    var sidebar = document.getElementById('appSidebar');
    if (toggle && sidebar) {
        toggle.addEventListener('click', function () {
            sidebar.classList.toggle('show');
        });
    }

    // Xac nhan truoc cac hanh dong nguy hiem (xoa, huy).
    document.querySelectorAll('form[data-confirm]').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            var message = form.getAttribute('data-confirm') || 'Bạn có chắc chắn muốn thực hiện hành động này?';
            if (!window.confirm(message)) {
                e.preventDefault();
            }
        });
    });

    // Tu dong an alert thanh cong sau vai giay.
    document.querySelectorAll('.alert-auto-dismiss').forEach(function (alertEl) {
        setTimeout(function () {
            var bsAlert = bootstrap.Alert.getOrCreateInstance(alertEl);
            bsAlert.close();
        }, 4000);
    });
});
