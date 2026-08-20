use bmos_platform;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 删除字典扩展表索引
ALTER TABLE `bp_unit_extend`
DROP INDEX `index_name_delete_del_flag`;
SET FOREIGN_KEY_CHECKS = 1;
