use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

alter table bm_ingredient_weigh_record
    add unit_id bigint null comment '单位id' after net_weight;
update bm_ingredient_weigh_record
set unit_id = (select unit_id
               from bm_ingredient_plan_material_batch
               where
                   bm_ingredient_plan_material_batch.ingredient_plan_id = bm_ingredient_weigh_record.ingredient_plan_id
                 and bm_ingredient_plan_material_batch.material_batch_id =
                     bm_ingredient_weigh_record.storage_material_batch_id
                 and bm_ingredient_plan_material_batch.is_deleted = 0)
where 1 = 1;
alter table bm_storage_material
    add product_plan_id bigint null comment '生产计划id' after container;
SET FOREIGN_KEY_CHECKS = 1;
