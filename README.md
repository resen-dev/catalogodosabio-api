# Catálogo do Sábio - API REST

API REST desenvolvida para uma livraria independente, permitindo a navegação de livros por gênero e autor. A aplicação é escalável, com dados fictícios gerados automaticamente e persistência via banco de dados relacional.

---

## I. Arquitetura de Solução e Arquitetura Técnica

### Tecnologias Utilizadas
- **Java 21**
- **Spring Boot**
- **Spring Security + JWT**
- **Spring Data JPA**
- **PostgreSQL** (via Docker)
- **Redis** (cache, via Docker)
- **Flyway** (controle de versões do banco)
- **Java Faker** (geração de dados fake)
- **Docker Compose** (ambiente completo)
- **JUnit** e **Spring Test** (testes unitários e de repositório)

### Estrutura do Projeto
A arquitetura do sistema segue o padrão de camadas:
- **Controller**: Camada de entrada, responsável pelos endpoints REST.
- **Service**: Camada de lógica de negócio.
- **Repository**: Acesso ao banco de dados via Spring Data JPA.
- **Domain**: Entidades JPA.
- **DTOs**: Representações de dados de entrada/saída.
- **Mapper**: Conversão entre entidades e DTOs.
- **Handler**: Tratamento global de exceções.
- **Config**: Configuração de beans como security e faker.

---

## II. Funcionalidades Implementadas

### Segurança e Autenticação

A segurança da aplicação é implementada com **Spring Security 6**, utilizando **JWT (JSON Web Token)** para autenticação stateless (sem sessões em servidor). O sistema define dois tipos de usuários:

- `ROLE_USER`: criado via endpoint público `/api/v1/auth/register`
- `ROLE_ADMIN`: carregado automaticamente via seeder `AdminUserSeeder`

**Credenciais do User Admin para testes:**
```json
{
  "email": "admin@admin.com",
  "password": "admin123"
}
```

### Endpoints REST

**Autorização  de Acesso dos Endpoints:**
- 🔓 Público (sem autenticação)
- 🔐 Requer login (qualquer usuário autenticado)
- 🛡️ Requer perfil ADMIN

##### AuthController
| Método | Endpoint                  | Acesso   | Descrição                        |
|--------|---------------------------|----------|----------------------------------|
| POST   | `/api/v1/auth/register`   | 🔓       | Registro de novo usuário         |
| POST   | `/api/v1/auth/login`      | 🔓       | Login e obtenção de JWT          |

##### BooksController
| Método | Endpoint                                  | Acesso   | Descrição                                                                 |
|--------|-------------------------------------------|----------|---------------------------------------------------------------------------|
| GET    | `/api/v1/books`                           | 🔓       | Listar livros com paginação                                               |
| GET    | `/api/v1/books/{id}`                      | 🔓       | Ver detalhes de um livro *(registra visualização assíncrona se usuário estiver logado)* |
| GET    | `/api/v1/books/genre/{id}`                | 🔓       | Listar livros por gênero                                                  |
| GET    | `/api/v1/books/author/{id}`               | 🔓       | Listar livros por autor                                                   |
| GET    | `/api/v1/books/recently-viewed`           | 🔐       | Livros visualizados recentemente pelo usuário autenticado                 |
| POST   | `/api/v1/books`                           | 🛡️       | Criar novo livro                                                          |
| PUT    | `/api/v1/books/{id}`                      | 🛡️       | Atualizar livro                                                           |
| DELETE | `/api/v1/books/{id}`                      | 🛡️       | Deletar livro                                                             |

##### AuthorController
| Método | Endpoint                    | Acesso   | Descrição                    |
|--------|-----------------------------|----------|------------------------------|
| GET    | `/api/v1/authors`           | 🔓       | Listar autores               |
| POST   | `/api/v1/authors`           | 🛡️       | Criar novo autor             |
| PUT    | `/api/v1/authors/{id}`      | 🛡️       | Atualizar autor              |
| DELETE | `/api/v1/authors/{id}`      | 🛡️       | Remover autor                |

##### GenreController
| Método | Endpoint                    | Acesso   | Descrição                    |
|--------|-----------------------------|----------|------------------------------|
| GET    | `/api/v1/genres`            | 🔓       | Listar gêneros               |
| POST   | `/api/v1/genres`            | 🛡️       | Criar novo gênero            |
| PUT    | `/api/v1/genres/{id}`       | 🛡️       | Atualizar gênero             |
| DELETE | `/api/v1/genres/{id}`       | 🛡️       | Remover gênero               |

### Cache com Redis

Para otimizar o desempenho e reduzir o número de consultas ao banco de dados, a aplicação utiliza **Redis** como cache distribuído.

- Operações de leitura (GET), especialmente aquelas que retornam listas paginadas ou dados frequentemente acessados, são cacheadas usando o Redis.
- Cada cache utiliza uma chave específica que considera parâmetros como página, tamanho, filtros ou ordenações, para garantir a granularidade e evitar dados incorretos.
- Sempre que uma entidade é criada, atualizada ou deletada, o cache relacionado é invalidado (limpo) automaticamente para manter a consistência dos dados.

## III. Como Executar a Aplicação

### Pré-requisitos

- Ter o [Docker](https://www.docker.com/get-started) instalado e rodando na sua máquina.  
- Ter o código fonte clonado com o arquivo `docker-compose.yml` na raiz do projeto.

### Passos para executar

1. **Navegue até a pasta do projeto no terminal:**

```bash
cd caminho/para/seu/projeto
```

2. **Suba os containers com o Docker Compose (buildando a imagem da API):**

```bash
docker-compose up --build
```

3. **Aguarde o build e a inicialização dos containers (Postgres, Redis e API).**

4. **Importe a collection no Postman:**

- Com o arquivo `postman_collection__catalogodosabio-api.json` que está na raiz do projeto você poderá testar todos os endpoints da API diretamente pelo Postman.


