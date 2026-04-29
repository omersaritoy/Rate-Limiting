# 🚦 Rate Limiting Project (Spring Boot + Bucket4j)

This project demonstrates **rate limiting** in a Spring Boot application using **Bucket4j**, including both:

- ✅ **Per-user rate limiting**
- ✅ **Global system rate limiting**
- ✅ **IP-based rate limiting**
- ✅ (Optional) **Redis-based distributed rate limiting**

---

## 📦 Tech Stack

- Java 25
- Spring Boot 4.x
- Bucket4j 8.10.1
- Maven## ⚙️ Rate Limiting Strategies

### 🍕 1. Order Placement (`/api/place-order`)

Dual-layer protection:

| Limit Type | Rule |
|-----------|------|
| Global    | 10,000 requests / minute |
| Per User  | 5 requests / minute |

📌 Purpose:
- Prevent system overload
- Prevent user spam

---

### 🔐 2. Forgot Password (`/api/forgot-password`)

| Limit Type | Rule |
|-----------|------|
| Global    | 3 requests / minute |

📌 Purpose:
- Protect against brute-force / abuse attacks

---

### 🌐 3. IP-Based Rate Limiting (`/hello`, `/status`)

| Limit Type | Rule |
|-----------|------|
| Per IP    | Configurable (via `RateLimitService`) |

📌 Purpose:
- Prevent abuse from specific IP addresses

---

## 🚀 API Endpoints

### 🍕 Place OrderPOST /api/place-order


#### Parameters:

| Name         | Type   | Required |
|--------------|--------|----------|
| userId       | String | ✅ |
| restaurantId | String | ✅ |
| items        | String | ✅ |
| amount       | String | ❌ (default: 299.00) |

#### ✅ Success Response

```json
{
  "success": true,
  "orderId": "ORD_1710000000000",
  "data": {
    "userId": "user1",
    "restaurantId": "rest1",
    "items": "Pizza",
    "amount": "₹299.00"
  },
  "remainingUserLimit": 4,
  "remainingGlobalLimit": 9999
}

❌ Rate Limited Response
{
  "success": false,
  "message": "You're placing orders too quickly.",
  "error": {
    "code": "USER_LIMIT_EXCEEDED"
  }
  
  "remainingUserLimit": 0,
  "remainingGlobalLimit": 9990
}




  🔐 Forgot Password
GET /api/forgot-password?email=test@example.com



{
  "success": true,
  "data": {
    "email": "test@example.com"
  },
  "remainingAttempts": 2
}




<dependency>
    <groupId>com.bucket4j</groupId>
    <artifactId>bucket4j-redis</artifactId>
    <version>8.10.1</version>
</dependency>
