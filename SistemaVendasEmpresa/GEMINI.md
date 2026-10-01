# Sistema de Vendas Empresa - Project Documentation

## Project Overview
A Java-based sales management system (PDV/ERP style) featuring both a Graphical User Interface (JavaFX) and a Command-Line Interface for administrative tasks. The system manages customers, products, sellers, and sales transactions with real-time stock updates.

### Core Architecture
- **Pattern:** Model-View-Controller (MVC) with a dedicated Repository layer.
- **Models:** Java POJOs representing the domain entities (Client, Product, Sale, Seller, etc.).
- **Views:** JavaFX FXML files for the UI and corresponding Controllers for logic.
- **Repositories:** Direct database access using JDBC (PostgreSQL).
- **Utilities:** Specialized classes for document validation (CPF/CNPJ).

## Technical Stack
- **Runtime:** Java 25
- **GUI Framework:** JavaFX 21 (Controls, FXML)
- **Database:** PostgreSQL (JDBC Driver 42.7.2)
- **Serialization:** Google Gson (for JSON processing, possibly CNPJ API integration)
- **Build Tool:** Maven

## Database Configuration
The application connects to a local PostgreSQL instance.

- **Database Name:** `sistema_vendas`
- **User:** `postgres` (override with `DB_USER`)
- **Password:** set via the `DB_PASSWORD` environment variable
- **Connection URL:** `jdbc:postgresql://localhost:5432/sistema_vendas` (override with `DB_URL`)

### Key Tables
- `clientes`: Stores customer data including CPF/CNPJ.
- `vendedores`: Stores seller information and commission percentages.
- `produtos`: Manages inventory and pricing.
- `vendas`: Transaction headers (subtotal, discount, total, payment method).
- `itens_venda`: Transaction details (product, quantity, subtotal).

## Building and Running

### Prerequisites
- Java 25+ Installed.
- Maven Installed.
- PostgreSQL running locally with the database `sistema_vendas` created.

### Commands
- **Compile:** `mvn clean compile`
- **Run GUI (Main Application):** `mvn javafx:run` (Entry point: `br.com.empresa.App`)
- **Run Admin CLI:** Run the class `br.com.empresa.Main` using your IDE or `mvn exec:java -Dexec.mainClass="br.com.empresa.Main"`

## Development Conventions
- **Database Access:** Use the `Repository` classes. Avoid writing SQL directly in Controllers.
- **Transactions:** Sales transactions are handled atomically in `VendaRepository.salvar()`, ensuring that sales headers, items, and stock updates either all succeed or all fail.
- **Validation:** Use `ValidadorDocumento` for verifying CPF/CNPJ before saving customers.
- **Module System:** The project uses JPMS (`module-info.java`). Any new dependency or package must be correctly exported/opened there.
