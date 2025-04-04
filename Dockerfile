# Usar Amazon Corretto 17 como base
FROM amazoncorretto:17

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR generado
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar

# Puerto expuesto por la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "application.jar"]