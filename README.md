# Oracle Training Microservices

This project provides several Spring Boot microservices:

- **eureka-server** – service registry
- **core-service** – customers, products, quotes, policies, claims
- **document-service** – document storage using MongoDB GridFS
- **payment-service** – record premium payments and claim payouts
- **notification-service** – consume email notifications from Kafka

## Prerequisites

Run supporting services locally:

- Oracle Database on `localhost:1521/XEPDB1`
  - Users: `CORE_USER/CORE_PASSWORD`, `PAYMENT_USER/PAYMENT_PASSWORD`, `NOTIF_USER/NOTIF_PASSWORD`
- MongoDB on `localhost:27017`
- Kafka broker on `localhost:9092`
- Eureka server on port `8761`

## Running

Start the services in order:

1. `mvn -pl eureka-server spring-boot:run`
2. `mvn -pl core-service spring-boot:run`
3. `mvn -pl document-service spring-boot:run`
4. `mvn -pl payment-service spring-boot:run`
5. `mvn -pl notification-service spring-boot:run`

Swagger UI is available at `/swagger-ui.html` for each service.

### Security

All services are secured with HTTP Basic authentication and in-memory users:

- `user` / `password` – role `USER` (read-only)
- `admin` / `admin` – role `ADMIN` (read/write)

The core service also requires the caller's user ID in the `X-USER-ID` request header.

## Example requests

```bash
# Upload document
curl -F "file=@/path/to/file.pdf" \
     -F 'meta={"ownerId":"1","ownerType":"POLICY","label":"policy"};type=application/json' \
     http://localhost:8082/api/v1/documents

# Create payment
curl -H 'Content-Type: application/json' -d '{"targetType":"POLICY","targetId":"1","amount":1000,"method":"CARD"}' \
     http://localhost:8083/api/v1/payments

# Send test email
curl -H 'Content-Type: application/json' -d '{"eventId":"1","template":"PLAIN","to":["test@example.com"],"subject":"Hello"}' \
     http://localhost:8084/api/v1/emails/test
```
