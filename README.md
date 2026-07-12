# URL-Shortener

A URL shortener built from scratch in raw Java — no web frameworks, no Spring Boot. The HTTP server, request/response parsing, and routing are all hand-written on top of Java's socket and I/O primitives, backed by PostgreSQL via JDBC.

## Why raw Java?

Most URL shortener tutorials reach for Spring Boot on day one. This project intentionally skips that layer to actually understand what a web framework is doing under the hood — parsing raw HTTP/1.1 requests off a socket, routing them, and writing responses back by hand.

## Features

- Shorten a long URL into a compact Base62-encoded code
- Redirect from the short code back to the original URL
- Click tracking on each shortened link
- Handles both JSON and form-urlencoded request bodies

## Tech Stack

- **Java** — core language, no frameworks
- **JDBC** — direct database access, no ORM
- **PostgreSQL** — persistence
- **Maven** — build and dependency management (Shade plugin for a fat jar)
- **JUnit 5** — testing
- **dotenv-java** — environment-based configuration

## Architecture

| Component | Responsibility |
|---|---|
| `ServerListenerThread` | Accepts incoming socket connections |
| `HttpConnectionWorkerThread` | Handles a single connection's request/response lifecycle |
| `HttpRequest` / `HttpResponse` | Models for parsed HTTP requests and outgoing responses |
| `Router` | Maps incoming requests to the right handler |
| `Base62Encoder` | Encodes URL IDs into short, URL-safe codes |
| `URLRepository` | JDBC/PostgreSQL data access layer |
| `JsonStringParser` | Parses JSON request bodies |
| `FormStringParser` | Parses form-urlencoded request bodies, branching by `Content-Type` |

The HTTP parsing layer includes a custom byte-level `readLine()` implementation, written specifically to avoid the over-reading behavior of Java's `BufferedReader` when parsing raw socket streams.

## Getting Started

### Prerequisites

- Java (JDK 17+ recommended)
- Maven
- PostgreSQL running locally (or accessible via connection string)

### Setup

1. Clone the repo
   ```bash
   git clone https://github.com/jerryme3/URL-Shortener.git
   cd URL-Shortener
   ```

2. Create a `.env` file in the project root:
   ```
   DB_URL=jdbc:postgresql://localhost:5432/urlshortener
   DB_USER=your_pg_user
   DB_PASSWORD=your_pg_password
   ```

3. Build the project:
   ```bash
   mvn clean package
   ```

4. Run it:
   ```bash
   java -jar target/URL-Shortener-shaded.jar
   ```
   *(adjust the jar name to match your actual build output)*

## Roadmap

- [ ] Dockerize the application
- [ ] Deploy to a live environment
- [ ] Write up full portfolio documentation

## License

Not yet specified.
