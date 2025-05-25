create table if not exists items
(
    id serial primary key,
    title text not null,
    price numeric(12,2) not null,
    description text not null,
    img_path text not null
);

create table if not exists orders
(
    id serial primary key,
    order_time timestamp not null default current_timestamp,
    session text not null,
    customer text not null,
    status text not null
);

create table if not exists order_items
(
    id serial primary key,
    order_id integer references orders (id) not null,
    item_id integer references items (id) not null,
    quantity smallint not null default 0,
    CONSTRAINT uk_order_item UNIQUE (order_id, item_id)
);

create table if not exists users (
     id serial primary key,
     username VARCHAR(255) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL,
     role VARCHAR(50) NOT NULL
);


--Наполнение таблицы

DO $$
DECLARE
    i INT := 1;
BEGIN
    WHILE i <= 100 LOOP
            INSERT INTO items (title, price, description, img_path)
            VALUES (
                       'Item ' || i,
                       (random() * 1000)::numeric(12,2),
                       'Description for item ' || i,
                       '/images/item' || i || '.jpg'
                   );
            i := i + 1;
        END LOOP;
END $$;
