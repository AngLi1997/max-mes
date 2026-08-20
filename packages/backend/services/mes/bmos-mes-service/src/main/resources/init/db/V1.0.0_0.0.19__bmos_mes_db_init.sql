use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 修改记录项名称字段长度
ALTER TABLE bm_batch_record_item
    MODIFY `name` VARCHAR(100);

SET FOREIGN_KEY_CHECKS = 1;