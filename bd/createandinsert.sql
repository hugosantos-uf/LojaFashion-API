-- SCRIPT COMPLETO PARA POSTGRESQL - LojaFashionAPI

-- Seção 2: Limpeza do Ambiente
DROP TABLE IF EXISTS ITEM_PEDIDO;
DROP TABLE IF EXISTS PEDIDO;
DROP TABLE IF EXISTS CLIENTE;
DROP TABLE IF EXISTS PRODUTO;
DROP TABLE IF EXISTS CATEGORIA;

DROP SEQUENCE IF EXISTS SEQ_ITEM_PEDIDO;
DROP SEQUENCE IF EXISTS SEQ_PEDIDO;
DROP SEQUENCE IF EXISTS SEQ_CLIENTE;
DROP SEQUENCE IF EXISTS SEQ_PRODUTO;
DROP SEQUENCE IF EXISTS SEQ_CATEGORIA;

-- Seção 3: Criação das Tabelas
CREATE TABLE CATEGORIA (
    id_categoria INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255)
);

CREATE TABLE PRODUTO (
     id_produto INT PRIMARY KEY,
     id_categoria INT NOT NULL,
     nome VARCHAR(150) NOT NULL,
     descricao VARCHAR(500),
     preco NUMERIC(10, 2) NOT NULL,
     tamanho VARCHAR(20),
     cor VARCHAR(50),
     quantidade_estoque INT DEFAULT 0,
     CONSTRAINT FK_PRODUTO_CATEGORIA FOREIGN KEY (id_categoria) REFERENCES CATEGORIA (id_categoria)
);

CREATE TABLE CLIENTE (
    id_cliente INT PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    data_nascimento DATE,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(15),
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE PEDIDO (
    id_pedido INT PRIMARY KEY,
    id_cliente INT NOT NULL,
    valor_total_pedido NUMERIC(12, 2) NOT NULL,
    data_pedido DATE DEFAULT CURRENT_DATE,
    status_pedido VARCHAR(20) DEFAULT 'PENDENTE' NOT NULL,
    CONSTRAINT FK_PEDIDO_CLIENTE FOREIGN KEY (id_cliente) REFERENCES CLIENTE (id_cliente)
);

CREATE TABLE ITEM_PEDIDO (
    id_item_pedido INT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_produto INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario NUMERIC(10, 2) NOT NULL,
    CONSTRAINT FK_ITEM_PEDIDO_PEDIDO FOREIGN KEY (id_pedido) REFERENCES PEDIDO (id_pedido),
    CONSTRAINT FK_ITEM_PEDIDO_PRODUTO FOREIGN KEY (id_produto) REFERENCES PRODUTO (id_produto)
);

-- Seção 4: Criação das Sequences
CREATE SEQUENCE SEQ_CATEGORIA START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SEQ_PRODUTO START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SEQ_CLIENTE START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SEQ_PEDIDO START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SEQ_ITEM_PEDIDO START WITH 1 INCREMENT BY 1;

-- Seção 5: Inserção de Dados de Amostra
INSERT INTO CATEGORIA (id_categoria, nome, descricao) VALUES (nextval('SEQ_CATEGORIA'), 'Camisetas', 'Camisetas de diversos estilos, masculinas e femininas');
INSERT INTO CATEGORIA (id_categoria, nome, descricao) VALUES (nextval('SEQ_CATEGORIA'), 'Calças', 'Calças jeans, sociais, leggings, etc.');
INSERT INTO CATEGORIA (id_categoria, nome, descricao) VALUES (nextval('SEQ_CATEGORIA'), 'Vestidos', 'Vestidos para diversas ocasiões');

INSERT INTO PRODUTO (id_produto, id_categoria, nome, descricao, preco, tamanho, cor, quantidade_estoque) VALUES (nextval('SEQ_PRODUTO'), 1, 'Camiseta Básica Branca', '100% Algodão, perfeita para o dia a dia.', 59.90, 'M', 'Branca', 100);
INSERT INTO PRODUTO (id_produto, id_categoria, nome, descricao, preco, tamanho, cor, quantidade_estoque) VALUES (nextval('SEQ_PRODUTO'), 2, 'Calça Jeans Skinny', 'Jeans com elastano para maior conforto.', 189.90, '40', 'Azul Escuro', 50);

INSERT INTO CLIENTE (id_cliente, nome_completo, cpf, data_nascimento, email, telefone, senha) VALUES (nextval('SEQ_CLIENTE'), 'Ana da Silva', '11122233344', '1995-05-15', 'ana.silva@email.com', '62988776655', 'senha123');
INSERT INTO CLIENTE (id_cliente, nome_completo, cpf, data_nascimento, email, telefone, senha) VALUES (nextval('SEQ_CLIENTE'), 'Carlos de Souza', '55566677788', '1988-11-20', 'carlos.souza@email.com', '62988112233', 'senha456');