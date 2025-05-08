create table if not exists account (
   id serial primary key,
   balance double precision not null
);

INSERT INTO account (balance) VALUES (1000.0);
