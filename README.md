🚌 Bus Management System
A role-based, object-oriented Bus Management System built as a mini-project for the Object-Oriented Analysis & Design (OOAD) course at PES University. The system streamlines public transportation operations for passengers, drivers, administrators, and maintenance staff, offering features like ticket booking, real-time tracking, fleet management, and automated scheduling.

📌 Table of Contents
Features

Technologies Used

System Architecture

Design Principles & Patterns

Screenshots

How to Run

Contributors

Future Scope

✨ Features
👤 Passenger Module
Login & registration

Search bus routes

Book tickets

Online payments (Stripe/PayPal integration)

View booking history

Receive real-time notifications

🚍 Driver Module
View assigned routes

Update bus location (live tracking)

Report mechanical issues

🛠️ Admin Module
Manage users (passengers, drivers, maintenance)

Assign buses to routes

Monitor fleet operations

Generate reports (revenue, occupancy, maintenance)

🧰 Maintenance Module
Schedule and track repairs

Issue work orders

Monitor bus status and fleet condition

🛠️ Technologies Used
Layer	Technology
Frontend	JavaFX / HTML
Backend	Java (Spring Boot) / C++
Database	MySQL
APIs	RESTful APIs for communication
Payments	Stripe / PayPal
Architecture	MVC + Three-Tier Architecture

🧠 System Architecture
Model-View-Controller (MVC)

Three-Tier Architecture (Presentation, Business Logic, Data Access)

Role-Based Access Control (RBAC)

💡 Design Principles & Patterns
SOLID Principles
SRP: Dedicated classes for booking, maintenance, user management

OCP: Extendable without modifying core logic

LSP: Subtypes like Passenger, Driver, and Admin can substitute User

ISP: Separated interfaces for each role

DIP: High-level modules depend on abstractions

Design Patterns Implemented
Singleton

Factory

Strategy

Observer

DAO (Data Access Object)

Command

Template Method

🖼️ Screenshots
Admin Dashboard	User Booking Page	Fleet Maintenance


<img width="1918" height="948" alt="image" src="https://github.com/user-attachments/assets/654c2c52-5a67-40fa-bd0c-e84803f24f95" />


🚀 How to Run
Clone the Repository


git clone https://github.com/mana-sg/BusReservationSystem.git
cd BusReservationSystem
Set up MySQL Database

Create a database named bus_management

Import the SQL schema from database/bus_management.sql

Backend (Spring Boot)

Navigate to backend folder

Run using your IDE or:

bash
Copy
Edit
mvn spring-boot:run
Frontend (JavaFX / HTML)

Run the main UI file using JavaFX or Qt

Ensure proper linking to REST APIs

👨‍💻 Contributors
Name	SRN	Module
Manas G	PES1UG22CS327	Route Management
Manish M Naik	PES1UG22CS330	Fleet Maintenance
Meghana Royal	PES1UG22CS334	Bus Scheduling & Dashboard
Junaid Hussain	PES1UG22CS354	Ticket Booking & Scheduling

🔭 Future Scope
AI-based route optimization based on traffic and time

Smart traffic system integration for live rerouting

Autonomous fleet management support

Mobile app interface with GPS-based live tracking

