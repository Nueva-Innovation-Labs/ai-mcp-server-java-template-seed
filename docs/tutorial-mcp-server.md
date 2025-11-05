Creating an **MCP (Model Context Protocol) Server** involves setting up a service that exposes specific functions (tools) to a Large Language Model (LLM) so the AI can retrieve real-time data or execute actions.

Here is a comprehensive tutorial based on the Spring Boot application you created, using the **WebMVC** (HTTP/SSE) transport.

-----

# 📚 Spring AI MCP Server Tutorial

## 1\. 🎯 What is an MCP Server?

The **Model Context Protocol (MCP)** is a standardized protocol that allows AI models (the **Host**) to communicate with external applications (the **Server**) to access real-world data and capabilities.

* **MCP Server (Provider):** Your Spring Boot application. It wraps your business logic (e.g., database access) and exposes it as structured **tools**.
* **MCP Client:** A component inside the AI Host that manages the connection to your server.
* **Host (LLM Application):** The consumer (e.g., Claude Desktop, a custom AI agent) that uses the tools to generate context-aware responses.

## 2\. 🛠️ Project Setup (The Spring Boot Application)

Your project is already set up to be an MCP Server using Spring AI.

### Dependencies (`pom.xml`):

The core dependencies for an MCP Server using Spring MVC are:

| Dependency | Purpose |
| :--- | :--- |
| `spring-boot-starter-web` | Provides the web server to handle HTTP/SSE transport. |
| `spring-ai-starter-mcp-server-webmvc` | The Spring AI starter for exposing tools over the WebMVC (HTTP/SSE) transport. |
| `spring-boot-starter-data-jpa` | Used to connect to the PostgreSQL database. |
| `lombok` | Simplifies DTO/Entity creation. |
| `postgresql` | PostgreSQL JDBC driver. |

### Configuration (`application.yaml`):

The server is configured to run on the default port `8080` and connect to your database.

```yaml
spring:
  # ... (Database configuration is here)
  ai:
    mcp:
      server:
        # Give your MCP server a descriptive name
        name: contact-database-context
        version: 1.0.0
```

## 3\. ✍️ Creating the Context Functions (Tools)

The core of an MCP Server is defining a regular Java method as an AI-callable **tool**.

### Step A: Define the Data Model

Use `@Entity` and **Lombok's** `@Data` to create your JPA entities.

```java
// src/main/java/.../model/Contact.java
@Entity
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;
    // ... other fields (firstName, lastName, email, phoneNumber)
}
```

### Step B: Create the Context Service

The service contains the methods you want the AI to call.

| Annotation | Purpose |
| :--- | :--- |
| `@Service` | Standard Spring component definition. |
| `@McpContextFunction` | Marks a method as an executable function/tool for the AI. |
| `@McpContext` | Provides the metadata (name, description) that the AI model uses to decide *when* to call the tool. |

```java
// src/main/java/.../service/ContactContextService.java

@Service
public class ContactContextService {

    // ... Repositories injected via constructor

    /** 1. Contact information context */
    @McpContextFunction
    @McpContext(name = "getContactInfo", description = "Retrieves a contact's full details by email address.")
    public Optional<Contact> getContactInfo(String email) {
        return contactRepository.findByEmail(email);
    }

    /** 2. Address information context */
    @McpContextFunction
    @McpContext(name = "getContactAddresses", description = "Retrieves a list of addresses for a contact using their ID.")
    public List<Address> getContactAddresses(Long contactId) {
        return addressRepository.findByContactId(contactId);
    }
}
```

## 4\. ▶️ Running and Testing the MCP Server

### Step 1: Run the Database (Docker)

Ensure your database is running using `docker-compose up -d`.

### Step 2: Start the Spring Boot Application

Run your application from the terminal:

```bash
./mvnw spring-boot:run
```

The application will start, connect to the database, and the **DatabaseSeeder** will create 1000 records.

### Step 3: Test Tool Discovery

When the application starts, it exposes the tool specifications at a dedicated endpoint.

* **URL:** `http://localhost:8080/v1/ai/context/functions`
* **Method:** `GET`

You can access this URL in your browser or Postman. The response will be a JSON array containing the specifications for `getContactInfo` and `getContactAddresses`, which the AI Client uses for tool discovery.

### Step 4: Test Tool Invocation

To test a tool call as the AI Host would:

* **URL:** `http://localhost:8080/v1/ai/context/functions`
* **Method:** `POST`
* **Headers:** `Content-Type: application/json`

| Context Function | Request Body (for Postman) |
| :--- | :--- |
| **Contact Info** (`getContactInfo`) | `json\n{\n    "name": "getContactInfo",\n    "arguments": {\n        "email": "user1@example.com"\n    }\n}` |
| **Address Context** (`getContactAddresses`) | `json\n{\n    "name": "getContactAddresses",\n    "arguments": {\n        "contactId": 1\n    }\n}` |

The server will execute the corresponding Java method and return the JSON result, confirming your MCP server is fully operational.

-----

You can learn more about how to set up and define MCP tools by watching [Build Your Own MCP Server in Under 15 Minutes | Spring AI Tutorial](https://www.youtube.com/watch?v=w5YVHG1j3Co).
http://googleusercontent.com/youtube_content/0