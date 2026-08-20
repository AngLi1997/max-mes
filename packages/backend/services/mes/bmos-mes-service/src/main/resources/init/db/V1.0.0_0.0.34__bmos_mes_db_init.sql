use bmos_mes;
set names utf8mb4;
set foreign_key_checks = 0;
alter table bm_product_formula_material
    modify quantity decimal(20, 9) not null comment '数量';

alter table bm_product_formula_material
    modify scale decimal(20, 9) null comment '物料精度';

alter table bm_product_formula_material
    modify dry_pure_param decimal(20, 9) null comment '折干折纯参数';

alter table bm_product_formula_material
    modify unpacking_tolerance_upper decimal(20, 9) null comment '拆包允差上限';

alter table bm_product_formula_material
    modify unpacking_tolerance_lower decimal(20, 9) null comment '拆包允差下限';

alter table bm_product_formula_material
    modify charge_mixture_tolerance_upper decimal(20, 9) null comment '配料允差上限';

alter table bm_product_formula_material
    modify charge_mixture_tolerance_lower decimal(20, 9) null comment '配料允差下限';

alter table bm_product_formula_material
    modify oddment_tolerance_upper decimal(20, 9) null comment '余料允差上限';

alter table bm_product_formula_material
    modify oddment_tolerance_lower decimal(20, 9) null comment '余料允差下限';



--

alter table bm_product_formula_material
    modify quantity varchar(64) not null comment '数量';

alter table bm_product_formula_material
    modify scale varchar(64) null comment '物料精度';

alter table bm_product_formula_material
    modify dry_pure_param varchar(64) null comment '折干折纯参数';

alter table bm_product_formula_material
    modify unpacking_tolerance_upper varchar(64) null comment '拆包允差上限';

alter table bm_product_formula_material
    modify unpacking_tolerance_lower varchar(64) null comment '拆包允差下限';

alter table bm_product_formula_material
    modify charge_mixture_tolerance_upper varchar(64) null comment '配料允差上限';

alter table bm_product_formula_material
    modify charge_mixture_tolerance_lower varchar(64) null comment '配料允差下限';

alter table bm_product_formula_material
    modify oddment_tolerance_upper varchar(64) null comment '余料允差上限';

alter table bm_product_formula_material
    modify oddment_tolerance_lower varchar(64) null comment '余料允差下限';


alter table bm_product_plan
    modify batch_quantity decimal(20, 9) null comment '生产批量';
alter table bm_product_plan
    modify batch_quantity varchar(64) not null comment '生产批量';

alter table bm_product_formula_version
    modify batch_quantity decimal(20, 9) null comment '生产批量';
alter table bm_product_formula_version
    modify batch_quantity varchar(64) not null comment '生产批量';


set foreign_key_checks = 1;