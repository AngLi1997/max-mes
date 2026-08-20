-- WMS 检验对接（自研 LIMS）建表脚本
-- 设计：spec 2026-06-15-bmos-wms-lims-integration-design.md
-- 计划：plans/2026-06-15-bmos-wms-lims-integration.md (T13)
-- 与 MES bm_inspect / bm_inspect_info / bm_inspect_result 字段对齐，剔除生产相关字段，加 cargo_id / batch_id / merge_code 等 WMS 特有字段。

-- ----------------------------------------------------------------
-- 1. bw_inventory_batch 增加 quality_status
-- ----------------------------------------------------------------
alter table bmos_wms.bw_inventory_batch
    add quality_status varchar(32) default 'QUARANTINE' null
        comment '质量状态：QUARANTINE/QUALIFIED/UNQUALIFIED/SAMPLED/RESTRICTED_RELEASE，与 MES MaterialQualityStatusEnum 同语义；新建批次默认 QUARANTINE';

update bmos_wms.bw_inventory_batch set quality_status = 'QUARANTINE' where quality_status is null;

-- ----------------------------------------------------------------
-- 2. bw_inspect 检验单主表（mirror bm_inspect，去掉生产字段）
-- ----------------------------------------------------------------
create table bmos_wms.bw_inspect
(
    id                  bigint               not null comment '主键'
        primary key,
    inspect_no          varchar(64)          null comment 'LIMS 检验单号（自研路径由 LIMS 生成后回写）',
    status              tinyint              not null comment '请验状态 1-请验中 2-已完成 3-已退回',
    inspect_result      varchar(64)          null comment '汇总检验结果（QUALIFIED/UNQUALIFIED 等，与 MES MaterialQualityStatusEnum 对齐）',
    reason              varchar(1024)        null comment '退回原因 / 重新发起原因',
    inspector_id        varchar(64)          null comment '请验人id',
    inspector           varchar(64)          null comment '请验人登录名',
    inspect_time        datetime             null comment '请验时间',
    inspect_config_id   bigint               null comment '请验单配置id（LIMS document_config.id）',
    scheme_id           bigint               null comment '检验方案id',
    scheme_version_id   bigint               null comment '检验方案版本id',
    cargo_id            bigint               null comment '货品id（bw_cargo.id）',
    batch_id            bigint               null comment '货品批次id（bw_inventory_batch.id），便于回写质量状态',
    cargo_name          varchar(255)         null comment '货品名称（冗余）',
    merge_code          varchar(64)          null comment '货品合并编码（冗余）',
    material_batch_no   varchar(100)         null comment '货品批号（= bw_inventory_batch.batch_no）',
    factory_batch_no    varchar(100)         null comment '原厂批号',
    unit_id             bigint               null comment '单位id',
    create_by           varchar(100)         null,
    update_by           varchar(100)         null,
    create_time         datetime             null,
    update_time         datetime             null,
    is_deleted          tinyint(1) default 0 not null
)
    comment 'WMS 检验单' row_format = DYNAMIC;

create index idx_bw_inspect_inspect_no on bmos_wms.bw_inspect (inspect_no);
create index idx_bw_inspect_cargo_batch on bmos_wms.bw_inspect (cargo_id, material_batch_no);

-- ----------------------------------------------------------------
-- 3. bw_inspect_info 请验单字段值（mirror bm_inspect_info）
-- ----------------------------------------------------------------
create table bmos_wms.bw_inspect_info
(
    id                     bigint               not null comment '主键'
        primary key,
    inspect_id             bigint               not null comment '请验单主键id（bw_inspect.id）',
    inspect_config_data_id bigint               null comment '请验单配置字段id（LIMS document_config_field 主键）',
    code                   varchar(64)          null comment '字段 code（字典 value 或前端约定的内置 code）',
    show_name              varchar(64)          null comment '展示名称',
    data_name              varchar(64)          null comment '字段名称',
    required               tinyint(1)           null comment '是否必填',
    value                  varchar(255)         null comment '所填的值',
    sort                   int                  null comment '排序',
    create_by              varchar(100)         null,
    update_by              varchar(100)         null,
    create_time            datetime             null,
    update_time            datetime             null,
    is_deleted             tinyint(1) default 0 not null
)
    comment 'WMS 请验单字段值' row_format = DYNAMIC;

create index idx_bw_inspect_info_inspect_id on bmos_wms.bw_inspect_info (inspect_id);

-- ----------------------------------------------------------------
-- 4. bw_inspect_result 检验结论（mirror bm_inspect_result）
-- ----------------------------------------------------------------
create table bmos_wms.bw_inspect_result
(
    id                   bigint               not null comment '主键'
        primary key,
    inspect_id           bigint               not null comment '请验单主键id（bw_inspect.id）',
    inspect_program_no   varchar(64)          null comment '检验项代码（LIMS 分析项 code）',
    inspect_dict_no      varchar(64)          null comment '字典对应的检验项目 code',
    inspect_program_name varchar(64)          null comment '检验项名称',
    inspect_result       varchar(64)          null comment '检验项结果（原始值）',
    inspect_conclusion   varchar(64)          null comment '检验结论（QUALIFIED/UNQUALIFIED）',
    create_by            varchar(100)         null,
    update_by            varchar(100)         null,
    create_time          datetime             null,
    update_time          datetime             null,
    is_deleted           tinyint(1) default 0 not null
)
    comment 'WMS 检验结论表' row_format = DYNAMIC;

create index idx_bw_inspect_result_inspect_id on bmos_wms.bw_inspect_result (inspect_id);
