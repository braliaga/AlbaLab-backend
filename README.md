# AlbaLab · Backend (microservicios Spring Boot)

Backend de la app móvil [AlbaLab](https://github.com/braliaga/AlbaLab) para **CORPALIV / Alba Lab**. Son tres microservicios de negocio y un API Gateway con JWT, todos en **Spring Boot 4.0.5 (Java 21)** con **MySQL 8.4**, que se levantan juntos con **Docker Compose**.

Proyecto de la asignatura DSY1105 · Desarrollo de Aplicaciones Móviles (Duoc UC) · Evaluación Parcial 4.

## Integrantes

- Branco Aliaga
- Paulo Catalán

## Arquitectura

```
App Android ──HTTP──▶ api-gateway :9090 ──┬──▶ ms-auth         :9093 ──▶ MySQL bd_auth
                     (valida JWT HS256)   ├──▶ ms-vinculacion  :9092 ──▶ MySQL bd_vinculacion
                                          └──▶ ms-contenidos   :9091 ──▶ MySQL bd_contenidos
```

| Servicio | Puerto | Responsabilidad | Base de datos |
|---|---|---|---|
| `api-gateway` | 9090 | Punto de entrada único. Enruta por ruta y valida el JWT (Spring Cloud Gateway + OAuth2 Resource Server) | — |
| `ms-auth` | 9093 | Login de gestores de vinculación. Contraseñas con **BCrypt**, emite JWT (1 h) | `bd_auth` |
| `ms-vinculacion` | 9092 | Solicitudes de colaboración: CRUD, cambio de estado e historial | `bd_vinculacion` |
| `ms-contenidos` | 9091 | Programas, casos de éxito, catálogo, indicadores y destino de aportes | `bd_contenidos` |

- Una sola instancia de MySQL con **una base por servicio**: cada microservicio solo accede a la suya (propuesta técnica, punto 4.3.3).
- Paquetes por servicio: `controller`, `service`, `repository`, `model`, `dto`, `exception`, `config`.
- Validación con Bean Validation y errores con un formato común (`ApiErrorResponse`: `status`, `message`, `campos`).

### Reglas de negocio implementadas

- Una solicitud exige nombre, al menos **un medio de contacto** (teléfono o correo), tipo de ayuda y aceptación de contacto.
- Toda solicitud nueva queda en estado **`RECIBIDA`** con fecha de creación automática.
- Estados posibles: `RECIBIDA`, `EN_REVISION`, `CONTACTADO`, `CONCRETADO`, `DESCARTADO`. `CONCRETADO` y `DESCARTADO` son finales y no admiten cambios.
- Cada cambio de estado queda en `historial_solicitud` (estado anterior, nuevo, observación y fecha).
- Los visitantes pueden **crear** solicitudes y **leer** contenido sin cuenta; listar, editar, cambiar estado y eliminar solicitudes exige el **token del gestor**.

## Cómo ejecutar

Requisito: **Docker Desktop** abierto (no hace falta instalar Java, Maven ni MySQL).

```
docker compose up --build -d     # construye y levanta MySQL + los 4 servicios
docker compose ps                # estado de los contenedores
docker compose logs -f ms-vinculacion
docker compose down              # apaga (los datos quedan en el volumen mysql-data)
docker compose down -v           # apaga y borra los datos
```

- Cada `Dockerfile` es **multi-etapa**: compila el jar con Maven dentro del contenedor y lo corre sobre un JRE liviano.
- Compose espera a que MySQL esté sano (`healthcheck`) antes de arrancar los servicios.
- Al iniciar por primera vez se cargan los datos: el **gestor de prueba** en `ms-auth` y el contenido institucional en `ms-contenidos` (datos ficticios o anonimizados).

**Gestor de prueba:** `gestor@albalab.cl` / `Alba2026!`

Desarrollo sin Docker (con un MySQL local en el puerto 3306): `./mvnw spring-boot:run` dentro de cada módulo.

## Endpoints

Todos se consumen a través del gateway: `http://localhost:9090` (desde el emulador Android: `http://10.0.2.2:9090`).

### ms-auth

| Método | Endpoint | Descripción | Token |
|---|---|---|---|
| POST | `/auth/login` | `{email, password}` → `{token, email, nombre, expiraEnSegundos}` | No |
| POST | `/auth/register` | Crea un gestor `{email, nombre, password}` → 201 | No |

### ms-vinculacion

| Método | Endpoint | Descripción | Token |
|---|---|---|---|
| POST | `/solicitudes` | Crea una solicitud (estado `RECIBIDA`) → 201 | No |
| GET | `/solicitudes` | Lista todas, de la más nueva a la más antigua | Sí |
| GET | `/solicitudes/{id}` | Detalle | Sí |
| PUT | `/solicitudes/{id}` | Actualiza `{telefono, contactoPreferido, recibeNovedades}` | Sí |
| PATCH | `/solicitudes/{id}/estado` | Cambia estado `{estado, observacion}` y registra historial | Sí |
| GET | `/solicitudes/{id}/historial` | Historial de cambios de estado | Sí |
| DELETE | `/solicitudes/{id}` | Elimina la solicitud y su historial → 204 | Sí |
| GET | `/solicitudes/{id}/exists` | `true` / `false` | Sí |

### ms-contenidos

CRUD completo para cada recurso: `/programas`, `/casos`, `/productos`, `/indicadores`, `/destinos`.

| Método | Endpoint | Token |
|---|---|---|
| GET | `/{recurso}` y `/{recurso}/{id}` | No |
| POST | `/{recurso}` → 201 | Sí |
| PUT | `/{recurso}/{id}` | Sí |
| DELETE | `/{recurso}/{id}` → 204 | Sí |

### Ejemplos con curl

```bash
# Login
curl -X POST http://localhost:9090/auth/login -H "Content-Type: application/json" \
  -d '{"email":"gestor@albalab.cl","password":"Alba2026!"}'

# Crear solicitud (visitante, sin token)
curl -X POST http://localhost:9090/solicitudes -H "Content-Type: application/json" \
  -d '{"nombre":"Ana Perez","telefono":"912345678","correo":"ana@mail.cl","direccion":"Calle 123",
       "personas":3,"fechaContacto":"15/12/2026","contactoPreferido":"CORREO","tipoAyuda":"DONACION",
       "aceptaContacto":true,"recibeNovedades":false}'

# Listar (gestor)
curl http://localhost:9090/solicitudes -H "Authorization: Bearer <TOKEN>"

# Cambiar estado (gestor)
curl -X PATCH http://localhost:9090/solicitudes/1/estado -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" -d '{"estado":"CONTACTADO","observacion":"Llamada realizada"}'
```

### Swagger

Cada servicio publica su documentación OpenAPI (acceso directo a su puerto). Los puertos 9091–9093 quedan expuestos **solo para desarrollo y revisión**: al entrar directo a un servicio no pasa por el JWT del gateway. La app usa siempre el gateway (9090).

- ms-contenidos: <http://localhost:9091/doc/swagger-ui.html>
- ms-vinculacion: <http://localhost:9092/doc/swagger-ui.html>
- ms-auth: <http://localhost:9093/doc/swagger-ui.html>

## Tests

JUnit 5 + Mockito en services y controllers (MockMvc standalone, sin levantar Spring ni MySQL):

```
cd ms-vinculacion && ./mvnw test    # 20 pruebas
cd ms-auth && ./mvnw test           # 8 pruebas
cd ms-contenidos && ./mvnw test     # 12 pruebas
```

## Seguridad

- Contraseñas almacenadas solo como hash **BCrypt**.
- JWT firmado con **HS256** y una clave compartida entre `ms-auth` y el gateway (`JWT_SECRET` en `docker-compose.yml`).
- El login responde el mismo mensaje para correo inexistente y contraseña errónea (no revela qué cuentas existen).
- La clave JWT y la contraseña de MySQL de `docker-compose.yml` son valores de desarrollo para el proyecto académico; en producción irían en variables de entorno o un gestor de secretos.
