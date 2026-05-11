# 🚗 Garage Microservice API

A RESTful microservice for managing **garages**, **vehicles**, and **accessories**.  
Built around a clean domain model with pagination support and UUID-based identifiers.

---

## 📌 Features

- ✅ Manage garages (CRUD)
- ✅ Manage vehicles across garages
- ✅ Attach accessories to vehicles
- ✅ Pagination & sorting support
- ✅ OpenAPI / Swagger compliant API

---

## 🧱 Tech Stack

- Java / Spring Boot
- Spring Web
- Spring Data (Pagination)
- OpenAPI 3.0 (Swagger)
- REST / JSON

---

## 🌍 Base URL

```
http://localhost:8080
```

---

## 🔐 API Conventions

- **Content-Type:** `application/json`
- **Identifiers:** UUID
- **Pagination:** Required for all list endpoints
- **HTTP Status Codes:** REST-compliant

---

## 📂 API Domains

| Domain | Description |
|------|------------|
| Garages | Garage management |
| Vehicles | Vehicle lifecycle |
| Accessoires | Vehicle accessories |

---

## 🚘 Vehicles API

### Search vehicles by brand
`GET /vehicles`

**Query Parameters**
- `brand` (optional)
- `page` (required)
- `size` (required)

✅ **Response:** `PageVehiculeDTO`

---

### Get vehicles of a garage
`GET /garages/{garageId}/vehicles`

✅ **Response:** `PageVehiculeDTO`

---

### Add vehicle to garage
`POST /garages/{garageId}/vehicle`

```json
{
  "brand": "BMW",
  "anneeFabrication": 2023,
  "typeCarburant": "ESSENCE"
}
```

---

### Update vehicle
`PUT /vehicles/{id}`

---

### Delete vehicle
`DELETE /vehicles/{id}`

---

## 🏠 Garages API

### Get all garages
`GET /garages`

✅ **Paginated**

---

### Get garage by ID
`GET /garages/{id}`

---

### Create garage
`POST /garages`

```json
{
  "name": "Central Garage",
  "address": "Casablanca",
  "telephone": "+212600000000",
  "email": "contact@garage.ma"
}
```

---

### Update garage
`PUT /garages/{id}`

---

### Delete garage
`DELETE /garages/{id}`

---

## 🧰 Accessoires API

### List accessories of a vehicle
`GET /vehicules/{vehiculeId}/accessoire`

✅ **Paginated**

---

### Add accessory to vehicle
`POST /vehicules/{vehiculeId}/accessoire`

```json
{
  "nom": "GPS Tracker",
  "description": "Real-time tracking",
  "prix": 120.0,
  "type": "ELECTRONIC"
}
```

---

### Update accessory
`PUT /accessoires/{id}`

---

### Delete accessory
`DELETE /accessoires/{id}`

---

## 📄 DTO Models

### VehiculeDTO
```json
{
  "id": "uuid",
  "brand": "string",
  "anneeFabrication": 2023,
  "typeCarburant": "string",
  "garageId": "uuid"
}
```

---

### GarageDTO
```json
{
  "id": "uuid",
  "name": "string",
  "address": "string",
  "telephone": "string",
  "email": "string",
  "horairesOuverture": {
    "MONDAY": [
      {
        "startTime": { "hour": 8, "minute": 0 },
        "endTime": { "hour": 18, "minute": 0 }
      }
    ]
  }
}
```

---

### AccessoireDTO
```json
{
  "id": "uuid",
  "nom": "string",
  "description": "string",
  "prix": 99.99,
  "type": "string",
  "vehiculeId": "uuid"
}
```

---

## 📑 Pagination

```json
{
  "page": 0,
  "size": 10,
  "sort": ["property,asc"]
}
```

---

## ⚠️ Error Handling

### 400 – Bad Request
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed"
}
```

### 404 – Not Found
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found"
}
```

### 500 – Internal Server Error
```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "Unexpected error occurred"
}
```

---

## 📜 API Documentation

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Spec: `http://localhost:8080/v3/api-docs`

---

## 👨‍💻 Author

Garage Microservice 🚀
