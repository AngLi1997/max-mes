-- 为样品表添加留样管理相关字段
ALTER TABLE lm_sample
    ADD COLUMN current_quantity VARCHAR(50) COMMENT '当前样品数量（默认等于quantity，领用后会减少）' AFTER quantity,
    ADD COLUMN destroyed TINYINT(1) DEFAULT 0 COMMENT '是否已销毁（0-否，1-是）' AFTER discarded;

-- 创建留样样品领用台账表
CREATE TABLE IF NOT EXISTS lm_sample_collection_ledger (
    id BIGINT NOT NULL COMMENT '主键ID',
    sample_id BIGINT COMMENT '样品ID',
    sample_no VARCHAR(50) NOT NULL COMMENT '样品编号',
    batch_no VARCHAR(100) COMMENT '批号',
    material_id BIGINT COMMENT '检品ID',
    material_name VARCHAR(200) COMMENT '物料名称',
    material_code VARCHAR(50) COMMENT '检品编码',
    material_spec VARCHAR(200) COMMENT '规格',
    collect_quantity VARCHAR(50) NOT NULL COMMENT '领用数量',
    unit_id BIGINT COMMENT '单位ID',
    collect_reason VARCHAR(500) COMMENT '领用原因',
    collector_id VARCHAR(50) COMMENT '领用人ID',
    collector_name VARCHAR(100) COMMENT '领用人姓名',
    collect_time DATETIME COMMENT '领用时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除（0-否，1-是）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    PRIMARY KEY (id),
    INDEX idx_sample_id (sample_id),
    INDEX idx_sample_no (sample_no),
    INDEX idx_batch_no (batch_no),
    INDEX idx_material_id (material_id),
    INDEX idx_collect_time (collect_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='留样样品领用台账表';

-- 为已有样品初始化 current_quantity 字段（等于 quantity）
UPDATE lm_sample SET current_quantity = quantity WHERE current_quantity IS NULL;
