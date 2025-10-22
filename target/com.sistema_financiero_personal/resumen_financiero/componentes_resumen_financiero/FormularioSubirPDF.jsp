<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%-- Definir que esta página generará HTML con codificación UTF-8 --%>
<%@ page isELIgnored="false" %> <%--  Habilita Expression Language (EL) para usar sintaxis ${variable} -->
<%-- Taglibs: Importa bibliotecas JSTL --%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <%-- c para control de flujo --%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %> <%-- fmt para formateo de numeros y fechas --%>

 <form action="subirPDF" method="post" enctype="multipart/form-data" style="max-width: 500px; margin: 1rem auto;">
            <div class="form-group">
                <label for="archivoPDF" class="btn btn-secondary" style="cursor: pointer; display: inline-block; margin-bottom: 1rem;">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="vertical-align: middle; margin-right: 0.5rem;">
                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                        <polyline points="7 10 12 15 17 10"></polyline>
                        <line x1="12" y1="15" x2="12" y2="3"></line>
                    </svg>
                    Seleccionar Archivo PDF
                </label>
                <input type="file" id="archivoPDF" name="archivoPDF" accept="application/pdf" required style="display: none;" onchange="updateFileName(this)">
                <p id="file-name" style="color: var(--text-primary); margin-top: 0.5rem; font-size: 0.9rem;">Ningún archivo seleccionado</p>
            </div>

            <div style="text-align: center; margin-top: 1. rem;">
                <button type="submit" class="btn btn-primary">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                        <polyline points="17 8 12 3 7 8"></polyline>
                        <line x1="12" y1="3" x2="12" y2="15"></line>
                    </svg>
                    <span>Procesar</span>
                </button>
            </div>
        </form>
    </div>