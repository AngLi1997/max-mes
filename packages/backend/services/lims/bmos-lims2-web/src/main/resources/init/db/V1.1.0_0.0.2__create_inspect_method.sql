/*
 * 描述: 创建分析方法表，建立分析项(InspectParameter)与方法(InspectMethod)的1:N关系
 * 作者: yigaohui
 * 日期: 2025-10-27
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 新增表 lm_inspect_parameter_record
 * 2. 建立唯一约束 uk_parameter_code(parameter_id, code)
 * 3. 建立索引 idx_parameter_id(parameter_id)
 */

CREATE TABLE IF NOT EXISTS lm_inspect_parameter_record (
    id BIGINT NOT NULL COMMENT '主键ID',
    parameter_id BIGINT NOT NULL COMMENT '分析项ID -> lm_inspect_parameter.id',
    code VARCHAR(100) NOT NULL COMMENT '方法编码',
    name VARCHAR(200) NOT NULL COMMENT '方法名称',
    version VARCHAR(64) NULL COMMENT '方法版本',
    standard VARCHAR(500) NULL COMMENT '方法标准/描述',
    remark VARCHAR(500) NULL COMMENT '备注',
    create_time DATETIME NULL COMMENT '创建时间',
    update_time DATETIME NULL COMMENT '更新时间',
    create_by VARCHAR(60) NULL COMMENT '创建人',
    update_by VARCHAR(60) NULL COMMENT '更新人',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分析项-方法关联记录表';


