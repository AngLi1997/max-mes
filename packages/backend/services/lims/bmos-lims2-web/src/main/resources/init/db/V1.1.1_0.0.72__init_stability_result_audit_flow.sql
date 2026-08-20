-- 稳定性结果审核流程初始化数据
-- category code: 120020040 (AuditCategoryCodeEnum.STABILITY_RESULT_AUDIT)

-- 稳定性结果审核子分类（父分类 12002003 稳定性配置 已存在）
insert into bmos_lims2.lm_flow_audit_category (id, name, code, parent_id, create_by, update_by, create_time, update_time, is_deleted, tree_code, tree_name)
values (120020040, '稳定性结果审核', '120020040', 12002003, '1', '1', '2026-03-25 00:00:00', '2026-03-25 00:00:00', 0, '120020040', '稳定性配置/稳定性结果审核');

-- 稳定性结果审核内置流程
insert into bmos_lims2.lm_flow_audit (id, code, name, category_code, create_time, update_time, create_by, update_by, is_deleted)
values (1985800000000000001, '120020040', '稳定性结果审核内置流程', '120020040', '2026-03-25 00:00:00', '2026-03-25 00:00:00', '1', '1', 0);

-- 稳定性结果审核内置流程部署
insert into bmos_lims2.audit_deployment (id, rev, version, name, business_key, category, deployment_id,
                                         deployment_version_id, remark, meta_info, element_info, deploy_by, deploy_time,
                                         deploy_status, create_by, create_time, update_by, update_time)
values (200, null, 1, '稳定性结果审核内置流程', null, '120020040', 'a1b2c3d4-e5f6-4a7b-8c9d-e0f1a2b3c4d5',
        'a1b2c3d4-e5f6-4a7b-8c9d-e0f1a2b3c4d5:1', null,
        '[{"key":"f1e2d3c4-b5a6-4789-ab01-234567890abc","name":"开始","type":"START_EVENT","outgoing":[],"incoming":[],"metaInfo":{"position":{"x":500,"y":60},"size":{"width":120,"height":44},"view":"vue-shape-view","shape":"custom-vue-start-node","id":"f1e2d3c4-b5a6-4789-ab01-234567890abc","data":{"label":"开始"},"ports":{"groups":{"top":{"position":"top","attrs":{"circle":{"r":6,"magnet":true,"stroke":"#108ee9","strokeWidth":1,"fill":"transparent","style":{"visibility":"hidden"}}}},"right":{"position":"right","attrs":{"circle":{"r":6,"magnet":true,"stroke":"#108ee9","strokeWidth":1,"fill":"transparent","style":{"visibility":"hidden"}}}},"bottom":{"position":"bottom","attrs":{"circle":{"r":6,"magnet":true,"stroke":"#108ee9","strokeWidth":1,"fill":"transparent","style":{"visibility":"hidden"}}}},"left":{"position":"left","attrs":{"circle":{"r":6,"magnet":true,"stroke":"#108ee9","strokeWidth":1,"fill":"transparent","style":{"visibility":"hidden"}}}}},"items":[{"id":"start-top-port","group":"top"},{"id":"start-right-port","group":"right"},{"id":"start-left-port","group":"left"},{"id":"start-bottom-port","group":"bottom"}]},"zIndex":1},"payload":{"settings":"{\"name\":\"开始\"}"}},{"key":"c4d3e2f1-a0b9-4876-cd23-45678901bcde","name":"结束","type":"END_EVENT","outgoing":[],"incoming":[],"metaInfo":{"position":{"x":500,"y":500},"size":{"width":120,"height":44},"view":"vue-shape-view","shape":"custom-vue-end-node","id":"c4d3e2f1-a0b9-4876-cd23-45678901bcde","data":{"label":"结束"},"ports":{"groups":{"top":{"position":"top","attrs":{"circle":{"r":6,"magnet":true,"stroke":"#108ee9","strokeWidth":1,"fill":"transparent","style":{"visibility":"hidden"}}}},"right":{"position":"right","attrs":{"circle":{"r":6,"magnet":true,"stroke":"#108ee9","strokeWidth":1,"fill":"transparent","style":{"visibility":"hidden"}}}},"bottom":{"position":"bottom","attrs":{"circle":{"r":6,"magnet":true,"stroke":"#108ee9","strokeWidth":1,"fill":"transparent","style":{"visibility":"hidden"}}}},"left":{"position":"left","attrs":{"circle":{"r":6,"magnet":true,"stroke":"#108ee9","strokeWidth":1,"fill":"transparent","style":{"visibility":"hidden"}}}}},"items":[{"id":"end-top-port","group":"top"},{"id":"end-right-port","group":"right"},{"id":"end-left-port","group":"left"},{"id":"end-bottom-port","group":"bottom"}]},"zIndex":1},"payload":{"settings":"{\"name\":\"结束\"}"}}]',
        '{"f1e2d3c4-b5a6-4789-ab01-234567890abc":{"key":"f1e2d3c4-b5a6-4789-ab01-234567890abc","name":"开始","type":"START_EVENT","outgoing":[],"incoming":[],"payload":{"settings":"{\"name\":\"开始\"}"},"startEvent":true,"endEvent":false},"c4d3e2f1-a0b9-4876-cd23-45678901bcde":{"key":"c4d3e2f1-a0b9-4876-cd23-45678901bcde","name":"结束","type":"END_EVENT","outgoing":[],"incoming":[],"payload":{"settings":"{\"name\":\"结束\"}"},"endEvent":true,"startEvent":false}}',
        null, '2026-03-25 00:00:00', 0, '1', '2026-03-25 00:00:00', '1', '2026-03-25 00:00:00');

-- 稳定性结果审核流程版本
insert into bmos_lims2.lm_flow_audit_version (id, flow_audit_id, history_version, version, state, remark, deployment_id, create_time, update_time, create_by, update_by, is_deleted)
values (1985800000000000002, 1985800000000000001, null, '1', 1, null, 'a1b2c3d4-e5f6-4a7b-8c9d-e0f1a2b3c4d5', '2026-03-25 00:00:00', '2026-03-25 00:00:00', '1', '1', 0);
