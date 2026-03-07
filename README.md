# Task Management API 📝

Una API RESTful simple construida con Spring Boot para la gestión de tareas.

## 🚀 Tecnologías

* Java 21
* Spring Boot
* Spring Data JPA
* Base de datos PostgreSQL
* Maven

## ⚙️ Funcionalidades

* Crear una nueva tarea.
* Listar todas las tareas.
* Actualizar el estado de una tarea (Completada/No completada).
* Actualizar la descripción de una tarea.
* Eliminar una tarea.

## 🛠️ Instalación y Uso Local

1. Clonar el repositorio: https://github.com/GIT-Marcos/task-api-springboot.git
2. Tener PostgreSQL instalado y corriendo en el puerto 5432.
3. Crear una base de datos vacía en Postgres llamada tasks_db.
4. Ir a la carpeta src/main/resources/.
5. Tomar el archivo application-dev.template.properties, renombrarlo a application-dev.properties (este archivo está
   ignorado por git por seguridad).
6. Reemplazar `NOMBRE_USUARIO` y `CONTRASEÑA` con tus credenciales de Postgres locales.
7. Ejecutar el proyecto.
