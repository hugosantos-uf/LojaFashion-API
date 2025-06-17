# LojaFashionAPI

API backend para uma loja de moda online, desenvolvida com Spring Boot. O projeto implementa um sistema de e-commerce completo, com gerenciamento de produtos, clientes e pedidos, protegido por um robusto sistema de controle de acesso baseado em cargos (RBAC) com autenticação via Token JWT.

## Funcionalidades Principais

### Gerenciamento da Loja
- **CRUD de Categorias:** Gerenciamento completo de categorias de produtos.
- **CRUD de Produtos:** Gestão completa e paginada de produtos, associados a categorias.
- **CRUD de Clientes:** Gestão completa e paginada de clientes.
- **Gestão de Pedidos:** Sistema de pedidos que suporta múltiplos itens, com cálculo de valor total, atualização de status e envio de e-mail de confirmação.
- **Módulo de Relatórios:** Endpoints para gerar relatórios complexos com JPQL, como ranking de produtos mais vendidos e pedidos por cliente.

### Segurança e Autenticação
- **Autenticação via Token JWT:** Autenticação stateless utilizando JSON Web Tokens (JWT) para proteger os endpoints.
- **Criptografia de Senhas com BCrypt:** Todas as senhas de usuários são armazenadas de forma segura no banco de dados utilizando o algoritmo de hashing BCrypt.
- **Controle de Acesso Baseado em Cargos (RBAC):** Sistema de permissões com 3 níveis de acesso: `ROLE_ADMIN`, `ROLE_CLIENTE` e `ROLE_MARKETING`.
- **Gerenciamento de Usuários:**
  - Cadastro público de clientes com atribuição automática do cargo `ROLE_CLIENTE`.
  - Endpoint exclusivo para `ROLE_ADMIN` criar novos usuários com cargos específicos.
  - Troca de senha segura, com validação da senha atual.
  - Endpoint para desativação de contas de usuário (restrito ao `ROLE_ADMIN`).

## Diagrama de Permissionamento de Acesso

A tabela abaixo detalha as permissões para cada recurso da API, com base nos cargos definidos.

| Recurso | Método HTTP | ROLE_ADMIN | ROLE_CLIENTE | ROLE_MARKETING | Público (Sem Login) |
| :--- | :--- | :---: | :---: | :---: | :---: |
| **/auth** | `POST` | ✔️ | ✔️ | ✔️ | ✔️ |
| **/cliente** | `POST` | ✔️ | ✔️ | ✔️ | ✔️ |
| | | | | | |
| **/produto** | `POST` | ✔️ | ❌ | ✔️ | ❌ |
| **/produto/{id}**| `PUT` | ✔️ | ❌ | ✔️ | ❌ |
| **/produto/{id}**| `DELETE` | ✔️ | ❌ | ✔️ | ❌ |
| **/produto/** | `GET` | ✔️ | ✔️ | ✔️ | ✔️ |
| | | | | | |
| **/categoria** | `POST` | ✔️ | ❌ | ✔️ | ❌ |
| **/categoria/{id}**| `PUT` | ✔️ | ❌ | ✔️ | ❌ |
| **/categoria/{id}**| `DELETE` | ✔️ | ❌ | ✔️ | ❌ |
| **/categoria/** | `GET` | ✔️ | ✔️ | ✔️ | ✔️ |
| | | | | | |
| **/pedido** | `POST` | ❌ | ✔️ | ❌ | ❌ |
| **/pedido/{id}/status**|`PUT` | ✔️ | ❌ | ❌ | ❌ |
| **/pedido/{id}** | `DELETE` | ✔️ | ❌ | ❌ | ❌ |
| **/pedido/por-cliente/{id}**| `GET` | ✔️ | ✔️ | ❌ | ❌ |
| **/pedido/{id}** | `GET` | ✔️ | ❌ | ❌ | ❌ |
| | | | | | |
| **/relatorio/** | `GET` | ✔️ | ❌ | ❌ | ❌ |
| | | | | | |
| **/usuario** | `POST` | ✔️ | ❌ | ❌ | ❌ |
| **/usuario/{id}** | `DELETE` | ✔️ | ❌ | ❌ | ❌ |
| **/usuario/logado** | `GET` | ✔️ | ✔️ | ✔️ | ❌ |
| **/usuario/trocar-senha**|`PUT` | ✔️ | ✔️ | ✔️ | ❌ |

## Tecnologias Utilizadas
* **Java 17**
* **Spring Boot 2.7.6**
  * Spring Web
  * Spring Data JPA
  * Spring Security
  * Spring Validation
  * Spring Boot Starter Mail
* **Maven**
* **Oracle Database** (Driver `ojdbc11`)
* **Lombok**
* **JJwt** (para tokens JWT)
* **SpringDoc OpenAPI UI (Swagger)** (para documentação interativa da API)
* **FreeMarker** (para templates de e-mail)

## Como Executar

### Pré-requisitos
* JDK 17 ou superior.
* Maven 3.6 ou superior.
* Uma instância do Oracle Database em execução.
* Um cliente SQL (DBeaver, SQL Developer, etc.).

### Passos
1.  **Clone o repositório:**
    ```bash
    git clone <url-do-seu-repositorio>
    cd LojaFashion-API
    ```
2.  **Configure o Banco de Dados:**
  * Abra o arquivo `src/main/resources/application.properties`.
  * Altere as propriedades `spring.datasource.url`, `spring.datasource.username` e `spring.datasource.password` com os dados da sua instância Oracle.
  * Ajuste as configurações de e-mail (`spring.mail.*`) se desejar testar o envio.
3.  **Crie a Estrutura do Banco:**
  * Execute o script `bd/createandinsert.sql` no seu cliente SQL para criar todas as tabelas, sequences e cargos iniciais.
4.  **Execute a Aplicação:**
    ```bash
    mvn spring-boot:run
    ```
5.  **Acesse a Documentação:**
  * Após a inicialização, acesse a documentação interativa da API (Swagger UI) no seu navegador: `http://localhost:8080/`

## Endpoints Principais
* `/auth` - Autenticação e geração de token.
* `/usuario` - Gerenciamento de usuários e senhas.
* `/cliente` - Cadastro público de clientes.
* `/categoria` - Gerenciamento de Categorias.
* `/produto` - Gerenciamento de Produtos.
* `/pedido` - Gerenciamento de Pedidos.
* `/relatorio` - Endpoints para geração de relatórios.