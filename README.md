# 💱 CriptoValor API

API RESTful desarrollada con Spring Boot para gestionar monedas fiat, criptomonedas y sus valores históricos.

---

## 🚀 Instalación

### Requisitos

- Java 21
- Maven 3.8+
- PostgreSQL (o cualquier base de datos compatible con JPA)

### Pasos

1. Clona el repositorio:

   ```bash
   git clone https://github.com/ilichgithub/bt-backend.git
   cd bt-backend

2. Instala Postgres y crea la base de datos:

   ```bash
   configurar en el archivo src/main/resources/application.properties
   las credenciales para acceder a la base de datos
   ajustar el nombre de la base de datos el usuario y la contraseña
   db-bt es el nombre de la base de datos actual
   
   spring.datasource.url=jdbc:postgresql://localhost:5432/db-bt
   spring.datasource.username=postgres
   spring.datasource.password=testpg

3. Una vez ya con el repositorio clonado y configurado con las credenciales para la base de datos:

   ```bash
   # Compilar el proyecto y descargar dependencias
   mvn clean install
   # y para ejecutar
   mvn spring-boot:run
   
   # recordemos validar la version de Java es 21
   # Nota: La API se ejecutará en http://localhost:8080 por defecto.
   
4. Los endpoints son:

   ```bash
    🔐 Endpoints de autenticación
    # Registro de usuario 
    
    curl -X POST http://localhost:8080/auth/register \
    -H "Content-Type: application/json" \
    -d '{
    "email": "ilich@example.com",
    "password": "MiClaveSegura123"
    }'
    
    # Login de usuario
    curl -X POST http://localhost:8080/auth/login \
    -H "Content-Type: application/json" \
    -d '{
    "email": "ilich@example.com",
    "password": "MiClaveSegura123"
    }'
   
    💵 Endpoints de Moneda (con autenticacion JWT)
    # Crear una moneda

    curl -X POST http://localhost:8080/moneda \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer TU_TOKEN_JWT" \
    -d '{
    "nombre": "Dólar",
    "codigo": "USD",
    "simbolo": "$"
    }'
   
    # listar las monedas
    curl http://localhost:8080/moneda \
    -H "Authorization: Bearer TU_TOKEN_JWT"
   
    # Crear una criptomoneda asociada a una moneda
    curl -X POST http://localhost:8080/criptomonedas \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer TU_TOKEN_JWT" \
    -d '{
    "nombre": "Bitcoin",
    "codigo": "BTC",
    "monedaId": 1
    }'
   
    # Actualizar una criptomoneda y registrar valores históricos
    curl -X PUT http://localhost:8080/criptomonedas/1 \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer TU_TOKEN_JWT" \
    -d '{
    "nombre": "Bitcoin",
    "codigo": "BTC",
    "valores": [
    { "monedaId": 1, "valor": 27350.75 },
    { "monedaId": 2, "valor": 25000.00 }
    ]
    }'
   
    # Listar todas las criptomonedas con sus monedas y último valor
    curl http://localhost:8080/criptomoneda \
    -H "Authorization: Bearer TU_TOKEN_JWT"
   
    # Filtrar criptomonedas por moneda específica
    curl http://localhost:8080/criptomoneda?moneda=USD \
    -H "Authorization: Bearer TU_TOKEN_JWT"

📊 Modelo de Datos (Esquema simplificado)\
El diseño de la base de datos sigue un modelo relacional para gestionar la complejidad de los valores históricos.

Entidad	Campos Clave y Relaciones\
Usuario	id (PK), email, password\
Moneda	id (PK), nombre, codigo, simbolo\
Criptomoneda	id (PK), nombre, codigo\
Criptomoneda_Moneda	Tabla de unión para relación Many-to-Many entre Criptomoneda y Moneda.\
ValorHistorico	id (PK), criptomoneda_id (FK), moneda_id (FK), valor, fecha


📚 Licencia
©2025 Ilich