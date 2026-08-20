-- 检验方案去掉实验包层级：将 lm_inspection_scheme 和 lm_inspection_scheme_version 中
-- package_id / package_code 由 NOT NULL 改为 NULL，保留历史数据不做删除

ALTER TABLE lm_inspection_scheme
    MODIFY COLUMN package_id   bigint       NULL COMMENT '关联的实验包ID（已废弃，保留历史数据）',
    MODIFY COLUMN package_code varchar(50)  NULL COMMENT '关联的实验包编码（已废弃，保留历史数据）';

ALTER TABLE lm_inspection_scheme_version
    MODIFY COLUMN package_id   bigint       NULL COMMENT '关联的实验包ID（已废弃，保留历史数据）',
    MODIFY COLUMN package_code varchar(50)  NULL COMMENT '关联的实验包编码（已废弃，保留历史数据）';
