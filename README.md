# Toscana Communication Platform

A **Spring Boot 3.3.5 / Java 21** multi-channel notification service built with Clean (Hexagonal) Architecture.

## Tech Stack

| Layer | Technology |
|---|---|
| Web | Spring WebFlux (Reactive) |
| Persistence | Spring Data JPA + PostgreSQL |
| Distributed Cache | Redis (Reactive) + Redisson |
| Local Cache | Caffeine |
| Messaging | Spring Cloud Stream + Kafka + RabbitMQ |
| Templates | Thymeleaf |
| Build | Gradle 8.10.2 (Kotlin DSL) |

## Package Structure

```
src/main/java/com/clt/toscana/
├── config/                  # @Configuration beans (Redis, Cache, Async, WebFlux)
├── consumer/                # Spring Cloud Stream consumers (Kafka / RabbitMQ)
├── core/
│   ├── service/             # Dispatcher, IdempotencyService, TemplateService
│   └── exception/           # Custom domain exceptions
├── domain/
│   ├── entity/              # JPA @Entity classes
│   ├── dto/                 # Data Transfer Objects
│   └── enums/               # ChannelType, MessageStatus, …
├── repository/              # Spring Data JPA repositories
├── adapter/
│   ├── NotificationSender.java  # Strategy interface
│   └── impl/                # Telegram, Email, LINE, WebSocket, …
└── controller/              # REST APIs (admin + user)
```

## Architecture Rules

1. **`core`** must NEVER import from `adapter.impl` — communicate only via `NotificationSender`.
2. **`adapter.impl`** classes are wired by Spring; the `core` layer receives them as `List<NotificationSender>`.
3. JPA (blocking) calls inside a WebFlux context are wrapped with `Schedulers.boundedElastic()`.

## Getting Started

```bash
# 1. Generate Gradle wrapper (first time only)
gradle wrapper

# 2. Start infrastructure (Docker)
docker compose up -d   # postgres, redis, kafka, rabbitmq

# 3. Run the application
./gradlew bootRun
```

## Environment Variables

| Variable | Default | Description |
|---|---|---|
| `DB_HOST` | `localhost` | PostgreSQL host |
| `DB_PORT` | `5432` | PostgreSQL port |
| `DB_NAME` | `toscana_db` | Database name |
| `DB_USERNAME` | `postgres` | DB user |
| `DB_PASSWORD` | `postgres` | DB password |
| `REDIS_HOST` | `localhost` | Redis host |
| `REDIS_PORT` | `6379` | Redis port |
| `KAFKA_BROKERS` | `localhost:9092` | Kafka bootstrap servers |
| `RABBITMQ_ADDRESS` | `localhost:5672` | RabbitMQ address |
