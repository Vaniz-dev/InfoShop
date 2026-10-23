# Criação do banco

CREATE DATABASE IF NOT EXISTS jr_loja ;
USE jr_loja;

CREATE TABLE IF NOT EXISTS usuario(
	id INT PRIMARY KEY auto_increment,
    nome VARCHAR(75),
    email VARCHAR(150),
    senha VARCHAR(50),
    telefone bigint(11),
    nivel enum("user","admin")
);


CREATE TABLE IF NOT EXISTS produtos(
	id INT PRIMARY KEY auto_increment,
    nome VARCHAR(75),
    valor decimal(9,2),
    descricao text,
    caminho_imagem text
);



CREATE TABLE IF NOT EXISTS venda (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cliente_id INT,
    FOREIGN KEY (cliente_id) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS venda_produto (
    venda_id INT,
    produto_id INT,
    quantidade INT NOT NULL DEFAULT 1,

    PRIMARY KEY (venda_id, produto_id),

    FOREIGN KEY (venda_id) REFERENCES venda(id),
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
);


# Popular o banco

INSERT INTO usuario(nome,email,senha,telefone,nivel)
VALUES
	('admin', 'admin@gmail.com','admin',38997466794,'admin'),
    ('jose','jose@gmail.com','jose123',25999845213,'user'),
    ('gabrielmarcos','marcosg@gmail.com','marcos123',33999145698,'user');

INSERT INTO produtos (nome, valor, descricao, caminho_imagem)
VALUES
    ('Notebook Lenovo', 3499.90, 'Notebook com 8GB de RAM, SSD de 256GB e processador Intel Core i5.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT-p5jxJKvl9242mFXyyDN1ZoCV5sodF6j3QMPK4aHjaQ&s=10'),
    ('Mouse Gamer', 129.90, 'Mouse gamer com 6 botões, iluminação RGB e ajuste de DPI.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSXzP5IvbqZVNH83I1g7sjjKpYZaGOQi0Okw6e4aIOTWw&s=10'),
    ('Teclado Mecânico', 249.90, 'Teclado mecânico com switches azuis e iluminação RGB.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQz8DTgifBCHE-PKgVfcS6LenY14ft-hy4N7X6U_rwDTQ&s=10'),
    ('Monitor LG 24 polegadas', 899.90, 'Monitor Full HD de 24 polegadas com taxa de atualização de 75Hz.', 'https://www.lg.com/content/dam/channel/wcms/br/images/monitores/24ml600m-b_awzm_essp_br_c/gallery/1100-x-730-01v-2.jpg'),
    ('Headset Gamer', 179.90, 'Headset com microfone integrado e áudio estéreo.', 'https://m.media-amazon.com/images/I/61hj6TZ6FwL.jpg'),
    ('Mousepad Grande', 59.90, 'Mousepad gamer de superfície antiderrapante.', 'https://images.kabum.com.br/produtos/fotos/sync_mirakl/466082/Mouse-Pad-Gamer-Speed-Extra-Grande-Profissional-70x35-Drag-o-Azul_1727809845_gg.jpg'),
    ('Webcam Full HD', 219.90, 'Webcam com resolução Full HD 1080p para videochamadas.', 'https://m.media-amazon.com/images/I/61-K2lXmHQL.jpg'),
    ('SSD 480GB', 299.90, 'SSD de 480GB para melhorar o desempenho do computador.', 'https://img.terabyteshop.com.br/produto/g/ssd-kingston-a400-480gb-sata-iii-leitura-500mbs-gravacao-450mbs-sa400s37480g-imp_129879.jpg');
    
INSERT INTO venda (cliente_id)
VALUES
    (2),
    (2),
    (2);
    
INSERT INTO venda_produto (venda_id, produto_id, quantidade)
VALUES
    -- Venda 1
    (1, 1, 1), -- 1 Notebook Lenovo
    (1, 2, 2), -- 2 Mouses Gamer

    -- Venda 2
    (2, 3, 1), -- 1 Teclado Mecânico
    (2, 5, 1), -- 1 Headset Gamer
    (2, 6, 2), -- 2 Mousepads Grandes

    -- Venda 3
    (3, 4, 1), -- 1 Monitor LG
    (3, 7, 1), -- 1 Webcam Full HD
    (3, 8, 1); -- 1 SSD 480GB
    

