use bmos_mes;
set names utf8mb4;

alter table bm_batch_record_component
    add date_type varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '时间类型';

CREATE TABLE `bm_procedure_task` (
  `id` bigint NOT NULL COMMENT '主键id',
  `process_id` bigint NOT NULL COMMENT '工艺id',
  `process_version` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工艺版本',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `procedure_model_id` bigint NOT NULL COMMENT '工序模型id',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='工序模型任务基础信息表';

CREATE TABLE `bm_procedure_task_instance` (
  `id` bigint NOT NULL COMMENT '主键id',
  `instance_id` varchar(60) COLLATE utf8mb4_general_ci NOT NULL COMMENT '发起流程后工作流程实例id',
  `task_id` bigint NOT NULL COMMENT '任务id',
  `plan_id` bigint NOT NULL COMMENT '计划id',
  `process_id` bigint NOT NULL COMMENT '工艺id',
  `process_version` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工艺版本',
  `flow_state` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'EDIT' COMMENT '流程状态：默认编辑，启动后进入作业中，任务完成进入确定',
  `flow_enable` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否启动，默认为false',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `procedure_model_id` bigint NOT NULL COMMENT '工序模型id',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '记录任务是否重做过',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='工序模型任务信息实例表';

CREATE TABLE `bm_procedure_task_instance_history` (
  `id` bigint NOT NULL COMMENT '主键id',
  `instance_id` varchar(60) COLLATE utf8mb4_general_ci NOT NULL COMMENT '发起流程后工作流程实例id',
  `task_id` bigint NOT NULL COMMENT '任务id',
  `plan_id` bigint NOT NULL COMMENT '计划id',
  `process_id` bigint NOT NULL COMMENT '工艺id',
  `process_version` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工艺版本',
  `flow_state` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'EDIT' COMMENT '流程状态：默认编辑，启动后进入作业中，任务完成进入确定',
  `flow_enable` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否启动，默认为false',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `procedure_model_id` bigint NOT NULL COMMENT '工序模型id',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '记录任务是否重做过',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='工序模型任务信息实例表';


CREATE TABLE `bm_procedure_expression` (
  `id` bigint NOT NULL COMMENT '主键id',
  `step_task_id` bigint NOT NULL COMMENT '工步或者是工序任务id',
  `result` tinyint(1) NOT NULL DEFAULT '0' COMMENT '条件最终结果默认false',
  `expression_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '条件类型：完成条件/执行条件',
  `node_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流程节点id',
  `expression` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表达式',
  `procedure_model_id` bigint NOT NULL COMMENT '工艺模型id',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='工步/任务表达式表';

CREATE TABLE `bm_operate_rule` (
  `id` bigint NOT NULL COMMENT '主键id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件编号',
  `category_id` bigint NOT NULL COMMENT '分类id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='操作规程主表';


CREATE TABLE `bm_operate_rule_category` (
  `id` bigint NOT NULL COMMENT '主键id',
  `name` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '分类名称',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '编码',
  `parent_id` bigint DEFAULT '0' COMMENT '上级id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `del_flag` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='操作规程分类表';

CREATE TABLE `bm_operate_rule_version` (
  `id` bigint NOT NULL COMMENT '主键id',
  `operate_id` bigint NOT NULL COMMENT '主表id',
  `version` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本号',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `history_state` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'edit' COMMENT '历史状态',
  `state` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'edit' COMMENT '状态',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件上传地址',
  `instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流程id',
  `audit_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流类型',
  `effect_date` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '-' COMMENT '生效日期',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

CREATE TABLE `bm_procedure_condition` (
  `id` bigint NOT NULL COMMENT '主键id',
  `expression_id` bigint NOT NULL COMMENT '表达式id',
  `code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `condition_details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务详情json数据',
  `condition_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务类型',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `default_result` tinyint(1) NOT NULL COMMENT '默认结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='工序逻辑表达式条件表';


CREATE TABLE `bm_procedure_condition_instance` (
  `id` bigint NOT NULL COMMENT '主键id',
  `expression_id` bigint NOT NULL COMMENT '表达式id',
  `procedure_model_id` bigint NOT NULL COMMENT '工艺模型id',
  `code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `condition_details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务详情json数据',
  `task_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务类型：任务完成条件/执行条件等',
  `task_result` tinyint NOT NULL DEFAULT '0' COMMENT '任务执行结果，默认为false',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `plan_id` bigint NOT NULL COMMENT '计划id',
  `condition_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '条件类型',
  `default_result` bigint DEFAULT NULL COMMENT '默认结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='工序逻辑表达式条件表实例表';

CREATE TABLE `bm_procedure_condition_instance_history` (
  `id` bigint NOT NULL COMMENT '主键id',
  `expression_id` bigint NOT NULL COMMENT '表达式id',
  `procedure_model_id` bigint NOT NULL COMMENT '工艺模型id',
  `code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `condition_details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务详情json数据',
  `task_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务类型：任务完成条件/物料判断条件等',
  `task_result` tinyint NOT NULL DEFAULT '0' COMMENT '任务执行结果，默认为false',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `plan_id` bigint NOT NULL COMMENT '计划id',
  `condition_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '条件类型',
  `default_result` bigint DEFAULT NULL COMMENT '默认结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='工序逻辑表达式条件表实例表';

-- 操作规程流程内置数据
INSERT INTO `bmos_mes`.`bm_flow_audit_category` (`id`, `name`, `code`, `parent_id`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `tree_code`, `tree_name`) VALUES (120020004, '操作规程启用审核', '120020004', 120020, '1', '1', '2024-04-09 10:48:26', '2024-03-08 18:18:10', 0, '120020004', '生产配置/操作规程启用审核');
INSERT INTO `bmos_mes`.`bm_flow_audit_category` (`id`, `name`, `code`, `parent_id`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `tree_code`, `tree_name`) VALUES (120020005, '操作规程停用审核', '120020005', 120020, '1', '1', '2024-04-09 10:48:26', '2024-03-08 18:18:10', 0, '120020005', '生产配置/操作规程停用审核');

INSERT INTO `bmos_mes`.`bm_flow_audit` (`id`, `code`, `name`, `category_code`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (1740318445775486977, '120020004', '操作规程启用审核内置数据', '120020004', '2023-12-28 18:27:05', '2024-06-19 09:59:17', '1', '888888888888888880', 0);
INSERT INTO `bmos_mes`.`bm_flow_audit` (`id`, `code`, `name`, `category_code`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (1740318445775486978, '120020005', '操作规程停用审核内置数据', '120020005', '2023-12-28 18:27:05', '2024-06-19 09:59:17', '1', '888888888888888880', 0);

INSERT INTO `bmos_mes`.`bm_flow_audit_version` (`id`, `flow_audit_id`, `history_version`, `version`, `state`, `remark`, `deployment_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (1740318446748598277, 1740318445775486977, NULL, 1, 1, '操作规程启用审核内置流程，勿删', '8066d103-1822-4e0d-8460-2ad03366fb55', '2023-12-28 18:27:05', '2024-06-19 09:59:17', '1', '888888888888888880', 0);
INSERT INTO `bmos_mes`.`bm_flow_audit_version` (`id`, `flow_audit_id`, `history_version`, `version`, `state`, `remark`, `deployment_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (1740318446748598278, 1740318445775486978, NULL, 1, 1, '操作规程停用审核内置流程，勿删', '8066d103-1822-4e0d-8460-2ad03366fb56', '2023-12-28 18:27:05', '2024-06-19 09:59:17', '1', '888888888888888880', 0);

INSERT INTO `bmos_mes`.`audit_deployment` (`id`, `rev`, `version`, `name`, `business_key`, `category`, `deployment_id`, `deployment_version_id`, `remark`, `meta_info`, `element_info`, `deploy_by`, `deploy_time`, `deploy_status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (5, NULL, 1, '操作规程启用审核内置流程', NULL, '120020004', '8066d103-1822-4e0d-8460-2ad03366fb55', '8066d103-1822-4e0d-8460-2ad03366fb55:1', '操作规程启用审核内置流程，勿删', '[{\"key\":\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":60},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-start-node\",\"id\":\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\",\"data\":{\"label\":\"开始\"},\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"start-top-port\",\"group\":\"top\"},{\"id\":\"start-right-port\",\"group\":\"right\"},{\"id\":\"start-left-port\",\"group\":\"left\"},{\"id\":\"start-bottom-port\",\"group\":\"bottom\"}]},\"zIndex\":1},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\"}\"}},{\"key\":\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":500},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-end-node\",\"id\":\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\",\"data\":{\"label\":\"结束\"},\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"end-top-port\",\"group\":\"top\"},{\"id\":\"end-right-port\",\"group\":\"right\"},{\"id\":\"end-left-port\",\"group\":\"left\"},{\"id\":\"end-bottom-port\",\"group\":\"bottom\"}]},\"zIndex\":1},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\"}\"}}]', '{\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\":{\"key\":\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\"}\"},\"startEvent\":true,\"endEvent\":false},\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\":{\"key\":\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\"}\"},\"endEvent\":true,\"startEvent\":false}}', NULL, NULL, 0, '1', '2023-12-28 18:27:05', '1', '2023-12-28 18:27:05');
INSERT INTO `bmos_mes`.`audit_deployment` (`id`, `rev`, `version`, `name`, `business_key`, `category`, `deployment_id`, `deployment_version_id`, `remark`, `meta_info`, `element_info`, `deploy_by`, `deploy_time`, `deploy_status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (6, NULL, 1, '操作规程停用审核内置流程', NULL, '120020005', '8066d103-1822-4e0d-8460-2ad03366fb56', '8066d103-1822-4e0d-8460-2ad03366fb56:1', '操作规程停用审核内置流程，勿删', '[{\"key\":\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":60},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-start-node\",\"id\":\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\",\"data\":{\"label\":\"开始\"},\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"start-top-port\",\"group\":\"top\"},{\"id\":\"start-right-port\",\"group\":\"right\"},{\"id\":\"start-left-port\",\"group\":\"left\"},{\"id\":\"start-bottom-port\",\"group\":\"bottom\"}]},\"zIndex\":1},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\"}\"}},{\"key\":\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":500},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-end-node\",\"id\":\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\",\"data\":{\"label\":\"结束\"},\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"end-top-port\",\"group\":\"top\"},{\"id\":\"end-right-port\",\"group\":\"right\"},{\"id\":\"end-left-port\",\"group\":\"left\"},{\"id\":\"end-bottom-port\",\"group\":\"bottom\"}]},\"zIndex\":1},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\"}\"}}]', '{\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\":{\"key\":\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\"}\"},\"startEvent\":true,\"endEvent\":false},\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\":{\"key\":\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\"}\"},\"endEvent\":true,\"startEvent\":false}}', NULL, NULL, 0, '1', '2023-12-28 18:27:05', '1', '2023-12-28 18:27:05');

