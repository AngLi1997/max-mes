use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
alter table bm_ingredient_input_record
    drop column ingredient_weigh_batch_process_id;
SET FOREIGN_KEY_CHECKS = 1;
