# E-commerce API

A REST API developed with Spring Boot for e-commerce management with customer, product, and order functionalities.

## 🛠 Technologies

- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Data JPA** - For data persistence
- **Spring Boot Validation** - For data validation
- **H2 Database** - In-memory database
- **Lombok** - To reduce boilerplate code
- **MapStruct 1.5.5** - For object mapping
- **Swagger/OpenAPI 2.5.0** - For API documentation
- **Maven** - For dependency management
- **Spring Boot DevTools** - For development
- **Spotless** - For code formatting

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.5.6

## 🚀 Running the application locally

### 1. Clone the repository
```bash
git clone https://github.com/germanmarqueztrujillo
cd Proyecto3
```

### 2. Compile the project
```bash
mvn clean compile
```

### 3. Run the application
```bash
mvn spring-boot:run
```

### 4. Verify the application is running

The application will be available at: `http://localhost:8080`

## 📚 API Documentation

Once the application is running, you can access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `user`
  - Password: `user`

## 🧪 Running tests

### Run all tests
```bash
mvn test
```

## 🔧 Useful commands

### Format code
```bash
mvn spotless:apply
```

### Check code format
```bash
mvn spotless:check
```

## 🌐 Main endpoints

- **Customers**: `/api/customers`
- **Products**: `/api/products`  
- **Orders**: `/api/orders`

For more details about available endpoints, check the Swagger documentation once the application is running.