# Imagen base de Tomcat con JDK 21
FROM tomcat:10.1-jdk21-temurin

# Metadata
LABEL maintainer="Sistema Financiero Personal - Equipo 1"
LABEL description="Aplicación web de gestión financiera personal con Hibernate y PostgreSQL"

# Eliminar aplicaciones por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el archivo WAR generado por Maven
# El nombre final es com.sistema_financiero_personal.war según tu pom.xml
COPY target/com.sistema_financiero_personal.war /usr/local/tomcat/webapps/ROOT.war

# Exponer puerto 8080
EXPOSE 8080

# Tomcat inicia automáticamente (CMD heredado de imagen base)
CMD ["catalina.sh", "run"]