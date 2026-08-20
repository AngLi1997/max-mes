-- 稳定性样品移动记录表
CREATE TABLE `lm_stability_sample_move_log`
(
    `id`              BIGINT       NOT NULL COMMENT '主键',
    `plan_sample_id`  BIGINT       NOT NULL COMMENT '稳定性计划样品ID（lm_stability_plan_sample.id）',
    `from_location`   VARCHAR(255) NULL COMMENT '原储存位置',
    `to_location`     VARCHAR(255) NOT NULL COMMENT '目标储存位置',
    `move_reason`     VARCHAR(500) NOT NULL COMMENT '移动原因',
    `operator_id`     VARCHAR(64)  NULL COMMENT '操作人ID',
    `operator_name`   VARCHAR(64)  NULL COMMENT '操作人姓名',
    `move_time`       DATETIME     NOT NULL COMMENT '移动时间',
    `create_time`     DATETIME     NOT NULL COMMENT '创建时间',
    `update_time`     DATETIME     NOT NULL COMMENT '更新时间',
    `create_by`       VARCHAR(64)  NULL COMMENT '创建人',
    `update_by`       VARCHAR(64)  NULL COMMENT '更新人',
    `is_deleted`      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    INDEX `idx_plan_sample_id` (`plan_sample_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '稳定性样品移动记录';
