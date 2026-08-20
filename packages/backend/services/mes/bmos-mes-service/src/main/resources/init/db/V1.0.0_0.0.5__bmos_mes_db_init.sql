use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 字段重命名
ALTER TABLE `bmos_mes`.`bm_batch_release` RENAME COLUMN `generated` to `release_generated`;

SET FOREIGN_KEY_CHECKS = 1;
