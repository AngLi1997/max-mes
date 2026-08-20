/*
 * 描述: 创建 方法 与 操作规程 绑定关系表（n:n）
 * 作者: yigaohui
 * 日期: 2025-11-03
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 新增表 lm_inspect_method_operate_bind
 * 2. 建立唯一约束 uk_record_operate(record_id, operate_id)
 * 3. 建立索引 idx_record_id(record_id)、idx_operate_id(operate_id)
 */

CREATE TABLE IF NOT EXISTS lm_inspect_method_operate_bind (
    id BIGINT NOT NULL COMMENT '主键ID',
    record_id BIGINT NOT NULL COMMENT '方法记录ID -> bm_batch_record.id',
    operate_id BIGINT NOT NULL COMMENT '操作规程ID -> bm_operate_rule.id',
    create_time DATETIME NULL COMMENT '创建时间',
    update_time DATETIME NULL COMMENT '更新时间',
    create_by VARCHAR(60) NULL COMMENT '创建人',
    update_by VARCHAR(60) NULL COMMENT '更新人',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (id),
    KEY idx_record_id (record_id),
    KEY idx_operate_id (operate_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='方法-操作规程 绑定关系表';


