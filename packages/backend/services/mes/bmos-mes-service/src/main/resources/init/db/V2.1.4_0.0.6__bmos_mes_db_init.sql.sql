CREATE TABLE `bm_batch_record_expression` (
                                              `record_id` bigint NOT NULL,
                                              `expression_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='记录与公式绑定关系';