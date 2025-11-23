# E-Wallet Java Swing Application

## Project Overview
The E-Wallet application is a Java Swing-based desktop application that allows users to manage their digital wallet. It supports user registration, login, deposit, withdrawal, and money transfer functionalities. The application uses a relational database for persistence and follows a layered architecture with service and database classes.

## Features
- User Registration and Login
- Deposit and Withdraw Funds
- Transfer Money Between Users
- View Transaction History
- Reset Password Functionality

## Technologies Used
- **Java Swing** for GUI
- **JDBC** for database connectivity
- **MySQL** 
- **Minimal MVC Architecture**

## Project Structure
- `EWalletUI.java` – Main UI class with CardLayout navigation
- `EWalletService.java` – Service layer handling business logic
- `Database.java` – Database operations using JDBC
- `User.java` – Model class for user details
- `Transaction.java` – Model class for transactions
- `CommonUtils.java` – Utility class for constants and helper methods

``` bash
.ewallet/src
└── com
    └── groupproject
        └── ewallet
            ├── CommonUtils.java
            ├── Database.java
            ├── EWalletService.java
            ├── IEWalletService.java
            ├── test
            │   └── TestEWalletService.java
            ├── Transaction.java
            ├── ui
            │   ├── Capture.PNG
            │   ├── EWalletUI.form
            │   ├── EWalletUI.java
            │   ├── man_145849.png
            │   ├── profile_2.jpg
            │   └── profile.png
            └── User.java
```

## Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/gmaroko/ewallet
   ```
2. Open the project in **NetBeans** or any Java IDE.
3. Configure the database connection in `Database.java`.
4. Create the required tables using the schema below.

## Database Schema
### Users Table
```sql
CREATE TABLE users (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(20),
    balance DOUBLE
);
```

### Transactions Table
```sql
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20),
    amount DOUBLE,
    fee DOUBLE,
    sender_id VARCHAR(50),
    receiver_id VARCHAR(50),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## How to Run
- Launch the application from IDE using `main` method in `EWalletUI.java`.

## Security Considerations
- Password hashing (e.g., using SHA-256 or BCrypt) should be implemented.
- Validate all inputs to prevent SQL Injection.

## Future Enhancements
- Add email notifications for transactions.
- Implement password recovery via email.
- Add charts for transaction analytics.
- Implement Admin dashboards
- Add edit self user account

---
**Collaborators:** 
- MAROKO GIDEON
- DENG ATEM
- IAN MUTHUNGA
- AMBACHOW KAHSAY
- DAU ALUONG DENG 