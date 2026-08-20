use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
alter table bm_procedure_step_model
    change device equipment varchar(255) null;
SET FOREIGN_KEY_CHECKS = 1;
