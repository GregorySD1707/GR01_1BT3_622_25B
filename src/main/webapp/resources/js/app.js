// JS para filtro client-side en el listado de recordatorios
window.addEventListener('DOMContentLoaded', function(){
    const remindersSearch = document.getElementById('searchReminders');
    if(remindersSearch){
        remindersSearch.addEventListener('input', function(e){
            // Obtiene el valor de búsqueda, lo convierte a minúsculas y quita espacios
            const q = e.target.value.toLowerCase().trim();

            // Selecciona todas las tarjetas de recordatorios
            document.querySelectorAll('#remindersList .card').forEach(card => {
                // Obtiene la descripción del atributo 'data-description' de la tarjeta
                const description = card.dataset.description ? card.dataset.description.toLowerCase() : '';

                // Comprueba si la descripción incluye el texto de búsqueda
                const match = description.includes(q);

                // Muestra la tarjeta si coincide, de lo contrario la oculta
                card.style.display = match ? '' : 'none';
            });
        });
    }
});
