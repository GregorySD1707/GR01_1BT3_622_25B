# Usar imagen base de Tomcat con JDK
FROM tomcat:10.1-jdk21-temurin

# Información del mantenedor
LABEL maintainer="Equipo 1"

# Eliminar aplicaciones por defecto de Tomcat (opcional pero recomendado)
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el archivo WAR al directorio webapps de Tomcat
# El nombre del WAR determina la ruta de acceso (ROOT.war = /)
COPY target/*.war /usr/local/tomcat/webapps/com.sistema_financiero_personal.war

# Exponer el puerto 8080
EXPOSE 8080

# Tomcat se inicia automáticamente con CMD heredado de la imagen base
# CMD ["catalina.sh", "run"]