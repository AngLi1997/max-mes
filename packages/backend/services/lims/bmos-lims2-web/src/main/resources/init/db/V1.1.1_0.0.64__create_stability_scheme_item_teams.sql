-- 创建稳定性方案检验项目班组关联表（与检验方案保持一致，使用独立关联表替代JSON字段）
CREATE TABLE IF NOT EXISTS `lm_stability_scheme_item_teams` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `scheme_id`       BIGINT       NOT NULL COMMENT '方案ID',
    `version_id`      BIGINT       NOT NULL COMMENT '版本ID',
    `inspect_item_id` BIGINT       NOT NULL COMMENT '检验项目ID',
    `item_config_id`  BIGINT       NOT NULL COMMENT '检验项目配置ID',
    `team_id`         BIGINT       NOT NULL COMMENT '班组ID',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`       VARCHAR(64)  DEFAULT NULL COMMENT '创建人',
    `update_by`       VARCHAR(64)  DEFAULT NULL COMMENT '更新人',
    `is_deleted`      TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_item_config_id` (`item_config_id`),
    KEY `idx_version_id` (`version_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='稳定性方案检验项目班组关联表';

-- 将现有 team_ids JSON 数据迁移到新关联表
INSERT INTO `lm_stability_scheme_item_teams` (scheme_id, version_id, inspect_item_id, item_config_id, team_id, create_by, update_by)
SELECT
    si.scheme_id,
    si.version_id,
    si.inspect_item_id,
    si.id AS item_config_id,
    team_id_val.team_id,
    si.create_by,
    si.update_by
FROM `lm_stability_scheme_item` si
JOIN JSON_TABLE(si.team_ids, '$[*]' COLUMNS (team_id BIGINT PATH '$')) AS team_id_val
WHERE si.is_deleted = 0 AND si.team_ids IS NOT NULL AND si.team_ids != '[]' AND si.team_ids != 'null';

-- 移除 lm_stability_scheme_item 表中的 team_ids JSON 字段
ALTER TABLE `lm_stability_scheme_item` DROP COLUMN  `team_ids`;
