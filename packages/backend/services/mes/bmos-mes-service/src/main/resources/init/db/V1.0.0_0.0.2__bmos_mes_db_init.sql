use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 修改模型表名称字段长度
ALTER TABLE audit_process_instance MODIFY `name` VARCHAR(100);
ALTER TABLE audit_hi_process_instance MODIFY `name` VARCHAR(100);

SET FOREIGN_KEY_CHECKS = 1;
