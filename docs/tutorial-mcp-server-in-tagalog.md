-----

# 🇵🇭 `tutorial-mcp-server.md`: Paggawa at Paggamit ng Spring AI MCP Server (Tagalog Tutorial)

## 1\. 🎯 Ano ang MCP Server? (Model Context Protocol)

Ang **Model Context Protocol (MCP)** ay isang standardized protocol na nagbibigay-daan sa mga AI model (ang **Host**) na makipag-ugnayan sa mga panlabas na application (ang **Server**) para makakuha ng **real-time na data** o mag-execute ng mga aksyon.

* **MCP Server (Provider):** Ito ang iyong **Spring Boot** application. Sinasalo nito ang iyong business logic (tulad ng pag-access sa database) at inilalantad ito bilang naka-structure na **tools** (mga function).
* **MCP Client:** Isang bahagi sa loob ng AI Host na namamahala sa koneksyon sa iyong server.
* **Host (LLM Application):** Ang gumagamit (hal. Claude Desktop, isang custom AI agent) na gumagamit ng mga tool upang makabuo ng mga tugon na may tamang konteksto.

Ang layunin ay payagan ang AI na magtanong ng tulad nito: *"Ano ang address ni user1@example.com?"* Sa halip na hulaan, tatawagin ng AI ang iyong `getContactAddresses` tool para kunin ang sagot mula sa iyong PostgreSQL database.

-----

## 2\. 🛠️ Project Setup: Paghahanda ng Spring Boot

Ang iyong proyekto ay naka-set up na bilang isang MCP Server gamit ang Spring AI. Ang pangunahing ginagamit ay ang **WebMVC** (HTTP/SSE) transport.

### Dependencies (Mga Kailangan sa `pom.xml`):

Ang mga sumusunod na dependency ay mahalaga:

| Dependency | Layunin |
| :--- | :--- |
| `spring-boot-starter-web` | Nagbibigay ng web server para sa HTTP/SSE communication. |
| `spring-ai-starter-mcp-server-webmvc` | Ang Spring AI starter para ilantad ang mga tool gamit ang WebMVC (HTTP/SSE). |
| `spring-boot-starter-data-jpa` | Ginagamit upang kumonekta sa PostgreSQL database. |
| `lombok` | Nagpapadali sa paggawa ng DTO/Entity classes. |
| `postgresql` | Ang PostgreSQL JDBC driver. |

### Configuration (`application.yaml`):

Ang configuration ay tumutukoy kung paano makikilala ang iyong server at kung paano ito kumonekta sa database.

```yaml
spring:
  # -------------------------------------------------------------------
  # Database Configuration (PostgreSQL) - Makikita sa application.yaml
  # -------------------------------------------------------------------
  datasource:
    url: jdbc:postgresql://localhost:5432/ai_database
    username: user1234
    password: password1234
    # ... iba pang settings ...
    
  # -------------------------------------------------------------------
  # MCP Server Configuration
  # -------------------------------------------------------------------
  ai:
    mcp:
      server:
        # Tiyakin na mayroon itong natatanging pangalan na gagamitin ng AI Client
        name: contact-database-context 
        version: 1.0.0 
```

-----

## 3\. ✍️ Paggawa ng Context Functions (Tools)

Ang mga method na ito ay mga karaniwang serbisyo ng Spring, ngunit may idinagdag na annotation para makilala ng MCP.

### Step A: Model at Repository

Ang `Contact.java`, `Address.java`, at ang mga `JpaRepository` ay kumakatawan sa iyong data layer (Ang mga entity at repository na iyong ginawa).

### Step B: I-expose ang mga Function

Gamitin ang `@McpContextFunction` at `@McpContext` para i-declare ang iyong mga method bilang AI tools.

```java
// src/main/java/.../service/ContactContextService.java

@Service
public class ContactContextService {

    // ... Repositories injected

    /** 1. Context ng Impormasyon ng Contact */
    @McpContextFunction
    @McpContext(name = "getContactInfo", description = "Kumukuha ng pangalan, email, at phone number ng isang contact gamit ang email address.")
    public Optional<Contact> getContactInfo(String email) {
        // ... Logic na gumagamit ng contactRepository.findByEmail(email)
    }

    /** 2. Context ng Impormasyon ng Address */
    @McpContextFunction
    @McpContext(name = "getContactAddresses", description = "Kumukuha ng listahan ng mga address (kalye, lungsod, zip code) ng isang contact gamit ang kanilang ID.")
    public List<Address> getContactAddresses(Long contactId) {
        // ... Logic na gumagamit ng addressRepository.findByContactId(contactId)
    }
}
```

-----

## 4\. ▶️ Pagsisimula at Pagsusuri (Testing)

### Step 1: Simulan ang Database (Gamit ang Docker)

Siguraduhin na tumatakbo ang iyong PostgreSQL database container:

```bash
docker-compose up -d
```

### Step 2: Simulan ang Spring Boot Application

Patakbuhin ang iyong application:

```bash
./mvnw spring-boot:run
```

Sa pag-start, isasagawa ng `DatabaseSeeder` ang paggawa ng 1000 records sa database.

### Step 3: Subukan ang Tool Discovery

Maaaring makita ng isang AI Client kung anong mga tool ang inilalantad ng iyong server sa pamamagitan ng pag-access sa endpoint na ito:

* **URL:** `http://localhost:8080/v1/ai/context/functions`
* **Method:** `GET`

Kapag binuksan mo ito sa browser, makikita mo ang JSON specifications para sa `getContactInfo` at `getContactAddresses`.

### Step 4: Subukan ang Tool Invocation (Gamit ang Postman)

Ito ang paraan ng pagtawag ng AI Host sa iyong mga function. Gagamitin natin ang Postman (o ang provided na `ai-mcp-context-postman.json`) para dito.

* **URL:** `http://localhost:8080/v1/ai/context/functions`
* **Method:** `POST`
* **Headers:** `Content-Type: application/json`

| Function na Tatawagin | Request Body (Halimbawa) |
| :--- | :--- |
| **getContactInfo** | `json\n{\n    "name": "getContactInfo",\n    "arguments": {\n        "email": "user1@example.com"\n    }\n}` |
| **getContactAddresses** | `json\n{\n    "name": "getContactAddresses",\n    "arguments": {\n        "contactId": 1\n    }\n}` |

Kapag nag-POST ka, isasagawa ng iyong Spring Boot server ang kaukulang Java method at ibabalik ang resulta sa JSON format. Ito ay nagpapatunay na gumagana na ang iyong MCP Server\!

-----

*Ang tutorial na ito ay sumasaklaw sa pag-set up ng **MCP Server**. Para magamit ito ng isang Large Language Model, kailangan mo ng isang **MCP Client** (na maaaring isang pangalawang Spring AI application o isang AI development environment tulad ng Claude Desktop) na kokonekta sa `http://localhost:8080` endpoint.*