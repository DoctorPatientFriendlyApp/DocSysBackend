
# 🩺 **DocSys Backend — Doctor–Patient Friendly Application (Spring Boot)**

A secure, scalable, and modular backend built using **Spring Boot**, designed to help doctors manage patients, treatments, medical reports, and follow-up history. This backend powers the DocSys frontend and exposes clean REST APIs secured using JWT authentication.

---

## 🚀 **Key Features**

### 🔐 **Authentication & Authorization**

* Login using email + password
* JWT token generation
* Protected routes using Spring Security
* Password hashing with BCrypt

### 👨‍⚕️ **Patient Management**

* Add new patients
* View all patients
* Update patient details
* Delete patient records
* Filter/search patients

### 🧪 **Treatment Module**

* Add treatment with:

  * Medicine
  * Potency
  * Before/After condition
  * Repertory used
  * Differential diagnosis
  * Miasma
  * Hahnemannian classification
* Get complete treatment history for each patient

### 📄 **Report & File Upload Module**

* Upload PDF/images for any patient
* File stored securely on **Cloudinary**
* Preview URL returned instantly
* Delete and manage reports

### 📚 **Clean Layered Architecture**

* **Controller → Service → Repository → Entity → DTO**
* Global exception handling
* Proper validation and request/response models

---

## 🛠️ **Tech Stack**

| Component         | Technology                     |
| ----------------- | ------------------------------ |
| Backend Framework | Spring Boot                    |
| Security          | Spring Security + JWT          |
| ORM               | Hibernate / JPA                |
| Database          | MySQL                          |
| File Storage      | Cloudinary API                 |
| Build Tool        | Maven                          |
| Dev Tools         | Lombok, Postman, Swagger-ready |

---

## 📁 **Project Structure**

```
src/main/java/com/docsys/
 ├── config/           # Spring Security, JWT filters
 ├── controller/       # REST Controllers
 ├── dto/              # Request / Response DTOs
 ├── entity/           # Database entities
 ├── exception/        # Global exception handling
 ├── repository/       # JPA repositories
 ├── security/         # JWT utilities, authentication
 ├── service/          # Business logic layer
 └── DocSysApplication.java
```

---

## ⚙️ **Setup & Installation**

### 1️⃣ Clone the Repo

```bash
git clone https://github.com/DoctorPatientFriendlyApp/backend.git
cd backend
```

### 2️⃣ Configure `application.properties`

Add your MySQL + Cloudinary + JWT details:

```properties
# MySQL Database
spring.datasource.url=jdbc:mysql://localhost:3306/docsys
spring.datasource.username=YOUR_USER
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update

# Cloudinary
cloudinary.cloud_name=YOUR_CLOUD_NAME
cloudinary.api_key=YOUR_API_KEY
cloudinary.api_secret=YOUR_API_SECRET

# JWT Secret
jwt.secret=YOUR_SECRET_KEY
jwt.expirationMs=86400000
```

### 3️⃣ Install Dependencies

```bash
mvn clean install
```

### 4️⃣ Run the Server

```bash
mvn spring-boot:run
```

Backend runs on:

```
http://localhost:8080
```

---

## 🔐 **Authentication Flow**

1. Doctor logs in using `/auth/login`
2. Backend verifies credentials
3. JWT token is returned
4. All further requests include:

```
Authorization: Bearer <token>
```

5. Access to protected routes allowed only with valid JWT.

---

## 🔗 **API Endpoints Summary**

### **Auth**

| Method | Endpoint         | Description     |
| ------ | ---------------- | --------------- |
| POST   | `/auth/register` | Register doctor |
| POST   | `/auth/login`    | Login & get JWT |

---

### **Patients**

| Method | Endpoint         | Description       |
| ------ | ---------------- | ----------------- |
| POST   | `/patients`      | Add new patient   |
| GET    | `/patients`      | List all patients |
| GET    | `/patients/{id}` | Get patient by ID |
| PUT    | `/patients/{id}` | Update patient    |
| DELETE | `/patients/{id}` | Delete patient    |

---

### **Treatment**

| Method | Endpoint                 | Description               |
| ------ | ------------------------ | ------------------------- |
| POST   | `/treatment/{patientId}` | Add treatment for patient |
| GET    | `/treatment/{patientId}` | Get all treatments        |

---

### **Reports**

| Method | Endpoint                      | Description                |
| ------ | ----------------------------- | -------------------------- |
| POST   | `/reports/upload/{patientId}` | Upload report (Cloudinary) |
| GET    | `/reports/{patientId}`        | View patient reports       |
| DELETE | `/reports/{reportId}`         | Delete report              |

---

## 🗄️ **Database Schema Overview**

* **doctor** (Authentication)
* **patient** (Demographics + medical profile)
* **treatment** (Records for each visit)
* **report** (Cloudinary links)
* **follow_up** (Optional extended module)

Foreign keys maintain full relational integrity.

---

## 🚀 **Production Deployment Guide**

### Recommended Platforms:

* Render
* Railway
* AWS Elastic Beanstalk
* Azure App Service
* GCP
* VPS (Linux + PM2 + Nginx)

### Build JAR

```bash
mvn clean package
```

Generated JAR:

```
target/docsys-backend.jar
```

---


