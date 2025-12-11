# Safe Spend API Documentation

## Base URL
```
http://localhost:8080/api
```

## Table of Contents
- [Parent Endpoints](#parent-endpoints)
- [Child Endpoints](#child-endpoints)
- [Data Models](#data-models)
- [Error Responses](#error-responses)

---

## Parent Endpoints

### Create Parent Account
Creates a new parent account.

**Endpoint:** `POST /parents`

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "username": "johndoe"
}
```

**Response:** `200 OK`
```json
{
  "parentId": "parent_1234567890",
  "name": "John Doe",
  "email": "john@example.com",
  "username": "johndoe",
  "children": [],
  "chores": [],
  "storeInventory": [],
  "transactions": []
}
```

**Error Responses:**
- `400 Bad Request` - Missing required fields or duplicate username/email
```json
{
  "error": "Username already taken"
}
```

---

### Parent Login
Authenticate a parent user.

**Endpoint:** `POST /parents/login`

**Request Body:**
```json
{
  "username": "johndoe"
}
```

**Response:** `200 OK`
```json
{
  "parentId": "parent_1234567890",
  "name": "John Doe",
  "email": "john@example.com",
  "username": "johndoe",
  "children": [...],
  "chores": [...],
  "storeInventory": [...],
  "transactions": [...]
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid username
```json
{
  "error": "Invalid username"
}
```

---

### Get Parent
Retrieve parent account information.

**Endpoint:** `GET /parents/{parentId}`

**Response:** `200 OK`
```json
{
  "parentId": "parent_1234567890",
  "name": "John Doe",
  "email": "john@example.com",
  "username": "johndoe",
  "children": [...],
  "chores": [...],
  "storeInventory": [...],
  "transactions": [...]
}
```

---

### Create Child Account
Create a new child account under a parent.

**Endpoint:** `POST /parents/{parentId}/children`

**Request Body:**
```json
{
  "name": "Tommy",
  "username": "tommy123"
}
```

**Response:** `200 OK`
```json
{
  "childId": "child_1234567890",
  "name": "Tommy",
  "username": "tommy123",
  "balance": 0.0,
  "parentId": "parent_1234567890",
  "pin": "123456"
}
```

**Error Responses:**
- `400 Bad Request` - Missing fields or duplicate username
- `404 Not Found` - Parent not found

---

### Get Children
Get all children for a parent.

**Endpoint:** `GET /parents/{parentId}/children`

**Response:** `200 OK`
```json
[
  {
    "childId": "child_1234567890",
    "name": "Tommy",
    "username": "tommy123",
    "balance": 25.50,
    "parentId": "parent_1234567890"
  }
]
```

---

### Get Child PIN
Retrieve a child's login PIN.

**Endpoint:** `GET /parents/{parentId}/children/{childId}/pin`

**Response:** `200 OK`
```json
{
  "pin": "123456"
}
```

**Error Responses:**
- `403 Forbidden` - Child does not belong to this parent
- `404 Not Found` - Parent or child not found

---

### Regenerate Child PIN
Generate a new PIN for a child.

**Endpoint:** `POST /parents/{parentId}/children/{childId}/regenerate-pin`

**Request Body:** `{}` (empty)

**Response:** `200 OK`
```json
{
  "pin": "789012"
}
```

**Error Responses:**
- `403 Forbidden` - Child does not belong to this parent
- `404 Not Found` - Parent or child not found

---

### Update Child
Update child account information.

**Endpoint:** `PUT /parents/{parentId}/children/{childId}`

**Request Body:**
```json
{
  "name": "Thomas",
  "username": "thomas123"
}
```

**Response:** `200 OK`
```json
{
  "childId": "child_1234567890",
  "name": "Thomas",
  "username": "thomas123",
  "balance": 25.50,
  "parentId": "parent_1234567890"
}
```

---

### Add Chore
Create a new chore.

**Endpoint:** `POST /parents/{parentId}/chores`

**Request Body:**
```json
{
  "choreName": "Clean Kitchen",
  "choreDescription": "Wash dishes and wipe counters",
  "chorePrice": 5.00,
  "assignedChildId": "child_1234567890"
}
```

**Note:** `assignedChildId` is optional. Omit or set to `null` for unassigned chores.

**Response:** `200 OK`
```json
{
  "choreId": "chore_1234567890",
  "choreName": "Clean Kitchen",
  "choreDescription": "Wash dishes and wipe counters",
  "chorePrice": 5.00,
  "assignedChildId": "child_1234567890",
  "status": "AVAILABLE"
}
```

---

### Get Chores
Get all chores for a parent.

**Endpoint:** `GET /parents/{parentId}/chores`

**Response:** `200 OK`
```json
[
  {
    "choreId": "chore_1234567890",
    "choreName": "Clean Kitchen",
    "choreDescription": "Wash dishes and wipe counters",
    "chorePrice": 5.00,
    "assignedChildId": "child_1234567890",
    "status": "AVAILABLE"
  }
]
```

---

### Update Chore
Update an existing chore.

**Endpoint:** `PUT /parents/{parentId}/chores/{choreId}`

**Request Body:**
```json
{
  "choreName": "Deep Clean Kitchen",
  "choreDescription": "Wash dishes, wipe counters, and mop floor",
  "chorePrice": 7.50,
  "assignedChildId": "child_1234567890"
}
```

**Response:** `200 OK`
```json
{
  "choreId": "chore_1234567890",
  "choreName": "Deep Clean Kitchen",
  "choreDescription": "Wash dishes, wipe counters, and mop floor",
  "chorePrice": 7.50,
  "assignedChildId": "child_1234567890",
  "status": "AVAILABLE"
}
```

---

### Delete Chore
Delete a chore.

**Endpoint:** `DELETE /parents/{parentId}/chores/{choreId}`

**Response:** `200 OK`
```json
{
  "message": "Chore deleted successfully"
}
```

---

### Get Pending Chores
Get all chores awaiting approval.

**Endpoint:** `GET /parents/{parentId}/chores/pending`

**Response:** `200 OK`
```json
[
  {
    "choreId": "chore_1234567890",
    "choreName": "Clean Kitchen",
    "choreDescription": "Wash dishes and wipe counters",
    "chorePrice": 5.00,
    "assignedChildId": "child_1234567890",
    "status": "PENDING"
  }
]
```

---

### Approve Chore Completion
Approve a child's chore completion request.

**Endpoint:** `POST /parents/{parentId}/chores/{choreId}/approve`

**Request Body:** `{}` (empty)

**Response:** `200 OK`
```json
{
  "id": 1,
  "type": "ALLOWANCE",
  "amount": 5.00,
  "childId": "child_1234567890",
  "description": "Completed: Clean Kitchen",
  "timestamp": "2024-12-11T14:30:00"
}
```

**Side Effects:**
- Child's balance is increased by chore price
- Chore status changes to `COMPLETED`
- Transaction is created and added to both parent and child records

---

### Deny Chore Completion
Deny a child's chore completion request.

**Endpoint:** `POST /parents/{parentId}/chores/{choreId}/deny`

**Request Body:** `{}` (empty)

**Response:** `200 OK`
```json
{
  "choreId": "chore_1234567890",
  "choreName": "Clean Kitchen",
  "choreDescription": "Wash dishes and wipe counters",
  "chorePrice": 5.00,
  "assignedChildId": null,
  "status": "AVAILABLE"
}
```

**Side Effects:**
- Chore status returns to `AVAILABLE`
- Child assignment is removed

---

### Add Store Item
Add an item to the store inventory.

**Endpoint:** `POST /parents/{parentId}/store-items`

**Request Body:**
```json
{
  "itemName": "Lego Set",
  "availableInventory": 5,
  "itemPrice": 29.99
}
```

**Response:** `200 OK`
```json
{
  "itemID": "item_1234567890",
  "itemName": "Lego Set",
  "availableInventory": 5,
  "itemPrice": 29.99,
  "available": true
}
```

---

### Get Store Items
Get all store items.

**Endpoint:** `GET /parents/{parentId}/store-items`

**Response:** `200 OK`
```json
[
  {
    "itemID": "item_1234567890",
    "itemName": "Lego Set",
    "availableInventory": 5,
    "itemPrice": 29.99,
    "available": true
  }
]
```

---

### Update Store Item
Update a store item.

**Endpoint:** `PUT /parents/{parentId}/store-items/{itemId}`

**Request Body:**
```json
{
  "itemName": "Deluxe Lego Set",
  "availableInventory": 3,
  "itemPrice": 39.99
}
```

**Response:** `200 OK`
```json
{
  "itemID": "item_1234567890",
  "itemName": "Deluxe Lego Set",
  "availableInventory": 3,
  "itemPrice": 39.99,
  "available": true
}
```

---

### Delete Store Item
Delete a store item.

**Endpoint:** `DELETE /parents/{parentId}/store-items/{itemId}`

**Response:** `200 OK`
```json
{
  "message": "Store item deleted successfully"
}
```

---

### Get Transactions
Get all transactions for a parent.

**Endpoint:** `GET /parents/{parentId}/transactions`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "type": "ALLOWANCE",
    "amount": 5.00,
    "childId": "child_1234567890",
    "description": "Completed: Clean Kitchen",
    "timestamp": "2024-12-11T14:30:00"
  },
  {
    "id": 2,
    "type": "PURCHASE",
    "amount": 29.99,
    "childId": "child_1234567890",
    "description": "Purchased: Lego Set",
    "timestamp": "2024-12-11T15:45:00"
  }
]
```

---

### Add Allowance
Manually add allowance to a child's account.

**Endpoint:** `POST /parents/{parentId}/children/{childId}/add-allowance`

**Request Body:**
```json
{
  "amount": 10.00
}
```

**Response:** `200 OK`
```json
{
  "childId": "child_1234567890",
  "name": "Tommy",
  "username": "tommy123",
  "balance": 35.50,
  "parentId": "parent_1234567890"
}
```

**Side Effects:**
- Child's balance is increased by the amount
- Transaction is created with description "Allowance from parent"

---

## Child Endpoints

### Child Login
Authenticate a child user.

**Endpoint:** `POST /children/login`

**Request Body:**
```json
{
  "username": "tommy123",
  "pin": "123456"
}
```

**Response:** `200 OK`
```json
{
  "childId": "child_1234567890",
  "name": "Tommy",
  "username": "tommy123",
  "balance": 25.50,
  "parentId": "parent_1234567890"
}
```

**Error Responses:**
- `400 Bad Request` - Missing username or PIN
- `401 Unauthorized` - Invalid username or PIN
```json
{
  "error": "Invalid username or PIN"
}
```

---

### Get Child
Get child account information.

**Endpoint:** `GET /children/{childId}`

**Response:** `200 OK`
```json
{
  "childId": "child_1234567890",
  "name": "Tommy",
  "username": "tommy123",
  "balance": 25.50,
  "parentId": "parent_1234567890"
}
```

---

### Get Balance
Get child's current balance.

**Endpoint:** `GET /children/{childId}/balance`

**Response:** `200 OK`
```json
{
  "balance": 25.50
}
```

---

### Get Transactions
Get child's transaction history.

**Endpoint:** `GET /children/{childId}/transactions`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "type": "ALLOWANCE",
    "amount": 5.00,
    "childId": "child_1234567890",
    "description": "Completed: Clean Kitchen",
    "timestamp": "2024-12-11T14:30:00"
  },
  {
    "id": 2,
    "type": "PURCHASE",
    "amount": 29.99,
    "childId": "child_1234567890",
    "description": "Purchased: Lego Set",
    "timestamp": "2024-12-11T15:45:00"
  }
]
```

---

### Get Available Chores
Get all chores available to the child.

**Endpoint:** `GET /children/{childId}/available-chores`

**Response:** `200 OK`
```json
[
  {
    "choreId": "chore_1234567890",
    "choreName": "Clean Kitchen",
    "choreDescription": "Wash dishes and wipe counters",
    "chorePrice": 5.00,
    "assignedChildId": null,
    "status": "AVAILABLE"
  },
  {
    "choreId": "chore_9876543210",
    "choreName": "Mow Lawn",
    "choreDescription": "Mow the front and back lawn",
    "chorePrice": 15.00,
    "assignedChildId": "child_1234567890",
    "status": "PENDING"
  }
]
```

---

### Get Store Items
Get all items available for purchase.

**Endpoint:** `GET /children/{childId}/store-items`

**Response:** `200 OK`
```json
[
  {
    "itemID": "item_1234567890",
    "itemName": "Lego Set",
    "availableInventory": 5,
    "itemPrice": 29.99,
    "available": true
  },
  {
    "itemID": "item_9876543210",
    "itemName": "Video Game",
    "availableInventory": 0,
    "itemPrice": 49.99,
    "available": false
  }
]
```

---

### Request Chore Completion
Request approval for completing a chore.

**Endpoint:** `POST /children/{childId}/chores/{choreId}/request-completion`

**Request Body:** `{}` (empty)

**Response:** `200 OK`
```json
{
  "choreId": "chore_1234567890",
  "choreName": "Clean Kitchen",
  "choreDescription": "Wash dishes and wipe counters",
  "chorePrice": 5.00,
  "assignedChildId": "child_1234567890",
  "status": "PENDING"
}
```

**Side Effects:**
- Chore is assigned to the child
- Chore status changes to `PENDING`

---

### Purchase Item
Purchase an item from the store.

**Endpoint:** `POST /children/{childId}/purchase/{itemId}`

**Request Body:** `{}` (empty)

**Response:** `200 OK`
```json
{
  "id": 3,
  "type": "PURCHASE",
  "amount": 29.99,
  "childId": "child_1234567890",
  "description": "Purchased: Lego Set",
  "timestamp": "2024-12-11T16:00:00"
}
```

**Side Effects:**
- Child's balance is decreased by item price
- Item inventory is decreased by 1
- Transaction is created

**Error Responses:**
- `500 Internal Server Error` - Insufficient balance or item out of stock
```json
{
  "error": "Insufficient balance"
}
```

---

## Data Models

### Parent
```json
{
  "parentId": "string",
  "name": "string",
  "email": "string",
  "username": "string",
  "children": [Child],
  "chores": [Chore],
  "storeInventory": [StoreItem],
  "transactions": [Transaction]
}
```

### Child
```json
{
  "childId": "string",
  "name": "string",
  "username": "string",
  "balance": "number",
  "parentId": "string",
  "pin": "string" // Only returned on account creation
}
```

### Chore
```json
{
  "choreId": "string",
  "choreName": "string",
  "choreDescription": "string",
  "chorePrice": "number",
  "assignedChildId": "string | null",
  "status": "AVAILABLE | PENDING | COMPLETED"
}
```

**Status Values:**
- `AVAILABLE` - Chore is available for any child to claim
- `PENDING` - Child has requested completion, awaiting parent approval
- `COMPLETED` - Parent has approved completion, child has been paid

### StoreItem
```json
{
  "itemID": "string",
  "itemName": "string",
  "availableInventory": "number",
  "itemPrice": "number",
  "available": "boolean"
}
```

**Note:** `available` is computed as `availableInventory > 0`

### Transaction
```json
{
  "id": "number",
  "type": "ALLOWANCE | PURCHASE",
  "amount": "number",
  "childId": "string",
  "description": "string",
  "timestamp": "ISO 8601 datetime string"
}
```

**Transaction Types:**
- `ALLOWANCE` - Money added to child's account (chore completion, manual allowance)
- `PURCHASE` - Money deducted from child's account (store purchase)

---

## Error Responses

All error responses follow this general format:

```json
{
  "error": "Error message description"
}
```

### Common HTTP Status Codes

- **200 OK** - Request succeeded
- **400 Bad Request** - Invalid request body or missing required fields
- **401 Unauthorized** - Authentication failed
- **403 Forbidden** - User doesn't have permission to access resource
- **404 Not Found** - Requested resource doesn't exist
- **500 Internal Server Error** - Server error or business logic constraint violated

### Example Error Responses

**Missing Required Field:**
```json
{
  "error": "Name is required"
}
```

**Duplicate Username:**
```json
{
  "error": "Username already taken"
}
```

**Insufficient Balance:**
```json
{
  "error": "Insufficient balance"
}
```

**Item Out of Stock:**
```json
{
  "error": "Item is out of stock"
}
```

**Resource Not Found:**
```json
{
  "error": "Child not found"
}
```

---

## Authentication

Currently, the API uses simple authentication:

- **Parents**: Login with username only (password authentication to be added with OAuth)
- **Children**: Login with username and 6-digit PIN

Authentication state is managed client-side using the `AuthService` in the Angular frontend.

---

## CORS Configuration

The API is configured to accept requests from:
- `http://localhost:4200` (Angular development server)

CORS is configured in `CorsConfig.java`:
```java
config.addAllowedOrigin("http://localhost:4200");
config.addAllowedHeader("*");
config.addAllowedMethod("*");
```

---

## Request/Response Format

All requests and responses use **JSON** format.

**Request Headers:**
```
Content-Type: application/json
```

**Response Headers:**
```
Content-Type: application/json
```

---

## Notes

1. All monetary values are represented as floating-point numbers (e.g., `25.50`)
2. All timestamps follow ISO 8601 format (e.g., `2024-12-11T14:30:00`)
3. IDs are generated server-side using timestamps (e.g., `parent_1234567890`)
4. PINs are 6-digit numeric strings (e.g., `"123456"`)
5. Empty request bodies are represented as `{}` or omitted entirely

---

## Testing the API

API calls were tested using Postman

---

## Version Information

- **API Version**: 1.0
- **Spring Boot Version**: 3.5.6
- **Java Version**: 21
- **Database**: MySQL
