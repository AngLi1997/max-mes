use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

alter table bm_storage_material_batch
    add supplier varchar(100) null comment '供应商' after no_hydration_content;

alter table bm_storage_material_batch
    add producer varchar(100) null comment '生产商' after supplier;

update bm_storage_material_batch
    left join (select id,
                      if(r.supplier = 'null', null, r.supplier) as s,
                      if(r.producer = 'null', null, r.producer) as p
               from (select id,
                            json_unquote(json_extract(expand_info, '$.supplier')) as supplier,
                            json_unquote(json_extract(expand_info, '$.producer')) as producer
                     from bm_material) r) rs on bm_storage_material_batch.material_id = rs.id
set bm_storage_material_batch.supplier = rs.s,
    bm_storage_material_batch.producer = rs.p
where 1 = 1;

SET FOREIGN_KEY_CHECKS = 1;
