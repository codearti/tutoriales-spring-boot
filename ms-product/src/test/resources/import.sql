INSERT INTO category (name) VALUES('Electronico');
INSERT INTO category (name) VALUES('Libros');
INSERT INTO category (name) VALUES('Zapatos');

INSERT INTO product (name, stock, price, creation_date, status, deleted, category_id) VALUES('Laptop', 10, 800, NOW(), 0, 0, 1);
INSERT INTO product (name, stock, price, creation_date, status, deleted, category_id) VALUES('Monitor', 10, 700, NOW(), 0, 0, 1);
INSERT INTO product (name, stock, price, creation_date, status, deleted, category_id) VALUES('Mouse', 10, 1000, NOW(), 0, 0, 1);
INSERT INTO product (name, stock, price, creation_date, status, deleted, category_id) VALUES('Old Mouse', 10, 1000, NOW(), 0, 1, 1);