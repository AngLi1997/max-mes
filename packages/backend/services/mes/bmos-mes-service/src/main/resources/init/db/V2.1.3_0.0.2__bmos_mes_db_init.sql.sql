-- 异常描述字段长度修改
ALTER TABLE `bm_execute_exception` MODIFY COLUMN `exception_description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '异常描述';