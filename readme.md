# 🍎 Food Equivalence API - Backend

Sistema de equivalência alimentar por calorias com Spring Boot e MySQL.

## 📋 Descrição do Projeto

API REST que permite calcular equivalências entre alimentos do mesmo grupo com base em suas calorias. Usuários podem buscar alimentos por grupo ou descrição e obter sugestões de substituições equivalentes em termos nutricionais.

**Exemplo:** Se o usuário seleciona 100g de Arroz (125 kcal), o sistema sugere quantos gramas de outros carboidratos forneceriam a mesma quantidade de calorias.

---

## 🏗️ Arquitetura

### Banco de Dados
- **Tabela:** `tbl_substituicao`
- **Colunas:**
  - `id` (INT, PK, Auto-increment)
  - `codigo_substituicao` (VARCHAR 20, Unique)
  - `grupo` (ENUM: 'Carboidratos', 'Frutas', 'Gordura Vegetal', 'Laticíneos', 'Proteína')
  - `descricao` (VARCHAR 255)
  - `energia_kcal` (DECIMAL 10,2)
  - `created_at` (TIMESTAMP)
  - `updated_at` (TIMESTAMP)

### Estrutura do Projeto

```
src/main/java/br/com/sistema/equivalence/
├── converter/
│   └── GrupoAlimentarConverter.java
├── controller/
│   └── EquivalenciaController.java
├── entity/
│   └── Alimento.java
├── enums/
│   └── GrupoAlimentar.java
├── repository/
│   └── AlimentoRepository.java
├── service/
│   └── EquivalenciaService.java
├── dtos/
│   ├── request/
│   │   └── CalcularEquivalenciasRequest.java
│   └── response/
│       ├── AlimentoDTO.java
│       ├── AlimentoListaDTO.java
│       ├── EquivalenciaResponse.java
│       └── EquivalenteDTO.java
├── exception/
│   ├── AlimentoNaoEncontradoException.java
│   └── QuantidadeInvalidaException.java
└── Startup.java
```

---

## 🔑 Componentes Principais

### 1. Entity - Alimento
- Mapeia a tabela `tbl_substituicao`
- Campo `grupo` é um **Enum** (`GrupoAlimentar`)
- Usa **Converter JPA** para mapear Enum ↔ String/ENUM do banco

### 2. Enum - GrupoAlimentar
```java
CARBOIDRATOS("Carboidratos")
FRUTAS("Frutas")
GORDURA_VEGETAL("Gordura Vegetal")
LATICINEOS("Laticíneos")
PROTEINA("Proteína")
```
- Cada valor tem uma descrição que corresponde aos valores do banco

### 3. Converter - GrupoAlimentarConverter
- Converte Enum → String para salvar no banco
- Converte String → Enum ao carregar do banco
- Usa o método `getDescricao()` do Enum

### 4. Repository
- Usa Spring Data JPA
- Métodos com `Sort` para ordenação alfabética
- Query nativa para grupos distintos

### 5. Service
- Lógica de negócio
- Cálculo de equivalências por caloria
- Retorna DTOs tipados

### 6. Controller
- Endpoints REST
- Conversão de descrição para Enum
- Tratamento de erros

---

## 📡 Endpoints

### 1. Listar Grupos Disponíveis
```
GET /api/v1/equivalencias/grupos
```
**Resposta:**
```json
[
  "Carboidratos",
  "Frutas",
  "Gordura Vegetal",
  "Laticíneos",
  "Proteína"
]
```

---

### 2. Listar Alimentos por Grupo
```
GET /api/v1/equivalencias/alimentos/grupo/{grupo}
```
**Exemplo:** `GET /api/v1/equivalencias/alimentos/grupo/Carboidratos`

**Resposta:**
```json
[
  {
    "id": 1,
    "descricao": "Abóbora Cabotian",
    "grupo": "Carboidratos",
    "energiaKcal": 55.0
  },
  {
    "id": 2,
    "descricao": "Água de Coco",
    "grupo": "Carboidratos",
    "energiaKcal": 21.0
  }
]
```
**Ordenação:** Alfabética por descrição (ASC)

---

### 3. Buscar Alimentos por Descrição
```
GET /api/v1/equivalencias/alimentos/buscar?descricao={termo}
```
**Exemplo:** `GET /api/v1/equivalencias/alimentos/buscar?descricao=banana`

**Resposta:**
```json
[
  {
    "id": 5,
    "descricao": "Banana",
    "grupo": "Frutas",
    "energiaKcal": 91.0
  }
]
```
**Ordenação:** Alfabética por descrição (ASC)

---

### 4. Calcular Equivalências
```
POST /api/v1/equivalencias/calcular
Content-Type: application/json

{
  "alimentoId": 3,
  "quantidade": 100
}
```

**Resposta:**
```json
{
  "alimentoSelecionado": {
    "id": 3,
    "codigoTaco": "CARB.003",
    "grupo": "CARBOIDRATOS",
    "descricao": "Arroz branco ou integral",
    "energiaKcal": 125.0
  },
  "quantidade": 100.0,
  "calorias": 125.0,
  "equivalentes": [
    {
      "id": 8,
      "descricao": "Cuscuz (desidratado)",
      "quantidadeEquivalenteG": 33.2,
      "kcalPor100g": 376.0,
      "kcalTotais": 125.0,
      "diferencaPercentual": 2.76
    }
  ]
}
```

---

## 🗄️ Banco de Dados

### DDL - Criar Tabela
```sql
CREATE TABLE tbl_substituicao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_substituicao VARCHAR(20) NOT NULL UNIQUE,
    grupo ENUM(
        'Carboidratos',
        'Frutas',
        'Gordura Vegetal',
        'Laticíneos',
        'Proteína'
    ) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    energia_kcal DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_grupo (grupo),
    INDEX idx_descricao (descricao)
);
```

### Dados - 95 Alimentos

| Grupo | Quantidade |
|-------|-----------|
| Frutas | 31 |
| Carboidratos | 27 |
| Proteína | 15 |
| Laticíneos | 16 |
| Gordura Vegetal | 6 |
| **Total** | **95** |

---

## 🔧 Tecnologias

- **Framework:** Spring Boot 3.x
- **Banco de Dados:** MySQL 8.x
- **ORM:** JPA/Hibernate
- **Build:** Maven
- **Java:** 17+
- **Lombok:** Redução de boilerplate

---

## 📦 Dependências Principais

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

---

## ⚙️ Configuração (application.properties)

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco
spring.datasource.username=root
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Logging
logging.level.br.com.sistema.equivalence=INFO
```

---

## 🚀 Como Executar

### 1. Clonar o repositório
```bash
git clone https://github.com/seu-usuario/food-equivalence-api.git
cd food-equivalence-api
```

### 2. Configurar banco de dados
```bash
# Executar script de criação
mysql -u root -p < src/main/resources/schema.sql
```

### 3. Instalar dependências
```bash
mvn clean install
```

### 4. Executar a aplicação
```bash
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

---

## 🧪 Testes com Postman/Insomnia

### 1. Listar grupos
```
GET http://localhost:8080/api/v1/equivalencias/grupos
```

### 2. Listar alimentos de um grupo
```
GET http://localhost:8080/api/v1/equivalencias/alimentos/grupo/Frutas
```

### 3. Buscar alimento
```
GET http://localhost:8080/api/v1/equivalencias/alimentos/buscar?descricao=banana
```

### 4. Calcular equivalência
```
POST http://localhost:8080/api/v1/equivalencias/calcular
Body:
{
  "alimentoId": 1,
  "quantidade": 100
}
```

---

## 🔐 Segurança

- ✅ Validação de entrada em DTOs
- ✅ Tratamento de exceções customizadas
- ✅ CORS habilitado para frontend
- ⚠️ Em produção, adicionar autenticação/autorização

---

## 📝 Detalhes Técnicos

### Converter JPA
O `GrupoAlimentarConverter` é responsável por:
- Converter `GrupoAlimentar.CARBOIDRATOS` → `'Carboidratos'` (ao salvar)
- Converter `'Carboidratos'` → `GrupoAlimentar.CARBOIDRATOS` (ao carregar)

Isso permite que a Entity use **Enum** enquanto o banco armazena **String/ENUM**.

### Ordenação
Todos os endpoints que retornam listas usam:
```java
Sort sort = Sort.by("descricao").ascending();
```

### Cálculo de Equivalência
```
Calorias do alimento = (energiaKcal / 100) × quantidade
Quantidade equivalente = (calorias alvo × 100) / energiaKcal
```

---

## 🐛 Troubleshooting

### Erro: "No enum constant"
**Causa:** Mismatch entre Enum Java e valores do banco
**Solução:** Verificar se o Converter está sendo aplicado corretamente

### Erro: "Alimento não encontrado"
**Causa:** ID inválido na requisição
**Solução:** Verificar IDs disponíveis no banco

### Erro: CORS
**Causa:** Frontend em domínio diferente
**Solução:** Já configurado com `@CrossOrigin` nos Controllers (se necessário)

---

## 📚 Documentação Adicional

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [MySQL Documentation](https://dev.mysql.com/doc/)

---

## 👨‍💻 Autor

**César Augusto Vieira Bezerra**
- Portfolio: https://portfolio.cesaraugusto.dev.br/
- API Produção: https://api-subistituicao.cesaravb.com.br

---

## 📄 Licença

Este projeto está sob licença MIT.
