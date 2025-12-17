# Relay 🚀

**Relay** is a high-performance, distributed rate limiter built with **Java 21** and **Spring Boot 3**. It uses **Redis** as a centralized state store to manage request counters across multiple application instances, making it suitable for microservices and distributed architectures.

##  Features

*   **Distributed Design**: Stateless service instances share state via Redis.
*   **Policy-Based Configuration**: Define rate limits based on URL patterns or keys.
*   **Rate Limiting Algorithm**: Fixed window counter using Redis atomic operations (sliding-window–like behavior).
*   **High Performance**: Minimal overhead using Redis atomic increment and TTL operations.
*   **Easy Deployment**: Docker Compose support for instant setup.

##  Tech Stack

*   **Java 21**
*   **Spring Boot 3.5** (Web, Data Redis)
*   **Redis 7**
*   **Docker & Docker Compose**
*   **Maven**

##  Prerequisites

*   [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed.
*   (Optional) [Java 21 SDK](https://adoptium.net/) if running without Docker.

##  Getting Started

### Option 1: Run with Docker Compose (Recommended)

This is the fastest way to get everything running (Relay Service + Redis).

1.  **Build the application:**
    ```bash
    ./mvnw clean package -DskipTests
    ```
    *(Note: The Dockerfile expects the jar to be in `target/`)*

2.  **Start the services:**
    ```bash
    docker-compose up --build
    ```

3.  The application will start on port `8080` and Redis on port `6379`.

### Option 2: Run Locally

1.  **Start Redis:**
    You need a running Redis instance on `localhost:6379`.
    ```bash
    docker run -p 6379:6379 redis:7
    ```

2.  **Run the Application:**
    ```bash
    ./mvnw spring-boot:run
    ```

##  Configuration

Rate limiting policies are configured in `src/main/resources/application.yml`.

The system matches requests using a simple substring match on the "key" (usually a URL path or user ID).

```yaml
ratelimiter:
  policies:
    # Allow 5 requests every 3 minutes for login
    - pattern: "/login"
      limit: 5
      windowSeconds: 180

    # Allow 100 requests every minute for search
    - pattern: "/search"
      limit: 100
      windowSeconds: 60

  # Default policy if no pattern matches
  default:
    limit: 50
    windowSeconds: 60
```

##  API Reference

### Check Rate Limit

Endpoint: `POST /check`

Check if a specific key is allowed.

**Request Body:**

```json
{
  "key": "/search/user/123"
}
```

**Response:**

```json
{
  "allowed": true,
  "remaining": 99
}
```

### Example Usage

```bash
curl -X POST http://localhost:8080/check \
     -H "Content-Type: application/json" \
     -d '{"key": "/search"}'
```

If the limit is exceeded:
```json
{
  "allowed": false,
  "remaining": 0
}
```

## 🧪 Testing

Run unit and integration tests with Maven:

```bash
./mvnw test
```
