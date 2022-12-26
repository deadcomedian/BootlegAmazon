alter table shop.t_order add column payment_id character varying unique;
update shop.t_order set payment_id = '0G272472SF8986300' where order_id = 1;
update shop.t_order set payment_id = '25233643PW9490836' where order_id = 2;
update shop.t_order set payment_id = '0J313905N9880714M' where order_id = 3;
update shop.t_order set payment_id = '3W840089PR0536103' where order_id = 4;
update shop.t_order set payment_id = '4N367990R07930140' where order_id = 5;
ALTER TABLE shop.t_order ALTER COLUMN payment_id SET NOT NULL;