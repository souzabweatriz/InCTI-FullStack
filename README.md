<div align="center">

# InCTI

### Plataforma de inteligência de dados para consultores

Aplicação web com frontend em Vue 3 e backend em Spring Boot, integrada ao PostgreSQL Flexible Server hospedado no Azure.

</div>

## Sobre o projeto

O InCTI permite importar planilhas de dados e visualizar uma prévia tratada para apoiar análises de clientes, serviços, contratos, insights e telemetrias.

O projeto é dividido em duas aplicações:

- **Frontend:** interface em Vue 3, Vue Router, Pinia, Vite e Tailwind CSS.
- **Backend:** API REST em Spring Boot com Spring Data JPA, validação e PostgreSQL.

## Tecnologias

### Frontend

- Vue 3
- Vue Router
- Pinia
- Vite
- Tailwind CSS
- PrimeVue e PrimeIcons
- SheetJS (`xlsx`) para leitura de `.xlsx`, `.xls` e `.csv`

### Backend

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA / Hibernate
- Bean Validation
- PostgreSQL JDBC Driver
- Maven Wrapper

### Infraestrutura

- Azure Database for PostgreSQL Flexible Server
- DBeaver para administração e consulta do banco

## Pré-requisitos

Instale:

- Java 21 ou superior
- Node.js e npm
- PostgreSQL ou acesso ao servidor PostgreSQL do Azure
- DBeaver, opcional, para consultar o banco

Confirme as versões:

```bash
java -version
node --version
npm --version
```

## Estrutura do projeto

```text
InCTI/
├── backend/
│   ├── src/main/java/com/incti/backend/
│   │   ├── controller/    # Endpoints HTTP
│   │   ├── model/         # Entidades JPA
│   │   ├── repository/    # Acesso ao banco
│   │   └── service/       # Regras de negócio
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── pom.xml
│   └── mvnw.cmd
├── frontend/
│   ├── src/
│   │   ├── components/    # Header, Sidebar e Footer
│   │   ├── router/        # Rotas do Vue Router
│   │   ├── stores/        # Estado global com Pinia
│   │   ├── services/      # Comunicação com a API
│   │   └── views/         # Telas da aplicação
│   ├── package.json
│   └── vite.config.js
└── README.md
```

## Configuração do banco

O backend usa PostgreSQL. Crie ou edite localmente o arquivo:

```text
backend/src/main/resources/application.properties
```

Exemplo para o Azure PostgreSQL Flexible Server:

```properties
spring.application.name=backend

spring.datasource.url=jdbc:postgresql://SEU_SERVIDOR.postgres.database.azure.com:5432/postgres?sslmode=require
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Não publique senhas, tokens ou chaves no repositório. O `application.properties` está no `.gitignore` raiz para evitar o versionamento de credenciais locais.

No Azure, antes de iniciar a aplicação:

1. Acesse o servidor PostgreSQL.
2. Abra **Rede**.
3. Habilite o acesso público, se necessário.
4. Adicione o IP atual do computador nas regras de firewall.
5. Salve e aguarde o servidor voltar ao estado **Ready**.
6. Use SSL com modo `require` no DBeaver.

## Executando o backend

No Windows:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
cd backend
./mvnw spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

Para apenas compilar:

```powershell
.\mvnw.cmd -DskipTests compile
```

## Executando o frontend

Em outro terminal:

```bash
cd frontend
npm install
npm run dev
```

O Vite exibirá o endereço local, normalmente:

```text
http://localhost:5173
```

Para gerar a versão de produção:

```bash
npm run build
```

## Rotas do frontend

| Rota | Tela |
|---|---|
| `/` | Página inicial |
| `/login` | Login |
| `/dashboard/upload` | Upload e prévia da planilha |
| `/dashboard/relatorios` | Relatórios |
| `/dashboard` | Redireciona para o upload |

## API de consultores

Base local:

```text
http://localhost:8080/consultores
```

### Listar consultores

```http
GET /consultores
```

### Buscar consultor por ID

```http
GET /consultores/{id}
```

### Criar consultor

```http
POST /consultores
Content-Type: application/json
```

```json
{
	"nome": "Ana Beatriz",
	"matricula": "12345",
	"email": "ana@example.com",
	"senha": "senha-segura"
}
```

### Fazer login

```http
POST /consultores/login
Content-Type: application/json
```

```json
{
	"email": "ana@example.com",
	"senha": "senha-segura"
}
```

### Atualizar consultor

```http
PUT /consultores/{id}
Content-Type: application/json
```

```json
{
	"nome": "Ana Beatriz Atualizada",
	"matricula": "12345",
	"email": "ana@example.com",
	"senha": "nova-senha"
}
```

### Excluir consultor

```http
DELETE /consultores/{id}
```

## Entidades do banco

As entidades JPA atuais são:

- `consultores`
- `clientes`
- `contratos`
- `insights`
- `servicos`
- `telemetrias`

Com `spring.jpa.hibernate.ddl-auto=update`, o Hibernate cria ou atualiza a estrutura das tabelas ao iniciar o backend. Em produção, prefira migrations versionadas para controlar alterações de schema.

## Upload de planilhas

Na tela `/dashboard/upload`, o frontend aceita:

- `.xlsx`
- `.xls`
- `.csv`

O arquivo é lido no navegador pela biblioteca SheetJS. A aplicação:

1. Valida a extensão.
2. Lê a primeira aba da planilha.
3. Remove espaços extras de valores textuais.
4. Padroniza alguns valores de `segmento`.
5. Exibe linhas, colunas e uma prévia dos dados tratados.

Atualmente, a leitura da planilha não envia os dados para a API automaticamente.

## Arquitetura do backend

O fluxo de uma requisição é:

```text
Cliente HTTP
		↓
Controller  → recebe a requisição e devolve a resposta
		↓
Service    → aplica regras de negócio
		↓
Repository → acessa o PostgreSQL via Spring Data JPA
		↓
Banco de dados
```

Essa separação mantém HTTP, regras de negócio e persistência organizados em responsabilidades diferentes.

## Testes manuais

Você pode testar a API usando Thunder Client, Postman ou Insomnia.

Para requisições `POST` e `PUT`, envie o header:

```text
Content-Type: application/json
```

Verifique os registros no DBeaver em:

```text
public → Tables → consultores → View Data
```

Consulta de exemplo:

```sql
SELECT id, nome, matricula, email
FROM public.consultores
ORDER BY id;
```

## Solução de problemas

### `Read timed out` no PostgreSQL

Confirme se:

- O servidor Azure está com estado **Ready**.
- O endpoint está correto.
- A porta é `5432`.
- O IP atual está liberado no firewall da Azure.
- O SSL está configurado como `require`.
- A rede atual não está bloqueando conexões PostgreSQL.

### `401 Unauthorized` no login

Confirme se o e-mail existe e se a senha enviada corresponde ao cadastro.

### `404 Not Found`

Confira o caminho da requisição. Por exemplo, use `/consultores` no plural.

### `415 Unsupported Media Type`

Adicione `Content-Type: application/json` em requisições que enviam JSON.

## Segurança

- Não versione senhas reais.
- Não use `AllowAll` no firewall em produção.
- Troque senhas que tenham sido expostas.
- Para produção, use variáveis de ambiente ou um serviço de secrets.
- Evite armazenar senhas em texto puro; use hash com BCrypt antes de disponibilizar o login em produção.
