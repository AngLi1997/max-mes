use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

alter table bm_ingredient_weigh_process
    add component_id bigint null comment '组件id' after ingredient_plan_id;

SET FOREIGN_KEY_CHECKS = 1;
