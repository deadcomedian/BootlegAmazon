drop schema if exists postgres.shop cascade;

create schema shop;

create table shop.tcl_category
(
    category_id serial,
    category_name character varying not null unique,
    category_active boolean not null,
    primary key (category_id)
);

create table shop.tcl_status
(
    status_id serial,
    status_name character varying not null unique,
    primary key (status_id)
);

create table shop.tcl_role
(
    role_id serial,
    role_name character varying not null unique
);

create table shop.t_user
(
    user_id serial,
    user_name character varying not null,
    user_phone character varying not null,
    user_login character varying not null unique,
    user_pass character varying not null,
    role_id int not null,
    user_active boolean not null,
    primary key (user_id),
    foreign key (role_id) references shop.tcl_role (role_id) on delete cascade
);

create table shop.t_cart
(
    cart_id serial,
    customer_id int not null,
    cart_active boolean not null,
    primary key (cart_id),
    foreign key (customer_id) references shop.t_customer (customer_id) on delete cascade
);

create table shop.t_order
(
    order_id serial,
    user_id int not null,
    status_id int not null,
    order_address character varying not null,
    order_date date not null,
    primary key (order_id),
    foreign key (user_id) references shop.t_user (user_id) on delete cascade,
    foreign key (status_id) references shop.tcl_status (status_id) on delete cascade
);

create table shop.t_article
(
    article_id serial,
    category_id int not null,
    article_name character varying not null,
    article_author character varying not null,
    article_description character varying,
    article_photo character varying,
    article_price double precision not null,
    article_amount int check (article_amount >= 0),
    article_active boolean not null,
    article_rating double precision,
    primary key (article_id),
    foreign key (category_id) references shop.tcl_category (category_id) on delete cascade
);

create table shop.t_invoice
(
    invoice_id serial,
    article_id int not null,
    invoice_count int not null,
    primary key (invoice_id),
    foreign key (article_id) references shop.t_article (article_id) on delete cascade
);

create table shop.t_cart_article
(
    cart_id int,
    article_id int,
    article_amount int not null,
    primary key (cart_id, article_id),
    FOREIGN KEY (cart_id) references shop.t_cart (cart_id) on delete cascade,
    FOREIGN KEY (article_id) references shop.t_article (article_id) on delete cascade
);

create table shop.t_order_article
(
    order_id int,
    article_id int,
    article_amount int not null,
    article_order_price double precision not null,
    primary key (order_id, article_id),
    FOREIGN KEY (order_id) references shop.t_order (order_id) on delete cascade,
    FOREIGN KEY (article_id) references shop.t_article (article_id) on delete cascade
);


