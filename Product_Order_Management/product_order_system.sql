CREATE DATABASE product_order_system;

\c product_order_system;

CREATE TABLE admin (
    id SERIAL PRIMARY KEY,
    email VARCHAR(200),
    password VARCHAR(50),
    discount int
);

INSERT INTO admin (1, 'admin@gmail.com', '1234', 20);

CREATE TABLE supplier (
    sid int NOT NULL,
    sname VARCHAR(100) PRIMARY KEY,
    semail VARCHAR(100),
    spassword VARCHAR(100),
    sphone VARCHAR(15),
    saddress1 TEXT,
    saddress2 TEXT
);

CREATE TABLE users (
    uid SERIAL PRIMARY KEY,
    uname VARCHAR(200),
    uemail VARCHAR(100),
    upassword VARCHAR(100),
    uphone VARCHAR(15),
    usecqus TEXT,
    uans TEXT,
    uaddress1 TEXT,
    uaddress2 TEXT
);

CREATE TABLE category (
    cid int NOT NULL,
    cname VARCHAR(200) PRIMARY KEY,
    cdesc TEXT,
    UNIQUE (cid)
);

CREATE TABLE product (
    pid SERIAL PRIMARY KEY,
    pname VARCHAR(200),
    cname VARCHAR(200),
    pqty INT,
    pprice NUMERIC(10,2),
    CONSTRAINT fk_category_name FOREIGN KEY (cname) REFERENCES category (cname) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE purchase (
    id SERIAL PRIMARY KEY,
    uid INT,
    uname VARCHAR(200),
    uphone VARCHAR(15),
    pid INT,
    product_name VARCHAR(200),
    qty INT,
    price NUMERIC(10,2),
    total NUMERIC(10,2),
    p_date VARCHAR(20),
    uaddress TEXT,
    receive_date VARCHAR(20),
    supplier VARCHAR(200),
    status VARCHAR(50),
    CONSTRAINT fk_supplier_name FOREIGN KEY (supplier) REFERENCES supplier (sname) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_user_uid FOREIGN KEY (uid) REFERENCES users (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table orders_details (
	id serial primary key, 
	order_id varchar(50), 
	uid int, 
	pid int, 
	product_name varchar(200),
	qty int,
	price numeric(10,2), 
	total numeric(10,2),
	CONSTRAINT fk_user_uid FOREIGN KEY (uid) REFERENCES users (uid) ON DELETE CASCADE ON UPDATE CASCADE
);


create table orders_master (
	order_id varchar(50) primary key, 
	uid int,
	uname varchar(100),
	uphone varchar(10), 
	uaddress text, 
	total numeric(10,2),
	order_date varchar(20),
	CONSTRAINT fk_user_uid FOREIGN KEY (uid) REFERENCES users (uid) ON DELETE CASCADE ON UPDATE CASCADE
);
						
						
create table purchase_details (
	id serial primary key,
	order_id varchar(50),
	uid int, 
	uname varchar, 
	uphone varchar(10),
	total numeric(10,2), 
	purchase_date varchar(20),
	receive_date varchar(20), 
	supplier varchar(100),
	order_status varchar(100),
	payment_status varchar(100),
	CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES orders_master (order_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_user_uid FOREIGN KEY (uid) REFERENCES users (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table payment_details (
	payment_id varchar(50) primary key,
	order_id varchar(50),
	uid int,
	uname varchar(100),
	uphone varchar(10),
	total numeric(10,2),
	payment_date varchar(20),
	payment_status varchar(50),
	CONSTRAINT fk_user_uid FOREIGN KEY (uid) REFERENCES users (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE wishlist (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    product_name varchar(100) NOT NULL,
    quantity INT NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(uid),
    FOREIGN KEY (product_id) REFERENCES product(pid)
);

CREATE TABLE cart (
    id int PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    product_name varchar(100),
    quantity INT NOT NULL,
    price NUMERIC(10,2),
    total NUMERIC(10,2),
    total_discount NUMERIC(10,2),
    grand_total NUMERIC(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(uid),
    FOREIGN KEY (product_id) REFERENCES product(pid)
);

create table supplier_request(
	id serial primary key,
	firstname varchar(50),
	lastname varchar(50),
	email varchar(50),
	phone varchar(10),
	address1 varchar(100),
	address2 varchar(100)
)
