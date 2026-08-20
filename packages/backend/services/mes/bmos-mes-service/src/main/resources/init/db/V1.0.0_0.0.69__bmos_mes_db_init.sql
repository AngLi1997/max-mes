alter table bm_ingredient_weigh_batch_process add reuse boolean null comment '是否复用' after component_id;
alter table bm_ingredient_weigh_process add reuse boolean null comment '是否复用' after component_id;
alter table bm_output_weigh_process add reuse boolean null comment '是否复用' after component_id;