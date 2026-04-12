# Task Management API 📝

Una API RESTful simple construida con Spring Boot para la gestión de tareas.

## 🚀 Tecnologías

* Java 21
* Spring Boot
* Spring Data JPA
* Base de datos PostgreSQL
* Render
* Maven
* Docker
* OpenAPI (antiguo Swagger UI)
* Prometheus - Grafana

## ⚙️ Funcionalidades

* Crear una nueva tarea.
* Listar todas las tareas.
* Actualizar el estado de una tarea (Completada/No completada).
* Actualizar la descripción de una tarea.
* Eliminar una tarea.

## 🛠️ Instalación y Uso Local

1. Clonar el repositorio: https://github.com/GIT-Marcos/task-api-springboot.git
2. Instalar y abrir Docker Desktop.
3. Ir a la carpeta src/main/resources/.
4. Tomar el archivo application-dev.template.properties, renombrarlo a _application-dev.properties_ (este archivo está
   ignorado por git por seguridad).
5. Ejecutar `docker compose up -d` en la consola del IDE.
6. Ejecutar el proyecto.

## 📚 Documentación de la API (Swagger UI)

La API está completamente autodocumentada utilizando Springdoc OpenAPI. Una vez que la aplicación esté en ejecución,
es posible explorar, visualizar y probar todos los endpoints directamente desde el navegador:

🌐 Interfaz Gráfica (Swagger UI):
http://localhost:8080/docs

📄 Especificación OpenAPI (JSON puro):
http://localhost:8080/api-docs

Tener en cuenta que por seguridad el acceso a esta documentación está bloqueado en producción, por lo tanto, solo puede
ser consultada desde el entorno de desarrollo local.

## 📡 Probar la API

Para facilitar las pruebas, fueron subidas al repositorio las peticiones más comunes en formato .json listas para
importar a herramientas como Hoppscotch o Postman.

1. Ir a una herramienta de pruebas de API's (https://hoppscotch.io/ o https://www.postman.com/)
2. Importar la colección desde la carpeta `/docs/hoppscotch_collection.json`.
3. Importar el entorno desde `/docs/hoppscotch_environment.json` y seleccionarlo.

La API es hosteada por Render, y cuando no se usa el servicio por 15 minutos Render lo "duerme" para optimizar
recursos. Por esto es probable que la primera petición tarde varios segundos hasta que Render levante la API.
