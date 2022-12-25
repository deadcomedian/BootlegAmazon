alter table shop.t_cart drop column user_id;
alter table shop.t_cart add column user_id character varying not null default 'DEFAULT-USER-ID' ;