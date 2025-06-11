# LojaFashionAPI

Este projeto foi desenvolvido como uma API backend para uma loja de moda online. Ele implementa operações CRUD completas para as principais entidades de um e-commerce de moda, segue o padrão MVC (Controller-Service-Repository) e utiliza **Spring Data JPA** para persistência de dados em um banco Oracle.

## Principais Mudanças e Novas Funcionalidades (Versão Atual)

Esta versão representa uma evolução significativa em relação à original, com foco em maior robustez, funcionalidades avançadas e melhores práticas de desenvolvimento. As principais melhorias são:

* **Migração para Spring Data JPA:** A camada de persistência foi completamente refatorada, saindo de JDBC para Spring Data JPA. Isso simplifica o código de acesso a dados, elimina a necessidade de SQL manual para operações CRUD e habilita recursos poderosos como a criação de queries a partir de nomes de métodos.

* **Pedidos com Múltiplos Itens:** O sistema de pedidos foi redesenhado para refletir um e-commerce real. Agora, um único pedido pode conter múltiplos produtos com quantidades variadas, através da nova entidade `ItemPedido`.

* **Paginação em Endpoints:** Os endpoints de listagem de Clientes e Produtos agora são paginados (`/cliente`, `/produto`), permitindo que os consumidores da API controlem a quantidade de dados recebida por requisição, melhorando a performance e a escalabilidade.

* **Módulo de Relatórios com JPQL:** Foi adicionado um novo controller (`/relatorio`) que expõe relatórios complexos gerados com JPQL (Java Persistence Query Language). Isso inclui relatórios com múltiplos `JOINs` e agregações (`GROUP BY`, `SUM`), demonstrando uma capacidade analítica mais avançada.

## Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 2.7.6**
  * Spring Web (para APIs REST)
  * **Spring Data JPA** (para acesso ao banco de dados)
  * Spring Validation (para validação de DTOs)
  * Spring Boot Starter Mail (para envio de e-mails)
* **Maven** (Gerenciador de dependências e build)
* **Oracle Database** (Banco de dados relacional, usando o driver `ojdbc11`)
* **Lombok** (Para reduzir código boilerplate)
* **SpringDoc OpenAPI UI (Swagger)** (Para documentação interativa da API, versão 1.6.8)
* **FreeMarker** (Para templates de e-mail)

## Funcionalidades Principais

* Gerenciamento completo (CRUD) de **Categorias** de produtos.
* Gerenciamento completo (CRUD) e **paginado** de **Produtos**, associados a categorias.
* Gerenciamento completo (CRUD) e **paginado** de **Clientes**.
  * Envio de e-mail de boas-vindas no cadastro de novos clientes.
* Gerenciamento de **Pedidos**, com suporte a **múltiplos produtos por pedido**.
  * Cálculo automático de valor total.
  * Atualização de status do pedido.
  * Envio de e-mail de confirmação de pedido com lista de itens detalhada.
* Geração de **Relatórios** via endpoints, como:
  * Listagem de pedidos por cliente.
  * Ranking de produtos mais vendidos com receita gerada.
* Validação de dados de entrada em todas as DTOs.
* Documentação da API com Swagger UI.

## Diagrama ER V1
![img](https://github.com/user-attachments/assets/7c874462-d255-467a-819e-71fe472c79e6)

## Diagrama ER V2
![image](https://github.com/user-attachments/assets/36b82b69-7d00-4345-a057-64c5742a2780)

## Pré-requisitos

* JDK 17 ou superior.
* Maven 3.6 ou superior.
* Uma instância do Oracle Database em execução.
* Um cliente SQL (como DBeaver, SQL Developer) para executar o script inicial do banco.

## Endpoints Principais

* `/categoria` - Gerenciamento de Categorias
* `/produto` - Gerenciamento de Produtos (com paginação)
* `/cliente` - Gerenciamento de Clientes (com paginação)
* `/pedido` - Gerenciamento de Pedidos
* `/relatorio` - Endpoints para geração de relatórios

Consulte o Swagger UI (`/`) para detalhes completos de cada endpoint.

## Estrutura do Projeto (Simplificada)

O projeto segue uma estrutura padrão de aplicações Spring Boot, organizada em pacotes:

* `br.com.dbc.vemser.lojafashionapi`
    * `config`: Configurações da aplicação (OpenAPI, Conexão com BD).
    * `controller`: Controladores REST que expõem a API.
    * `dto`: Objetos de Transferência de Dados para requisições e respostas.
    * `entity`: Entidades que mapeiam as tabelas do banco de dados.
    * `enums`: Enumerações (ex: `StatusPedido`).
    * `exception`: Classes de exceção customizadas e handler global.
    * `repository`: Classes responsáveis pelo acesso e manipulação dos dados no banco (JDBC).
    * `service`: Classes que contêm a lógica de negócio da aplicação.
    * `LojaFashionApiApplication.java`: Classe principal para iniciar a aplicação.
* `src/main/resources`
    * `application.properties`: Arquivo de configuração principal.
    * `templates/`: Templates FreeMarker para os e-mails.
