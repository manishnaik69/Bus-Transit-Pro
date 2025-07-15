# 🚌 Bus Management System

A Java-based Bus Management System developed as part of the Object-Oriented Analysis & Design (OOAD) mini project at PES University. The system helps streamline public transport operations through modules for passengers, drivers, administrators, and maintenance staff.

---

## ✨ Features

### 👤 Passenger
- Secure login & registration
- Search and book tickets online
- View booking history
- Receive real-time notifications

### 🚍 Driver
- View assigned routes
- Update live bus location
- Report issues or delays

### 🛠️ Admin
- Manage users, routes, and schedules
- Assign buses to routes
- Generate reports (revenue, occupancy, etc.)

### 🔧 Maintenance
- Track fleet condition
- Schedule and monitor repairs
- Issue work orders

---

## ⚙️ Tech Stack

| Layer             | Technology                    |
|------------------|-------------------------------|
| Frontend          | JavaFX / HTML                 |
| Backend           | Java with Spring Boot         |
| Database          | MySQL                         |
| Architecture      | MVC + Three-Tier              |
| API               | REST APIs                     |
| Payment Gateway   | Stripe / PayPal Integration   |

---

## 🧠 Design Principles & Patterns

### SOLID Principles
- Single Responsibility
- Open/Closed
- Liskov Substitution
- Interface Segregation
- Dependency Inversion

### Design Patterns Used
- Singleton
- Factory
- Observer
- Strategy
- DAO
- Command
- Template Method

---

## 🏗️ System Architecture

- **Model-View-Controller (MVC)** structure
- **Three-Tier Architecture**: Presentation, Business Logic, Data Access
- **Role-Based Access Control (RBAC)** for different user types

---

## 🚀 How to Run

### Prerequisites
- Java 11+
- MySQL
- Maven
- (Optional) JavaFX SDK

### Steps
1. **Clone the repository**
   ```bash
   git clone https://github.com/manishnaik69/Bus-Transit-Pro.git
   cd Bus-Transit-Pro
2) ** Go to the main directory **
mvn spring-boot:run

🔭 Future Scope

AI-based route and schedule optimization

Smart traffic integration for rerouting

Autonomous fleet support

Mobile app with GPS-based live tracking
