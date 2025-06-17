-- Limpa os dados existentes para evitar duplicatas, mantendo a estrutura
TRUNCATE TABLE ITEM_PEDIDO, PEDIDO, CLIENTE, USUARIO_CARGO, CATEGORIA, PRODUTO, USUARIO RESTART IDENTITY CASCADE;

-- INSERINDO USUÁRIOS
-- IDs: 1 (admin), 2 (marketing), 3 (cliente_ana), 4 (cliente_bruno)
INSERT INTO USUARIO (login, senha, ativo) VALUES
('admin', '$2a$12$o/3X5oKAtaugiRJRm5MULel6f5YVo5iXPkF99fRr4HBeFy93uIzN6', true),
('marketing', '$2a$12$nbZD1DddQja/ibgNo9Wz6.VE/v5uVe2dBMsdaFgeruDqDuJ9yPmYm', true),
('ana.silva@email.com', '$2a$12$8Zu4CIMnhVfgXQVXjQ36CujwtiWcuh4rHHTnajdueaassx7UGPJAy', true),
('bruno.costa@email.com', '$2a$12$8Zu4CIMnhVfgXQVXjQ36CujwtiWcuh4rHHTnajdueaassx7UGPJAy', true);

-- INSERINDO CARGOS (já devem existir, mas para garantir)
INSERT INTO CARGO (nome) VALUES ('ROLE_ADMIN'), ('ROLE_CLIENTE'), ('ROLE_MARKETING') ON CONFLICT (nome) DO NOTHING;

-- ASSOCIANDO CARGOS AOS USUÁRIOS
-- IDs dos cargos: 1 (ADMIN), 2 (CLIENTE), 3 (MARKETING)
INSERT INTO USUARIO_CARGO (id_usuario, id_cargo) VALUES
(1, 1), -- admin.user -> ROLE_ADMIN
(2, 3), -- marketing.user -> ROLE_MARKETING
(3, 2), -- ana.silva@email.com -> ROLE_CLIENTE
(4, 2); -- bruno.costa@email.com -> ROLE_CLIENTE

-- INSERINDO CLIENTES (associados aos usuários clientes)
INSERT INTO CLIENTE (id_usuario, nome_completo, cpf, data_nascimento, email, telefone) VALUES
(3, 'Ana Silva', '11122233301', '1995-10-20', 'ana.silva@email.com', '11987654321'),
(4, 'Bruno Costa', '22233344402', '1990-03-15', 'bruno.costa@email.com', '21988776655');

-- INSERINDO 5 CATEGORIAS
-- IDs: 1 (Camisetas), 2 (Calças), 3 (Vestidos), 4 (Sapatos), 5 (Acessórios)
INSERT INTO CATEGORIA (nome, descricao) VALUES
('Camisetas', 'Camisetas de diversos estilos, masculinas e femininas'),
('Calças', 'Calças jeans, sociais, leggings, etc.'),
('Vestidos', 'Vestidos para diversas ocasiões, do casual à festa'),
('Sapatos', 'Calçados masculinos e femininos para todos os estilos'),
('Acessórios', 'Bolsas, cintos, joias e outros complementos');

-- INSERINDO 10 PRODUTOS
INSERT INTO PRODUTO (id_categoria, nome, descricao, preco, tamanho, cor, quantidade_estoque) VALUES
(1, 'Camiseta Gola V Básica', 'Camiseta de algodão com toque macio e caimento perfeito.', 79.90, 'M', 'Branca', 50),
(1, 'Camiseta Estampada Vintage', 'Estampa exclusiva com temática retrô, 100% algodão.', 99.90, 'G', 'Preta', 30),
(2, 'Calça Jeans Skinny', 'Jeans com alta elasticidade que modela o corpo.', 249.90, '40', 'Azul Escuro', 25),
(2, 'Calça de Alfaiataria', 'Corte reto e tecido de alta qualidade, ideal para o trabalho.', 299.00, '42', 'Cinza', 15),
(3, 'Vestido Floral Midi', 'Vestido leve com estampa floral, perfeito para o verão.', 199.90, 'M', 'Estampado', 20),
(3, 'Vestido de Festa Longo', 'Vestido com detalhes em renda e saia fluida.', 499.99, 'P', 'Vermelho', 10),
(4, 'Tênis Casual de Couro', 'Tênis versátil para o dia a dia, em couro legítimo.', 350.00, '41', 'Branco', 40),
(4, 'Sapato Scarpin Salto Fino', 'Clássico e elegante, em verniz preto.', 280.00, '37', 'Preto', 18),
(5, 'Bolsa Tote de Couro', 'Bolsa espaçosa e estruturada, ideal para o trabalho.', 450.00, 'Único', 'Caramelo', 12),
(5, 'Cinto de Couro Fino', 'Cinto delicado para marcar a cintura.', 89.90, 'M', 'Marrom', 35);

-- INSERINDO PEDIDOS E ITENS (PRODUTOS VENDIDOS)

-- Pedido 1 (Cliente Ana Silva - ID 1)
-- Total: (1 * 99.90) + (1 * 249.90) = 349.80
INSERT INTO PEDIDO (id_cliente, valor_total_pedido, status_pedido) VALUES (1, 349.80, 'PAGO');
INSERT INTO ITEM_PEDIDO (id_pedido, id_produto, quantidade, preco_unitario) VALUES
(1, 2, 1, 99.90),  -- Camiseta Estampada Vintage
(1, 3, 1, 249.90); -- Calça Jeans Skinny

-- Pedido 2 (Cliente Bruno Costa - ID 2)
-- Total: (2 * 79.90) + (1 * 350.00) = 159.80 + 350.00 = 509.80
INSERT INTO PEDIDO (id_cliente, valor_total_pedido, status_pedido) VALUES (2, 509.80, 'ENVIADO');
INSERT INTO ITEM_PEDIDO (id_pedido, id_produto, quantidade, preco_unitario) VALUES
(2, 1, 2, 79.90),  -- Camiseta Gola V Básica
(2, 7, 1, 350.00); -- Tênis Casual de Couro

-- Pedido 3 (Cliente Ana Silva - ID 1)
-- Total: (1 * 499.99) + (1 * 280.00) + (1 * 89.90) = 869.89
INSERT INTO PEDIDO (id_cliente, valor_total_pedido, status_pedido) VALUES (1, 869.89, 'ENTREGUE');
INSERT INTO ITEM_PEDIDO (id_pedido, id_produto, quantidade, preco_unitario) VALUES
(3, 6, 1, 499.99), -- Vestido de Festa Longo
(3, 8, 1, 280.00), -- Sapato Scarpin Salto Fino
(3, 10, 1, 89.90); -- Cinto de Couro Fino

-- Pedido 4 (Cliente Bruno Costa - ID 2) - Pendente
-- Total: (1 * 450.00) = 450.00
INSERT INTO PEDIDO (id_cliente, valor_total_pedido, status_pedido) VALUES (2, 450.00, 'PENDENTE');
INSERT INTO ITEM_PEDIDO (id_pedido, id_produto, quantidade, preco_unitario) VALUES
(4, 9, 1, 450.00); -- Bolsa Tote de Couro