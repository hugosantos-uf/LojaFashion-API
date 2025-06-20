-- Apagar tabelas em ordem de dependência para evitar erros
DROP TABLE IF EXISTS ITEM_PEDIDO;
DROP TABLE IF EXISTS PEDIDO;
DROP TABLE IF EXISTS CLIENTE;
DROP TABLE IF EXISTS USUARIO_CARGO;
DROP TABLE IF EXISTS CARGO;
DROP TABLE IF EXISTS USUARIO;
DROP TABLE IF EXISTS PRODUTO;
DROP TABLE IF EXISTS CATEGORIA;


-- Tabela USUARIO
CREATE TABLE USUARIO (
    id_usuario SERIAL PRIMARY KEY,
    login VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela CARGO
CREATE TABLE CARGO (
    id_cargo SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);

-- Tabela de ligação USUARIO_CARGO
CREATE TABLE USUARIO_CARGO (
    id_usuario INTEGER,
    id_cargo INTEGER,
    CONSTRAINT PK_USUARIO_CARGO PRIMARY KEY (id_usuario, id_cargo),
    CONSTRAINT FK_UC_USUARIO FOREIGN KEY (id_usuario) REFERENCES USUARIO (id_usuario) ON DELETE CASCADE,
    CONSTRAINT FK_UC_CARGO FOREIGN KEY (id_cargo) REFERENCES CARGO (id_cargo) ON DELETE CASCADE
);

-- Inserir cargos padrão
INSERT INTO CARGO (nome) VALUES ('ROLE_ADMIN'), ('ROLE_CLIENTE'), ('ROLE_MARKETING');

-- Tabela CATEGORIA
CREATE TABLE CATEGORIA (
    id_categoria SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255)
);

-- Tabela PRODUTO
CREATE TABLE PRODUTO (
    id_produto SERIAL PRIMARY KEY,
    id_categoria INTEGER NOT NULL,
    nome VARCHAR(150) NOT NULL,
    descricao VARCHAR(500),
    preco NUMERIC(10, 2) NOT NULL,
    tamanho VARCHAR(20),
    cor VARCHAR(50),
    quantidade_estoque INTEGER DEFAULT 0,
    CONSTRAINT FK_PRODUTO_CATEGORIA FOREIGN KEY (id_categoria) REFERENCES CATEGORIA (id_categoria)
);

-- Tabela CLIENTE
CREATE TABLE CLIENTE (
    id_cliente SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL UNIQUE,
    nome_completo VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    data_nascimento DATE,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(15),
    CONSTRAINT FK_CLIENTE_USUARIO FOREIGN KEY (id_usuario) REFERENCES USUARIO (id_usuario) ON DELETE CASCADE
);

-- Tabela PEDIDO
CREATE TABLE PEDIDO (
    id_pedido SERIAL PRIMARY KEY,
    id_cliente INTEGER NOT NULL,
    valor_total_pedido NUMERIC(12, 2) NOT NULL,
    data_pedido DATE DEFAULT CURRENT_DATE,
    status_pedido VARCHAR(20) DEFAULT 'PENDENTE' NOT NULL,
    CONSTRAINT FK_PEDIDO_CLIENTE FOREIGN KEY (id_cliente) REFERENCES CLIENTE (id_cliente)
);

-- Tabela ITEM_PEDIDO
CREATE TABLE ITEM_PEDIDO (
    id_item_pedido SERIAL PRIMARY KEY,
    id_pedido INTEGER NOT NULL,
    id_produto INTEGER NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_unitario NUMERIC(10, 2) NOT NULL,
    CONSTRAINT FK_ITEM_PEDIDO_PEDIDO FOREIGN KEY (id_pedido) REFERENCES PEDIDO (id_pedido) ON DELETE CASCADE,
    CONSTRAINT FK_ITEM_PEDIDO_PRODUTO FOREIGN KEY (id_produto) REFERENCES PRODUTO (id_produto)
);