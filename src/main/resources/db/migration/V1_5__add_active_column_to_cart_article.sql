alter table shop.t_cart_article add column if not exists active boolean;
update shop.t_cart_article set active = true where cart_id = 2;
update shop.t_cart_article set active = false where cart_id = 4;
ALTER TABLE shop.t_cart_article ALTER COLUMN active SET NOT NULL;
