-- MySQL dump 10.13  Distrib 8.0.36, for macos14.2 (arm64)
--
-- Host: 172.30.1.160    Database: bmos_platform
-- ------------------------------------------------------
-- Server version	8.0.36












--
-- Table structure for table `bp_active`
--

DROP TABLE IF EXISTS `bp_active`;


CREATE TABLE `bp_active`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT,
    `active_code` text         NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


--
-- Dumping data for table `bp_active`
--



--
-- Table structure for table `bp_auth_role_menu`
--

DROP TABLE IF EXISTS `bp_auth_role_menu`;


CREATE TABLE `bp_auth_role_menu`
(
    `role_id` bigint DEFAULT NULL,
    `menu_id` bigint DEFAULT NULL,
    UNIQUE KEY `uk_roleId_menuId` (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `bp_auth_role_menu`
--



--
-- Table structure for table `bp_business_parameter`
--

DROP TABLE IF EXISTS `bp_business_parameter`;


CREATE TABLE `bp_business_parameter`
(
    `id`            bigint                                                        NOT NULL COMMENT '主键',
    `code`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
    `value`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '值',
    `value_type`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '值类型 STRING  NUMBER BOOLEAN ENUM JSON',
    `business_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '业务类型 BUSINESS 业务 SYSTEM 系统',
    `belong`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '所属应用 中文',
    `description`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '描述',
    `sort`          int                                                                    DEFAULT NULL COMMENT '排序',
    `value_range`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '取值范围',
    `is_display`    tinyint                                                       NOT NULL DEFAULT '1' COMMENT '是否展示',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL,
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL,
    `create_time`   datetime                                                               DEFAULT NULL,
    `update_time`   datetime                                                               DEFAULT NULL,
    `is_deleted`    tinyint(1)                                                    NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='业务参数表';


--
-- Dumping data for table `bp_business_parameter`
--



--
-- Table structure for table `bp_code_rule`
--

DROP TABLE IF EXISTS `bp_code_rule`;


CREATE TABLE `bp_code_rule`
(
    `id`          bigint                                                        NOT NULL COMMENT '主键',
    `code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '名称',
    `can_update`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'TRUE' COMMENT '是否支持修改',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL,
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL,
    `create_time` datetime                                                               DEFAULT NULL,
    `update_time` datetime                                                               DEFAULT NULL,
    `is_deleted`  tinyint(1)                                                    NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='编码规则主表';


--
-- Dumping data for table `bp_code_rule`
--

LOCK TABLES `bp_code_rule` WRITE;

INSERT INTO `bp_code_rule`
VALUES (1766411043942305792, 'lims.inspect.order.code', 'lims请验单内置编号', 'TRUE', '1760852189758922752',
        '1760852189758922752', '2024-03-09 18:29:45', '2024-03-09 18:29:45', 0);

UNLOCK TABLES;

--
-- Table structure for table `bp_code_rule_dept`
--

DROP TABLE IF EXISTS `bp_code_rule_dept`;


CREATE TABLE `bp_code_rule_dept`
(
    `code_rule_id` bigint NOT NULL COMMENT '字典表',
    `dept_id`      bigint NOT NULL COMMENT '部门id',
    PRIMARY KEY (`code_rule_id`, `dept_id`) USING BTREE,
    KEY `idx_deptId` (`dept_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='字典部门表';


--
-- Dumping data for table `bp_code_rule_dept`
--



--
-- Table structure for table `bp_code_rule_use`
--

DROP TABLE IF EXISTS `bp_code_rule_use`;


CREATE TABLE `bp_code_rule_use`
(
    `id`          bigint                                                        NOT NULL COMMENT '主键',
    `code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码code 关联bp_code_rule#code',
    `full_no`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '完整标号',
    `sequence`    bigint                                                                 DEFAULT NULL COMMENT '序列号',
    `reset_no`    varchar(255) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '' COMMENT '重置字段数据',
    `is_confirm`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'FALSE' COMMENT '是否业务方确认',
    `reset_date`  date                                                                   DEFAULT NULL COMMENT '重置日期 存放重置规则日期最大值',
    `create_time` datetime                                                      NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `is_skip`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'FALSE' COMMENT '是否跳过',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_resetNo` (`reset_no`) USING BTREE,
    KEY `idx_code_isConfirm` (`code`, `is_confirm`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='编码规则使用表';


--
-- Dumping data for table `bp_code_rule_use`
--



--
-- Table structure for table `bp_code_rule_version`
--

DROP TABLE IF EXISTS `bp_code_rule_version`;


CREATE TABLE `bp_code_rule_version`
(
    `id`               bigint                                                        NOT NULL COMMENT '主键',
    `rule_code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码规则编码 -- 关联bp_code_rule#code',
    `version`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本号',
    `dict_id`          bigint                                                                 DEFAULT NULL COMMENT '字典id',
    `description`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '版本描述',
    `version_status`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'EDIT' COMMENT '编辑 EDIT 确认 CONFIRM',
    `status`           tinyint                                                       NOT NULL DEFAULT '0' COMMENT '启用 停用',
    `reset_rule`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '重置规则 bp_code_rule_version_detail中id字段',
    `create_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL,
    `update_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL,
    `create_time`      datetime                                                               DEFAULT NULL,
    `update_time`      datetime                                                               DEFAULT NULL,
    `is_deleted`       tinyint(1)                                                    NOT NULL DEFAULT '0',
    `del_version_flag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_ruleCode_version` (`rule_code`, `version`, `del_version_flag`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='编码规则版本信息表';


--
-- Dumping data for table `bp_code_rule_version`
--

LOCK TABLES `bp_code_rule_version` WRITE;

INSERT INTO `bp_code_rule_version`
VALUES (1766411044382707712, 'lims.inspect.order.code', 'v1.0', 100020001003001, 'v1', 'CONFIRM', 1, '[2]',
        '1760852189758922752', '1760852189758922752', '2024-03-09 18:29:45', '2024-03-11 11:52:14', 0, '0'),
       (1767785736607764480, 'lims.inspect.order.code', 'v1.1', 100020001003001, 'v1', 'EDIT', 0, '[2]',
        '1760850209980325888', '1760850209980325888', '2024-03-13 13:32:18', '2024-03-13 13:32:18', 0, '0');

UNLOCK TABLES;

--
-- Table structure for table `bp_code_rule_version_detail`
--

DROP TABLE IF EXISTS `bp_code_rule_version_detail`;


CREATE TABLE `bp_code_rule_version_detail`
(
    `id`                   bigint                                                       NOT NULL COMMENT '主键',
    `code_rule_version_id` bigint                                                       NOT NULL COMMENT '编码规则版本ID',
    `type`                 varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型 常量 CONSTANT 参数 PARAMETER 日期 date 流水号 SEQUENCE',
    `value`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '值 类型为常量使用',
    `parameter_id`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '参数id',
    `date_type`            varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '日期类型前端展示数字 年月等',
    `date_format`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '日期格式 yyyyMMdd yyyy-MM-dd yyyy/MM/dd 类似此格式',
    `start_no`             int                                                                   DEFAULT NULL COMMENT '开始编号',
    `max_length`           int                                                                   DEFAULT NULL COMMENT '最大长度',
    `step`                 int                                                                   DEFAULT NULL COMMENT '步长',
    `fill_zero`            varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '是否补零 TRUE FALSE',
    `sort`                 int                                                                   DEFAULT NULL COMMENT '排序',
    `create_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL,
    `update_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL,
    `create_time`          datetime                                                              DEFAULT NULL,
    `update_time`          datetime                                                              DEFAULT NULL,
    `is_deleted`           tinyint(1)                                                   NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='编码规则版本明细表';


--
-- Dumping data for table `bp_code_rule_version_detail`
--

LOCK TABLES `bp_code_rule_version_detail` WRITE;

INSERT INTO `bp_code_rule_version_detail`
VALUES (1767031676711604224, 1766411044382707712, 'CONSTANT', 'T', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2,
        '1760852189758922752', '1760852189758922752', '2024-03-11 11:35:56', '2024-03-11 11:35:56', 0),
       (1767031676715798528, 1766411044382707712, 'SEQUENCE', '1-1-9', NULL, NULL, NULL, 1, 9, 1, 'TRUE', 3,
        '1760852189758922752', '1760852189758922752', '2024-03-11 11:35:56', '2024-03-11 11:35:56', 0),
       (1767785736796508160, 1767785736607764480, 'CONSTANT', 'T', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2,
        '1760850209980325888', '1760850209980325888', '2024-03-13 13:32:18', '2024-03-13 13:32:18', 0),
       (1767785736804896768, 1767785736607764480, 'SEQUENCE', '1-1-9', NULL, NULL, NULL, 1, 9, 1, 'TRUE', 3,
        '1760850209980325888', '1760850209980325888', '2024-03-13 13:32:18', '2024-03-13 13:32:18', 0);

UNLOCK TABLES;

--
-- Table structure for table `bp_dept`
--

DROP TABLE IF EXISTS `bp_dept`;


CREATE TABLE `bp_dept`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门编码',
    `dept_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门名称',
    `parent_id`   bigint                                                        DEFAULT NULL COMMENT '上级部门id',
    `remark`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `create_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '更新人',
    `is_deleted`  tinyint(1)                                                    DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='部门表';


--
-- Dumping data for table `bp_dept`
--



--
-- Table structure for table `bp_dict`
--

DROP TABLE IF EXISTS `bp_dict`;


CREATE TABLE `bp_dict`
(
    `id`          bigint       NOT NULL COMMENT '主键id',
    `dict_name`   varchar(100) NOT NULL COMMENT '字典名称',
    `dict_code`   varchar(100)          DEFAULT NULL,
    `state`       tinyint(1)   NOT NULL DEFAULT '0' COMMENT '是否内置（0：非内置,可编辑删除，1：内置，不可编辑删除）',
    `create_by`   varchar(64)           DEFAULT NULL,
    `update_by`   varchar(64)           DEFAULT NULL,
    `create_time` datetime              DEFAULT NULL,
    `update_time` datetime              DEFAULT NULL,
    `is_deleted`  tinyint(1)   NOT NULL DEFAULT '0',
    `del_flag`    bigint       NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `index_code_deleted_del_flag` (`dict_code`, `is_deleted`, `del_flag`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='字典表';


--
-- Dumping data for table `bp_dict`
--

LOCK TABLES `bp_dict` WRITE;

INSERT INTO `bp_dict`
VALUES (100020001001, '生产批号', 'ProductionBatchNoParameter', 1, '1', '', '2023-12-25 10:43:15',
        '2023-12-25 10:43:15', 0, 0),
       (100020001002, '生产计划编号', 'ProductionPlanNoParameter', 1, '1', '', '2023-12-25 10:43:16',
        '2023-12-25 10:43:16', 0, 0),
       (120020009002, '生产计划编号规则', 'ProductionPlanNumberingRules', 1, '1', '', '2023-12-21 13:58:23',
        '2023-12-21 13:58:23', 0, 0),
       (1729066680262463488, '生产批号编号规则', 'ProductionBatchNumberingRules', 1, '1', '', '2023-11-21 15:24:48',
        '2023-11-21 17:08:59', 0, 0),
       (1730513339114741760, '编号规则', 'NumberingRules', 1, '1', '', '2023-11-21 15:24:48', '2023-11-21 17:08:59', 0,
        0);

UNLOCK TABLES;

--
-- Table structure for table `bp_dict_detail`
--

DROP TABLE IF EXISTS `bp_dict_detail`;


CREATE TABLE `bp_dict_detail`
(
    `id`          bigint       NOT NULL COMMENT '主键id',
    `dict_label`  varchar(100) NOT NULL COMMENT '数据标签',
    `dict_value`  varchar(100) NOT NULL COMMENT '数据值',
    `dict_id`     bigint       NOT NULL COMMENT '字典id',
    `create_by`   varchar(64)           DEFAULT NULL,
    `update_by`   varchar(64)           DEFAULT NULL,
    `create_time` datetime              DEFAULT NULL,
    `update_time` datetime              DEFAULT NULL,
    `is_deleted`  tinyint(1)   NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `index_dict_id_value` (`dict_value`, `dict_id`, `is_deleted`) USING BTREE COMMENT '字典类型与数据值唯一索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='字典详情表';


--
-- Dumping data for table `bp_dict_detail`
--

LOCK TABLES `bp_dict_detail` WRITE;

INSERT INTO `bp_dict_detail`
VALUES (100020001001001, '产品名称', 'productName', 100020001001, '', '', '2024-04-09 10:12:53', '2024-04-09 10:12:53',
        0),
       (100020001001002, '产品编码', 'productMergeCode', 100020001001, '', '', '2024-04-09 10:12:53',
        '2024-04-09 10:12:53', 0),
       (100020001001003, '内包规格', 'innerPackingSpecification', 100020001001, '', '', '2024-04-09 10:12:53',
        '2024-04-09 10:12:53', 0),
       (100020001001004, '包装规格', 'packingSpecification', 100020001001, '', '', '2024-04-09 10:12:53',
        '2024-04-09 10:12:53', 0),
       (100020001002001, '产品名称', 'productName', 100020001002, '', '', '2024-04-09 10:12:53', '2024-04-09 10:12:53',
        0),
       (100020001002002, '产品编码', 'productMergeCode', 100020001002, '', '', '2024-04-09 10:12:53',
        '2024-04-09 10:12:53', 0),
       (100020001002003, '内包规格', 'innerPackingSpecification', 100020001002, '', '', '2024-04-09 10:12:53',
        '2024-04-09 10:12:53', 0),
       (100020001002004, '包装规格', 'packingSpecification', 100020001002, '', '', '2024-04-09 10:12:53',
        '2024-04-09 10:12:53', 0),
       (100020001003001, '生产批号', 'ProductionBatchNoParameter', 1730513339114741760, '', '', '2024-04-09 10:12:53',
        '2024-04-09 10:12:53', 0),
       (100020001003002, '生产计划编号', 'ProductionPlanNoParameter', 1730513339114741760, '', '',
        '2024-04-09 10:12:53', '2024-04-09 10:12:53', 0);

UNLOCK TABLES;

--
-- Table structure for table `bp_login_log`
--

DROP TABLE IF EXISTS `bp_login_log`;


CREATE TABLE `bp_login_log`
(
    `id`               bigint                                                       NOT NULL COMMENT '主键',
    `user_name`        varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名称',
    `login_name`       varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
    `user_id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户id',
    `create_time`      datetime                                                     DEFAULT NULL COMMENT '创建时间/操作时间',
    `is_deleted`       tinyint(1)                                                   DEFAULT '0' COMMENT '是否删除（0代表否 1代表是）',
    `operation_action` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作动作',
    `ip`               varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作ip',
    `description_code` int                                                          DEFAULT NULL COMMENT '操作描述编码',
    `operation_state`  tinyint(1)                                                   NOT NULL COMMENT '操作是否成功',
    `create_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `update_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `update_time`      datetime                                                     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='用户表';


--
-- Dumping data for table `bp_login_log`
--



--
-- Table structure for table `bp_material`
--

DROP TABLE IF EXISTS `bp_material`;


CREATE TABLE `bp_material`
(
    `id`                    bigint                                                  NOT NULL,
    `material_category_id`  bigint                                                  NOT NULL COMMENT '分类id',
    `principal_material_id` bigint                                                           DEFAULT NULL COMMENT '所属物料id',
    `name`                  varchar(100) COLLATE utf8mb4_general_ci                          DEFAULT NULL COMMENT '名称',
    `code`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '编码',
    `specification`         varchar(100) COLLATE utf8mb4_general_ci                          DEFAULT NULL COMMENT '规格',
    `unit_id`               bigint                                                  NOT NULL COMMENT '单位',
    `is_sub_material`       tinyint(1)                                              NOT NULL COMMENT '是否是成员物料',
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    DEFAULT NULL COMMENT '备注',
    `status`                tinyint(1)                                              NOT NULL DEFAULT '0' COMMENT '启停状态',
    `create_by`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     DEFAULT NULL,
    `update_by`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     DEFAULT NULL,
    `create_time`           datetime                                                         DEFAULT NULL,
    `update_time`           datetime                                                         DEFAULT NULL,
    `is_deleted`            tinyint(1)                                              NOT NULL DEFAULT '0',
    `merge_code`            varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '合并编码:分类合并编码+自身编码',
    `dispense_record`       varchar(255) COLLATE utf8mb4_general_ci                          DEFAULT NULL COMMENT '已下发业务',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='物料信息';


--
-- Dumping data for table `bp_material`
--



--
-- Table structure for table `bp_material_category`
--

DROP TABLE IF EXISTS `bp_material_category`;


CREATE TABLE `bp_material_category`
(
    `id`              bigint                                                  NOT NULL,
    `parent_id`       bigint                                                  NOT NULL DEFAULT '0' COMMENT '父级id，默认0',
    `code`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '编码',
    `name`            varchar(100) COLLATE utf8mb4_general_ci                          DEFAULT NULL COMMENT '名称',
    `create_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     DEFAULT NULL,
    `update_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     DEFAULT NULL,
    `create_time`     datetime                                                         DEFAULT NULL,
    `update_time`     datetime                                                         DEFAULT NULL,
    `is_deleted`      tinyint(1)                                              NOT NULL DEFAULT '0',
    `merge_code`      varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '合并编码:父级合并编码+自身编码',
    `dispense_record` varchar(255) COLLATE utf8mb4_general_ci                          DEFAULT NULL COMMENT '已下发业务',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='物料分类';


--
-- Dumping data for table `bp_material_category`
--



--
-- Table structure for table `bp_material_extend_unit`
--

DROP TABLE IF EXISTS `bp_material_extend_unit`;


CREATE TABLE `bp_material_extend_unit`
(
    `material_id`    bigint NOT NULL,
    `extend_unit_id` bigint NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


--
-- Dumping data for table `bp_material_extend_unit`
--



--
-- Table structure for table `bp_menu`
--

DROP TABLE IF EXISTS `bp_menu`;


CREATE TABLE `bp_menu`
(
    `id`            bigint     NOT NULL COMMENT '主键',
    `name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
    `code`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '编码（字符串类型的id）',
    `parent_id`     bigint                                                        DEFAULT NULL COMMENT '所属父级id',
    `terminal_type` tinyint(1)                                                    DEFAULT NULL COMMENT '终端类型（0:pc/1:pad）',
    `is_menu`       tinyint(1)                                                    DEFAULT NULL COMMENT '是否是菜单（0代表否 1代表是）',
    `is_outside`    tinyint(1) NOT NULL                                           DEFAULT '0' COMMENT '是否外部链接0代表否 1代表是）',
    `outside_url`   varchar(255) COLLATE utf8mb4_general_ci                       DEFAULT NULL COMMENT '外部链接地址',
    `sort`          bigint                                                        DEFAULT NULL COMMENT '排序',
    `create_time`   datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `update_by`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '更新人',
    `is_deleted`    tinyint(1)                                                    DEFAULT '0' COMMENT '是否删除（0代表否 1代表是）',
    `alias`         varchar(255) COLLATE utf8mb4_general_ci                       DEFAULT NULL COMMENT '别名',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='菜单表';


--
-- Dumping data for table `bp_menu`
--



--
-- Table structure for table `bp_operation_log`
--

DROP TABLE IF EXISTS `bp_operation_log`;


CREATE TABLE `bp_operation_log`
(
    `id`                 bigint     NOT NULL,
    `operation_type`     tinyint(1)                                                    DEFAULT NULL COMMENT '操作类型',
    `operation_business` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '业务操作',
    `operation_object`   mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '操作对象',
    `user_name`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '操作人用户名',
    `user_id`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '操作人用户id',
    `remark`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `ip`                 varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`        datetime                                                      DEFAULT NULL,
    `update_time`        datetime                                                      DEFAULT NULL,
    `is_deleted`         tinyint(1) NOT NULL                                           DEFAULT '0',
    `menu_id`            bigint                                                        DEFAULT NULL COMMENT '菜单id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `bp_operation_log`
--



--
-- Table structure for table `bp_role`
--

DROP TABLE IF EXISTS `bp_role`;


CREATE TABLE `bp_role`
(
    `id`           bigint NOT NULL COMMENT '主键',
    `role_name`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色名称',
    `role_type_id` bigint                                                        DEFAULT NULL COMMENT '关联-角色分类id',
    `description`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
    `create_time`  datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `update_by`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '更新人',
    `is_deleted`   tinyint(1)                                                    DEFAULT '0' COMMENT '是否删除（0代表否 1代表是）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='角色表';


--
-- Dumping data for table `bp_role`
--



--
-- Table structure for table `bp_role_menu`
--

DROP TABLE IF EXISTS `bp_role_menu`;


CREATE TABLE `bp_role_menu`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `role_id`     bigint NOT NULL COMMENT '角色id',
    `menu_id`     bigint NOT NULL COMMENT '菜单id',
    `create_time` datetime                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
    `is_deleted`  tinyint(1)                                                   DEFAULT '0' COMMENT '是否删除（0代表否 1代表是）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='角色-菜单关联表（中间表）';


--
-- Dumping data for table `bp_role_menu`
--



--
-- Table structure for table `bp_role_type`
--

DROP TABLE IF EXISTS `bp_role_type`;


CREATE TABLE `bp_role_type`
(
    `id`             bigint NOT NULL COMMENT '主键',
    `role_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色分类名称',
    `parent_id`      bigint                                                        DEFAULT NULL COMMENT '父级id',
    `create_time`    datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `update_by`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '更新人',
    `is_deleted`     tinyint(1)                                                    DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='角色分类表';


--
-- Dumping data for table `bp_role_type`
--



--
-- Table structure for table `bp_signature_log`
--

DROP TABLE IF EXISTS `bp_signature_log`;


CREATE TABLE `bp_signature_log`
(
    `id`               bigint     NOT NULL COMMENT '是否chengg',
    `signature_type`   tinyint(1)                                                    DEFAULT NULL COMMENT '签名类型',
    `signature_data`   mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '签名对象',
    `signature_action` tinyint(1)                                                    DEFAULT NULL COMMENT '签名动作',
    `system_code`      bigint     NOT NULL COMMENT '系统编码',
    `remark`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `login_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '账户',
    `user_name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
    `user_id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '用户id',
    `success`          tinyint(1) NOT NULL COMMENT '是否成功',
    `create_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`      datetime                                                      DEFAULT NULL,
    `update_time`      datetime                                                      DEFAULT NULL,
    `is_deleted`       tinyint(1) NOT NULL                                           DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `bp_signature_log`
--



--
-- Table structure for table `bp_sys_expression`
--

DROP TABLE IF EXISTS `bp_sys_expression`;


CREATE TABLE `bp_sys_expression`
(
    `id`                     bigint     NOT NULL COMMENT '主键',
    `expression_category_id` bigint     NOT NULL COMMENT '分类id',
    `name`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
    `result`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '计算结果',
    `expression`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公式表达式',
    `expression_parse`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '公式表达式解析结果',
    `confirm_status`         tinyint    NOT NULL                                           DEFAULT '0' COMMENT '是否确认 TRUE  确认 FALSE 待确认',
    `create_by`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`            datetime                                                      DEFAULT NULL,
    `update_time`            datetime                                                      DEFAULT NULL,
    `is_deleted`             tinyint(1) NOT NULL                                           DEFAULT '0',
    `del_name_flag`          bigint     NOT NULL                                           DEFAULT '0' COMMENT '删除字段 与业务字段判断唯一一致性 默认 0 代表未删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_name_and_delNameFlag` (`name`, `del_name_flag`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='公式配置';


--
-- Dumping data for table `bp_sys_expression`
--



--
-- Table structure for table `bp_sys_expression_category`
--

DROP TABLE IF EXISTS `bp_sys_expression_category`;


CREATE TABLE `bp_sys_expression_category`
(
    `id`            bigint                                                        NOT NULL COMMENT '主键',
    `parent_id`     bigint                                                                 DEFAULT '0' COMMENT '父级id，默认0',
    `name`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '名称',
    `ancestor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '全分类名称',
    `ancestors`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类层级列表',
    `create_time`   datetime                                                               DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime                                                               DEFAULT NULL COMMENT '更新时间',
    `create_by`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL COMMENT '创建人',
    `update_by`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL COMMENT '更新人',
    `is_deleted`    tinyint(1)                                                             DEFAULT '0' COMMENT '是否删除',
    `del_flag`      bigint                                                        NOT NULL DEFAULT '0' COMMENT '删除字段 与业务字段判断唯一一致性 默认 0 代表未删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_parentId_and_name_and_delFlag` (`parent_id`, `name`, `del_flag`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='公式配置分类';


--
-- Dumping data for table `bp_sys_expression_category`
--



--
-- Table structure for table `bp_tag_define`
--

DROP TABLE IF EXISTS `bp_tag_define`;


CREATE TABLE `bp_tag_define`
(
    `id`           bigint NOT NULL COMMENT '物理主键',
    `tag_style`    varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签样式',
    `tag_width`    int                                     DEFAULT NULL COMMENT '标签宽度(mm)',
    `tag_height`   int                                     DEFAULT NULL COMMENT '标签高度(mm)',
    `cmd`          longtext COLLATE utf8mb4_general_ci COMMENT '指令模板',
    `cmd_type`     varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '指令类型',
    `preview_html` longtext COLLATE utf8mb4_general_ci COMMENT '预览html模板',
    `create_time`  datetime                                DEFAULT NULL,
    `update_time`  datetime                                DEFAULT NULL,
    `create_by`    varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `update_by`    varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `is_deleted`   tinyint(1)                              DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='标签定义表';


--
-- Dumping data for table `bp_tag_define`
--



--
-- Table structure for table `bp_tag_instance`
--

DROP TABLE IF EXISTS `bp_tag_instance`;


CREATE TABLE `bp_tag_instance`
(
    `id`            bigint NOT NULL COMMENT '物理主键',
    `tag_name`      varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签名称',
    `tag_type_id`   bigint                                  DEFAULT NULL COMMENT '标签类型id',
    `tag_scene_id`  bigint                                  DEFAULT NULL COMMENT '标签场景id',
    `tag_define_id` bigint                                  DEFAULT NULL COMMENT '标签定义id',
    `config_fields` text COLLATE utf8mb4_general_ci COMMENT '标签字段配置',
    `is_enable`     varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '启用状态',
    `create_time`   datetime                                DEFAULT NULL,
    `update_time`   datetime                                DEFAULT NULL,
    `create_by`     varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `update_by`     varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `is_deleted`    tinyint(1)                              DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='标签实例';


--
-- Dumping data for table `bp_tag_instance`
--



--
-- Table structure for table `bp_tag_scene`
--

DROP TABLE IF EXISTS `bp_tag_scene`;


CREATE TABLE `bp_tag_scene`
(
    `id`                       bigint NOT NULL COMMENT '物理主键',
    `tag_scene_name`           varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签场景名称',
    `tag_scene_desc`           varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签场景描述',
    `tag_type_id`              bigint                                  DEFAULT NULL COMMENT '标签类型id',
    `data_source_service_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据源服务名称',
    `data_source_interface`    varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据源接口地址',
    `qr_code_field`            varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '二维码字段（标签显示的二维码信息， 来源与数据源接口）',
    `sort`                     int                                     DEFAULT NULL COMMENT '排序',
    `create_time`              datetime                                DEFAULT NULL,
    `update_time`              datetime                                DEFAULT NULL,
    `create_by`                varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `update_by`                varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `is_deleted`               tinyint(1)                              DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='标签场景（脚本控制数据来源）';


--
-- Dumping data for table `bp_tag_scene`
--



--
-- Table structure for table `bp_tag_scene_field`
--

DROP TABLE IF EXISTS `bp_tag_scene_field`;


CREATE TABLE `bp_tag_scene_field`
(
    `id`            bigint NOT NULL COMMENT '物理主键',
    `tag_scene_id`  bigint                                  DEFAULT NULL COMMENT '标签场景id',
    `field`         varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段',
    `label`         varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段名称',
    `type`          varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段类型',
    `example_value` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '示例值',
    `create_time`   datetime                                DEFAULT NULL,
    `update_time`   datetime                                DEFAULT NULL,
    `create_by`     varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `update_by`     varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `is_deleted`    tinyint(1)                              DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='标签场景字段表';


--
-- Dumping data for table `bp_tag_scene_field`
--



--
-- Table structure for table `bp_tag_type`
--

DROP TABLE IF EXISTS `bp_tag_type`;


CREATE TABLE `bp_tag_type`
(
    `id`            bigint NOT NULL COMMENT '物理主键',
    `tag_type_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签类型名称',
    `tag_type_desc` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签类型描述',
    `sort`          int                                     DEFAULT NULL COMMENT '排序',
    `create_time`   datetime                                DEFAULT NULL,
    `update_time`   datetime                                DEFAULT NULL,
    `create_by`     varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `update_by`     varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `is_deleted`    tinyint(1)                              DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='标签类型（脚本控制数据来源）';


--
-- Dumping data for table `bp_tag_type`
--



--
-- Table structure for table `bp_unit`
--

DROP TABLE IF EXISTS `bp_unit`;


CREATE TABLE `bp_unit`
(
    `id`             bigint                                                       NOT NULL COMMENT '主键id',
    `unit_name`      varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标准单位名称',
    `unit_precision` bigint                                                       NOT NULL COMMENT '精度',
    `round_code`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修约规则code',
    `state`          tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否启用：0：未启用，1：启用',
    `remark`         varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL,
    `update_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL,
    `create_time`    datetime                                                              DEFAULT NULL,
    `update_time`    datetime                                                              DEFAULT NULL,
    `is_deleted`     tinyint(1)                                                   NOT NULL DEFAULT '0',
    `del_flag`       bigint                                                       NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `index_name_deleted_del_flag` (`unit_name`, `is_deleted`, `del_flag`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='单位管理表';


--
-- Dumping data for table `bp_unit`
--



--
-- Table structure for table `bp_unit_extend`
--

DROP TABLE IF EXISTS `bp_unit_extend`;


CREATE TABLE `bp_unit_extend`
(
    `id`               bigint                                                       NOT NULL COMMENT '主键id',
    `extend_unit_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '扩展单位名称',
    `unit_id`          bigint                                                       NOT NULL COMMENT '标准单位标识',
    `expression_value` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表达式值',
    `state`            tinyint(1)                                                            DEFAULT '0' COMMENT '是否启用；0：未启用；1：启用',
    `extend_precision` bigint                                                       NOT NULL COMMENT '扩展单位精度',
    `remark`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '备注',
    `create_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL,
    `update_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL,
    `create_time`      datetime                                                              DEFAULT NULL,
    `update_time`      datetime                                                              DEFAULT NULL,
    `is_deleted`       tinyint(1)                                                   NOT NULL DEFAULT '0',
    `del_flag`         bigint                                                       NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `index_name_delete_del_flag` (`extend_unit_name`, `is_deleted`, `del_flag`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='单位管理扩展表';


--
-- Dumping data for table `bp_unit_extend`
--



--
-- Table structure for table `bp_user`
--

DROP TABLE IF EXISTS `bp_user`;


CREATE TABLE `bp_user`
(
    `id`              bigint                                                        NOT NULL COMMENT '主键',
    `user_id`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '用户id',
    `user_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名称',
    `login_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
    `password`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码（解密后的密码）',
    `phone`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
    `email`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
    `gender`          tinyint(1)                                                    NOT NULL COMMENT '性别（0代表男性 1代表女性）  ',
    `remark`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `valid_time`      bigint                                                        DEFAULT NULL COMMENT '有效期截止时间',
    `pwd_error_count` int                                                           DEFAULT '0' COMMENT '密码错误次数',
    `active_status`   tinyint                                                       DEFAULT '0' COMMENT '状态（0代表待激活 1代表激活 2代表密码过期 3代表密码锁定）',
    `state`           tinyint(1)                                                    DEFAULT '0' COMMENT '启停状态（0代表停 1代表启）',
    `create_time`     datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_time`     datetime                                                      DEFAULT NULL COMMENT '更新时间',
    `create_by`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `update_by`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '更新人',
    `is_deleted`      tinyint(1)                                                    DEFAULT '0' COMMENT '是否删除（0代表否 1代表是）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='用户表';


--
-- Dumping data for table `bp_user`
--

LOCK TABLES `bp_user` WRITE;

INSERT INTO `bp_user`
VALUES (1, '1', '管理员', 'admin', '1aadf96235e3f7bb7729488fd2e73b85', '', NULL, 0, NULL, 9999999999999, 0, 1, 1,
        '2023-11-13 19:00:54', '2023-12-07 18:11:43', '1', '1', 0);

UNLOCK TABLES;

--
-- Table structure for table `bp_user_dept`
--

DROP TABLE IF EXISTS `bp_user_dept`;


CREATE TABLE `bp_user_dept`
(
    `id`          bigint                                                       NOT NULL COMMENT '主键',
    `user_id`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
    `dept_id`     bigint                                                       NOT NULL COMMENT '部门id',
    `create_time` datetime                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
    `is_deleted`  tinyint(1) unsigned zerofill                                 DEFAULT '0' COMMENT '是否删除（0代表否 1代表是）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='人员-部门关联表（中间表）';


--
-- Dumping data for table `bp_user_dept`
--



--
-- Table structure for table `bp_user_role`
--

DROP TABLE IF EXISTS `bp_user_role`;


CREATE TABLE `bp_user_role`
(
    `id`          bigint                                                       NOT NULL COMMENT '主键',
    `user_id`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
    `role_id`     bigint                                                       NOT NULL COMMENT '角色id',
    `create_time` datetime                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
    `is_deleted`  tinyint(1)                                                   DEFAULT '0' COMMENT '是否删除（0代表否 1代表是）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='人员-角色关联表（中间表）';


--
-- Dumping data for table `bp_user_role`
--












-- Dump completed on 2024-04-09 10:47:59
