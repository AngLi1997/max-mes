-- 工艺绑定操作规程表
CREATE TABLE `bm_procedure_step_sop` (
                                         `id` bigint NOT NULL COMMENT '主键id',
                                         `step_model_id` bigint NOT NULL COMMENT '工艺配置工步主键id',
                                         `operation_sop_id` bigint NOT NULL COMMENT '操作规程id',
                                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                         `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                         `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
                                         `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
                                         `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工步绑定操作规程表';