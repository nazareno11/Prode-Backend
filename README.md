# Prode Mundial – Backend

API REST para un sistema de pronósticos (prode) del Mundial. Permite a los usuarios registrarse, predecir resultados de partidos, formar grupos privados con amigos y competir en un ranking global, mientras los datos de equipos y partidos se sincronizan automáticamente desde una API externa de fútbol.

Este documento describe la arquitectura, el modelo de datos, los endpoints disponibles y cómo levantar el proyecto localmente, incluyendo la documentación interactiva generada con **Swagger / OpenAPI**.

---

## Tabla de contenidos

1. [Stack tecnológico](#stack-tecnológico)
2. [Arquitectura del proyecto](#arquitectura-del-proyecto)
3. [Modelo de datos](#modelo-de-datos)
4. [Configuración y puesta en marcha](#configuración-y-puesta-en-marcha)
5. [Autenticación y seguridad](#autenticación-y-seguridad)
6. [Documentación con Swagger / OpenAPI](#documentación-con-swagger--openapi)
7. [Endpoints de la API](#endpoints-de-la-api)
8. [Manejo de errores](#manejo-de-errores)
9. [Integración con Football-Data.org](#integración-con-football-dataorg)
10. [Frontend](#frontend)

---

## Stack tecnológico

| Componente | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 4 (Web MVC) |
| Persistencia | Spring Data JPA / Hibernate |
| Base de datos | MySQL |
| Seguridad | Spring Security + JWT (`io.jsonwebtoken` / JJWT) |
| Documentación de API | springdoc-openapi (Swagger UI) |
| Build | Maven (Maven Wrapper incluido) |
| Boilerplate | Lombok |
| Integración externa | [football-data.org](https://www.football-data.org/) API v4, vía `RestTemplate` |
| Tareas programadas | Spring `@Scheduled` |

---

## Arquitectura del proyecto

El backend está organizado **por dominio/feature** (no por capa técnica clásica tipo "todos los controllers juntos"). Cada paquete de dominio agrupa sus propios `controller`, `dto`, `entity`/`model`, `repository` y `service`:

```
src/main/java/com/prog4_tpi_grupo1/backend/
├── auth/            # Registro, login, JWT, UserDetails
├── usuario/         # Perfil de usuario y avatares
├── equipo/          # Equipos participantes del Mundial
├── fecha/           # Jornadas / fechas de partidos
├── partido/         # Partidos individuales
├── pronostico/      # Pronósticos de los usuarios y puntuación
├── grupo/           # Grupos privados entre usuarios (estilo "liga")
├── ranking/         # Tabla de posiciones global
├── footballdata/    # Cliente + sincronización con la API externa
└── shared/          # Configuración transversal (seguridad, excepciones, OpenAPI)
```

> 📌 **Nota didáctica:** esta organización "por feature" facilita ubicar todo lo relacionado a una funcionalidad (por ejemplo, todo lo de `pronostico`) en una sola carpeta, en vez de tener que saltar entre capas (`controllers/`, `services/`, `repositories/`) repartidas por todo el proyecto.

### Resumen de cada módulo

- **`auth`**: registro e inicio de sesión, generación y validación de JWT, carga de usuario para Spring Security.
- **`usuario`**: perfil del usuario autenticado (`/api/users/me`) y gestión de su avatar.
- **`equipo`**: catálogo de selecciones/equipos participantes (de solo lectura desde la API, se completa por sincronización).
- **`fecha`**: agrupación de partidos por jornada (`matchday`) y su estado (`PROGRAMADA`, `EN_JUEGO`, `FINALIZADA`).
- **`partido`**: cada partido individual, con equipos, resultado y estado.
- **`pronostico`**: predicción de un usuario para un partido (goles local/visitante) y el cálculo de puntos obtenidos.
- **`grupo`**: grupos privados con código de invitación, donde los usuarios compiten entre sí.
- **`ranking`**: ranking global de usuarios según puntos acumulados.
- **`footballdata`**: integración con la API externa football-data.org para traer equipos y partidos reales del Mundial, y mantenerlos actualizados.
- **`shared`**: `SecurityConfig`, `JwtAuthenticationFilter`, `GlobalExceptionHandler`, excepciones personalizadas y configuración de OpenAPI/Swagger.

---

## Modelo de datos

### Entidades principales

**`Usuario`** (`usuarios`)
| Campo | Tipo | Detalle |
|---|---|---|
| id | Long | PK autogenerada |
| username | String | único |
| email | String | único, usado como identificador para login y JWT |
| password | String | hasheada con BCrypt |
| rol | Enum `Rol` | `USER` \| `ADMIN` |
| puntosTotales | Integer | puntos acumulados por pronósticos |
| plenosAcertados | Integer | cantidad de resultados exactos |
| avatar | Enum `Avatar` | avatar elegido (default: `DEFAULT`) |
| grupos | Set\<Grupo\> | relación N:M con `Grupo` |

**`Grupo`** (`grupos`)
| Campo | Tipo | Detalle |
|---|---|---|
| id | Long | PK |
| nombre | String | nombre del grupo |
| codigoInvitacion | String | único, usado para que otros usuarios se unan |
| creador | Usuario | relación N:1 |
| miembros | Set\<Usuario\> | relación N:M (tabla intermedia `grupo_usuarios`) |

**`Equipo`** (`equipos`): `id`, `externalId` (id en la API externa), `nombre`, `abreviatura`, `escudo`.

**`Fecha`** (`fechas`): `id`, `nombre`, `matchday`, `grupo` (grupo de la fase, ej. "Grupo A"), `estado` (`EstadoFecha`), y la lista de `partidos` asociados.

**`Partido`**: equipo local y visitante (relación a `Equipo`), `fechaHora`, `estado` (`EstadoPartido`: `POR_JUGARSE`, `EN_JUEGO`, `FINALIZADO`), `matchday`, `grupo`, `stage`, resultado local/visitante, y relación a `Fecha`.

**`Pronostico`** (`pronosticos`): `usuario` + `partido` (con restricción **única** por par usuario-partido, para que no se pueda pronosticar dos veces el mismo partido), `golesLocal`, `golesVisitante`, `fechaRegistro` (autogestionada con `@PrePersist`/`@PreUpdate`).

### Enums

| Enum | Valores |
|---|---|
| `Rol` | `USER`, `ADMIN` |
| `EstadoFecha` | `PROGRAMADA`, `EN_JUEGO`, `FINALIZADA` |
| `EstadoPartido` | `POR_JUGARSE`, `EN_JUEGO`, `FINALIZADO` |
| `Avatar` | `DEFAULT`, `AVATAR_1` … `AVATAR_5` (cada uno con su URL de imagen) |

---

## Configuración y puesta en marcha

### Requisitos previos

- Java 21
- MySQL en ejecución
- Maven (o usar el wrapper incluido `./mvnw` / `mvnw.cmd`)

### Variables de configuración (`src/main/resources/application.properties`)

```properties
spring.application.name=backend
spring.profiles.active=local

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

football.api.url=https://api.football-data.org/v4
football.api.token=<TOKEN_DE_FOOTBALL_DATA_ORG>
```

> ⚠️ El proyecto usa el perfil `local` (`spring.profiles.active=local`). Las credenciales de conexión a MySQL (usuario, password, URL de la base) deberían definirse en un `application-local.properties` (no incluido en este resumen) o como variables de entorno, evitando subir credenciales reales al repositorio.

### Pasos para levantar el proyecto

```bash
# 1. Clonar el repositorio
git clone <url-del-repo>
cd backend

# 2. Levantar la base de datos MySQL y crear el esquema (o dejar que Hibernate lo actualice solo)
#    spring.jpa.hibernate.ddl-auto=update se encarga de crear/actualizar las tablas

# 3. Ejecutar la aplicación
./mvnw spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

---

## Autenticación y seguridad

- La seguridad está implementada con **Spring Security** + **JWT** (librería `io.jsonwebtoken`).
- El flujo es el clásico de **stateless authentication**:
  1. El usuario se registra (`POST /api/auth/register`) o inicia sesión (`POST /api/auth/login`).
  2. El backend responde con un token JWT (`AuthResponse`).
  3. El cliente debe enviar ese token en cada request protegido, en el header:
     ```
     Authorization: Bearer <token>
     ```
  4. Un filtro (`JwtAuthenticationFilter`) intercepta cada request, valida el token y carga el usuario autenticado en el contexto de seguridad de Spring.
- El token se firma con HMAC-SHA y **expira a las 24 horas**.
- Las contraseñas se almacenan hasheadas con **BCrypt**.
- La política de sesión es `STATELESS` (no se guarda sesión en el servidor; toda la información de autenticación viaja en el token).

### Endpoints públicos (no requieren token)

- `POST /api/auth/login`
- `POST /api/auth/register`
- `GET /api/avatars`
- `/swagger-ui/**`, `/v3/api-docs/**` (la documentación misma es pública)

**Todo el resto de los endpoints requiere JWT válido.**

### CORS

Está habilitado explícitamente para `http://localhost:5173`, que es el puerto por defecto del servidor de desarrollo de **Vite** (usado por el frontend en React). Se permiten los métodos `GET`, `POST`, `PUT`, `DELETE`, `PATCH`, `OPTIONS`, todos los headers y el envío de credenciales.

---

## Documentación con Swagger / OpenAPI

El proyecto expone documentación interactiva generada automáticamente con **springdoc-openapi**, a partir de las anotaciones `@Tag`, `@Operation`, `@ApiResponse` y `@SecurityRequirement` presentes en los controllers.

- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **Especificación OpenAPI (JSON):** `http://localhost:8080/v3/api-docs`

Desde Swagger UI se puede:

- Ver todos los endpoints agrupados por **tag** (Autenticación, Usuarios, Equipos, Fechas, Partidos, Pronósticos, Grupos, Ranking, Administración Football Data).
- Probar cada endpoint directamente desde el navegador ("Try it out").
- Autenticarse una sola vez con el botón **Authorize** pegando el JWT obtenido del login, para que Swagger lo envíe automáticamente en los endpoints protegidos (esquema `bearerAuth` definido en `OpenApiConfig`).

<img width="1300" height="1038" alt="1" src="https://github.com/user-attachments/assets/d75173f2-1f0b-4ed3-a287-53ef6317ab94" />
<img width="1294" height="831" alt="2" src="https://github.com/user-attachments/assets/4dbcf91e-ead5-411e-aa90-3cd090356abf" />
<img width="1289" height="938" alt="3" src="https://github.com/user-attachments/assets/5817be47-8446-4ba0-b317-1d32bce32617" />
Endpoint expandido:
<img width="1287" height="803" alt="4" src="https://github.com/user-attachments/assets/13d99e7d-e794-4987-86d9-269af507d24e" />

---

## Endpoints de la API

> 🔒 = requiere header `Authorization: Bearer <token>`

### Autenticación — `/api/auth`

| Método | Endpoint | Descripción | Body | Respuesta |
|---|---|---|---|---|
| POST | `/api/auth/register` | Crea una cuenta nueva y devuelve un JWT | `RegisterRequest` | `201` `AuthResponse` |
| POST | `/api/auth/login` | Autentica y devuelve un JWT | `LoginRequest` | `200` `AuthResponse` / `401` si credenciales inválidas |

```jsonc
// RegisterRequest
{ "username": "string", "email": "user@mail.com", "password": "string" }

// LoginRequest
{ "email": "user@mail.com", "password": "string" }

// AuthResponse
{ "token": "eyJ...", "type": "Bearer", "username": "string" }
```

### Usuarios — `/api/users` 🔒

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/users/me` | Devuelve el perfil del usuario autenticado |
| PATCH | `/api/users/me/avatar` | Actualiza el avatar del usuario |

```jsonc
// UserProfileResponse (GET /me)
{
  "id": 1,
  "username": "string",
  "email": "user@mail.com",
  "puntosTotales": 10,
  "plenosAcertados": 2,
  "cantidadPronosticos": 5,
  "cantidadGrupos": 1,
  "ranking": 3,
  "avatar": "AVATAR_1"
}

// UpdateAvatarRequest (PATCH /me/avatar)
{ "avatar": "AVATAR_2" }
```

### Avatares — `/api/avatars` (público)

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/avatars` | Lista los avatares disponibles (nombre + URL de imagen) |

### Equipos — `/api/equipos`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/equipos` | Lista todos los equipos registrados |

### Fechas — `/api/fechas`

| Método | Endpoint | Descripción | Query params |
|---|---|---|---|
| GET | `/api/fechas` | Lista todas las fechas, o filtra por estado | `estado` (opcional, ej. `FINALIZADA`) |

### Partidos — `/api/partidos`

| Método | Endpoint | Descripción | Query params |
|---|---|---|---|
| GET | `/api/partidos` | Lista todos los partidos, o filtra por fecha | `fechaId` (opcional) |

### Pronósticos — `/api/pronosticos` 🔒

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/pronosticos` | Crea o modifica el pronóstico del usuario para un partido |
| GET | `/api/pronosticos/mis-pronosticos` | Lista los pronósticos propios (filtro opcional `estado`) |
| GET | `/api/pronosticos/partido/{partidoId}/terceros` | Consulta los pronósticos de otros usuarios para un partido |

```jsonc
// PronosticoRequestDTO
{ "partidoId": 1, "golesLocal": 2, "golesVisitante": 1 }

// PronosticoResponseDTO
{
  "id": 10,
  "usuarioId": 1,
  "nombreUsuario": "string",
  "partidoId": 1,
  "detallePartido": "string",
  "golesLocal": 2,
  "golesVisitante": 1,
  "fechaRegistro": "2026-06-26T10:00:00"
}
```

> 💡 Por la restricción única `(usuario_id, partido_id)` en la entidad `Pronostico`, un usuario solo puede tener **un** pronóstico por partido; el mismo endpoint `POST` sirve tanto para crear como para modificarlo.

### Grupos — `/api/grupos` 🔒

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/grupos` | Crea un grupo nuevo (el creador queda asociado) |
| POST | `/api/grupos/unirse` | Une al usuario autenticado a un grupo mediante código de invitación |
| GET | `/api/grupos/mis-grupos` | Lista los grupos a los que pertenece el usuario |
| GET | `/api/grupos/{grupoId}/ranking` | Ranking interno de un grupo específico |

```jsonc
// CrearGrupoRequestDTO
{ "nombre": "string" }

// UnirseGrupoRequestDTO
{ "codigoInvitacion": "string" }

// GrupoResponseDTO
{ "id": 1, "nombre": "string", "codigoInvitacion": "ABC123", "creador": "string", "cantidadMiembros": 4 }

// RankingGrupoResponseDTO
{ "usuarioId": 1, "username": "string", "puntosTotales": 10, "plenosAcertados": 2, "posicion": 1 }
```

### Ranking — `/api/ranking` 🔒

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/ranking` | Ranking global de todos los usuarios |

### Administración Football Data — `/api/admin/football`

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/admin/football/sync-teams` | Dispara la sincronización manual de equipos |
| POST | `/api/admin/football/sync-matches` | Dispara la sincronización manual de partidos |
| POST | `/api/admin/football/sync-fechas` | Dispara la sincronización manual de fechas |

> ⚠️ Estos endpoints **no tienen restricción explícita por rol** en el código actual (más allá de requerir estar autenticado, por la regla general `anyRequest().authenticated()` de `SecurityConfig`); están pensados para uso administrativo/interno.

---

## Manejo de errores

Las respuestas de error siguen el estándar **RFC 9457 (`ProblemDetail`)**, manejadas centralmente por `GlobalExceptionHandler`:

| Situación | Status | Estructura |
|---|---|---|
| Excepción de negocio personalizada (`CustomException` y sus subclases: `BadRequestException`, `ConflictException`, `NotFoundException`, `UnauthorizedException`) | según la excepción | `{ "title", "detail", "errors": [...] }` (opcional) |
| Validación de datos de entrada (`@Valid` falla) | `400` | `{ "title": "Error de validación", "detail": "Revise los campos indicados.", "errors": ["campo: mensaje", ...] }` |
| Credenciales inválidas en login | `401` | `{ "title": "Credenciales inválidas", "detail": "Email o contraseña incorrectos." }` |
| Cualquier otro error no controlado | `500` | `{ "title": "Error interno del servidor", "detail": "Ocurrió un error inesperado", "errors": ["Contacte al administrador"] }` |

---

## Integración con Football-Data.org

El módulo `footballdata` se encarga de mantener sincronizada la base de datos local con la API pública [football-data.org](https://www.football-data.org/) (competencia configurada para el Mundial):

- **`FootballDataClient`**: cliente HTTP (vía `RestTemplate`) que consulta equipos y partidos a la API externa, usando el token configurado en `football.api.token`.
- **`FootballDataService`**: orquesta la sincronización — `syncTeams()`, `syncMatches()`, `syncFechas()` — mapeando los DTOs externos (`TeamDTO`, `MatchDTO`, etc.) a las entidades propias (`Equipo`, `Partido`, `Fecha`).
- **`FootballDataScheduler`**: ejecuta automáticamente `syncMatches()` **cada 5 minutos** (`@Scheduled(cron = "0 */5 * * * *")`), para mantener actualizados los resultados y estados de los partidos en curso.
- Además, existen endpoints administrativos (`/api/admin/football/*`) para forzar la sincronización manualmente.

---

## Frontend

El consumo de esta API está pensado para un frontend desarrollado en **React + Vite** (de ahí que el CORS esté habilitado específicamente para `http://localhost:5173`, puerto por defecto del servidor de desarrollo de Vite). El frontend se autentica contra `/api/auth/login` o `/api/auth/register`, guarda el JWT recibido, y lo envía en el header `Authorization` para consumir el resto de los endpoints descritos en este documento.
https://github.com/nazareno11/Prode-Frontend.git
