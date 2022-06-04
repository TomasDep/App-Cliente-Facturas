INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('John', 'Doe', 'john.doe@email.com', '2022-02-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Jane', 'Doe', 'jane.doe@email.com', '2022-02-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Richard', 'Doe', 'richard.doe@email.com', '2022-02-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Maria', 'Doe', 'maria.doe@email.com', '2022-02-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Camila', 'Doe', 'camila.doe@email.com', '2022-02-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Sam', 'Doe', 'sam.doe@email.com', '2022-02-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Frodo', 'Doe', 'frodo.doe@email.com', '2022-02-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Merry', 'Doe', 'merry.doe@email.com', '2022-02-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Pippin', 'Doe', 'pippin.doe@email.com', '2022-02-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Ronald', 'McDonald', 'rdonal@email.com', '2022-02-27', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Coronel', 'Sanders', 'Cnel.Sanders@email.com', '2022-02-27', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('JRR', 'Tolkien', 'JRR123Tolkien@email.com', '2022-02-27', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Agatha', 'Cristie', 'ACristie@email.com', '2022-02-27', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Arthur Conan', 'Doyle', 'ACD@email.com', '2022-02-27', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Dmitri', 'Glukhovsky', 'D_Glukhovsky@email.com', '2022-02-27', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Papel', 'Lucho', 'papelucho@email.com', '2022-02-27', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Junji', 'Ito', 'Jito@email.com', '2022-02-27', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Bruce', 'Lee', 'bruce.lee@email.com', '2022-02-28', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Jackie', 'Chan', 'jchan@email.com', '2022-02-28', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Jean-Claude', 'Van Damme', 'JC-Van-Damme123@email.com', '2022-02-28', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Manny', 'Pacquiao', 'Manny8888@email.com', '2022-02-28', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Mike', 'Tyson', 'Mtyson@email.com', '2022-02-28', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Muhammad', 'Ali', 'Muhammad.ali@email.com', '2022-02-28', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Evander', 'Holyfield', 'holyEvander@email.com', '2022-02-28', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Ricardo', 'Lopez', 'finito.lopes@email.com', '2022-02-28', '');

INSERT INTO productos (nombre, precio, create_at) VALUES('Smart TV LG LCD', 300000, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Camara digital Sony', 125000, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Samsung Galaxy 12', 450000, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Notebook Lenovo Legion', 800000, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('LCD AOC', 120000, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Notebook Sony Z110', 59990, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Teclado Mecanico RAZER', 89990, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('MacBook Pro', 1149000, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('RTX 3060', 579000, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('iPhone 13 Pro Max 128GB', 1189990, NOW());

INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES("Factura equipos de oficina", null, 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 7);

INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES("Factura equipos Apple", "Observaciones...", 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 2, 8);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 2, 10);

INSERT INTO users (username, password, enabled) VALUES('usuario', '$2a$10$iIlr8Dwlg4jLmK.c6pNB/.XdTbL8g0DXF2iJktRagddZLs2bKjgvC', 1);
INSERT INTO users (username, password, enabled) VALUES('administrador', '$2a$10$kdhVHvvc3C9aB9P2heb13ubCqWZo8/oxUh.cBnpjdshTZEUVppgX2', 1);

INSERT INTO authorities (user_id, authority) VALUES(1, 'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES(2, 'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES(2, 'ROLE_ADMIN');