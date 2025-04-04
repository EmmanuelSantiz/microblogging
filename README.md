# Aplicación de Microblogging

Una plataforma simple de microblogging construida con Spring Boot, PostgreSQL, RabbitMQ y Redis.

## Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Requisitos Previos](#requisitos-previos)
- [Primeros Pasos](#primeros-pasos)
  - [Clonar el Repositorio](#clonar-el-repositorio)
  - [Construir la Aplicación](#construir-la-aplicación)
  - [Ejecutar con Docker Compose](#ejecutar-con-docker-compose)
  - [Acceder a la Aplicación](#acceder-a-la-aplicación)
- [Documentación de la API](#documentación-de-la-api)
- [Características](#características)
- [Comandos Docker](#comandos-docker)
- [Solución de Problemas](#solución-de-problemas)

## Descripción General

Esta aplicación de microblogging permite a los usuarios:
- Crear y gestionar cuentas de usuario
- Publicar tweets
- Seguir a otros usuarios
- Dar "me gusta" y comentar en tweets
- Ver una línea de tiempo personalizada

La aplicación está construida utilizando:
- Spring Boot 3.x para el backend
- PostgreSQL para almacenamiento de datos
- RabbitMQ para la cola de mensajes
- Redis para caché
- Swagger para documentación de la API

## Requisitos Previos

Antes de comenzar, asegúrate de tener instalado lo siguiente:
- [Git](https://git-scm.com/downloads)
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Java 17+](https://adoptium.net/) (solo necesario para desarrollo local)
- [Maven](https://maven.apache.org/download.cgi) (solo necesario para desarrollo local)

## Primeros Pasos

### Clonar el Repositorio

```bash
# Clonar el repositorio
git clone https://github.com/EmmanuelSantiz/microblogging.git

# Navegar al directorio del proyecto
cd microblogging
```

### Construir la Aplicación

Puedes construir la aplicación localmente o usar la imagen Docker pre-construida.

#### Opción 1: Construir localmente con Maven

```bash
# Construir la aplicación
mvn clean package -DskipTests
```

#### Opción 2: Construir imagen Docker

```bash
# Construir la imagen Docker
docker build -t microblogging:latest .
```

### Ejecutar con Docker Compose

La forma más sencilla de ejecutar la aplicación es usando Docker Compose, que iniciará todos los servicios requeridos:

```bash
# Iniciar todos los servicios en modo desacoplado
docker-compose up -d
```

Este comando iniciará:
- La aplicación de microblogging en el puerto 8080 sin contenedor o bien sin nginx
- La aplicacion de microblogging en el puerto 80
- Base de datos PostgreSQL en el puerto 5432
- RabbitMQ en los puertos 5672 (AMQP) y 15672 (Interfaz de Administración)
- Redis en el puerto 6379

### Acceder a la Aplicación

Una vez que todos los servicios estén en ejecución, puedes acceder a:
- La aplicación: http://localhost
- Swagger UI: http://localhost/swagger-ui.html
- Interfaz de Administración de RabbitMQ: http://localhost:15672 (usuario: root, contraseña: root)

## Documentación de la API

La documentación de la API está disponible a través de Swagger UI en http://localhost/swagger-ui.html cuando la aplicación está en ejecución.

Los endpoints clave incluyen:
- `/api/users` - Gestión de usuarios
- `/api/tweets` - Operaciones de tweets
- `/api/tweets/interactions` - Comentarios y me gusta

## Características

- **Gestión de Usuarios**: Crear y gestionar cuentas de usuario
- **Operaciones de Tweets**: Publicar, ver y eliminar tweets
- **Características Sociales**: Seguir a otros usuarios, dar me gusta y comentar en tweets
- **Línea de Tiempo**: Ver una línea de tiempo personalizada de tweets de usuarios seguidos

## Comandos Docker

Aquí hay algunos comandos Docker útiles para gestionar la aplicación:

```bash
# Iniciar todos los servicios en modo desacoplado
docker-compose up -d

# Ver logs de todos los servicios
docker-compose logs

# Ver logs de un servicio específico
docker-compose logs app
docker-compose logs db
docker-compose logs rabbitmq
docker-compose logs redis

# Detener todos los servicios pero mantener los contenedores
docker-compose stop

# Detener y eliminar contenedores, redes y volúmenes
docker-compose down

# Detener y eliminar contenedores, redes, volúmenes e imágenes
docker-compose down --rmi all

# Reconstruir y reiniciar solo el servicio de la aplicación
docker-compose up -d --build app
```

## Solución de Problemas

### Problemas de Conexión a la Base de Datos

Si la aplicación no puede conectarse a la base de datos:
1. Verifica si el contenedor de PostgreSQL está en ejecución: `docker-compose ps`
2. Verifica las credenciales de la base de datos en `docker-compose.yml`
3. Revisa los logs de la aplicación: `docker-compose logs app`

### Problemas de Conexión a RabbitMQ

Si la aplicación no puede conectarse a RabbitMQ:
1. Asegúrate de que el contenedor de RabbitMQ esté en ejecución: `docker-compose ps`
2. Verifica las credenciales de RabbitMQ en `docker-compose.yml`
3. Comprueba si la interfaz de administración de RabbitMQ es accesible: http://localhost:15672

### Problemas de Conexión a Redis

Si la aplicación no puede conectarse a Redis:
1. Verifica si el contenedor de Redis está en ejecución: `docker-compose ps`
2. Verifica la configuración de Redis en `docker-compose.yml`
3. Revisa los logs de la aplicación: `docker-compose logs app`

---

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - consulta el archivo LICENSE para más detalles.

## Agradecimientos

- Spring Boot por el excelente framework
- Docker por la containerización
- Todos los contribuyentes a este proyecto
