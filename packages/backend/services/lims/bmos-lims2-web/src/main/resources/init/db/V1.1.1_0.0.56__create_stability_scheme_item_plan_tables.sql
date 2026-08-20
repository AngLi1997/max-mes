-- 创建稳定性方案检验项目配置表
CREATE TABLE IF NOT EXISTS lm_stability_scheme_item (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    scheme_id BIGINT NOT NULL COMMENT '方案ID',
    version_id BIGINT NOT NULL COMMENT '版本ID',
    inspect_item_id BIGINT NOT NULL COMMENT '检验项目ID',
    inspect_item_name VARCHAR(200) COMMENT '检验项目名称',
    inspect_item_code VARCHAR(100) COMMENT '检验项目编码',
    duration INT COMMENT '检验工时数量',
    time_unit VARCHAR(20) COMMENT '检验工时单位（DAY/HOUR/MINUTE）',
    team_ids TEXT COMMENT '检验班组ID列表（JSON数组）',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by VARCHAR(50) COMMENT '更新人',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    tenant_id VARCHAR(50) COMMENT '租户ID',
    PRIMARY KEY (id),
    KEY idx_version_id (version_id),
    KEY idx_scheme_id (scheme_id),
    KEY idx_inspect_item_id (inspect_item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='稳定性方案检验项目配置表';

-- 创建稳定性方案分析项配置表
CREATE TABLE IF NOT EXISTS lm_stability_scheme_parameter (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    scheme_id BIGINT NOT NULL COMMENT '方案ID',
    version_id BIGINT NOT NULL COMMENT '版本ID',
    item_config_id BIGINT NOT NULL COMMENT '检验项目配置ID（lm_stability_scheme_item.id）',
    parameter_id BIGINT NOT NULL COMMENT '分析项ID',
    parameter_name VARCHAR(200) COMMENT '分析项名称',
    parameter_code VARCHAR(100) COMMENT '分析项编码',
    standard_rule VARCHAR(500) COMMENT '标准规定',
    is_reportable TINYINT(1) DEFAULT 1 COMMENT '是否报告项（0-否，1-是）',
    execute_method VARCHAR(20) COMMENT '执行方式（LIMS/ELN）',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by VARCHAR(50) COMMENT '更新人',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    tenant_id VARCHAR(50) COMMENT '租户ID',
    PRIMARY KEY (id),
    KEY idx_version_id (version_id),
    KEY idx_item_config_id (item_config_id),
    KEY idx_parameter_id (parameter_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='稳定性方案分析项配置表';

-- 创建稳定性方案检验计划表
CREATE TABLE IF NOT EXISTS lm_stability_scheme_plan (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    scheme_id BIGINT NOT NULL COMMENT '方案ID',
    version_id BIGINT NOT NULL COMMENT '版本ID',
    experiment_type VARCHAR(100) COMMENT '试验类型（长期试验/加速试验/中间条件试验等）',
    storage_condition VARCHAR(200) COMMENT '试验储存条件',
    total_sample_amount DECIMAL(15,4) COMMENT '整体取样量',
    total_sample_unit VARCHAR(50) COMMENT '整体取样量单位',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by VARCHAR(50) COMMENT '更新人',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    tenant_id VARCHAR(50) COMMENT '租户ID',
    PRIMARY KEY (id),
    KEY idx_version_id (version_id),
    KEY idx_scheme_id (scheme_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='稳定性方案检验计划表';

-- 创建稳定性方案检验计划时间点表
CREATE TABLE IF NOT EXISTS lm_stability_scheme_plan_timepoint (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    plan_id BIGINT NOT NULL COMMENT '计划ID',
    version_id BIGINT NOT NULL COMMENT '版本ID',
    time_value INT COMMENT '检测时间点数值',
    time_unit VARCHAR(20) COMMENT '时间单位（月/年/天/周）',
    sample_amount DECIMAL(15,4) COMMENT '取样量',
    sample_unit VARCHAR(50) COMMENT '取样量单位',
    select_all TINYINT(1) DEFAULT 0 COMMENT '是否全选方案配置中的所有分析项（1-全选，0-按关联表）',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by VARCHAR(50) COMMENT '更新人',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    tenant_id VARCHAR(50) COMMENT '租户ID',
    PRIMARY KEY (id),
    KEY idx_plan_id (plan_id),
    KEY idx_version_id (version_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='稳定性方案检验计划时间点表';
