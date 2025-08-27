# Yoga App - Backend

This project was generated with Spring Boot 2.6.1 and uses Spring Security with JWT authentication.

## Install and start the project

Git clone:

> git clone https://github.com/OpenClassrooms-Student-Center/P5-Full-Stack-testing

Go inside folder:

> cd back

Install dependencies:

> mvn clean install

## Install database

Create MySQL database:

```sql
CREATE DATABASE yoga_app;
```

Update `src/main/resources/application.properties` with your database settings:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/yoga_app?serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password
```

By default the admin account is:
- login: yoga@studio.com
- password: test!1234

## Launch application

Start the backend:

> mvn spring-boot:run

Application will be accessible at: `http://localhost:8080`

## Tests

### Unit and Integration Tests

Launch all tests:

> mvn test

Launch only unit tests:

> mvn test -Dtest="*Test"

Launch only integration tests:

> mvn test -Dtest="*IntegrationTest"

### Coverage Reports

Generate JaCoCo coverage report:

> mvn clean test

Report is available here:

> target/site/jacoco/index.html

Check coverage threshold (90% minimum):

> mvn jacoco:check

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Users
- `GET /api/user/{id}` - Get user details
- `DELETE /api/user/{id}` - Delete user

### Sessions
- `GET /api/session` - List all sessions
- `GET /api/session/{id}` - Get session details
- `POST /api/session` - Create session
- `PUT /api/session/{id}` - Update session
- `DELETE /api/session/{id}` - Delete session
- `POST /api/session/{id}/participate/{userId}` - Participate in session
- `DELETE /api/session/{id}/participate/{userId}` - Stop participating

### Teachers
- `GET /api/teacher` - List all teachers
- `GET /api/teacher/{id}` - Get teacher details

## Technologies

- Spring Boot 2.6.1
- Spring Security
- Spring Data JPA
- MySQL / H2 (for tests)
- JWT
- JaCoCo (code coverage)
- Lombok
- MapStruct