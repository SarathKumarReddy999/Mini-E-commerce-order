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
### A. Eureka Naming Server (Service Discovery)
* **Port:** 8761
* **Role:** Tracks all active microservices. Shows a UI dashboard at `localhost:8761`.

### B. Product Service (`product-service`)
* **Port:** 9071
* **Database:** H2 (`jdbc:h2:mem:productdb`)
* **Role:** Registers with Eureka on startup. Returns product JSON data.

### C. Order Service (`order-service`)
* **Port:** 9081
* **Database:** H2 (`jdbc:h2:mem:orderdb`)
* **Concept Highlight:** Uses a `@LoadBalanced RestClient`. It asks Eureka, "Where is `product-service`?", gets the active IP behind the scenes, and makes the API call dynamically to calculate the total price.