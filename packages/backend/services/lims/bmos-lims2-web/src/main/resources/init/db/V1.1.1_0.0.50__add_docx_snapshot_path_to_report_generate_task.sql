-- 报告生成任务新增DOCX快照路径字段
-- 首次生成报告时保存冻结快照（含数据点/基础信息/自定义字段），后续重渲染（确认/审批）从快照加载，
-- 避免重新查DB导致多次渲染间数据不一致
ALTER TABLE lm_report_generate_task
    ADD COLUMN docx_snapshot_path VARCHAR(512) NULL COMMENT '报告DOCX快照路径（用于重渲染时加载稳定内容）' AFTER inspection_conclusion;
