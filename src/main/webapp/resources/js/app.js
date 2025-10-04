document.addEventListener('DOMContentLoaded', () => {

    // --- 1. GESTIÓN DEL MENÚ DESPLEGABLE DEL USUARIO (código que ya tenías) ---
    const userMenuButton = document.querySelector('.user-menu .user-avatar');
    const userDropdownMenu = document.querySelector('.user-menu .dropdown-menu');

    if (userMenuButton && userDropdownMenu) {
        userMenuButton.addEventListener('click', (event) => {
            event.stopPropagation();
            // Cierra el otro menú si está abierto
            if (notificationList && notificationList.classList.contains('active')) {
                notificationList.classList.remove('active');
            }
            userDropdownMenu.classList.toggle('active');
        });
    }

    // --- 2. FILTRO DE BÚSQUEDA EN TIEMPO REAL ---
    // (Tu código de búsqueda de recordatorios va aquí si lo tienes)

    // --- 3. CÓDIGO PARA LA SIDEBAR (código que ya tenías) ---
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('overlay');
    const openBtn = document.getElementById('sidebar-toggle-btn');
    const closeBtn = document.getElementById('close-sidebar-btn');

    if (sidebar && overlay && openBtn && closeBtn) {
        const openSidebar = () => {
            sidebar.classList.add('active');
            overlay.classList.add('active');
        };

        const closeSidebar = () => {
            sidebar.classList.remove('active');
            overlay.classList.remove('active');
        };

        openBtn.addEventListener('click', openSidebar);
        closeBtn.addEventListener('click', closeSidebar);
        overlay.addEventListener('click', closeSidebar);
    }

    // =================================================================
    // --- 4. NUEVO CÓDIGO PARA NOTIFICACIONES ---
    // =================================================================
    const notificationBtn = document.querySelector('.notification-btn');
    const notificationList = document.getElementById('notification-list');
    const notificationCountBadge = document.getElementById('notification-count');

    if (notificationBtn && notificationList && notificationCountBadge) {

        // (La función fetchNotifications y updateNotificationUI no cambian, siguen igual)
        async function fetchNotifications() {
            try {
                const response = await fetch(`${CONTEXT_PATH}/api/notificaciones`);
                if (!response.ok) {
                    throw new Error('La respuesta del servidor no fue exitosa.');
                }
                const notifications = await response.json();
                updateNotificationUI(notifications);
            } catch (error) {
                console.error('Falló la carga de notificaciones:', error);
                notificationList.innerHTML = '<div class="dropdown-item">Error al cargar.</div>';
            }
        }
        function updateNotificationUI(notifications) {
            notificationList.innerHTML = '';
            if (notifications.length === 0) {
                notificationCountBadge.style.display = 'none';
                notificationList.innerHTML = '<div class="dropdown-item no-notifications">No hay notificaciones nuevas.</div>';
            } else {
                notificationCountBadge.textContent = notifications.length;
                notificationCountBadge.style.display = 'block';
                notifications.forEach(notif => {
                    const item = document.createElement('a');
                    item.href = `${CONTEXT_PATH}/recordatorios`;
                    item.classList.add('dropdown-item');
                    item.innerHTML = `<strong>${notif.descripcion}</strong><br><small>Vence el: ${notif.fecha}</small>`;
                    notificationList.appendChild(item);
                });
            }
        }

        notificationBtn.addEventListener('click', (event) => {
            console.log("¡Clic en la campana detectado!");
            event.stopPropagation();
            // Cierra el otro menú si está abierto
            if (userDropdownMenu && userDropdownMenu.classList.contains('active')) {
                userDropdownMenu.classList.remove('active');
            }
            // ===================
            //  CAMBIO PRINCIPAL
            // ===================
            // En lugar de cambiar el estilo 'display', usamos la clase '.active'
            notificationList.classList.toggle('active');

            // Ocultamos el contador solo cuando el menú se activa
            if (notificationList.classList.contains('active')) {
                notificationCountBadge.style.display = 'none';
            }
        });

        fetchNotifications();
    }

    // --- 5. GESTOR GLOBAL DE CLICS PARA CERRAR MENÚS ---
    document.addEventListener('click', () => {
        if (userDropdownMenu && userDropdownMenu.classList.contains('active')) {
            userDropdownMenu.classList.remove('active');
        }
        // ==============================================
        //  CAMBIO PRINCIPAL EN EL GESTOR GLOBAL
        // ==============================================
        // Ahora también busca la clase '.active' en el menú de notificaciones
        if (notificationList && notificationList.classList.contains('active')) {
            notificationList.classList.remove('active');
        }
    });

});