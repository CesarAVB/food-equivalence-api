# Food Equivalence API

Sistema de cálculo de equivalências alimentares baseado em dados nutricionais da TACO, permitindo substituições de alimentos por equivalentes calóricos no mesmo grupo alimentar.

## 📋 Descrição

A **Food Equivalence API** é uma solução REST que facilita a busca por alimentos equivalentes em valor calórico. Nutricionistas e aplicações de saúde podem usar esta API para fornecer alternativas alimentares mantendo o mesmo aporte calórico.

### Funcionalidade Principal

- Selecione um alimento e uma quantidade em gramas
- A API calcula o total de calorias
- Retorna uma lista de alimentos do mesmo grupo com quantidades equivalentes em calorias
- Resultados ordenados por proximidade calórica

## 🚀 Tecnologias

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **MySQL**
- **Lombok**
- **Maven**

## 📦 Dependências

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

## 🔧 Configuração

### 1. Pré-requisitos

- Java 17 ou superior
- MySQL 8.0 ou superior
- Maven 3.6+

### 2. Clonar o repositório

```bash
git clone https://github.com/seu-usuario/food-equivalence-api.git
cd food-equivalence-api
```

### 3. Configurar banco de dados

Edite `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/food_equivalence
    username: seu_usuario
    password: sua_senha
```

### 4. Criar banco de dados

```sql
CREATE DATABASE food_equivalence CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 5. Criar tabela

```sql
USE food_equivalence;

CREATE TABLE tbl_alimentos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo_taco VARCHAR(10) NOT NULL,
    grupo VARCHAR(100) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    umidade DECIMAL(10,2),
    energia_kcal DECIMAL(10,2) NOT NULL,
    energia_kj DECIMAL(10,2),
    proteina_g DECIMAL(10,2),
    lipideos_g DECIMAL(10,2),
    colesterol_mg DECIMAL(10,2),
    carboidratos_g DECIMAL(10,2),
    fibra_alimentar_g DECIMAL(10,2),
    cinzas_g DECIMAL(10,2),
    observacao TEXT,
    fonte VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_grupo (grupo)
);
```

### 6. Inserir dados TACO

Importe os dados da Tabela de Composição de Alimentos (TACO) brasileira na tabela `tbl_alimentos`.

### 7. Build e execução

```bash
# Build
mvn clean install

# Executar
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 📚 Documentação da API

### Endpoint Principal

#### **POST** `/api/v1/equivalencias/calcular`

Calcula alimentos equivalentes em calorias para um alimento selecionado.

**Request Body:**

```json
{
    "alimentoId": 1,
    "quantidade": 100
}
```

**Parâmetros:**

| Campo | Tipo | Descrição | Obrigatório |
|-------|------|-----------|-------------|
| `alimentoId` | Integer | ID do alimento na TACO | Sim |
| `quantidade` | Double | Quantidade em gramas | Sim |

**Response (200 OK):**

```json
{
    "alimentoSelecionado": {
        "id": 1,
        "codigoTaco": "1",
        "grupo": "CEREAIS_E_DERIVADOS",
        "descricao": "Arroz, integral, cozido",
        "energiaKcal": 123.53
    },
    "quantidade": 100.0,
    "calorias": 123.53,
    "equivalentes": [
        {
            "id": 7,
            "descricao": "Aveia, flocos, crua",
            "quantidadeEquivalenteG": 31.36,
            "kcalPor100g": 393.82,
            "kcalTotais": 123.53,
            "diferencaPercentual": 0.0
        },
        {
            "id": 64,
            "descricao": "Abóbora, cabotian, cozida",
            "quantidadeEquivalenteG": 257.27,
            "kcalPor100g": 48.04,
            "kcalTotais": 123.53,
            "diferencaPercentual": 0.0
        }
    ]
}
```

### Response Fields

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `alimentoSelecionado` | Object | Dados do alimento selecionado |
| `quantidade` | Double | Quantidade em gramas informada |
| `calorias` | Double | Total de calorias calculadas |
| `equivalentes` | Array | Lista de equivalentes ordenados por proximidade calórica |
| `diferencaPercentual` | Double | Margem de erro em relação às calorias calculadas (%) |

### Codes de Erro

| Code | Erro | Descrição |
|------|------|-----------|
| 400 | `Quantidade inválida` | Quantidade deve ser maior que 0 |
| 400 | `Erro de validação` | Campos obrigatórios não preenchidos |
| 404 | `Alimento não encontrado` | ID do alimento não existe na base |
| 500 | `Erro interno do servidor` | Erro inesperado no processamento |

## 📊 Exemplo de Uso

### cURL

```bash
curl -X POST http://localhost:8080/api/v1/equivalencias/calcular \
  -H "Content-Type: application/json" \
  -d '{
    "alimentoId": 1,
    "quantidade": 100
  }'
```

### JavaScript/Fetch

```javascript
const request = {
    alimentoId: 1,
    quantidade: 100
};

fetch('http://localhost:8080/api/v1/equivalencias/calcular', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify(request)
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error('Erro:', error));
```

### Python/Requests

```python
import requests
import json

url = "http://localhost:8080/api/v1/equivalencias/calcular"
payload = {
    "alimentoId": 1,
    "quantidade": 100
}

response = requests.post(url, json=payload)
print(json.dumps(response.json(), indent=2))
```

## 🏗️ Estrutura do Projeto

```
src/main/java/br/com/sistema/equivalence/
├── controller/
│   └── EquivalenciaController.java        # Endpoints REST
├── service/
│   └── EquivalenciaService.java           # Lógica de cálculo
├── repository/
│   └── AlimentoRepository.java            # Acesso ao banco
├── entity/
│   ├── Alimento.java                      # Entidade JPA
│   └── GrupoAlimentar.java                # Enum de grupos
├── dtos/
│   ├── request/
│   │   └── CalcularEquivalenciasRequest.java
│   └── response/
│       ├── AlimentoDTO.java
│       ├── EquivalenteDTO.java
│       └── EquivalenciaResponse.java
├── exception/
│   ├── AlimentoNaoEncontradoException.java
│   ├── QuantidadeInvalidaException.java
│   └── GlobalExceptionHandler.java
├── config/
│   └── JpaConfig.java
└── FoodEquivalenceApplication.java        # Classe principal
```

## 🧮 Fórmulas Utilizadas

### Cálculo de Calorias Totais

```
calorias_totais = (quantidade × kcal_por_100g) / 100
```

### Cálculo de Quantidade Equivalente

```
quantidade_equivalente = (calorias_totais × 100) / kcal_equivalente_por_100g
```

### Cálculo de Diferença Percentual

```
diferenca_percentual = |calorias_totais - calorias_equivalente| / calorias_totais × 100
```

## 📝 Grupos Alimentares Suportados

- Cereais e derivados
- Verduras, hortaliças e derivados
- Frutas e derivados
- Gorduras e óleos
- Pescados e frutos do mar
- Carnes e derivados
- Leite e derivados
- Bebidas (alcoólicas e não alcoólicas)
- Ovos e derivados
- Produtos açucarados
- Miscelâneas
- Outros alimentos industrializados
- Alimentos preparados
- Leguminosas e derivados
- Nozes e sementes

## 🔐 Tratamento de Erros

A API implementa tratamento global de exceções com respostas padronizadas em JSON incluindo:

- Timestamp da requisição
- Status HTTP
- Tipo de erro
- Mensagem descritiva
- Campos inválidos (quando aplicável)

## 📦 Fonte de Dados

Os dados nutricionais utilizados são provenientes da **Tabela de Composição de Alimentos (TACO)** - Edição 4ª - desenvolvida pela Universidade Federal de São Paulo (UNIFESP).

Referência: http://www.nepa.mpsp.mp.br/

## 🤝 Contribuindo

1. Faça um Fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

---

**Desenvolvido para nutricionistas e aplicações de saúde**