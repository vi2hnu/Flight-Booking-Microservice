# Flight Booking Microservice

## Overview
The Flight Booking Microservice handles creating and managing flight tickets, passengers, and booking details. It interacts with the Flight Service to confirm schedules, seats, and availability. As a microservice, it runs independently with its own data and logic, and communicates with other services through both lightweight APIs and Kafka-based event messaging.

## Application Features
- Search flights by source, destination, and date
- Book tickets for available flights
- View booking history using email ID
- Cancel tickets up to 24 hours before departure
- Retrieve ticket details using PNR
- Manage flight inventory with conflict checks
- Input validation and custom exception handling

---

## Architecture & System Features
- **Real-time email notifications** triggered during booking, cancellation and addition of flights in inventory
- **Microservice architecture** with independently deployable services and isolated databases
- **Service Registry (Eureka)** for automatic service discovery
- **API Gateway** to route external requests and provide a single unified entry point
- **Config Server** for centralized configuration management
- **Circuit Breaker (Resilience4j)** to prevent cascading failures and improve resilience
- **Kafka event streaming** for asynchronous, reliable, and decoupled inter-service communication
- **Independent scalability** allowing each service to scale based on its own load  

