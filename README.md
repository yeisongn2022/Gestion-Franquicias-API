# 🚀 Gestión de Franquicias API (Reactive & Clean Architecture)

Este proyecto es una API REST desarrollada con **Spring Boot 4.0.6** y **WebFlux**, diseñada para gestionar franquicias, sucursales y productos bajo los principios de **Clean Architecture** y programación reactiva.

## 🛠️ Tecnologías y Prácticas
- **Lenguaje:** Java 17
- **Framework:** Spring Boot 4.0.6 (WebFlux)
- **Base de Datos:** MongoDB (Persistencia reactiva)
- **Contenerización:** Docker & Docker Compose
- **IaC:** Terraform (Aprovisionamiento de infraestructura como código)
- **Arquitectura:** Clean Architecture (Independencia de frameworks)

---

## 🏗️ Estructura del Proyecto
Siguiendo Clean Architecture, el código se organiza en:
- **Dominio:** Entidades de negocio y contratos (interfaces) de repositorios.
- **Aplicación:** Casos de uso y servicios de orquestación.
- **Infraestructura:** Adaptadores (MongoDB, Controladores REST) y configuraciones técnicas.

---

## 🚀 Instrucciones de Despliegue Local

### Requisitos Previos
- Docker y Docker Compose instalados.
- Java 17 (opcional si se usa solo Docker).

### Paso 1: Clonar y Compilar
- git clone https://github.com/yeisongn2022/Gestion-Franquicias-API.git
- cd franquicias-api
- Si se vá hacer la prueba en local, antes de generar el JAR. Editar los archivos application.properties y docker-compose.yml cambiando las credenciales del spring.data.mongodb.uri como se detalla en este documento anexo -> https://docs.google.com/document/d/15BOxRKmoFzQaI3eoFsCUL54MOv_uGZmXBrjmi1fyG4g/edit?tab=t.0 (Solicitar permiso de lectura)
- Otra opcion para la prueba en local, es reemplazar y adicionar las siguientes líneas en los archivos de application.properties y docker-compose.yml respectivamente de esta forma:
  - En application.properties -> spring.data.mongodb.uri=${MONGODB_URI:mongodb://localhost:27017/gestion_franquicias}
  - En docker-compose.yml: 
  ```yaml
    services:
      app-franquicias:
        build: .
        ports:
          - "8081:8081"
        environment:
          - SPRING_DATA_MONGODB_URI=mongodb://db:27017/gestion_franquicias
        depends_on:
        - db
    
      db:
        image: mongo:latest
        ports:
          - "27017:27017"
  ```
- ./mvnw clean package -DskipTests (Compilar y generar el archivo JAR omitiendo los tests)

### Paso 2: Levantar con Docker Compose
- docker-compose up --build -d  (Ejecutamos este comando en la raiz del proyecto)
- En local la aplicación con los endpoints estará disponible en: http://localhost:8081/swagger-ui/index.html#/
- En la nube el servicio está disponible en: https://gestion-franquicias-api.onrender.com/swagger-ui/index.html#/

### ☁️ Despliegue en la Nube (Terraform)
En la carpeta /terraform se encuentran los scripts para aprovisionar la infraestructura en MongoDB Atlas.
- Inicializar: terraform init
- Planificar: terraform plan
- Aplicar: terraform apply 
- Nota: Los recursos ya están creados, por lo tánto estos archivos son meramente informativo para ejemplificar como se haria el proceso de aprovisionar los recursos.

### 🧪 Pruebas Unitarias
Tiene implementados tests con JUnit y Mockito 
- ./mvnw test ó en el IDE ejecutar el Run 'FranquiciaServicioTest'