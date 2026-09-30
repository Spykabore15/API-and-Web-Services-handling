# API and Web Services Handling (TP2)

A Spring Boot REST API demonstrating how to build and consume web services. The
project exposes two independent modules:

- **Calculator** — persists arithmetic operations and exposes full CRUD over them.
- **Currency Converter** — converts amounts between currencies, either by calling
  an **external exchange-rate API** or by using a rate supplied manually, and keeps
  a history of every transaction.

It illustrates common API concerns: input validation, centralized error handling,
pagination, calling remote services with `WebClient`, retries, and caching.

---

## Tech stack

| Concern            | Technology                                   |
| ------------------ | -------------------------------------------- |
| Language           | Java 17                                      |
| Framework          | Spring Boot 4.1.1                            |
| Web (REST)         | `spring-boot-starter-webmvc`                 |
| Remote calls       | `spring-boot-starter-webflux` (`WebClient`)  |
| Persistence        | Spring Data JPA + Hibernate                  |
| Database           | PostgreSQL                                   |
| Validation         | `spring-boot-starter-validation` (Jakarta)   |
| Resilience         | Spring Retry, Spring AOP / AspectJ           |
| Caching            | `spring-boot-starter-cache`                  |
| Build              | Maven (with wrapper)                         |

---

## Project structure

```
demo/
├── pom.xml
├── mvnw / mvnw.cmd                # Maven wrapper
└── src/
    ├── main/
    │   ├── java/juvenis/example/tp2/
    │   │   ├── Tp2Application.java          # Spring Boot entry point
    │   │   ├── calculator/                  # Calculator module
    │   │   │   ├── CalculatorController.java
    │   │   │   ├── CalculatorService.java
    │   │   │   ├── Operation.java           # JPA entity
    │   │   │   ├── OperationRepository.java
    │   │   │   ├── OperationRequest.java    # request DTO
    │   │   │   └── OperationResponse.java   # response DTO
    │   │   ├── currency/                    # Currency converter module
    │   │   │   ├── CurrencyConverterController.java
    │   │   │   ├── CurrencyService.java
    │   │   │   ├── CurrencyRateClient.java  # calls the external rate API
    │   │   │   ├── CurrencyTransaction.java # JPA entity
    │   │   │   ├── CurrencyTransactionRepository.java
    │   │   │   └── dto/
    │   │   │       ├── CurrencyConversionResponse.java
    │   │   │       ├── ExternalRateResponse.java
    │   │   │       ├── ManualConversionRequest.java
    │   │   │       └── UpdateRateRequest.java
    │   │   ├── config/                      # Cross-cutting configuration
    │   │   │   ├── CacheConfig.java
    │   │   │   ├── RetryConfig.java
    │   │   │   └── WebClientConfig.java
    │   │   └── exception/                   # Error handling
    │   │       ├── ApiError.java
    │   │       ├── GlobalExceptionHandler.java
    │   │       ├── ExternalServiceException.java
    │   │       ├── ManualConversionException.java
    │   │       ├── ResourceNotFoundException.java
    │   │       └── ZeroDivisionException.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/juvenis/example/demo/
            └── DemoApplicationTests.java
```

---

## Getting started

### Prerequisites

- JDK 17 or later
- PostgreSQL 12+ running locally
- (Optional) Maven — the project ships with the Maven wrapper (`mvnw`)

### 1. Create the database

```sql
CREATE DATABASE tp2_db;
```

### 2. Configure the application

The app reads its configuration from
`demo/src/main/resources/application.properties`. **Do not commit real
credentials** — prefer environment variables:

```properties
spring.application.name=demo

spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/tp2_db}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Then set the secret before running:

```powershell
$env:DB_PASSWORD = "your-password"
```

> Security note: the repository currently stores the database password directly in
> `application.properties`. It is strongly recommended to externalize it (as above)
> and rotate the exposed value.

### 3. Run the application

```powershell
cd demo
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

### 4. Run the tests

```powershell
cd demo
./mvnw test
```

---

## API reference

### Calculator — `/api/calculator`

Supported operations: `addition`, `soustraction`, `multiplication`, `division`.

| Method   | Endpoint                | Description                                  |
| -------- | ----------------------- | -------------------------------------------- |
| `POST`   | `/api/calculator`       | Perform and store an operation               |
| `GET`    | `/api/calculator/{id}`  | Retrieve one stored operation                |
| `GET`    | `/api/calculator/history` | List operations (paginated)                |
| `PUT`    | `/api/calculator/{id}`  | Recompute and update an operation            |
| `DELETE` | `/api/calculator/{id}`  | Delete an operation                          |

**Create an operation**

```http
POST /api/calculator
Content-Type: application/json

{
  "a": 10,
  "b": 4,
  "operation": "division"
}
```

```json
{
  "id": 1,
  "a": 10.0,
  "b": 4.0,
  "operateur": "division",
  "resultat": 2.5,
  "date": "2026-09-30T12:00:00"
}
```

Dividing by zero returns an error handled by `ZeroDivisionException`.

**Paginated history**

```http
GET /api/calculator/history?page=0&size=20&sort=id,desc
```

---

### Currency converter — `/api/currency`

| Method   | Endpoint                              | Description                                                |
| -------- | ------------------------------------- | --------------------------------------------------------- |
| `GET`    | `/api/currency/convert`               | Automatic conversion using the external rate API          |
| `POST`   | `/api/currency/convert`               | Manual conversion using a client-supplied rate            |
| `GET`    | `/api/currency/history`               | List all stored conversion transactions                   |
| `PUT`    | `/api/currency/{id}/rate`             | Update a transaction's rate (converted amount recomputed)  |
| `DELETE` | `/api/currency/{id}`                  | Delete a transaction                                       |

**Automatic conversion** (rate fetched from the external service)

```http
GET /api/currency/convert?amount=100&from=EUR&to=USD
```

**Manual conversion** (rate provided in the body, no external call)

```http
POST /api/currency/convert
Content-Type: application/json

{
  "amount": 100,
  "from": "EUR",
  "to": "USD",
  "rate": 1.08
}
```

```json
{
  "id": 1,
  "amount": 100.0,
  "from": "EUR",
  "to": "USD",
  "rate": 1.08,
  "convertedAmount": 108.0,
  "date": "2026-09-30T12:00:00"
}
```

**Update a rate**

```http
PUT /api/currency/1/rate
Content-Type: application/json

{
  "rate": 1.10
}
```

Validation rules enforced by the service layer:

- Amount and rate must be present, finite, and strictly positive.
- Currency codes are normalized (trimmed, upper-cased) and must be exactly three
  letters (e.g. `EUR`, `USD`).
- Source and target currencies must differ.

---

## Error handling

All errors are funneled through `GlobalExceptionHandler`, which returns a
consistent `ApiError` payload:

| Situation                          | HTTP status | Exception                    |
| ---------------------------------- | ----------- | ---------------------------- |
| Resource not found                 | 404         | `ResourceNotFoundException`  |
| Division by zero                   | 400         | `ZeroDivisionException`      |
| Invalid input / validation failure | 400         | `IllegalArgumentException`   |
| External rate API failure          | 5xx         | `ExternalServiceException`   |
| Invalid manual conversion          | 400         | `ManualConversionException`  |

---

## Notes

- The application uses `WebClient` (from WebFlux) purely to call the external
  exchange-rate service; the rest of the API is servlet-based Spring MVC.
- `spring.jpa.hibernate.ddl-auto=update` lets Hibernate manage the schema during
  development. Use a migration tool (Flyway/Liquibase) for production.

