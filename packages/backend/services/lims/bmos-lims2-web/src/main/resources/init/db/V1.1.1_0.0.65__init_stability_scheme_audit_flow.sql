-- 稳定性方案版本审核流程初始化数据
-- category code: 120020003 (AuditCategoryCodeEnum.STABILITY_SCHEME_AUDIT)

-- 稳定性配置父分类
insert into bmos_lims2.lm_flow_audit_category (id, name, code, parent_id, create_by, update_by, create_time, update_time, is_deleted, tree_code, tree_name)
values (12002003, '稳定性配置', '12002003', 0, '1', '1', '2024-04-09 10:48:26', '2024-03-08 18:18:10', 0, '12002003,120020003', '稳定性配置');

-- 稳定性方案审批子分类
insert into bmos_lims2.lm_flow_audit_category (id, name, code, parent_id, create_by, update_by, create_time, update_time, is_deleted, tree_code, tree_name)
values (120020003, '稳定性方案审批', '120020003', 12002003, '1', '1', '2024-04-09 10:48:26', '2024-03-08 18:18:10', 0, '120020003', '稳定性配置/稳定性方案审批');

-- 稳定性方案审批内置流程
insert into bmos_lims2.lm_flow_audit (id, code, name, category_code, create_time, update_time, create_by, update_by, is_deleted)
values (1985700000000000001, '120020003', '稳定性方案审批内置流程', '120020003', '2026-03-20 00:00:00', '2026-03-20 00:00:00', '1', '1', 0);

-- 稳定性方案审批内置流程部署
insert into bmos_lims2.audit_deployment (id, rev, version, name, business_key, category, deployment_id,
                                         deployment_version_id, remark, meta_info, element_info, deploy_by, deploy_time,
                                         deploy_status, create_by, create_time, update_by, update_time)
values (100, null, 1, '稳定性方案审批内置流程', null, '120020003', 'e7f8a9b0-c1d2-4e3f-9a0b-c1d2e3f4a5b6',
        'e7f8a9b0-c1d2-4e3f-9a0b-c1d2e3f4a5b6:1', null,
        '[{\"key\":\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":60},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-start-node\",\"id\":\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\",\"data\":{\"label\":\"开始\"},\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"start-top-port\",\"group\":\"top\"},{\"id\":\"start-right-port\",\"group\":\"right\"},{\"id\":\"start-left-port\",\"group\":\"left\"},{\"id\":\"start-bottom-port\",\"group\":\"bottom\"}]},\"zIndex\":1},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\"}\"}},{\"key\":\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":500},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-end-node\",\"id\":\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\",\"data\":{\"label\":\"结束\"},\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"end-top-port\",\"group\":\"top\"},{\"id\":\"end-right-port\",\"group\":\"right\"},{\"id\":\"end-left-port\",\"group\":\"left\"},{\"id\":\"end-bottom-port\",\"group\":\"bottom\"}]},\"zIndex\":1},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\"}\"}}]',
        '{\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\":{\"key\":\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\"}\"},\"startEvent\":true,\"endEvent\":false},\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\":{\"key\":\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\"}\"},\"endEvent\":true,\"startEvent\":false}}',
        null, '2026-03-20 00:00:00', 0, '1', '2026-03-20 00:00:00', '1', '2026-03-20 00:00:00');

-- 稳定性方案审批流程版本
insert into bmos_lims2.lm_flow_audit_version (id, flow_audit_id, history_version, version, state, remark, deployment_id, create_time, update_time, create_by, update_by, is_deleted)
values (1985700000000000002, 1985700000000000001, null, '1', 1, null, 'e7f8a9b0-c1d2-4e3f-9a0b-c1d2e3f4a5b6', '2026-03-20 00:00:00', '2026-03-20 00:00:00', '1', '1', 0);
