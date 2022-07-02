create table users (
  id                    bigserial,
  username              varchar(30) not null unique,
  password              varchar(80) not null,
  enabled               boolean not null,
  primary key (id)
);

create table roles (
  id                    serial,
  name                  varchar(50) not null,
  primary key (id)
);

create table users_roles (
  user_id               bigint not null,
  role_id               int not null,
  primary key (user_id, role_id),
  foreign key (user_id) references users (id) on delete cascade on update cascade,
  foreign key (role_id) references roles (id) on delete cascade on update cascade
);


CREATE TABLE products (
id bigserial PRIMARY KEY,
name VARCHAR(100),
price REAL NOT NULL,
creation_date TIMESTAMP,
description VARCHAR);


create table product_cart (
cart_id bigint not null,
user_id bigint not null,
primary key (cart_id)
);

create table cart_item (
cart_id bigint not null,
product_id bigint not null,
foreign key (product_id) references products,
foreign key (cart_id) references product_cart
);

create sequence cart_sequence start 1 increment 1;
create sequence product_sequence start 1 increment 1;
create sequence user_sequence start 1 increment 1;
-- create table product_cart (
--   id bigserial PRIMARY KEY,
--   username              varchar(30) not null,
--   product_id             bigserial not null,
--   foreign key (username) references users (username) on delete cascade on update cascade,
--   foreign key (product_id) references products (id) on delete cascade on update cascade
-- );