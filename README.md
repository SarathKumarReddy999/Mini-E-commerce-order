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

## 4. Service #1: Product Service (Java/Spring Boot)
* **Build Tool:** Gradle (`build.gradle` manages dependencies).
* **Port:** 9071
* **Database:** H2 (In-memory, isolated to this service only).
* **Endpoints:**
    * `GET /products` -> Returns a list of all products.
    * `POST /products` -> Creates a new product.
* **Microservice Rule Applied:** *Database Isolation*. The Product Service manages its own data. If the Order Service wants product info, it CANNOT read the H2 database directly. It must call `GET /products`.
