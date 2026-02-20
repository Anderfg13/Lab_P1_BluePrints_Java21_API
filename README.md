## Laboratorio #4 – REST API Blueprints (Java 21 / Spring Boot 3.3.x)
# Escuela Colombiana de Ingeniería – Arquitecturas de Software  

---

## 📋 Requisitos
- Java 21
- Maven 3.9+

## Integrantes
- Anderson Fabian Garcia Nieto  
- Juana Lozano Chaves

## Link del informe de laboratorio

- Link: https://docs.google.com/document/d/1hokZ0NKmrk-SieZNwBOXzKU8qL_QwcyreaFpDZeBnBk/edit?usp=sharing

## ▶️ Ejecución del proyecto
```bash
mvn clean install
mvn spring-boot:run
```
Probar con `curl`:
```bash
curl -s http://localhost:8080/blueprints | jq
curl -s http://localhost:8080/blueprints/john | jq
curl -s http://localhost:8080/blueprints/john/house | jq
curl -i -X POST http://localhost:8080/blueprints -H 'Content-Type: application/json' -d '{ "author":"john","name":"kitchen","points":[{"x":1,"y":1},{"x":2,"y":2}] }'
curl -i -X PUT  http://localhost:8080/blueprints/john/kitchen/points -H 'Content-Type: application/json' -d '{ "x":3,"y":3 }'
```

> Si deseas activar filtros de puntos (reducción de redundancia, *undersampling*, etc.), implementa nuevas clases que implementen `BlueprintsFilter` y cámbialas por `IdentityFilter` con `@Primary` o usando configuración de Spring.
---

Abrir en navegador:  
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)  

---

## 🗂️ Estructura de carpetas (arquitectura)

```
src/main/java/edu/eci/arsw/blueprints
  ├── model/         # Entidades de dominio: Blueprint, Point
  ├── persistence/   # Interfaz + repositorios (InMemory, Postgres)
  │    └── impl/     # Implementaciones concretas
  ├── services/      # Lógica de negocio y orquestación
  ├── filters/       # Filtros de procesamiento (Identity, Redundancy, Undersampling)
  ├── controllers/   # REST Controllers (BlueprintsAPIController)
  └── config/        # Configuración (Swagger/OpenAPI, etc.)
```

> Esta separación sigue el patrón **capas lógicas** (modelo, persistencia, servicios, controladores), facilitando la extensión hacia nuevas tecnologías o fuentes de datos.

---

## 📖 Actividades del laboratorio

### 1. Familiarización con el código base
- Revisa el paquete `model` con las clases `Blueprint` y `Point`.  
- Entiende la capa `persistence` con `InMemoryBlueprintPersistence`.  
- Analiza la capa `services` (`BlueprintsServices`) y el controlador `BlueprintsAPIController`.

### 2. Migración a persistencia en PostgreSQL
- Configura una base de datos PostgreSQL (puedes usar Docker).  
- Implementa un nuevo repositorio `PostgresBlueprintPersistence` que reemplace la versión en memoria.  
- Mantén el contrato de la interfaz `BlueprintPersistence`.  

### 3. Buenas prácticas de API REST
- Cambia el path base de los controladores a `/api/v1/blueprints`.  
- Usa **códigos HTTP** correctos:  
  - `200 OK` (consultas exitosas).  
  - `201 Created` (creación).  
  - `202 Accepted` (actualizaciones).  
  - `400 Bad Request` (datos inválidos).  
  - `404 Not Found` (recurso inexistente).  
- Implementa una clase genérica de respuesta uniforme:
  ```java
  public record ApiResponse<T>(int code, String message, T data) {}
  ```
  Ejemplo JSON:
  ```json
  {
    "code": 200,
    "message": "execute ok",
    "data": { "author": "john", "name": "house", "points": [...] }
  }
  ```

### 4. OpenAPI / Swagger
- Configura `springdoc-openapi` en el proyecto.  
- Expón documentación automática en `/swagger-ui.html`.  
- Anota endpoints con `@Operation` y `@ApiResponse`.

### 5. Filtros de *Blueprints*
- Implementa filtros:
  - **RedundancyFilter**: elimina puntos duplicados consecutivos.  
  - **UndersamplingFilter**: conserva 1 de cada 2 puntos.  
- Activa los filtros mediante perfiles de Spring (`redundancy`, `undersampling`).  

---

## ✅ Entregables

1. Repositorio en GitHub con:  
   - Código fuente actualizado.  
   - Configuración PostgreSQL (`application.yml` o script SQL).  
   - Swagger/OpenAPI habilitado.  
   - Clase `ApiResponse<T>` implementada.  

2. Documentación:  
   - Informe de laboratorio con instrucciones claras.  
   - Evidencia de consultas en Swagger UI y evidencia de mensajes en la base de datos.  
   - Breve explicación de buenas prácticas aplicadas.  

---

## 📊 Criterios de evaluación

## 🗂️ Estructura de carpetas (actualizada)

```
src/main/java/edu/eci/arsw/blueprints
  ├── model/         # Entidades de dominio: Blueprint, Point
  ├── persistence/   # Interfaz y repositorios (InMemory, Postgres)
  ├── services/      # Lógica de negocio y orquestación
  ├── filters/       # Filtros de procesamiento (Identity, Redundancy, Undersampling)
  ├── controllers/   # REST Controllers (BlueprintsAPIController)
  ├── dto/           # Data Transfer Objects y mapeadores
  └── config/        # Configuración (Swagger/OpenAPI, etc.)
src/main/resources
  ├── application.properties  # Configuración principal
  └── application.yml        # Configuración alternativa
src/test/java/edu/eci/arsw/blueprints
  ├── BlueprintsAPIControllerTest.java
  ├── BlueprintsFilterTest.java
  ├── BlueprintsServicesTest.java
  └── BlueprintsSmokeTest.java
Dockerfile                    # Build personalizado para contenedor
pom.xml                       # Dependencias y plugins Maven
```
| Criterio | Peso |
|----------|------|
Con Actuator habilitado, puedes acceder a:

- [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health) — Estado de la aplicación
- [http://localhost:8080/actuator/info](http://localhost:8080/actuator/info) — Información general
- [http://localhost:8080/actuator/metrics](http://localhost:8080/actuator/metrics) — Métricas generales
- [http://localhost:8080/actuator](http://localhost:8080/actuator) — Lista de todos los endpoints disponibles
| Diseño de API (versionamiento, DTOs, ApiResponse) | 25% |
| Migración a PostgreSQL (repositorio y persistencia correcta) | 25% |
El proyecto requiere una base de datos PostgreSQL corriendo en `localhost:5432` con:

- Base de datos: `mi_basedatos`
- Usuario: `admin`
- Contraseña: `admin123`

Puedes levantar una instancia local rápidamente con Docker:

```bash
docker run --name blueprints-postgres -e POSTGRES_DB=mi_basedatos -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin123 -p 5432:5432 -d postgres:16
```

La configuración puede ser modificada en `src/main/resources/application.properties` o `application.yml`.
| Uso correcto de códigos HTTP y control de errores | 20% |
| Documentación con OpenAPI/Swagger + README | 15% |
Puedes construir la imagen usando Spring Boot Buildpacks o el Dockerfile:

**Opción 1: Buildpacks (recomendado)**

```bash
mvn spring-boot:build-image
```
Esto generará una imagen llamada `blueprints-api:latest`.

**Opción 2: Dockerfile clásico**

```bash
docker build -t blueprints-api:latest .
```

Para ejecutar el contenedor:

```bash
docker run -p 8080:8080 --name blueprints-api --link blueprints-postgres:postgres -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/mi_basedatos -e SPRING_DATASOURCE_USERNAME=admin -e SPRING_DATASOURCE_PASSWORD=admin123 blueprints-api:latest
```

> Si usas Docker Compose, asegúrate de definir ambos servicios (app y postgres) y las variables de entorno.
| Pruebas básicas (unitarias o de integración) | 15% |

**Bonus**:  

- Imagen de contenedor (`spring-boot:build-image`).  
- Métricas con Actuator.  