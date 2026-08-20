drop index uk_order_field on lm_inspection_order_custom_field;

alter table lm_inspection_order_custom_field
drop foreign key fk_custom_field_inspection_order;

alter table lm_inspection_sampling
drop foreign key fk_sampling_inspection_order;

