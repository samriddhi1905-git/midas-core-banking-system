# Midas
Project repo for the JPMC Advanced Software Engineering Forage program

# Midas Core Banking Backend Simulation

A Spring Boot backend banking simulation project built as part of the JPMorgan Chase Software Engineering Job Simulation.

This project processes financial transactions using Apache Kafka, validates transactions, stores records in a database, integrates with an external Incentive API, and exposes REST endpoints for querying balances.

---

## Technologies Used

- Java
- Spring Boot
- Apache Kafka
- REST APIs
- Maven
- H2 Database
- JPA / Hibernate
- Git & GitHub

---

## Features

- Kafka transaction consumer
- Transaction validation system
- Balance update logic
- Incentive API integration
- REST API for balance queries
- Database persistence using JPA
- Event-driven backend architecture

---

## Project Architecture

Transactions are produced into a Kafka topic and consumed by the Spring Boot application.

Flow:

Transaction Producer  
↓  
Kafka Topic  
↓  
Kafka Consumer  
↓  
Validate Transaction  
↓  
Update User Balances  
↓  
Call Incentive API  
↓  
Store Transaction in Database  
↓  
Expose Balance REST API

---

## API Endpoint

### Get User Balance

```http
GET /balance?userId={id}
