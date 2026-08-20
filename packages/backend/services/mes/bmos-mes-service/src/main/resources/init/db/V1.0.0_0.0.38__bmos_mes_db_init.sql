use bmos_mes;
set names utf8mb4;

alter table bm_ingredient_input_record
    add quantity varchar(255) null comment '投料量（预定量）' after storage_material_no;
alter table bm_ingredient_input_record
    add unit_id bigint null comment '投料单位' after quantity;

update bm_ingredient_input_record
set bm_ingredient_input_record.unit_id = (select if(bm_storage_material.unit_extend_id is not null, bm_storage_material.unit_extend_id, bm_storage_material.unit_id) from bm_storage_material where bm_ingredient_input_record.storage_material_id = bm_storage_material.id),
    bm_ingredient_input_record.quantity = (select consume_quantity from bm_storage_material where bm_ingredient_input_record.storage_material_id = bm_storage_material.id)
where 1=1;