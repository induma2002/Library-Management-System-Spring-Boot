# Library Management API

Comprehensive Spring Boot service that manages catalog, members, loans, reservations, and fines for a library. It exposes a REST API backed by Spring Data JPA and H2 (in-memory by default) and ships with Swagger UI for live exploration.

## Features
- CRUD operations for books and members with validation and business safeguards.
- Loan processing with reservation queues, overdue detection, and automatic fine creation.
- Reservation lifecycle management (place, fulfil, expire holds).
- Fine tracking with settlement/waiver support and dashboard metrics summary.
- Global exception handling with concise JSON error envelopes.
- Seed data for quick local testing and auto-generated OpenAPI docs.

## Tech Stack
- Java 17, Spring Boot 3.5
- Spring Data JPA, H2 (in-memory demo database)
- SpringDoc OpenAPI for Swagger UI
- Maven Wrapper for build orchestration

## Getting Started

### Prerequisites
- Java 17 JDK
- Internet access for Maven dependency download (first build only)

### Run Locally
```bash
./mvnw spring-boot:run
```

Application defaults:
- Base URL: `http://localhost:8080`
- H2 console: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:librarydb`)

### API Documentation
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI spec: `http://localhost:8080/v3/api-docs`

### Seed Data
Two members (`MEM-001`, `MEM-002`) and popular software engineering titles are inserted on startup for quick experimentation.

### Running Tests
```bash
./mvnw test
```

## Docker

The provided multi-stage Dockerfile builds the fat jar and runs it on a lightweight JRE image.

Build the image:
```bash
docker build -t library-api .
```

Run the container:
```bash
docker run --rm -p 8080:8080 library-api
```

Override Spring properties (for example, to switch databases) with standard environment variables, e.g.:
```bash
docker run --rm -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/library \
  -e SPRING_DATASOURCE_USERNAME=app \
  -e SPRING_DATASOURCE_PASSWORD=secret \
  library-api
```

## Example Requests

Create a book:
```bash
curl -X POST http://localhost:8080/api/books \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Effective Java",
    "author": "Joshua Bloch",
    "isbn": "9780134685991",
    "publisher": "Addison-Wesley",
    "category": "Software",
    "language": "EN",
    "publicationYear": 2018,
    "totalCopies": 4
  }'
```

Checkout a book:
```bash
curl -X POST http://localhost:8080/api/loans \
  -H 'Content-Type: application/json' \
  -d '{
    "bookId": 1,
    "memberId": 1,
    "loanPeriodDays": 14
  }'
```

List pending fines for a member:
```bash
curl http://localhost:8080/api/fines/members/1?status=PENDING
```

## Project Structure
- `src/main/java` – domain entities, repositories, services, controllers, configuration.
- `src/main/resources` – application properties.
- `src/test/java` – integration tests for critical flows.
- `Dockerfile` – container build definition.

## Extending
- Swap H2 for PostgreSQL or MySQL by updating `application.properties` (or environment variables) and providing the respective driver.
- Integrate Spring Security or OAuth2 for authentication/authorization.
- Add scheduled jobs to auto-expire reservations or send overdue reminders.

## License
MIT License (update as required).
