# Mini-E-commerce
# My Microservices Master Guide

## 1. Core Concepts
* **Monolith:** All features in one big box. Hard to scale, one bug crashes everything.
* **Microservices:** Small, independent apps talking over a network.
* **ELI5:** One giant glued-together robot vs. a team of small robots using walkie-talkies.

## 2. Project Architecture: Mini E-Commerce
* **Product Service:** Owns product data.
* **Order Service:** Owns purchasing logic.
* **Notification Service:** Owns alerts and emails.

## 3. Design Rules we will follow:
1. **Independent Codebases:** Each service gets its own folder/repository.
2. **Database per Service:** Services DO NOT share a database. They must ask each other for data via APIs.

## 4. Service Designs & Flows

### A. API Gateway (`api-gateway`)
* **Port:** 9070
* **Concept Highlight:** Uses `spring-cloud-starter-gateway`. It catches a request at `localhost:9070/products`, asks Eureka where `product-service` is, and forwards the request seamlessly.

### B. Eureka Naming Server (Service Discovery)
* **Port:** 8761
* **Role:** Tracks all active microservices. Shows a UI dashboard at `localhost:8761`.

### C. Product Service (`product-service`)
* **Port:** 9071
* **Database:** H2 (`jdbc:h2:mem:productdb`)
* **Role:** Registers with Eureka on startup. Returns product JSON data.

### D. Order Service (`order-service`)
* **Port:** 9081
* **Database:** H2 (`jdbc:h2:mem:orderdb`)
* **Concept Highlight:** Uses a `@LoadBalanced RestClient`. It asks Eureka, "Where is `product-service`?", gets the active IP behind the scenes, and makes the API call dynamically to calculate the total price.

### E. Order Service (`order-service`)
* **Port:** 9081
* **Endpoints:** `POST /orders` 
* **Concept Highlight (Resilience4j):** Wraps the HTTP call to `product-service` in a `@CircuitBreaker`. If `product-service` is down, it executes `placeOrderFallback()` instantly, preventing thread starvation and keeping the Order Service alive.
Testing a Circuit Breaker (Count-Based)
1. **Window Size:** If size is 10, the circuit *will not trip* until it logs exactly 10 requests.
2. **Triggering OPEN:** Stop the target service. Send 10 requests. The 11th request will fail instantly without waiting for a network timeout.
3. **Triggering HALF-OPEN:** Wait the configured duration (e.g., 10s). The next single request will be allowed through to test the network.