# 🔗 Blockchain Supply Chain Tracker

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.14-brightgreen?style=flat-square&logo=springboot)
![Maven](https://img.shields.io/badge/Maven-3.9+-blue?style=flat-square&logo=apachemaven)
![H2](https://img.shields.io/badge/Database-H2-lightblue?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen?style=flat-square)

> A supply chain tracking REST API built with Java 21 & Spring Boot — featuring a **custom blockchain engine written from scratch** with SHA-256 hashing, chain integrity validation, and an immutable event ledger exposed via REST.

---

## 🧠 How It Works

A blockchain is a linked list where each block contains the **SHA-256 hash of the previous block**. This means if anyone tampers with a block, every subsequent block's hash becomes invalid — making the chain tamper-proof by design.

```
[Genesis Block] → [Block 1: MANUFACTURED] → [Block 2: SHIPPED] → [Block 3: DELIVERED]
  hash: "000a"      hash: "4f3c"               hash: "9b21"          hash: "cc74"
  prevHash: "0"     prevHash: "000a"            prevHash: "4f3c"      prevHash: "9b21"
```

Each supply chain event (manufactured, shipped, delivered, etc.) is recorded as an **immutable block** on the chain. The event is also persisted to an H2 database for querying, with each DB record storing its `blockIndex` and `blockHash` as proof of its position on the chain.

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────┐
│            REST Controllers             │
│   ProductController | BlockchainController│
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│           SupplyChainService            │
│     Business logic & orchestration      │
└──────┬─────────────────────┬────────────┘
       │                     │
┌──────▼──────┐    ┌─────────▼──────────┐
│  Blockchain  │    │   JPA Repositories  │
│   Engine     │    │  Product | Event    │
│ (in-memory)  │    │  (H2 Database)      │
└──────────────┘    └────────────────────┘
```

---

## 🚀 Getting Started

### Prerequisites
- Java 21 (Eclipse Temurin recommended)
- Maven 3.9+

### Run Locally
```bash
git clone https://github.com/YOUR_USERNAME/blockchain-tracker.git
cd blockchain-tracker
./mvnw spring-boot:run
```

App runs on `http://localhost:8080`

H2 Console at `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:supplychain`
- Username: `sa`
- Password: *(leave empty)*

---

## 📡 API Endpoints

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/products` | Register a new product |
| `POST` | `/api/products/{id}/events` | Record a supply chain event |
| `GET` | `/api/products/{id}/history` | Get full event history for a product |

### Blockchain
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/blockchain/chain` | View the full blockchain |
| `GET` | `/api/blockchain/validate` | Validate chain integrity |

---

## 🧪 Example Usage

### 1. Register a Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MacBook Pro",
    "serialNumber": "MBP-2024-001",
    "origin": "Shenzhen, China",
    "description": "16 inch MacBook Pro M3"
  }'
```

### 2. Record a Supply Chain Event
```bash
curl -X POST http://localhost:8080/api/products/1/events \
  -H "Content-Type: application/json" \
  -d '{
    "eventType": "SHIPPED",
    "location": "Shenzhen Port, China",
    "handledBy": "DHL Logistics",
    "notes": "Departed via cargo ship"
  }'
```

### 3. Validate the Chain
```bash
curl http://localhost:8080/api/blockchain/validate
```

```json
{
  "valid": true,
  "chainLength": 3,
  "message": "Chain is intact — no tampering detected"
}
```

---

## 🔑 Key Concepts Demonstrated

- **Custom Blockchain Engine** — built from scratch in Java, no external blockchain libraries
- **SHA-256 Hashing** — using Java's built-in `MessageDigest`, same algorithm as Bitcoin
- **Chain Integrity Validation** — detects any tampering by re-computing and cross-checking hashes
- **Immutability** — all block fields are `final`; once created a block cannot be altered
- **Spring Boot 3.x** — REST controllers, dependency injection, `@Transactional` service layer
- **JPA + H2** — persistent event ledger with lazy loading and automatic schema generation
- **Lombok** — builder pattern, constructor injection, zero boilerplate
- **DTO Pattern** — clean separation between API contracts and internal entities

---

## 📦 Event Types

```
MANUFACTURED → QUALITY_CHECKED → SHIPPED → IN_TRANSIT → 
CUSTOMS_CLEARED → WAREHOUSE_RECEIVED → OUT_FOR_DELIVERY → DELIVERED
```

---

## 📄 License
MIT