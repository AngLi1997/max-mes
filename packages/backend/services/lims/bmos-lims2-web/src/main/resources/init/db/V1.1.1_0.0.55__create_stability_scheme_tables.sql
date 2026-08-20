-- 创建稳定性方案表
CREATE TABLE IF NOT EXISTS lm_stability_scheme (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(200) NOT NULL COMMENT '方案名称',
    code VARCHAR(100) NOT NULL COMMENT '方案编码',
    material_id BIGINT NOT NULL COMMENT '检品ID',
    material_name VARCHAR(200) NOT NULL COMMENT '检品名称',
    material_code VARCHAR(100) NOT NULL COMMENT '检品编码',
    active_version_no VARCHAR(50) COMMENT '当前生效版本号',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by VARCHAR(50) COMMENT '更新人',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    tenant_id VARCHAR(50) COMMENT '租户ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_code (code),
    KEY idx_material_id (material_id),
    KEY idx_name (name),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='稳定性方案表';

-- 创建稳定性方案版本表
CREATE TABLE IF NOT EXISTS lm_stability_scheme_version (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    scheme_id BIGINT NOT NULL COMMENT '方案ID',
    version_no VARCHAR(50) NOT NULL COMMENT '版本号',
    status VARCHAR(20) NOT NULL DEFAULT 'EDITING' COMMENT '版本状态（EDITING-编辑中，APPROVING-审批中，ACTIVE-生效，COMPLETED-完成，INACTIVE-停用，VOIDED-作废）',
    process_instance_id VARCHAR(100) COMMENT '审批流程实例ID',
    parent_version_id BIGINT COMMENT '父版本ID',
    description VARCHAR(500) COMMENT '版本描述',
    effective_date DATE COMMENT '生效日期',
    material_id BIGINT NOT NULL COMMENT '检品ID',
    material_name VARCHAR(200) NOT NULL COMMENT '检品名称',
    material_code VARCHAR(100) NOT NULL COMMENT '检品编码',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by VARCHAR(50) COMMENT '更新人',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    tenant_id VARCHAR(50) COMMENT '租户ID',
    PRIMARY KEY (id),
    KEY idx_scheme_id (scheme_id),
    KEY idx_status (status),
    KEY idx_material_id (material_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='稳定性方案版本表';
