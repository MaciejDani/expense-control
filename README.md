
# Expense Control App

Expense Control App is an application for managing expenses, allowing users to track their finances. It enables users to add budgets, expense categories, and individual expenses. The app supports generating reports, currency conversion, and exporting data to an Excel file.



## Tech Stack

- **Java 21**
- **Spring Boot**
- **Spring Security** (user authentication and login)
- **PostgreSQL** (production-ready database)
- **Docker** (containerization of backend and database)
- **Apache POI** (Excel export)
- **External Api** for currency conversion
- **JUnit & Mockito** (unit testing)


## Installation


Running with Docker

```bash
git clone https://github.com/MaciejDani/expense-control.git
cd expense-control
./mvnw clean package -DskipTests
docker-compose up --build -d
```
- The backend will be available at: http://localhost:8080
- PostgreSQL runs inside a container on port 5432




## Features
- User registration and login (Spring Security)
- Adding and managing budgets
- Creating expense categories
- Adding and removing expenses
- Generating financial reports
- Currency conversion using an external API
- Exporting expenses to an Excel file
## Authors

- [@MaciejDani](https://www.github.com/MaciejDani)

