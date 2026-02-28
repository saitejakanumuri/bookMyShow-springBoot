
## Configuration

Set these in the environment or in `application-dev.yml` (do not commit secrets):

| Variable | Description |
|----------|-------------|
| `DATABASE_URL` | JDBC URL, e.g. `jdbc:postgresql://localhost:5432/bookmyshow` or cloud URL with `?sslmode=require` |
| `DATABASE_USERNAME` | DB user |
| `DATABASE_PASSWORD` | DB password |
| `JWT_SECRET` | Same secret as Express backend if you need token compatibility |
| `FRONTEND_URL` | Allowed CORS origin, e.g. `http://localhost:3000` |
| `STRIPE_KEY` | Stripe secret key for payments |
| `RESEND_API_KEY` | Resend API key for emails (OTP, ticket) |
| `PORT` | Server port (default 8080) |

```bash
To make it work in your laptop

config:
1. In client folder
 create .env
 REACT_APP_STRIPE_PUBLISHABLE_KEY=xx

2. In spring-backend/src/main/resources folder
create application.yml

  spring:
  application:
    name: bookmyshow-backend
  datasource:
    url: ${DATABASE_URL:} 
    username: ${DATABASE_USERNAME:postgres}
    password: ${DATABASE_PASSWORD:}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        default_schema: public
    open-in-view: false

server:
  port: ${PORT:8081}

app:
  jwt-secret: ${JWT_SECRET:xxx}
  jwt-expiration-ms: 86400000
  frontend-url: ${FRONTEND_URL:http://localhost:3000}
  stripe-key: ${STRIPE_KEY:}
  resend-api-key: ${RESEND_API_KEY:}

```

## Run locally

1. Create a PostgreSQL database 

   ```bash
   createdb bookmyshow
   ```

2. Set environment variables (or use `application-dev.yml`).

3. Build and run:

   ```bash
   mvn spring-boot:run
   ```

   Or with profile:

   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

4. API base URL: `http://localhost:8081/api` 

## Point frontend to Spring Boot

In the React app, set:

- `REACT_APP_API_URL=http://localhost:8081/api`

Then run the client; it will use the Spring Boot backend.

## APIs

- **Users**: `POST /api/users/register`, `POST /api/users/login`, `GET /api/users/get-current-user`, `PATCH /api/users/forgetpassword`, `PATCH /api/users/resetpassword`
- **Movies**: CRUD at `/api/movies` and `/api/movies/:id`
- **Theatres**: `/api/theatres`, `/api/theatres/get-all-theatres`, `/api/theatres/get-all-theatres-by-owner`
- **Shows**: `/api/shows`, `/api/shows/get-all-shows-by-theatre`, `/api/shows/get-all-theatres-by-movie`, `/api/shows/:id`
- **Bookings**: `POST /api/bookings/make-payment`, `POST /api/bookings/book-show`, `GET /api/bookings/all-bookings`

Responses use the same shape: `{ success, message, data? }`. Entities expose `_id` for compatibility with the existing frontend.
