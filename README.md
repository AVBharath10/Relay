# Relay

Relay is a lightweight, distributed rate-limiting service built with [Spring Boot](https://spring.io/projects/spring-boot) and [Redis](https://redis.io/). It provides a simple REST API to verify if an action is permitted based on a sliding window rate limit.

## Features

- **Distributed Rate Limiting**: Leverages Redis to maintain consistent counters across multiple application instances.
- **Atomic Operations**: Uses Redis atomic increments to ensure thread-safety and accuracy in concurrent environments.
- **Simple API**: Minimalistic endpoint for easy integration.

## Requirements

- **Java 21** or higher
- **Redis Server**

## Getting Started

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/relay.git
   cd Relay
   ```

2. **Start Redis**: Ensure you have a Redis server running locally on port `6379`.
   - If you have Docker, you can run: `docker run -p 6379:6379 -d redis`

### Running the Application

You can run the application using the included Maven wrapper:

**Linux/macOS:**
```bash
./mvnw spring-boot:run
```

**Windows:**
```cmd
mvnw.cmd spring-boot:run
```

The application will start on `http://localhost:8080`.

## API Reference

### Check Rate Limit

Check if a specific identifier (like a User ID or API Key) has remaining quota.

- **URL**: `/check`
- **Method**: `POST`
- **Content-Type**: `application/json`

#### Request

```json
{
  "key": "user_identifier_123"
}
```

| Field | Type | Description |
| :--- | :--- | :--- |
| `key` | `string` | A unique identifier for the client (e.g., user ID, IP address). |

#### Response

```json
{
  "allowed": true,
  "remaining": 4
}
```

| Field | Type | Description |
| :--- | :--- | :--- |
| `allowed` | `boolean` | `true` if the request is within the limit, `false` otherwise. |
| `remaining` | `integer` | The number of requests remaining in the current time window. |

## Configuration

Currently, the rate limits are configured in `RateLimiter.java`:

- **Limit**: 5 requests
- **Window**: 180 seconds

## Tech Stack

- Java 21
- Spring Boot 3.5.8
- Spring Data Redis
- Maven

