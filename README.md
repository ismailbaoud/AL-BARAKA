# AL-BARAKA Digital Banking Platform

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Keycloak](https://img.shields.io/badge/Keycloak-24.0-red.svg)](https://www.keycloak.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](LICENSE)

A secure digital banking platform developed with Spring Boot, offering comprehensive account management, banking operations, and document handling with OAuth2/Keycloak authentication.

---

## Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
- [Architecture and Technologies](#architecture-and-technologies)
- [Prerequisites](#prerequisites)
- [Installation and Setup](#installation-and-setup)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Deployment and Hosting](#deployment-and-hosting)
- [Tests](#tests)
- [License](#license)
- [Credits](#credits)

---

## About the Project

**Al-Baraka Digital** is a modern and secure banking platform that enables comprehensive management of banking operations with robust authentication via OAuth2 and Keycloak. The system implements a multi-role architecture (Client, Banking Agent, Administrator) with operation validation and document management.

### Why This Project?

- **Enhanced Security**: OAuth2/JWT authentication with Keycloak
- **Operation Validation**: Large transactions require agent approval
- **Document Management**: Secure upload and storage of supporting documents
- **Multi-Profile**: Support for development and production profiles
- **Modern Architecture**: Microservices with Docker Compose

### Technologies Used

- **Backend**: Spring Boot 4.0.0, Java 21
- **Security**: Spring Security, OAuth2, JWT, Keycloak 24.0
- **Database**: PostgreSQL 15
- **ORM**: Hibernate/JPA
- **Mapping**: MapStruct
- **Template Engine**: Thymeleaf
- **Containerization**: Docker, Docker Compose
- **Build**: Maven
- **Others**: Lombok, Spring Validation, dotenv

---

## Features

### Authentication and Authorization

- **Dual Authentication Method**:
  - Classic form-based authentication with JWT
  - OAuth2 authentication via Keycloak
- **Role Management**: CLIENT, AGENT_BANCAIRE, ADMIN
- **Remember Me**: Persistent session for 30 days
- **Advanced Security**: CSRF protection, JWT validation

### Banking Operations

- **Deposits**: Add funds to account
- **Withdrawals**: Withdraw funds with balance verification
- **Transfers**: Transfer between accounts
- **Automatic Validation**: Operations ≤ 10,000 DH automatically approved
- **Manual Validation**: Operations > 10,000 DH require agent validation

### Role-Based Dashboards

- **Client**: 
  - Balance inquiry
  - Create operations
  - Upload supporting documents
  - Transaction history
  
- **Banking Agent**:
  - Pending operations list
  - Approve/reject operations
  - Query via OAuth2 with `operations.read` scope
  
- **Administrator**:
  - User management (CRUD)
  - View all accounts
  - Global administration

### Document Management

- **Secure Upload**: Support for PDF, JPG, PNG
- **Size Limit**: 5 MB maximum
- **Validation**: Type and size verification
- **Storage**: Files stored locally with unique names

---

## Architecture and Technologies

### Technical Stack

```
┌─────────────────────────────────────────────────┐
│               Frontend Layer                     │
│        Thymeleaf Templates + Tailwind CSS        │
└──────────────────┬──────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────┐
│            Security Layer                        │
│   Spring Security + OAuth2 + JWT + Keycloak     │
└──────────────────┬──────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────┐
│          Application Layer                       │
│    Controllers + Services + DTOs + Mappers       │
└──────────────────┬──────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────┐
│         Persistence Layer                        │
│       JPA/Hibernate + PostgreSQL                 │
└──────────────────────────────────────────────────┘
```

### Data Model

- **User**: Users with roles and authentication
- **Account**: Bank accounts linked to users
- **Operation**: Transactions (DEPOSIT, WITHDRAWAL, TRANSFER)
- **Document**: Supporting documents associated with operations

### Notable Technical Points

- **Dual Security Chain**: OAuth2 for external agents, classic JWT for the application
- **Business Validation**: 10,000 DH threshold for automatic validation
- **MapStruct**: Automatic Entity ↔ DTO conversion
- **Transaction Management**: `@Transactional` to ensure consistency

---

## Prerequisites

Before starting, make sure you have installed:

- **Java 21** or higher ([OpenJDK](https://openjdk.org/) or [Eclipse Temurin](https://adoptium.net/))
- **Maven 3.8+** ([Installation](https://maven.apache.org/install.html))
- **Docker** and **Docker Compose** ([Installation](https://docs.docker.com/get-docker/))
- **PostgreSQL 15** (if running without Docker)
- **Git** to clone the repository

---

## Installation and Setup

### Option 1: Docker Deployment (Recommended)

This method starts the application, PostgreSQL, and Keycloak with a single command.

#### 1. Clone the repository

```bash
git clone https://github.com/your-username/al-baraka.git
cd al-baraka
```

#### 2. Create the .env file

Create a `.env` file at the project root with the following variables:

```bash
# JWT Configuration
JWT_ISSUER_URI=http://localhost:8180/realms/albaraka
JWT_JWK_SET_URI=http://localhost:8180/realms/albaraka/protocol/openid-connect/certs

# Profile (dev or prod)
SPRING_PROFILES_ACTIVE=prod

# Database (Production - Docker)
PROD_DATABASE=albaraka_test
PROD_DB_USERNAME=happy
PROD_DB_PASSWORD=happy

# Database (Development - Local)
DEV_DATABASE=albaraka_dev
DEV_DB_USERNAME=postgres
DEV_DB_PASSWORD=postgres
```

#### 3. Build the project

```bash
./mvnw clean package -DskipTests
```

Or on Windows:

```bash
mvnw.cmd clean package -DskipTests
```

#### 4. Start Docker containers

```bash
docker-compose up -d
```

Services will be accessible at:
- **Application**: http://localhost:8081
- **Keycloak**: http://localhost:8180 (admin/admin)
- **PostgreSQL**: localhost:5431

#### 5. Access the application

Open your browser and navigate to: http://localhost:8081/login

**Test Banking Agent Account (via Keycloak)**:
- Username: `test`
- Password: `test`

---

### Option 2: Development Mode (without Docker)

#### 1. Install and start PostgreSQL

Create a database:

```sql
CREATE DATABASE albaraka_dev;
```

#### 2. Install and start Keycloak

```bash
# Download Keycloak 24.0
wget https://github.com/keycloak/keycloak/releases/download/24.0.0/keycloak-24.0.0.tar.gz
tar -xzf keycloak-24.0.0.tar.gz
cd keycloak-24.0.0

# Start in dev mode
bin/kc.sh start-dev
```

Import the realm: `keycloak-imports/albaraka-realm.json` via the admin interface.

#### 3. Configure the .env file

Use the `DEV_*` variables in your `.env`:

```bash
SPRING_PROFILES_ACTIVE=dev
DEV_DATABASE=albaraka_dev
DEV_DB_USERNAME=postgres
DEV_DB_PASSWORD=postgres
JWT_ISSUER_URI=http://localhost:8080/realms/albaraka
JWT_JWK_SET_URI=http://localhost:8080/realms/albaraka/protocol/openid-connect/certs
```

#### 4. Run the application

```bash
./mvnw spring-boot:run
```

The application will be available at: http://localhost:8082

---

## Usage

### Create a client account

**Endpoint**: `POST /auth/register`

```json
{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Response**: Returns an `AccountResponse` object with the generated account number.

### Login

**Web Interface**: http://localhost:8081/login

Or via **API**: `POST /auth/login`

```json
{
  "fullName": "John Doe",
  "password": "securePassword123"
}
```

### Perform an operation (Client)

**Endpoint**: `POST /api/client/operations`

```json
{
  "operationType": "DEPOSIT",
  "amount": 5000.0
}
```

**Possible statuses**:
- `APPROVED`: Operation < 10,000 DH, executed immediately
- `PENDING`: Operation ≥ 10,000 DH, awaiting validation

### Upload supporting document

**Endpoint**: `POST /api/client/operations/{operationId}/document`

**Type**: `multipart/form-data`

**Parameters**:
- `file`: File (PDF, JPG, PNG, max 5 MB)

### Validate an operation (Agent)

**Approve**: `PUT /api/agent/operations/{id}/approve`

**Reject**: `PUT /api/agent/operations/{id}/reject`

### Examples with curl

```bash
# Create an account
curl -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test User","email":"test@example.com","password":"test123"}'

# Login
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test User","password":"test123"}'

# Create a deposit
curl -X POST http://localhost:8081/api/client/operations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"operationType":"DEPOSIT","amount":5000}'
```

---

## Project Structure

```
al-baraka/
├── src/
│   ├── main/
│   │   ├── java/com/ismail/al_baraka/
│   │   │   ├── config/              # Spring Security, JWT, OAuth2 configuration
│   │   │   ├── controller/          # REST and MVC controllers
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── Exception/           # Exception handling
│   │   │   ├── helper/              # Utilities (account number generation, etc.)
│   │   │   ├── mapper/              # MapStruct mappers
│   │   │   ├── model/               # JPA entities
│   │   │   │   └── enums/           # Enumerations (Role, Status, OperationType)
│   │   │   ├── repository/          # JPA repositories
│   │   │   └── service/             # Business services
│   │   └── resources/
│   │       ├── application*.properties  # Profile-based configuration
│   │       ├── templates/           # Thymeleaf templates
│   │       └── static/              # Static assets
│   └── test/                        # Unit and integration tests
├── keycloak-imports/                # Keycloak configuration
├── upload/                          # Document storage folder
├── docker-compose.yml               # Docker orchestration
├── Dockerfile                       # Application Docker image
├── pom.xml                          # Maven dependencies
└── README.md                        # This file
```

---

## Deployment and Hosting

### Cloud Infrastructure - DigitalOcean

This project is **hosted on DigitalOcean**, a cloud platform recognized for its reliability and ease of use. Production deployment was successfully completed on a Linux Droplet server.

### Deployment Process

Deploying this banking platform involved several complex technical steps, providing an in-depth learning experience in system administration and DevOps:

#### 1. **Server Configuration**
- **Provisioning**: Creation and configuration of an Ubuntu 22.04 LTS Droplet
- **Hardening**: Configuration of non-root users with sudo privileges
- **System Updates**: Application of the latest security updates
- **Dependency Installation**: Java 21, PostgreSQL 15, Docker, Docker Compose

#### 2. **SSH Access and Management**
- **Secure Connection**: Establishing SSH connections with key-based authentication
- **SSH Key Management**: Generation and configuration of public/private key pairs
- **SSH Configuration**: Customization of `~/.ssh/config` file for simplified access

#### 4. **Firewall Management (UFW)**
- **Rule Configuration**: Selective opening of necessary ports
  - Port 22: SSH
  - Port 80: HTTP
  - Port 443: HTTPS
- **Enhanced Security**: Blocking all other ports by default

#### 5. **Application Deployment**
- **Production Build**: Compiling the WAR with `mvnw clean package`
- **Server Transfer**: Upload via SCP
- **Environment Variable Configuration**: `.env` file for production
- **Docker Orchestration**: Deployment with Docker Compose
- **Service Management**: Systemd configuration for automatic startup

#### 6. **Database and Keycloak**
- **PostgreSQL**: Installation and configuration with dedicated users
- **Keycloak**: Authentication server deployment in Docker container
- **Realm Import**: Automatic configuration via `keycloak-imports/albaraka-realm.json`
- **Automatic Backup**: Daily backup scripts implementation

### Skills Acquired

This deployment process enabled the development of essential skills in:

| Domain | Skills |
|--------|--------|
| **Linux Administration** | Ubuntu server management, bash commands, process and service management |
| **Security** | SSH configuration, UFW firewall management, SSL/TLS certificates, access hardening |
| **DevOps** | Continuous deployment, configuration management, shell script automation |
| **Networking** | Nginx configuration, reverse proxy, port management, DNS |
| **Containerization** | Docker, Docker Compose, image and volume management |
| **Monitoring** | Log analysis, performance monitoring, incident resolution |

### Best Practices Implemented

- **Environment Separation**: Distinct profiles for dev/prod
- **Secret Management**: Secure environment variables and `.env` files
- **Mandatory HTTPS**: Encryption of all communications
- **Configured Firewall**: Minimal attack surface
- **Centralized Logs**: Facilitates debugging and auditing
- **Regular Updates**: System and dependencies up to date

### Lessons Learned

Deploying this application on DigitalOcean represented a **real technical challenge** and an **exceptional learning opportunity**. Beyond application development, this project allowed me to:

- Understand **production challenges**: high availability, security, performance
- Master **system administration tools**: SSH, Nginx, UFW, systemd
- Grasp **networking issues**: DNS, ports, protocols
- Develop a **DevOps approach**: automation, monitoring, continuous deployment
- Strengthen **application security**: HTTPS, firewall, robust authentication

This hands-on experience consolidated the understanding of the **complete lifecycle** of a modern application, from design to production deployment.

---

## Tests

### Running Tests

```bash
# All tests
./mvnw test

# Specific test class
./mvnw test -Dtest=UserServiceTest

# With code coverage
./mvnw clean test jacoco:report
```

### Test Types

The project includes:
- **Unit Tests**: Services, mappers, utilities
- **Integration Tests**: Repositories, controllers
- **Security Tests**: Authentication, authorization

---

## License

This project is distributed under the **GNU General Public License v3.0 (GPL-3.0)**.

You are free to:
- Use this software for commercial purposes
- Modify the source code
- Distribute copies
- Use this software privately

**Conditions**:
- Disclose the source code of modifications
- Include the license and copyright
- State changes made
- Use the same license (GPL-3.0) for derivatives

See the [LICENSE](LICENSE) file for more details.

---

## Credits

### Lead Developer

- **Ismail** - Initial development and architecture

### Technologies and Frameworks

This project relies on quality open-source technologies:

- [Spring Framework](https://spring.io/) - Enterprise Java framework
- [Keycloak](https://www.keycloak.org/) - Open-source IAM solution
- [PostgreSQL](https://www.postgresql.org/) - Relational database
- [MapStruct](https://mapstruct.org/) - Java mapper generator
- [Lombok](https://projectlombok.org/) - Java boilerplate reduction
- [Thymeleaf](https://www.thymeleaf.org/) - Template engine
- [Tailwind CSS](https://tailwindcss.com/) - Utility-first CSS framework

### Resources and Inspirations

- [Spring Security OAuth2 Documentation](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Baeldung Spring Tutorials](https://www.baeldung.com/spring-tutorial)

---

## Support and Contact

For any questions or suggestions:
- **Bugs**: Open an issue on GitHub
- **Improvements**: Submit a pull request
- **Contact**: ismailbaoud04@gmail.com

---

<div align="center">

**If this project was helpful to you, feel free to give it a star!**

Developed with passion by Ismail

</div>

