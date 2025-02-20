
# Expense Control App

Expense Control App is an application for managing expenses, allowing users to track their finances. It enables users to add budgets, expense categories, and individual expenses. The app supports generating reports, currency conversion, and exporting data to an Excel file.



## Tech Stack

- **Java 21**
- **Spring Boot**
- **Spring Security** (user authentication and login)
- **H2 Database** (local database)
- **Docker** (application containerization)
- **Apache POI** (Excel export)
- **External Api** for currency conversion
- **JUnit & Mockito** (unit testing)


## Installation

1. Classic installation

```bash
git clone https://github.com/MaciejDani/expense-control.git
cd expense-control
mvn clean package
java -jar target/expense-control-0.0.1-SNAPSHOT.jar
```
2. Running with Docker

```bash
docker build -t expense-control-app .
docker run -p 8080:8080 expense-control-app
```




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

