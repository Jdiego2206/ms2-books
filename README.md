# ReadMe — MS2 Books

API REST para la gestión de libros, categorías y transacciones de ReadMe.

### Desarrollo local

**Variables de entorno**

```bash
cp .env.example .env
```

Edita `.env` si necesitas cambiar credenciales o puertos.

**Base de datos**

```bash
docker compose up -d
```

Levanta MySQL en el puerto 3306. Las tablas se crean automáticamente al arrancar la app. Las categorías se insertan solas si la tabla está vacía.

**Requisitos**

- Java 17 

**Correr la app**

```bash
export $(grep -v '^#' .env | xargs)
./mvnw spring-boot:run
```

El `export` carga las variables del `.env` en la terminal. Solo es necesario una vez por sesión.

La API queda disponible en `http://localhost:8002`.
Swagger UI en `http://localhost:8002/swagger-ui.html`.
