# Personal Finance Manager

A comprehensive personal finance management system built with Spring Boot that enables users to effectively track their income, expenses, and savings goals through a robust web-based application.

## Features

### Core Functionality
- **User Management & Authentication**
  - Secure user registration with email validation
  - Session-based authentication with secure cookies
  - Password encryption using BCrypt
  - Complete data isolation between users

- **Transaction Management**
  - Create, read, update, and delete financial transactions
  - Support for both income and expense transactions
  - Date-based filtering (cannot add future transactions)
  - Category-based organization
  - Optional transaction descriptions

- **Category Management**
  - Pre-defined default categories (Salary, Food, Rent, etc.)
  - Custom category creation for personalized organization
  - Category type validation (INCOME/EXPENSE)
  - Protection against deleting categories in use

- **Savings Goals**
  - Set financial goals with target amounts and dates
  - Automatic progress calculation based on income vs expenses
  - Progress percentage and remaining amount tracking
  - Goal management (create, update, delete)

- **Reports & Analytics**
  - Monthly financial reports with category breakdowns
  - Yearly financial summaries
  - Net savings calculations
  - Income and expense categorization

## 🛠 Technology Stack

| Component | Technology |
|-----------|------------|
| **Language** | Java 17+ |
| **Framework** | Spring Boot 3.2.1 |
| **Security** | Spring Security |
| **Database** | H2 (In-memory) |
| **Build Tool** | Maven |
| **Testing** | JUnit 5, Mockito |
| **Validation** | Jakarta Validation |
| **Documentation** | JavaDoc |

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Git (for cloning the repository)

## 🚀 Quick Start

### 1. Clone the Repository
\`\`\`bash
git clone https://github.com/somnath2004dhere/personal-finance-manager.git
cd personal-finance-manager
\`\`\`

### 2. Build the Project
\`\`\`bash
mvn clean install
\`\`\`

### 3. Run the Application
\`\`\`bash
mvn spring-boot:run
\`\`\`

The application will start on `http://localhost:8080/api`

### 4. Access H2 Console (Development)
- URL: `http://localhost:8080/api/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## 🧪 Testing

### Run Unit Tests
\`\`\`bash
mvn test
\`\`\`

### Generate Test Coverage Report
\`\`\`bash
mvn jacoco:report
\`\`\`

Coverage report will be available at `target/site/jacoco/index.html`

### Test Coverage Goals
- Minimum 80% code coverage
- Comprehensive service layer testing
- Controller integration testing
- Repository layer testing

## 📚 API Documentation

### Base URL
\`\`\`
http://localhost:8080/api
\`\`\`

### Authentication Endpoints

#### Register User
\`\`\`http
POST /auth/register
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123",
  "fullName": "John Doe",
  "phoneNumber": "+1234567890"
}
\`\`\`

**Response (201 Created):**
\`\`\`json
{
  "message": "User registered successfully",
  "userId": 1
}
\`\`\`

#### Login
\`\`\`http
POST /auth/login
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123"
}
\`\`\`

**Response (200 OK):**
\`\`\`json
{
  "message": "Login successful"
}
\`\`\`

#### Logout
\`\`\`http
POST /auth/logout
\`\`\`

**Response (200 OK):**
\`\`\`json
{
  "message": "Logout successful"
}
\`\`\`

### Transaction Endpoints

#### Create Transaction
\`\`\`http
POST /transactions
Content-Type: application/json
Cookie: JSESSIONID=<session-id>

{
  "amount": 50000.00,
  "date": "2024-01-15",
  "category": "Salary",
  "description": "January Salary"
}
\`\`\`

**Response (201 Created):**
\`\`\`json
{
  "id": 1,
  "amount": 50000.00,
  "date": "2024-01-15",
  "category": "Salary",
  "description": "January Salary",
  "type": "INCOME"
}
\`\`\`

#### Get Transactions
\`\`\`http
GET /transactions
GET /transactions?startDate=2024-01-01&endDate=2024-01-31
GET /transactions?category=Salary
\`\`\`

**Response (200 OK):**
\`\`\`json
{
  "transactions": [
    {
      "id": 1,
      "amount": 50000.00,
      "date": "2024-01-15",
      "category": "Salary",
      "description": "January Salary",
      "type": "INCOME"
    }
  ]
}
\`\`\`

#### Update Transaction
\`\`\`http
PUT /transactions/{id}
Content-Type: application/json

{
  "amount": 60000.00,
  "description": "Updated January Salary"
}
\`\`\`

#### Delete Transaction
\`\`\`http
DELETE /transactions/{id}
\`\`\`

### Category Endpoints

#### Get All Categories
\`\`\`http
GET /categories
\`\`\`

**Response (200 OK):**
\`\`\`json
{
  "categories": [
    {
      "name": "Salary",
      "type": "INCOME",
      "isCustom": false
    },
    {
      "name": "Food",
      "type": "EXPENSE",
      "isCustom": false
    }
  ]
}
\`\`\`

#### Create Custom Category
\`\`\`http
POST /categories
Content-Type: application/json

{
  "name": "SideBusinessIncome",
  "type": "INCOME"
}
\`\`\`

#### Delete Custom Category
\`\`\`http
DELETE /categories/{name}
\`\`\`

### Savings Goals Endpoints

#### Create Goal
\`\`\`http
POST /goals
Content-Type: application/json

{
  "goalName": "Emergency Fund",
  "targetAmount": 5000.00,
  "targetDate": "2026-01-01",
  "startDate": "2025-01-01"
}
\`\`\`

**Response (201 Created):**
\`\`\`json
{
  "id": 1,
  "goalName": "Emergency Fund",
  "targetAmount": 5000.00,
  "targetDate": "2026-01-01",
  "startDate": "2025-01-01",
  "currentProgress": 1000.00,
  "progressPercentage": 20.0,
  "remainingAmount": 4000.00
}
\`\`\`

#### Get All Goals
\`\`\`http
GET /goals
\`\`\`

#### Get Specific Goal
\`\`\`http
GET /goals/{id}
\`\`\`

#### Update Goal
\`\`\`http
PUT /goals/{id}
Content-Type: application/json

{
  "targetAmount": 6000.00,
  "targetDate": "2026-02-01"
}
\`\`\`

#### Delete Goal
\`\`\`http
DELETE /goals/{id}
\`\`\`

### Reports Endpoints

#### Monthly Report
\`\`\`http
GET /reports/monthly/{year}/{month}
\`\`\`

**Example Response:**
\`\`\`json
{
  "month": 1,
  "year": 2024,
  "totalIncome": {
    "Salary": 3000.00,
    "Freelance": 500.00
  },
  "totalExpenses": {
    "Food": 400.00,
    "Rent": 1200.00,
    "Transportation": 200.00
  },
  "netSavings": 1700.00
}
\`\`\`

#### Yearly Report
\`\`\`http
GET /reports/yearly/{year}
\`\`\`

## 🏗 Architecture

The application follows a clean layered architecture:

\`\`\`
┌─────────────────┐
│   Controllers   │ ← HTTP Request/Response handling
├─────────────────┤
│    Services     │ ← Business logic
├─────────────────┤
│  Repositories   │ ← Data access layer
├─────────────────┤
│    Entities     │ ← Database models
└─────────────────┘
\`\`\`

### Key Components

- **Controllers**: Handle HTTP requests, validation, and response formatting
- **Services**: Implement business logic and transaction management
- **Repositories**: Provide data access using Spring Data JPA
- **DTOs**: Data Transfer Objects for API request/response
- **Entities**: JPA entities representing database tables
- **Exception Handlers**: Global exception handling with appropriate HTTP status codes

## 🔒 Security Features

- **Authentication**: Session-based authentication with secure cookies
- **Password Security**: BCrypt password encoding
- **Data Isolation**: Users can only access their own data
- **Input Validation**: Comprehensive validation using Jakarta Validation
- **CSRF Protection**: Disabled for API usage (stateless)
- **Session Management**: Configurable session timeout and management

## 📊 Default Categories

The system provides the following pre-configured categories:

### Income Categories
- Salary

### Expense Categories
- Food
- Rent
- Transportation
- Entertainment
- Healthcare
- Utilities

## 🚨 Error Handling

The application returns appropriate HTTP status codes with descriptive error messages:

| Status Code | Description | Example |
|-------------|-------------|---------|
| 200 OK | Successful operation | Transaction retrieved |
| 201 Created | Resource created | User registered |
| 400 Bad Request | Validation errors | Invalid email format |
| 401 Unauthorized | Authentication required | Invalid credentials |
| 403 Forbidden | Access denied | Accessing other user's data |
| 404 Not Found | Resource not found | Transaction not found |
| 409 Conflict | Resource conflict | Duplicate category name |
| 500 Internal Server Error | Unexpected server error | Database connection issue |

### Error Response Format
\`\`\`json
{
  "error": "Descriptive error message"
}
\`\`\`

### Validation Error Response Format
\`\`\`json
{
  "fieldName": "Field-specific error message",
  "anotherField": "Another error message"
}
\`\`\`

## 🚀 Deployment

### Local Development
\`\`\`bash
mvn spring-boot:run
\`\`\`

### Production Build
\`\`\`bash
mvn clean package
java -jar target/personal-finance-manager-0.0.1-SNAPSHOT.jar
\`\`\`

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `PORT` | Server port | 8080 |
| `SPRING_PROFILES_ACTIVE` | Active profile | default |

### Render Deployment

1. Push code to GitHub repository
2. Connect repository to Render
3. Set environment variables:
   - `PORT`: 8080
   - `SPRING_PROFILES_ACTIVE`: prod
4. Deploy

### Docker Deployment (Optional)
\`\`\`dockerfile
FROM openjdk:17-jdk-slim
COPY target/personal-finance-manager-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
\`\`\`

## 🧪 Testing Strategy

### Unit Tests
- Service layer testing with Mockito
- Repository testing with @DataJpaTest
- Controller testing with @WebMvcTest

### Integration Tests
- End-to-end API testing
- Database integration testing
- Security integration testing

### Test Coverage
- Minimum 80% code coverage requirement
- Comprehensive test suite covering all business logic
- Edge case testing for error scenarios

## 📁 Project Structure

\`\`\`
src/
├── main/
│   ├── java/com/example/personalfinance/
│   │   ├── controller/          # REST controllers
│   │   ├── service/             # Business logic
│   │   ├── repository/          # Data access
│   │   ├── entity/              # JPA entities
│   │   ├── dto/                 # Data transfer objects
│   │   ├── exception/           # Custom exceptions
│   │   ├── config/              # Configuration classes
│   │   └── PersonalFinanceManagerApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/personalfinance/
        ├── controller/          # Controller tests
        ├── service/             # Service tests
        └── repository/          # Repository tests
\`\`\`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/new-feature`
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass: `mvn test`
6. Commit your changes: `git commit -am 'Add new feature'`
7. Push to the branch: `git push origin feature/new-feature`
8. Submit a pull request

### Code Style Guidelines
- Follow Java naming conventions
- Add JavaDoc comments for public methods
- Maintain test coverage above 80%
- Use meaningful variable and method names
- Follow Spring Boot best practices

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support, questions, or feature requests:

1. Check existing [Issues](https://github.com/yourusername/personal-finance-manager/issues)
2. Create a new issue with detailed description
3. Contact the development team

## 🔄 Version History

- **v1.0.0** - Initial release with core functionality
  - User authentication and management
  - Transaction CRUD operations
  - Category management
  - Savings goals tracking
  - Financial reports

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Spring Security for robust authentication
- H2 Database for development convenience
- JUnit and Mockito for testing capabilities

---

**Built with ❤️ using Spring Boot**
\`\`\`

This comprehensive README.md file includes:

✅ **Complete Documentation:**
- Detailed feature descriptions
- Technology stack overview
- Installation and setup instructions
- Complete API documentation with examples

✅ **Developer-Friendly:**
- Clear project structure
- Testing guidelines
- Contributing guidelines
- Code style recommendations

✅ **Deployment Ready:**
- Environment configuration
- Deployment instructions for various platforms
- Docker support information

✅ **Professional Presentation:**
- Well-organized sections with emojis
- Tables for better readability
- Code examples with proper formatting
- Error handling documentation

✅ **Maintenance Information:**
- Version history
- Support channels
- License information
- Acknowledgments

The README provides everything needed for developers to understand, set up, use, and contribute to the Personal Finance Manager system.
