# BookMyShow Clone – Spring Boot Backend

This is the Spring Boot (PostgreSQL) backend for the BookMyShow clone.
## Requirements

- Java 17+
- Maven 3.8+
- PostgreSQL (local or cloud-managed)
- Environment variables for JWT, Stripe, Resend (see below)

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

## Run locally

1. Create a PostgreSQL database and run migrations (Flyway runs on startup):

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

4. API base URL: `http://localhost:8080/api` (same paths as Express: `/api/users`, `/api/movies`, etc.).

## Point frontend to Spring Boot

In the React app, set:

- `REACT_APP_API_URL=http://localhost:8080/api`

Then run the client; it will use the Spring Boot backend.

## Migrating data from MongoDB

If you need to move existing data from the Express/MongoDB backend to PostgreSQL, see [docs/MONGO_TO_POSTGRES_MIGRATION.md](docs/MONGO_TO_POSTGRES_MIGRATION.md) for a one-time migration approach.

## API (same as Express)

- **Users**: `POST /api/users/register`, `POST /api/users/login`, `GET /api/users/get-current-user`, `PATCH /api/users/forgetpassword`, `PATCH /api/users/resetpassword`
- **Movies**: CRUD at `/api/movies` and `/api/movies/:id`
- **Theatres**: `/api/theatres`, `/api/theatres/get-all-theatres`, `/api/theatres/get-all-theatres-by-owner`
- **Shows**: `/api/shows`, `/api/shows/get-all-shows-by-theatre`, `/api/shows/get-all-theatres-by-movie`, `/api/shows/:id`
- **Bookings**: `POST /api/bookings/make-payment`, `POST /api/bookings/book-show`, `GET /api/bookings/all-bookings`

Responses use the same shape: `{ success, message, data? }`. Entities expose `_id` for compatibility with the existing frontend.
