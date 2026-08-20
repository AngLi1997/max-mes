-- MySQL dump 10.13  Distrib 8.0.36, for macos14.2 (arm64)
--
-- Host: 172.30.1.160    Database: bmos_mes
-- ------------------------------------------------------
-- Server version	8.0.36












--
-- Table structure for table `audit_de_message`
--

DROP TABLE IF EXISTS `audit_de_message`;


CREATE TABLE `audit_de_message`
(
    `id`            int NOT NULL AUTO_INCREMENT,
    `deployment_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `message_key`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `element_type`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `element_key`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `audit_de_message`
--



--
-- Table structure for table `audit_deployment`
--

DROP TABLE IF EXISTS `audit_deployment`;


CREATE TABLE `audit_deployment`
(
    `id`                    int NOT NULL AUTO_INCREMENT,
    `rev`                   int                                                           DEFAULT NULL,
    `version`               int                                                           DEFAULT NULL,
    `name`                  varchar(100) COLLATE utf8mb4_general_ci                       DEFAULT NULL,
    `business_key`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `category`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `deployment_id`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `deployment_version_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `meta_info`             longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
    `element_info`          longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
    `deploy_by`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `deploy_time`           datetime                                                      DEFAULT NULL,
    `deploy_status`         tinyint(1)                                                    DEFAULT NULL,
    `create_by`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`           datetime                                                      DEFAULT NULL,
    `update_by`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_time`           datetime                                                      DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1060
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='流程部署表';


--
-- Dumping data for table `audit_deployment`
--

LOCK TABLES `audit_deployment` WRITE;

INSERT INTO `audit_deployment`
VALUES (1, NULL, 1, '记录审核内置数据', NULL, '120020001', '8066d103-1822-4e0d-8460-2ad03366fb54',
        '8066d103-1822-4e0d-8460-2ad03366fb54:1', '记录审批内置流程，勿删',
        '[{\"key\":\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":60},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-start-node\",\"id\":\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\",\"data\":{\"label\":\"开始\"},\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"start-top-port\",\"group\":\"top\"},{\"id\":\"start-right-port\",\"group\":\"right\"},{\"id\":\"start-left-port\",\"group\":\"left\"},{\"id\":\"start-bottom-port\",\"group\":\"bottom\"}]},\"zIndex\":1},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\"}\"}},{\"key\":\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":500},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-end-node\",\"id\":\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\",\"data\":{\"label\":\"结束\"},\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"end-top-port\",\"group\":\"top\"},{\"id\":\"end-right-port\",\"group\":\"right\"},{\"id\":\"end-left-port\",\"group\":\"left\"},{\"id\":\"end-bottom-port\",\"group\":\"bottom\"}]},\"zIndex\":1},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\"}\"}}]',
        '{\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\":{\"key\":\"9b70f18f-8eb7-465b-ad23-2e6e30133af6\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\"}\"},\"startEvent\":true,\"endEvent\":false},\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\":{\"key\":\"44b3ed97-96b7-4a5c-bd8d-8afdbbc8d7fb\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\"}\"},\"endEvent\":true,\"startEvent\":false}}',
        NULL, NULL, 0, '1', '2023-12-28 18:27:05', '1', '2023-12-28 18:27:05'),
       (2, NULL, 1, '工艺审核内置流程', NULL, '120020002', 'f3b4eafb-49eb-40a4-8ebf-e70ffb1362e3',
        'f3b4eafb-49eb-40a4-8ebf-e70ffb1362e3:1', '工艺审批内置流程，勿删',
        '[{\"key\":\"bd766aa2-38ca-405f-9800-3884bd282698\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":60},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-start-node\",\"id\":\"bd766aa2-38ca-405f-9800-3884bd282698\",\"data\":{\"label\":\"开始\"},\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"start-top-port\",\"group\":\"top\"},{\"id\":\"start-right-port\",\"group\":\"right\"},{\"id\":\"start-left-port\",\"group\":\"left\"},{\"id\":\"start-bottom-port\",\"group\":\"bottom\"}]},\"zIndex\":1},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\"}\"}},{\"key\":\"d32b4004-0db8-4e7f-b72e-1b06027138b2\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":500},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-end-node\",\"id\":\"d32b4004-0db8-4e7f-b72e-1b06027138b2\",\"data\":{\"label\":\"结束\"},\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"end-top-port\",\"group\":\"top\"},{\"id\":\"end-right-port\",\"group\":\"right\"},{\"id\":\"end-left-port\",\"group\":\"left\"},{\"id\":\"end-bottom-port\",\"group\":\"bottom\"}]},\"zIndex\":1},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\"}\"}}]',
        '{\"d32b4004-0db8-4e7f-b72e-1b06027138b2\":{\"key\":\"d32b4004-0db8-4e7f-b72e-1b06027138b2\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\"}\"},\"endEvent\":true,\"startEvent\":false},\"bd766aa2-38ca-405f-9800-3884bd282698\":{\"key\":\"bd766aa2-38ca-405f-9800-3884bd282698\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\"}\"},\"startEvent\":true,\"endEvent\":false}}',
        NULL, NULL, 0, '1', '2023-12-28 18:27:48', '1', '2023-12-28 18:27:48'),
       (3, NULL, 1, '生产计划内置流程', NULL, '120030001', '1060d793-1fe0-456f-aebd-0525ffdab2bc',
        '1060d793-1fe0-456f-aebd-0525ffdab2bc:1', '生产计划审批内置流程，勿删',
        '[{\"key\":\"64cbf2a2-94c1-4c99-9ee6-a90055cf6335\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":60},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-start-node\",\"id\":\"64cbf2a2-94c1-4c99-9ee6-a90055cf6335\",\"data\":{\"label\":\"开始\"},\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"start-top-port\",\"group\":\"top\"},{\"id\":\"start-right-port\",\"group\":\"right\"},{\"id\":\"start-left-port\",\"group\":\"left\"},{\"id\":\"start-bottom-port\",\"group\":\"bottom\"}]},\"zIndex\":1},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\"}\"}},{\"key\":\"d6893218-3895-428c-b453-1c41ae8516c7\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":500},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-end-node\",\"id\":\"d6893218-3895-428c-b453-1c41ae8516c7\",\"data\":{\"label\":\"结束\"},\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"end-top-port\",\"group\":\"top\"},{\"id\":\"end-right-port\",\"group\":\"right\"},{\"id\":\"end-left-port\",\"group\":\"left\"},{\"id\":\"end-bottom-port\",\"group\":\"bottom\"}]},\"zIndex\":1},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\"}\"}}]',
        '{\"d6893218-3895-428c-b453-1c41ae8516c7\":{\"key\":\"d6893218-3895-428c-b453-1c41ae8516c7\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\"}\"},\"endEvent\":true,\"startEvent\":false},\"64cbf2a2-94c1-4c99-9ee6-a90055cf6335\":{\"key\":\"64cbf2a2-94c1-4c99-9ee6-a90055cf6335\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\"}\"},\"startEvent\":true,\"endEvent\":false}}',
        NULL, NULL, 0, '1', '2023-12-28 18:28:11', '1', '2023-12-28 18:28:11'),
       (4, NULL, 1, '批签发内置流程', NULL, '120040001', '0cfbdffa-7f8e-4c54-be43-fa294d2fed98',
        '0cfbdffa-7f8e-4c54-be43-fa294d2fed98:1', '批签发审批内置流程，勿删',
        '[{\"key\":\"bf1bc426-d11a-4f0a-a71b-943a27fc4c19\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":60},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-start-node\",\"id\":\"bf1bc426-d11a-4f0a-a71b-943a27fc4c19\",\"data\":{\"label\":\"开始\"},\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"start-top-port\",\"group\":\"top\"},{\"id\":\"start-right-port\",\"group\":\"right\"},{\"id\":\"start-left-port\",\"group\":\"left\"},{\"id\":\"start-bottom-port\",\"group\":\"bottom\"}]},\"zIndex\":1},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\"}\"}},{\"key\":\"bbc131a4-b3f2-477b-9c0c-f6f0e5a02952\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":500},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-end-node\",\"id\":\"bbc131a4-b3f2-477b-9c0c-f6f0e5a02952\",\"data\":{\"label\":\"结束\"},\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"end-top-port\",\"group\":\"top\"},{\"id\":\"end-right-port\",\"group\":\"right\"},{\"id\":\"end-left-port\",\"group\":\"left\"},{\"id\":\"end-bottom-port\",\"group\":\"bottom\"}]},\"zIndex\":1},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\"}\"}}]',
        '{\"bbc131a4-b3f2-477b-9c0c-f6f0e5a02952\":{\"key\":\"bbc131a4-b3f2-477b-9c0c-f6f0e5a02952\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\"}\"},\"endEvent\":true,\"startEvent\":false},\"bf1bc426-d11a-4f0a-a71b-943a27fc4c19\":{\"key\":\"bf1bc426-d11a-4f0a-a71b-943a27fc4c19\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\"}\"},\"startEvent\":true,\"endEvent\":false}}',
        NULL, NULL, 0, '1', '2023-12-28 18:28:32', '1', '2023-12-28 18:28:32'),
       (1059, NULL, 1, '配方审批内置模型', NULL, '120020003', 'ae2aa946-1ce0-4e63-8eb7-65ecd8be34e4',
        'ae2aa946-1ce0-4e63-8eb7-65ecd8be34e4:1', '配方审批内置',
        '[{\"key\":\"2aa4674b-9da8-49e3-9d94-837551d6fc3a\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":60},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-start-node\",\"id\":\"2aa4674b-9da8-49e3-9d94-837551d6fc3a\",\"data\":{\"label\":\"开始\",\"formData\":{\"name\":\"开始\",\"strategy\":[]}},\"zIndex\":1,\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"start-top-port\",\"group\":\"top\"},{\"id\":\"start-right-port\",\"group\":\"right\"},{\"id\":\"start-left-port\",\"group\":\"left\"},{\"id\":\"start-bottom-port\",\"group\":\"bottom\"}]}},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\",\\\"strategy\\\":[]}\"}},{\"key\":\"4f0fd8ca-2d20-4947-a94f-c6e4e1747d1d\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"metaInfo\":{\"position\":{\"x\":500,\"y\":500},\"size\":{\"width\":120,\"height\":44},\"view\":\"vue-shape-view\",\"shape\":\"custom-vue-end-node\",\"id\":\"4f0fd8ca-2d20-4947-a94f-c6e4e1747d1d\",\"data\":{\"label\":\"结束\",\"formData\":{\"name\":\"结束\",\"strategy\":[]}},\"zIndex\":1,\"ports\":{\"groups\":{\"top\":{\"position\":\"top\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"right\":{\"position\":\"right\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"bottom\":{\"position\":\"bottom\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}},\"left\":{\"position\":\"left\",\"attrs\":{\"circle\":{\"r\":6,\"magnet\":true,\"stroke\":\"#108ee9\",\"strokeWidth\":1,\"fill\":\"transparent\",\"style\":{\"visibility\":\"hidden\"}}}}},\"items\":[{\"id\":\"end-top-port\",\"group\":\"top\"},{\"id\":\"end-right-port\",\"group\":\"right\"},{\"id\":\"end-left-port\",\"group\":\"left\"},{\"id\":\"end-bottom-port\",\"group\":\"bottom\"}]}},\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\",\\\"strategy\\\":[]}\"}}]',
        '{\"2aa4674b-9da8-49e3-9d94-837551d6fc3a\":{\"key\":\"2aa4674b-9da8-49e3-9d94-837551d6fc3a\",\"name\":\"开始\",\"type\":\"START_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"开始\\\",\\\"strategy\\\":[]}\"},\"startEvent\":true,\"endEvent\":false},\"4f0fd8ca-2d20-4947-a94f-c6e4e1747d1d\":{\"key\":\"4f0fd8ca-2d20-4947-a94f-c6e4e1747d1d\",\"name\":\"结束\",\"type\":\"END_EVENT\",\"outgoing\":[],\"incoming\":[],\"payload\":{\"settings\":\"{\\\"name\\\":\\\"结束\\\",\\\"strategy\\\":[]}\"},\"endEvent\":true,\"startEvent\":false}}',
        NULL, NULL, 0, '1', '2024-03-26 11:44:01', '1', '2024-03-26 11:44:01');

UNLOCK TABLES;

--
-- Table structure for table `audit_execution_instance`
--

DROP TABLE IF EXISTS `audit_execution_instance`;


CREATE TABLE `audit_execution_instance`
(
    `id`                    int NOT NULL AUTO_INCREMENT,
    `element_key`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `element_type`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `execution_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `process_instance_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `deployment_version_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `state`                 tinyint                                                       DEFAULT NULL,
    `start_time`            datetime                                                      DEFAULT NULL,
    `start_by`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `end_time`              datetime                                                      DEFAULT NULL,
    `end_by`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `delete_reason`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1045
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='每个节点的流程实例表';


--
-- Dumping data for table `audit_execution_instance`
--



--
-- Table structure for table `audit_hi_execution`
--

DROP TABLE IF EXISTS `audit_hi_execution`;


CREATE TABLE `audit_hi_execution`
(
    `id`                    int NOT NULL AUTO_INCREMENT,
    `element_name`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_key`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_type`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `execution_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `process_instance_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `deployment_version_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `state`                 tinyint                                                       DEFAULT NULL,
    `start_time`            datetime                                                      DEFAULT NULL,
    `end_time`              datetime                                                      DEFAULT NULL,
    `start_by`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `end_by`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `delete_reason`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `remark`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 939
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `audit_hi_execution`
--



--
-- Table structure for table `audit_hi_process_instance`
--

DROP TABLE IF EXISTS `audit_hi_process_instance`;


CREATE TABLE `audit_hi_process_instance`
(
    `id`                        int NOT NULL AUTO_INCREMENT,
    `name`                      varchar(100) COLLATE utf8mb4_general_ci                       DEFAULT NULL,
    `deployment_id`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `deployment_version_id`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `process_instance_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `super_process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `root_process_instance_id`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `business_key`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `category`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `start_by`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `start_time`                datetime                                                      DEFAULT NULL,
    `end_time`                  datetime                                                      DEFAULT NULL,
    `process_state`             tinyint                                                       DEFAULT NULL,
    `ext_field`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 199
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `audit_hi_process_instance`
--



--
-- Table structure for table `audit_hi_task_instance`
--

DROP TABLE IF EXISTS `audit_hi_task_instance`;


CREATE TABLE `audit_hi_task_instance`
(
    `id`                    int NOT NULL AUTO_INCREMENT,
    `task_id`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `execution_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `process_instance_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `deployment_version_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_type`          varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_key`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_name`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `owner`                 varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `assignee`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `assignee_type`         varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `state`                 tinyint                                                       DEFAULT NULL,
    `start_time`            datetime                                                      DEFAULT NULL,
    `end_time`              datetime                                                      DEFAULT NULL,
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `comment`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `complete_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `delete_reason`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 365
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `audit_hi_task_instance`
--



--
-- Table structure for table `audit_hi_variable`
--

DROP TABLE IF EXISTS `audit_hi_variable`;


CREATE TABLE `audit_hi_variable`
(
    `id`                  int NOT NULL AUTO_INCREMENT,
    `name`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `type`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `category`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `value`               varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `execution_id`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `create_time`         datetime                                                       DEFAULT NULL,
    `update_time`         datetime                                                       DEFAULT NULL,
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `audit_hi_variable`
--



--
-- Table structure for table `audit_hi_variable_execution`
--

DROP TABLE IF EXISTS `audit_hi_variable_execution`;


CREATE TABLE `audit_hi_variable_execution`
(
    `id`                        bigint NOT NULL AUTO_INCREMENT,
    `var_name`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `process_instance_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `super_process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `execution_id`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `root_process_instance_id`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `audit_hi_variable_execution`
--



--
-- Table structure for table `audit_job_instance`
--

DROP TABLE IF EXISTS `audit_job_instance`;


CREATE TABLE `audit_job_instance`
(
    `id`                  int NOT NULL AUTO_INCREMENT,
    `type`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `biz_id`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `next_triggered_time` bigint                                                       DEFAULT NULL,
    `triggered_count`     int                                                          DEFAULT NULL,
    `create_time`         datetime                                                     DEFAULT NULL,
    `update_time`         datetime                                                     DEFAULT NULL,
    `status`              tinyint                                                      DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `audit_job_instance`
--



--
-- Table structure for table `audit_process_instance`
--

DROP TABLE IF EXISTS `audit_process_instance`;


CREATE TABLE `audit_process_instance`
(
    `id`                        int NOT NULL AUTO_INCREMENT,
    `name`                      varchar(100) COLLATE utf8mb4_general_ci                       DEFAULT NULL,
    `deployment_id`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `deployment_version_id`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `process_instance_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `category`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `super_process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `business_key`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `root_process_instance_id`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `start_by`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `start_time`                datetime                                                      DEFAULT NULL,
    `end_time`                  datetime                                                      DEFAULT NULL,
    `process_state`             tinyint                                                       DEFAULT NULL,
    `ext_field`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 218
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='整个流程实例表';


--
-- Dumping data for table `audit_process_instance`
--



--
-- Table structure for table `audit_re_variable_execution`
--

DROP TABLE IF EXISTS `audit_re_variable_execution`;


CREATE TABLE `audit_re_variable_execution`
(
    `id`                        bigint NOT NULL AUTO_INCREMENT,
    `var_name`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `process_instance_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `super_process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `execution_id`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `root_process_instance_id`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `audit_re_variable_execution`
--



--
-- Table structure for table `audit_ru_variable`
--

DROP TABLE IF EXISTS `audit_ru_variable`;


CREATE TABLE `audit_ru_variable`
(
    `id`                  int NOT NULL AUTO_INCREMENT,
    `name`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `type`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `category`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `value`               varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `execution_id`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `create_time`         datetime                                                       DEFAULT NULL,
    `update_time`         datetime                                                       DEFAULT NULL,
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `audit_ru_variable`
--



--
-- Table structure for table `audit_task_instance`
--

DROP TABLE IF EXISTS `audit_task_instance`;


CREATE TABLE `audit_task_instance`
(
    `id`                    int                                                          NOT NULL AUTO_INCREMENT,
    `task_id`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `execution_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `process_instance_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `deployment_version_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `element_type`          varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_key`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_name`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `owner`                 varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `assignee`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `assignee_type`         varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `state`                 tinyint                                                      NOT NULL,
    `start_time`            datetime                                                      DEFAULT NULL,
    `end_time`              datetime                                                      DEFAULT NULL,
    `comment`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `complete_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `delete_reason`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_process_instance_id` (`process_instance_id`) USING BTREE,
    KEY `idx_execution_id` (`execution_id`) USING BTREE,
    KEY `idx_task_id` (`task_id`) USING BTREE,
    KEY `idx_assignee_assigness_type` (`assignee`, `assignee_type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3882
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `audit_task_instance`
--



--
-- Table structure for table `bm_batch_record`
--

DROP TABLE IF EXISTS `bm_batch_record`;


CREATE TABLE `bm_batch_record`
(
    `id`          bigint                                                       NOT NULL COMMENT '主键id',
    `name`        varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '记录名称',
    `category_id` bigint                                                       NOT NULL COMMENT '分类id',
    `create_time` datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                                              DEFAULT NULL COMMENT '修改时间',
    `create_by`   varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT NULL COMMENT '创建人',
    `update_by`   varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT NULL COMMENT '修改人',
    `is_deleted`  tinyint                                                      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `index_name_delete` (`name`, `is_deleted`) USING BTREE COMMENT '唯一索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = DYNAMIC COMMENT ='批记录信息';


--
-- Dumping data for table `bm_batch_record`
--



--
-- Table structure for table `bm_batch_record_category`
--

DROP TABLE IF EXISTS `bm_batch_record_category`;


CREATE TABLE `bm_batch_record_category`
(
    `id`          bigint                                                       NOT NULL COMMENT '主键id',
    `name`        varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '分类名称',
    `code`        varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci         DEFAULT NULL,
    `parent_id`   bigint                                                                DEFAULT '0' COMMENT '上级id',
    `sort`        bigint                                                                DEFAULT NULL COMMENT '排序号',
    `create_time` datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                                              DEFAULT NULL COMMENT '修改时间',
    `create_by`   varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT NULL COMMENT '创建人',
    `update_by`   varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT NULL COMMENT '修改人',
    `is_deleted`  tinyint                                                               DEFAULT '0' COMMENT '是否删除',
    `del_flag`    bigint                                                       NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = DYNAMIC COMMENT ='记录配置分类表';


--
-- Dumping data for table `bm_batch_record_category`
--



--
-- Table structure for table `bm_batch_record_component`
--

DROP TABLE IF EXISTS `bm_batch_record_component`;


CREATE TABLE `bm_batch_record_component`
(
    `id`                 bigint NOT NULL COMMENT '主键id',
    `record_item_id`     bigint NOT NULL COMMENT '记录项id',
    `record_version_id`  bigint                                                       DEFAULT NULL COMMENT '版本id',
    `record_version`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '批记录版本号',
    `record_id`          bigint                                                       DEFAULT NULL COMMENT '批记录id',
    `component_type`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组件类型',
    `component_name`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组件名称',
    `field_id`           bigint                                                       DEFAULT NULL COMMENT '空格标识',
    `component_number`   bigint                                                       DEFAULT NULL COMMENT '组件关联表格最大下标值',
    `formula_precision`  bigint                                                       DEFAULT NULL COMMENT '精度',
    `component_detail`   longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '公式详细内容',
    `is_result`          tinyint                                                      DEFAULT NULL COMMENT '标记该组件是否是一个计算结果（0否1是，默认0）',
    `formula_id`         bigint                                                       DEFAULT NULL COMMENT '公式id',
    `formula_field`      longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '公式实际参数字段JSON',
    `formula_expression` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公式表达式',
    `formula_type`       varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公式类型',
    `round_code`         varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修约公式code',
    `parent_id`          bigint                                                       DEFAULT '0' COMMENT '父级id',
    `create_time`        datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_time`        datetime                                                     DEFAULT NULL COMMENT '修改时间',
    `create_by`          varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`          varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
    `is_deleted`         tinyint                                                      DEFAULT '0' COMMENT '是否删除',
    `used`               tinyint(1)                                                   DEFAULT NULL COMMENT '是否使用',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_record_item_version_field_id` (`record_item_id`, `record_version_id`, `field_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='记录组件表';


--
-- Dumping data for table `bm_batch_record_component`
--



--
-- Table structure for table `bm_batch_record_item`
--

DROP TABLE IF EXISTS `bm_batch_record_item`;


CREATE TABLE `bm_batch_record_item`
(
    `id`                bigint NOT NULL COMMENT '主键id',
    `name`              varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '记录项名称',
    `item_id`           bigint NOT NULL COMMENT '业务id',
    `item_path`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上传单个记录项指令集地址',
    `item_type`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '0:大纲内容false，1：页眉页脚内容true',
    `sort`              int                                                           DEFAULT NULL COMMENT '排序字段',
    `file_content`      mediumblob COMMENT '记录项内容',
    `max_number`        int                                                           DEFAULT NULL COMMENT '文档最大下标',
    `version`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '版本号',
    `page_config`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT '{"pattern":1}' COMMENT '文档配置',
    `record_version_id` bigint                                                        DEFAULT NULL COMMENT '记录版本表id',
    `create_time`       datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_time`       datetime                                                      DEFAULT NULL COMMENT '修改时间',
    `create_by`         varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci  DEFAULT NULL COMMENT '创建人',
    `update_by`         varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci  DEFAULT NULL COMMENT '修改人',
    `is_deleted`        tinyint                                                       DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_record_item_id_version` (`item_id`, `record_version_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='记录项表';


--
-- Dumping data for table `bm_batch_record_item`
--



--
-- Table structure for table `bm_batch_record_parse`
--

DROP TABLE IF EXISTS `bm_batch_record_parse`;


CREATE TABLE `bm_batch_record_parse`
(
    `id`           bigint NOT NULL COMMENT '主键id',
    `item_id`      bigint NOT NULL COMMENT '记录项id',
    `file_content` mediumblob COMMENT 'html字符串',
    `create_time`  datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime                                                     DEFAULT NULL COMMENT '修改时间',
    `create_by`    varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`    varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
    `is_deleted`   tinyint                                                      DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='记录解析html表';


--
-- Dumping data for table `bm_batch_record_parse`
--



--
-- Table structure for table `bm_batch_record_product`
--

DROP TABLE IF EXISTS `bm_batch_record_product`;


CREATE TABLE `bm_batch_record_product`
(
    `id`          bigint NOT NULL COMMENT '主键id',
    `record_id`   bigint NOT NULL COMMENT '批记录id',
    `product_id`  bigint NOT NULL COMMENT '产品id',
    `create_time` datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                                     DEFAULT NULL COMMENT '修改时间',
    `create_by`   varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`   varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
    `is_deleted`  tinyint                                                      DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='记录关联产品表';


--
-- Dumping data for table `bm_batch_record_product`
--



--
-- Table structure for table `bm_batch_record_version`
--

DROP TABLE IF EXISTS `bm_batch_record_version`;


CREATE TABLE `bm_batch_record_version`
(
    `id`          bigint                                                       NOT NULL COMMENT '主键id',
    `record_id`   bigint                                                       NOT NULL COMMENT '记录管理表id',
    `version`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本号',
    `state`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '状态：1：可编辑；2：审核；3：确定：4：作废',
    `instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '流程实例id',
    `file_path`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '存放文件地址',
    `remark`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '备注',
    `create_time` datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                                              DEFAULT NULL COMMENT '修改时间',
    `create_by`   varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT NULL COMMENT '创建人',
    `update_by`   varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT NULL COMMENT '修改人',
    `is_deleted`  tinyint                                                               DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `idex_record_id_version` (`record_id`, `version`) USING BTREE COMMENT '批记录id与版本号唯一索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='记录版本表';


--
-- Dumping data for table `bm_batch_record_version`
--



--
-- Table structure for table `bm_batch_release`
--

DROP TABLE IF EXISTS `bm_batch_release`;


CREATE TABLE `bm_batch_release`
(
    `id`                  bigint     NOT NULL,
    `plan_id`             bigint                                                        DEFAULT NULL COMMENT '生产计划id',
    `product_id`          bigint                                                        DEFAULT NULL COMMENT '成品id',
    `product_name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '成品名称',
    `process_id`          bigint                                                        DEFAULT NULL COMMENT '关联工艺',
    `process_name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联工艺名称',
    `process_version_id`  bigint                                                        DEFAULT NULL COMMENT '工艺版本id',
    `process_version`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工艺版本',
    `audit_state`         tinyint(1)                                                    DEFAULT NULL COMMENT '审核状态',
    `generated`           tinyint(1)                                                    DEFAULT NULL COMMENT '是否已生成批签发',
    `approval_time`       datetime                                                      DEFAULT NULL COMMENT '审核通过时间-作为批签发生成时间',
    `promoter_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成人',
    `batch_no`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生产批号',
    `template_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模板名称',
    `template_version_id` bigint                                                        DEFAULT NULL COMMENT '模板版本id',
    `template_version`    int                                                           DEFAULT NULL COMMENT '模板版本',
    `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '流程实例',
    `file_path`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成的批签发excel文件路径',
    `file_name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '批签发文件名',
    `file_url`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上传到服务器的文件url',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`         datetime                                                      DEFAULT NULL,
    `update_time`         datetime                                                      DEFAULT NULL,
    `is_deleted`          tinyint(1) NOT NULL                                           DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `bm_batch_release`
--



--
-- Table structure for table `bm_batch_release_history`
--

DROP TABLE IF EXISTS `bm_batch_release_history`;


CREATE TABLE `bm_batch_release_history`
(
    `id`                  bigint     NOT NULL,
    `plan_id`             bigint                                                        DEFAULT NULL COMMENT '生产计划id',
    `product_id`          bigint                                                        DEFAULT NULL COMMENT '成品id',
    `product_name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '成品名称',
    `process_id`          bigint                                                        DEFAULT NULL COMMENT '关联工艺',
    `process_name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联工艺名称',
    `process_version_id`  bigint                                                        DEFAULT NULL COMMENT '工艺版本id',
    `process_version`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工艺版本',
    `audit_state`         tinyint(1)                                                    DEFAULT NULL COMMENT '审核状态',
    `generated`           tinyint(1)                                                    DEFAULT NULL COMMENT '是否已生成批签发',
    `approval_time`       datetime                                                      DEFAULT NULL COMMENT '审核通过时间-作为批签发生成时间',
    `promoter_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成人',
    `batch_no`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生产批号',
    `template_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模板名称',
    `template_version_id` bigint                                                        DEFAULT NULL COMMENT '模板版本id',
    `template_version`    int                                                           DEFAULT NULL COMMENT '模板版本',
    `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '流程实例',
    `file_path`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成的批签发excel文件路径',
    `file_name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '批签发文件名',
    `file_url`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上传到服务器的文件url',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`         datetime                                                      DEFAULT NULL,
    `update_time`         datetime                                                      DEFAULT NULL,
    `is_deleted`          tinyint(1) NOT NULL                                           DEFAULT '0',
    `business_key`        bigint                                                        DEFAULT NULL COMMENT '流程查询key',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `bm_batch_release_history`
--



--
-- Table structure for table `bm_batch_release_template`
--

DROP TABLE IF EXISTS `bm_batch_release_template`;


CREATE TABLE `bm_batch_release_template`
(
    `id`           bigint     NOT NULL,
    `name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模板名称',
    `product_id`   bigint                                                        DEFAULT NULL COMMENT '关联成品id',
    `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联成品名称',
    `process_id`   bigint                                                        DEFAULT NULL COMMENT '关联工艺id',
    `process_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联工艺名',
    `remark`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`  datetime                                                      DEFAULT NULL,
    `update_time`  datetime                                                      DEFAULT NULL,
    `is_deleted`   tinyint(1) NOT NULL                                           DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `name_unique` (`name`, `product_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='批签发模板';


--
-- Dumping data for table `bm_batch_release_template`
--



--
-- Table structure for table `bm_batch_release_template_dataset`
--

DROP TABLE IF EXISTS `bm_batch_release_template_dataset`;


CREATE TABLE `bm_batch_release_template_dataset`
(
    `id`                  bigint     NOT NULL,
    `dataset_version_id`  bigint                                                       DEFAULT NULL COMMENT '数据集版本id',
    `template_version_id` bigint                                                       DEFAULT NULL COMMENT '模板版本id',
    `dataset_type`        tinyint(1)                                                   DEFAULT NULL COMMENT '数据集类型',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `create_time`         datetime                                                     DEFAULT NULL,
    `update_time`         datetime                                                     DEFAULT NULL,
    `is_deleted`          tinyint(1) NOT NULL                                          DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `bm_batch_release_template_dataset`
--



--
-- Table structure for table `bm_batch_release_template_version`
--

DROP TABLE IF EXISTS `bm_batch_release_template_version`;


CREATE TABLE `bm_batch_release_template_version`
(
    `id`             bigint     NOT NULL,
    `template_id`    bigint                                                        DEFAULT NULL COMMENT '关联模板id',
    `status`         tinyint(1)                                                    DEFAULT NULL COMMENT '0:编辑中 1:启用中 2:已确认',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `version_number` int                                                           DEFAULT NULL COMMENT '版本号',
    `source_version` int                                                           DEFAULT NULL COMMENT '源版本号',
    `process_id`     bigint                                                        DEFAULT NULL COMMENT '关联工艺id',
    `border_range`   longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '打印区域',
    `config`         longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'sheet配置',
    `sheet_data`     longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'sheet表格数据',
    `mark_data`      longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '标记哪些格子需要填充',
    `data_size`      double                                                        DEFAULT NULL COMMENT '数据大小',
    `create_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`    datetime                                                      DEFAULT NULL,
    `update_time`    datetime                                                      DEFAULT NULL,
    `is_deleted`     tinyint(1) NOT NULL                                           DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='批签发模板版本';


--
-- Dumping data for table `bm_batch_release_template_version`
--



--
-- Table structure for table `bm_cargo_position`
--

DROP TABLE IF EXISTS `bm_cargo_position`;


CREATE TABLE `bm_cargo_position`
(
    `id`          bigint NOT NULL AUTO_INCREMENT COMMENT '物理主键',
    `storage_id`  bigint                                  DEFAULT NULL COMMENT '所属区域 的id',
    `position`    varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '暂存货位',
    `id_path`     varchar(600) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所属区域id路径 id逗号隔开',
    `code`        varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货位编码',
    `remark`      varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `is_enable`   varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '启停',
    `create_time` datetime                                DEFAULT NULL,
    `update_time` datetime                                DEFAULT NULL,
    `create_by`   varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`   varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `is_deleted`  tinyint(1)                              DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='货位信息';


--
-- Dumping data for table `bm_cargo_position`
--



--
-- Table structure for table `bm_dataset`
--

DROP TABLE IF EXISTS `bm_dataset`;


CREATE TABLE `bm_dataset`
(
    `id`           bigint     NOT NULL,
    `name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据集名称',
    `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品名称',
    `product_id`   bigint                                                        DEFAULT NULL COMMENT '产品id',
    `process_id`   bigint                                                        DEFAULT NULL COMMENT '工艺id',
    `process_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工艺名称',
    `description`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
    `dataset_type` tinyint(1)                                                    DEFAULT NULL COMMENT '数据集类型',
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`  datetime                                                      DEFAULT NULL,
    `update_time`  datetime                                                      DEFAULT NULL,
    `is_deleted`   tinyint(1) NOT NULL                                           DEFAULT '0',
    `multi_source` tinyint(1)                                                    DEFAULT NULL COMMENT '是否为多数据源数据集',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `name_unique` (`name`, `product_id`, `dataset_type`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='批签发数据集';


--
-- Dumping data for table `bm_dataset`
--



--
-- Table structure for table `bm_dataset_data_point`
--

DROP TABLE IF EXISTS `bm_dataset_data_point`;


CREATE TABLE `bm_dataset_data_point`
(
    `id`                 bigint     NOT NULL,
    `name`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据点名称',
    `group_id`           bigint     NOT NULL COMMENT '数据点分组id',
    `dataset_version_id` bigint     NOT NULL COMMENT '数据集版本id',
    `dataset_id`         bigint     NOT NULL COMMENT '数据集id',
    `field_id`           bigint                                                        DEFAULT NULL COMMENT '组件fieldId',
    `process_id`         bigint                                                        DEFAULT NULL COMMENT '工艺id',
    `record_id`          bigint                                                        DEFAULT NULL COMMENT '记录项id',
    `procedure_step_id`  bigint                                                        DEFAULT NULL COMMENT '工序步骤id',
    `reused`             tinyint(1)                                                    DEFAULT NULL COMMENT '是否复用',
    `create_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`        datetime                                                      DEFAULT NULL,
    `update_time`        datetime                                                      DEFAULT NULL,
    `is_deleted`         tinyint(1) NOT NULL                                           DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='批签发数据点';


--
-- Dumping data for table `bm_dataset_data_point`
--



--
-- Table structure for table `bm_dataset_data_point_group`
--

DROP TABLE IF EXISTS `bm_dataset_data_point_group`;


CREATE TABLE `bm_dataset_data_point_group`
(
    `id`                 bigint     NOT NULL,
    `parent_id`          bigint                                                        DEFAULT NULL COMMENT '父级id',
    `name`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组名称',
    `dataset_id`         bigint     NOT NULL COMMENT '数据集id',
    `dataset_version_id` bigint     NOT NULL COMMENT '关联的数据集版本id',
    `create_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`        datetime                                                      DEFAULT NULL,
    `update_time`        datetime                                                      DEFAULT NULL,
    `is_deleted`         tinyint(1) NOT NULL                                           DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='数据集数据点分组';


--
-- Dumping data for table `bm_dataset_data_point_group`
--



--
-- Table structure for table `bm_dataset_data_point_handle`
--

DROP TABLE IF EXISTS `bm_dataset_data_point_handle`;


CREATE TABLE `bm_dataset_data_point_handle`
(
    `id`                 bigint     NOT NULL,
    `name`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据点名称',
    `group_id`           bigint     NOT NULL COMMENT '数据点分组id',
    `dataset_version_id` bigint     NOT NULL COMMENT '数据集版本id',
    `create_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`        datetime                                                      DEFAULT NULL,
    `update_time`        datetime                                                      DEFAULT NULL,
    `is_deleted`         tinyint(1) NOT NULL                                           DEFAULT '0',
    `function_type`      tinyint(1)                                                    DEFAULT NULL COMMENT '内置函数/配置函数',
    `function_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '函数名',
    `arguments`          longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '参数json',
    `scale`              int                                                           DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='批签发二次处理数据点';


--
-- Dumping data for table `bm_dataset_data_point_handle`
--



--
-- Table structure for table `bm_dataset_data_point_handle_group`
--

DROP TABLE IF EXISTS `bm_dataset_data_point_handle_group`;


CREATE TABLE `bm_dataset_data_point_handle_group`
(
    `id`                 bigint     NOT NULL,
    `parent_id`          bigint                                                        DEFAULT NULL COMMENT '父级id',
    `name`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组名称',
    `dataset_version_id` bigint     NOT NULL COMMENT '关联的数据集版本id',
    `create_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`        datetime                                                      DEFAULT NULL,
    `update_time`        datetime                                                      DEFAULT NULL,
    `is_deleted`         tinyint(1) NOT NULL                                           DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='数据集二次处理数据点分组';


--
-- Dumping data for table `bm_dataset_data_point_handle_group`
--



--
-- Table structure for table `bm_dataset_version`
--

DROP TABLE IF EXISTS `bm_dataset_version`;


CREATE TABLE `bm_dataset_version`
(
    `id`                      bigint     NOT NULL,
    `dataset_id`              bigint                                                        DEFAULT NULL COMMENT '数据集id',
    `process_id`              bigint                                                        DEFAULT NULL COMMENT '工艺id',
    `process_version_id_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '当前版本确认使用的工艺版本id字符串\r\n示例:6666,2222,4444',
    `status`                  tinyint(1)                                                    DEFAULT NULL COMMENT '状态:编辑中 启用中 已确认',
    `version_number`          int                                                           DEFAULT NULL COMMENT '版本号',
    `source_version`          int                                                           DEFAULT NULL COMMENT '源版本号',
    `source_version_id`       bigint                                                        DEFAULT NULL COMMENT '源版本id',
    `remark`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `create_by`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`             datetime                                                      DEFAULT NULL,
    `update_time`             datetime                                                      DEFAULT NULL,
    `is_deleted`              tinyint(1) NOT NULL                                           DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='批签发数据集版本表';


--
-- Dumping data for table `bm_dataset_version`
--



--
-- Table structure for table `bm_execute_attachment`
--

DROP TABLE IF EXISTS `bm_execute_attachment`;


CREATE TABLE `bm_execute_attachment`
(
    `id`                bigint                                                         NOT NULL,
    `batch_no`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `product_plan_id`   bigint                                                         NOT NULL,
    `process_version`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `process_id`        bigint                                                         NOT NULL,
    `record_item_id`    bigint                                                         NOT NULL,
    `record_version_id` bigint                                                         NOT NULL,
    `procedure_step_id` bigint                                                         NOT NULL,
    `is_reuse`          tinyint(1)                                                     NOT NULL,
    `copy_version`      int                                                            NOT NULL,
    `path`              varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `type`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `create_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `create_time`       datetime                                                     DEFAULT NULL,
    `update_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `update_time`       datetime                                                     DEFAULT NULL,
    `is_deleted`        tinyint(1)                                                   DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='生产执行附件';


--
-- Dumping data for table `bm_execute_attachment`
--



--
-- Table structure for table `bm_execute_form_data`
--

DROP TABLE IF EXISTS `bm_execute_form_data`;


CREATE TABLE `bm_execute_form_data`
(
    `id`                bigint                                                       NOT NULL,
    `value`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '填报值',
    `value_extension`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin                DEFAULT NULL COMMENT 'value扩展字段',
    `product_plan_id`   bigint                                                       NOT NULL COMMENT '生产计划id',
    `batch_no`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批号',
    `process_id`        bigint                                                       NOT NULL COMMENT '工艺id',
    `process_version`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工艺版本',
    `record_item_id`    bigint                                                       NOT NULL COMMENT '记录项id',
    `field_id`          bigint                                                       NOT NULL COMMENT '组件id',
    `component_type`    varchar(255) COLLATE utf8mb4_general_ci                      NOT NULL COMMENT '组件类型',
    `procedure_step_id` bigint                                                       NOT NULL COMMENT '历史工序步骤id',
    `is_reuse`          tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否是复用的',
    `is_system_create`  tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否是被系统创建',
    `is_discard`        tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否作废',
    `copy_version`      int                                                          NOT NULL DEFAULT '0' COMMENT '复制版本（未被复制默认0，每次复制加1）',
    `operation_type`    varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型',
    `operation_time`    datetime                                                     NOT NULL COMMENT '操作时间',
    `operation_user`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作人',
    `review_user`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '复核人',
    `review_time`       datetime                                                              DEFAULT NULL COMMENT '复核时间',
    `remark`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '备注',
    `rev`               int                                                          NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_field_data_operation` (`product_plan_id`, `record_item_id`, `is_reuse`, `copy_version`, `field_id`,
                                          `procedure_step_id`, `operation_type`, `rev`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='生产执行填报数据';


--
-- Dumping data for table `bm_execute_form_data`
--



--
-- Table structure for table `bm_execute_record_copy`
--

DROP TABLE IF EXISTS `bm_execute_record_copy`;


CREATE TABLE `bm_execute_record_copy`
(
    `id`                bigint                                                       NOT NULL,
    `batch_no`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `product_plan_id`   bigint                                                       NOT NULL COMMENT '生产计划id',
    `process_id`        bigint                                                       NOT NULL COMMENT '工艺id',
    `process_version`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工艺版本',
    `procedure_step_id` bigint                                                       NOT NULL DEFAULT '0' COMMENT '工序步骤id',
    `record_item_id`    bigint                                                       NOT NULL COMMENT '记录项id',
    `record_version_id` bigint                                                                DEFAULT NULL COMMENT '记录版本id',
    `version`           int                                                          NOT NULL COMMENT '版本号',
    `is_reuse`          tinyint(1)                                                   NOT NULL COMMENT '是否复用',
    `is_discard`        tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否废弃',
    `create_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL,
    `create_time`       datetime                                                              DEFAULT NULL,
    `update_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL,
    `update_time`       datetime                                                              DEFAULT NULL,
    `is_deleted`        tinyint(1)                                                   NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_productPlanId_recordItemId_copyVersion` (`product_plan_id`, `record_item_id`, `version`, `is_reuse`,
                                                            `procedure_step_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='记录项复制';


--
-- Dumping data for table `bm_execute_record_copy`
--



--
-- Table structure for table `bm_flow_audit`
--

DROP TABLE IF EXISTS `bm_flow_audit`;


CREATE TABLE `bm_flow_audit`
(
    `id`            bigint                                                       NOT NULL COMMENT '主键id',
    `code`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程编码',
    `name`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名称',
    `category_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类code',
    `create_time`   datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime                                                     DEFAULT NULL COMMENT '修改时间',
    `create_by`     varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`     varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
    `is_deleted`    tinyint                                                      DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `index_name` (`name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `bm_flow_audit`
--

LOCK TABLES `bm_flow_audit` WRITE;

INSERT INTO `bm_flow_audit`
VALUES (1740318445775486976, '120020001', '记录审核内置数据', '120020001', '2023-12-28 18:27:05', '2023-12-28 18:27:05',
        '1', '1', 0),
       (1740318624721305600, '120020002', '工艺审核内置流程', '120020002', '2023-12-28 18:27:48', '2023-12-28 18:27:48',
        '1', '1', 0),
       (1740318722641518592, '120030001', '生产计划内置流程', '120030001', '2023-12-28 18:28:14', '2023-12-28 18:28:14',
        '1', '1', 0),
       (1740318809333587968, '120040001', '批签发内置流程', '120040001', '2023-12-28 18:28:32', '2023-12-28 18:28:32',
        '1', '1', 0),
       (1765630953285357568, '120020003', '配方审批内置模型', '120020003', '2024-03-07 14:49:57', '2024-03-19 15:54:09',
        '1760850141218906112', '1760850209980325888', 0);

UNLOCK TABLES;

--
-- Table structure for table `bm_flow_audit_category`
--

DROP TABLE IF EXISTS `bm_flow_audit_category`;


CREATE TABLE `bm_flow_audit_category`
(
    `id`          bigint     NOT NULL COMMENT '主键id',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分类名称',
    `code`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分类编码',
    `parent_id`   bigint                                                       DEFAULT NULL COMMENT '上级id',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `create_time` datetime                                                     DEFAULT NULL,
    `update_time` datetime                                                     DEFAULT NULL,
    `is_deleted`  tinyint(1) NOT NULL                                          DEFAULT '0',
    `tree_code`   varchar(255) COLLATE utf8mb4_general_ci                      DEFAULT NULL COMMENT '层级code',
    `tree_name`   varchar(255) COLLATE utf8mb4_general_ci                      DEFAULT NULL COMMENT '层级名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='流程配置分类表';


--
-- Dumping data for table `bm_flow_audit_category`
--



--
-- Table structure for table `bm_flow_audit_message`
--

DROP TABLE IF EXISTS `bm_flow_audit_message`;


CREATE TABLE `bm_flow_audit_message`
(
    `id`            bigint                                                       NOT NULL,
    `node_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `user_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `message_type`  varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `deployment_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `create_time`   datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime                                                     DEFAULT NULL COMMENT '修改时间',
    `create_by`     varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`     varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
    `is_deleted`    tinyint                                                      DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `bm_flow_audit_message`
--



--
-- Table structure for table `bm_flow_audit_user`
--

DROP TABLE IF EXISTS `bm_flow_audit_user`;


CREATE TABLE `bm_flow_audit_user`
(
    `id`            bigint                                                       NOT NULL COMMENT '主键id',
    `deployment_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程定义id',
    `assignee`      bigint                                                       NOT NULL COMMENT '处理人',
    `assignee_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '处理人类型',
    `node_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点key',
    `create_time`   datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime                                                     DEFAULT NULL COMMENT '修改时间',
    `create_by`     varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`     varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
    `is_deleted`    tinyint                                                      DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `bm_flow_audit_user`
--



--
-- Table structure for table `bm_flow_audit_version`
--

DROP TABLE IF EXISTS `bm_flow_audit_version`;


CREATE TABLE `bm_flow_audit_version`
(
    `id`              bigint  NOT NULL COMMENT '主键id',
    `flow_audit_id`   bigint  NOT NULL COMMENT '管理表id',
    `history_version` bigint                                                       DEFAULT NULL COMMENT '引用版本',
    `version`         bigint  NOT NULL COMMENT '版本号',
    `state`           tinyint NOT NULL                                             DEFAULT '1' COMMENT '状态，1：设计中；2：启用中；3：历史',
    `remark`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `deployment_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流程部署id',
    `create_time`     datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_time`     datetime                                                     DEFAULT NULL COMMENT '修改时间',
    `create_by`       varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`       varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
    `is_deleted`      tinyint                                                      DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `bm_flow_audit_version`
--

LOCK TABLES `bm_flow_audit_version` WRITE;

INSERT INTO `bm_flow_audit_version`
VALUES (1740318446748598272, 1740318445775486976, NULL, 1, 1, '记录审批内置流程，勿删',
        '8066d103-1822-4e0d-8460-2ad03366fb54', '2023-12-28 18:27:05', '2023-12-28 18:27:05', '1', '1', 0),
       (1740318624905854976, 1740318624721305600, NULL, 1, 1, '工艺审批内置流程，勿删',
        'f3b4eafb-49eb-40a4-8ebf-e70ffb1362e3', '2023-12-28 18:27:48', '2023-12-28 18:27:48', '1', '1', 0),
       (1740318732728815616, 1740318722641518592, NULL, 1, 1, '生产计划审批内置流程，勿删',
        '1060d793-1fe0-456f-aebd-0525ffdab2bc', '2023-12-28 18:28:14', '2023-12-28 18:28:14', '1', '1', 0),
       (1740318809568456704, 1740318809333587968, NULL, 1, 1, '批签发审批内置流程，勿删',
        '0cfbdffa-7f8e-4c54-be43-fa294d2fed98', '2023-12-28 18:28:32', '2023-12-28 18:28:32', '1', '1', 0),
       (1770724076432789504, 1765630953285357568, NULL, 1, 1, '配方审批内置', 'ae2aa946-1ce0-4e63-8eb7-65ecd8be34e4',
        '2024-03-21 16:08:12', '2024-03-26 11:44:01', '1760850209980325888', '1', 0);

UNLOCK TABLES;

--
-- Table structure for table `bm_log_operation`
--

DROP TABLE IF EXISTS `bm_log_operation`;


CREATE TABLE `bm_log_operation`
(
    `id`             bigint                                                       NOT NULL,
    `business_id`    bigint                                                       NOT NULL COMMENT '业务数据id',
    `module`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务模块',
    `operation_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL,
    `update_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL,
    `create_time`    datetime                                                              DEFAULT NULL,
    `update_time`    datetime                                                              DEFAULT NULL,
    `is_deleted`     tinyint(1)                                                   NOT NULL DEFAULT '0',
    `node_name`      varchar(64) COLLATE utf8mb4_general_ci                                DEFAULT NULL COMMENT '审批节点名称',
    `comment`        varchar(255) COLLATE utf8mb4_general_ci                               DEFAULT NULL COMMENT '审批节点名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `bm_log_operation`
--



--
-- Table structure for table `bm_material`
--

DROP TABLE IF EXISTS `bm_material`;


CREATE TABLE `bm_material`
(
    `id`                          bigint                                                  NOT NULL,
    `material_category_id`        bigint                                                  NOT NULL COMMENT '分类id',
    `principal_material_id`       bigint                                                           DEFAULT NULL COMMENT '所属物料id',
    `name`                        varchar(100) COLLATE utf8mb4_general_ci                          DEFAULT NULL COMMENT '名称',
    `code`                        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '编码',
    `specification`               varchar(100) COLLATE utf8mb4_general_ci                          DEFAULT NULL COMMENT '规格',
    `unit_id`                     bigint                                                  NOT NULL COMMENT '单位',
    `is_sub_material`             tinyint(1)                                              NOT NULL COMMENT '是否是成员物料',
    `remark`                      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    DEFAULT NULL COMMENT '备注',
    `status`                      tinyint(1)                                              NOT NULL DEFAULT '0' COMMENT '启停状态',
    `create_by`                   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     DEFAULT NULL,
    `update_by`                   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     DEFAULT NULL,
    `create_time`                 datetime                                                         DEFAULT NULL,
    `update_time`                 datetime                                                         DEFAULT NULL,
    `is_deleted`                  tinyint(1)                                              NOT NULL DEFAULT '0',
    `merge_code`                  varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '合并编码:分类合并编码+自身编码',
    `unit_extend_id`              bigint                                                           DEFAULT NULL COMMENT '拓展单位id',
    `is_finish_product`           tinyint(1)                                              NOT NULL DEFAULT '0' COMMENT '是否是成品',
    `platform_material_id`        bigint                                                  NOT NULL COMMENT '平台物料关联id',
    `production_cycle`            int                                                              DEFAULT NULL COMMENT '生产周期（天）',
    `inner_packing_specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    DEFAULT NULL COMMENT '内包规格',
    `packing_specification`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    DEFAULT NULL COMMENT '包装规格',
    `expand_info`                 text COLLATE utf8mb4_general_ci,
    PRIMARY KEY (`id`, `is_finish_product`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='物料信息';


--
-- Dumping data for table `bm_material`
--



--
-- Table structure for table `bm_material_category`
--

DROP TABLE IF EXISTS `bm_material_category`;


CREATE TABLE `bm_material_category`
(
    `id`                   bigint                                                  NOT NULL,
    `parent_id`            bigint                                                  NOT NULL DEFAULT '0' COMMENT '父级id，默认0',
    `code`                 varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '编码',
    `name`                 varchar(100) COLLATE utf8mb4_general_ci                          DEFAULT NULL COMMENT '名称',
    `create_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     DEFAULT NULL,
    `update_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     DEFAULT NULL,
    `create_time`          datetime                                                         DEFAULT NULL,
    `update_time`          datetime                                                         DEFAULT NULL,
    `is_deleted`           tinyint(1)                                              NOT NULL DEFAULT '0',
    `merge_code`           varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '合并编码:父级合并编码+自身编码',
    `category_type`        int                                                              DEFAULT NULL COMMENT '分类类型 0原辅包信息、1中间品信息、2产品信息',
    `platform_category_Id` bigint                                                           DEFAULT NULL COMMENT '平台关联分类id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='物料分类';


--
-- Dumping data for table `bm_material_category`
--



--
-- Table structure for table `bm_material_log`
--

DROP TABLE IF EXISTS `bm_material_log`;


CREATE TABLE `bm_material_log`
(
    `id`                      bigint                                                        NOT NULL AUTO_INCREMENT,
    `operation_time`          datetime                                                      DEFAULT NULL COMMENT '操作时间',
    `operation_type`          tinyint(1)                                                    NOT NULL COMMENT '操作类型',
    `specific_operation_type` tinyint(1)                                                    NOT NULL COMMENT '具体操作类型',
    `category_type`           tinyint(1)                                                    DEFAULT NULL COMMENT '物料类型',
    `user_id`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
    `user_name`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
    `login_name`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录名',
    `material_name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料名称',
    `material_id`             bigint                                                        NOT NULL COMMENT '物料id',
    `material_code`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料合并编码',
    `material_batch_id`       bigint                                                        DEFAULT NULL COMMENT '物料批次id',
    `material_batch_no`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料批次号',
    `storage_material_id`     bigint                                                        DEFAULT NULL COMMENT '物料件id',
    `material_no`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料件号',
    `scheduled`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预定量',
    `available`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '可用量',
    `tare_weight`             varchar(255) COLLATE utf8mb4_general_ci                       DEFAULT NULL COMMENT '皮重',
    `gross_weight`            varchar(255) COLLATE utf8mb4_general_ci                       DEFAULT NULL COMMENT '毛重',
    `unit_name`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单位名称',
    `expiration_time`         date                                                          DEFAULT NULL COMMENT '有效期',
    `product_name`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品名称',
    `product_id`              bigint                                                        DEFAULT NULL COMMENT '产品id',
    `product_merge_code`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品合并编码',
    `batch_no`                varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '生产批号',
    `material_position_name`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '暂存货位',
    `material_position_code`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货位编码',
    `material_position_id`    bigint                                                        DEFAULT NULL COMMENT '货位id',
    `storage_id`              bigint                                                        DEFAULT NULL COMMENT '所属区域id',
    `material_position_path`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所属区域',
    `inspect_id`              bigint                                                        DEFAULT NULL COMMENT '校验id',
    `expand_info`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '物料拓展信息',
    `original_code`           varchar(100) COLLATE utf8mb4_general_ci                       DEFAULT NULL COMMENT '原始编码',
    `original_no`             varchar(100) COLLATE utf8mb4_general_ci                       DEFAULT NULL COMMENT '原厂编号',
    `request_verify_no`       varchar(100) COLLATE utf8mb4_general_ci                       DEFAULT NULL COMMENT '请验单号',
    `report_no`               varchar(100) COLLATE utf8mb4_general_ci                       DEFAULT NULL COMMENT '报告单号',
    `enable`                  tinyint(1)                                                    DEFAULT NULL COMMENT '可用状态',
    `remark`                  varchar(255) COLLATE utf8mb4_general_ci                       DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='物料日志';


--
-- Dumping data for table `bm_material_log`
--



--
-- Table structure for table `bm_operation_log`
--

DROP TABLE IF EXISTS `bm_operation_log`;


CREATE TABLE `bm_operation_log`
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
  ROW_FORMAT = DYNAMIC COMMENT ='操作日志表';


--
-- Dumping data for table `bm_operation_log`
--



--
-- Table structure for table `bm_procedure`
--

DROP TABLE IF EXISTS `bm_procedure`;


CREATE TABLE `bm_procedure`
(
    `id`         bigint NOT NULL,
    `process_id` bigint                                                       DEFAULT NULL,
    `name`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_processId_name` (`process_id`, `name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='工序基础信息';


--
-- Dumping data for table `bm_procedure`
--



--
-- Table structure for table `bm_procedure_model`
--

DROP TABLE IF EXISTS `bm_procedure_model`;


CREATE TABLE `bm_procedure_model`
(
    `id`                 bigint                                                       NOT NULL,
    `node_id`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点id',
    `name`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工序名称',
    `principal`          bigint                                                       NOT NULL COMMENT '负责人',
    `stage_code`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '阶段编码',
    `process_id`         bigint                                                       NOT NULL COMMENT '工艺id',
    `procedure_id`       bigint                                                       NOT NULL COMMENT '基础工序id',
    `process_version`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本号',
    `process_version_id` bigint                                                       NOT NULL COMMENT '工艺版本id',
    `process_model_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流程模型id',
    `duration`           bigint                                                       DEFAULT NULL COMMENT '工序时长',
    `time_unit`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '时长单位',
    `create_time`        datetime                                                     DEFAULT NULL,
    `create_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `update_time`        datetime                                                     DEFAULT NULL,
    `update_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `is_deleted`         tinyint(1)                                                   DEFAULT '0',
    `del_id_flag`        bigint                                                       DEFAULT '0' COMMENT '删除标识',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_processId_name_version_delIdFlag` (`process_id`, `name`, `process_version`, `del_id_flag`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='工序信息';


--
-- Dumping data for table `bm_procedure_model`
--



--
-- Table structure for table `bm_procedure_model_group`
--

DROP TABLE IF EXISTS `bm_procedure_model_group`;


CREATE TABLE `bm_procedure_model_group`
(
    `id`                 bigint NOT NULL,
    `procedure_model_id` bigint NOT NULL,
    `group_id`           bigint NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='工序班组';


--
-- Dumping data for table `bm_procedure_model_group`
--



--
-- Table structure for table `bm_procedure_model_material`
--

DROP TABLE IF EXISTS `bm_procedure_model_material`;


CREATE TABLE `bm_procedure_model_material`
(
    `procedure_model_id`          bigint NOT NULL COMMENT '工序id',
    `product_formula_material_id` bigint NOT NULL COMMENT '配方物料id'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='工序绑定配方物料表';


--
-- Dumping data for table `bm_procedure_model_material`
--



--
-- Table structure for table `bm_procedure_step`
--

DROP TABLE IF EXISTS `bm_procedure_step`;


CREATE TABLE `bm_procedure_step`
(
    `id`           bigint NOT NULL,
    `process_id`   bigint                                                       DEFAULT NULL,
    `procedure_id` bigint                                                       DEFAULT NULL,
    `name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_procedureId_name` (`procedure_id`, `name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='工序步骤基础信息';


--
-- Dumping data for table `bm_procedure_step`
--



--
-- Table structure for table `bm_procedure_step_config`
--

DROP TABLE IF EXISTS `bm_procedure_step_config`;


CREATE TABLE `bm_procedure_step_config`
(
    `id`                bigint                                                       NOT NULL COMMENT '主键id',
    `procedure_step_id` bigint                                                       NOT NULL COMMENT '工序步骤id',
    `node_id`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程节点id',
    `process_id`        bigint                                                       NOT NULL COMMENT '工艺id',
    `version`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本号',
    `record_item_id`    bigint                                                       NOT NULL COMMENT '记录项id',
    `config_info`       longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '配置信息',
    `component_id`      bigint                                                       NOT NULL COMMENT '组件标识',
    `create_time`       datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_time`       datetime                                                     DEFAULT NULL COMMENT '修改时间',
    `create_by`         varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`         varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
    `is_deleted`        tinyint                                                      DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_process_id_version` (`process_id`, `version`) USING BTREE,
    KEY `idx_procedure_step_id` (`procedure_step_id`) USING BTREE,
    KEY `idx_component_id` (`component_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='工艺步骤配置表';


--
-- Dumping data for table `bm_procedure_step_config`
--



--
-- Table structure for table `bm_procedure_step_model`
--

DROP TABLE IF EXISTS `bm_procedure_step_model`;


CREATE TABLE `bm_procedure_step_model`
(
    `id`                 bigint                                                       NOT NULL COMMENT '主键id',
    `node_id`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程模型节点id',
    `procedure_step_id`  bigint                                                                DEFAULT NULL COMMENT '基础工序步骤ID',
    `procedure_model_id` bigint                                                       NOT NULL COMMENT '工序模型id',
    `procedure_id`       bigint                                                       NOT NULL COMMENT '历史工序id',
    `process_id`         bigint                                                       NOT NULL COMMENT '工艺id',
    `process_version`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工艺版本',
    `node_function`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '节点功能',
    `name`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '步骤名称',
    `reusable`           tinyint(1)                                                            DEFAULT NULL COMMENT '是否可复用',
    `record_item_id`     bigint                                                       NOT NULL COMMENT '记录项',
    `record_version_id`  bigint                                                                DEFAULT NULL COMMENT '批记录版本id',
    `operation_sop`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '操作规程',
    `duration`           bigint                                                                DEFAULT NULL COMMENT '时长',
    `time_unit`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '时间单位',
    `create_time`        datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `update_time`        datetime                                                              DEFAULT NULL COMMENT '修改时间',
    `create_by`          varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT NULL COMMENT '创建人',
    `update_by`          varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT NULL COMMENT '修改人',
    `is_deleted`         tinyint                                                               DEFAULT '0' COMMENT '是否删除',
    `del_id_flag`        bigint                                                       NOT NULL DEFAULT '0' COMMENT '删除标识',
    `area`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL,
    `device`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_process_procedureId_name` (`process_id`, `procedure_model_id`, `name`, `del_id_flag`) USING BTREE,
    KEY `idx_process_id_version_reusable` (`process_id`, `process_version`, `reusable`) USING BTREE,
    KEY `idx_record_item_id` (`record_item_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='工艺配置步骤表';


--
-- Dumping data for table `bm_procedure_step_model`
--



--
-- Table structure for table `bm_procedure_step_role`
--

DROP TABLE IF EXISTS `bm_procedure_step_role`;


CREATE TABLE `bm_procedure_step_role`
(
    `procedure_step_id` bigint NOT NULL COMMENT '工序步骤id',
    `role_id`           bigint NOT NULL COMMENT '角色id',
    UNIQUE KEY `uk_procedure_step_role` (`procedure_step_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='工序步骤-角色关联';


--
-- Dumping data for table `bm_procedure_step_role`
--



--
-- Table structure for table `bm_process`
--

DROP TABLE IF EXISTS `bm_process`;


CREATE TABLE `bm_process`
(
    `id`                  bigint                                                       NOT NULL COMMENT '主键id',
    `product_id`          bigint                                                       NOT NULL COMMENT '产品id',
    `product_category_id` bigint                                                       DEFAULT NULL COMMENT '产品分类id',
    `name`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工艺名称',
    `active_version`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '激活版本',
    `create_time`         datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_time`         datetime                                                     DEFAULT NULL COMMENT '修改时间',
    `create_by`           varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`           varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
    `is_deleted`          tinyint(1)                                                   DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_name` (`name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='工艺信息表';


--
-- Dumping data for table `bm_process`
--



--
-- Table structure for table `bm_process_batch_record`
--

DROP TABLE IF EXISTS `bm_process_batch_record`;


CREATE TABLE `bm_process_batch_record`
(
    `id`                      bigint                                                       NOT NULL,
    `process_version`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工艺版本',
    `process_version_id`      bigint                                                       NOT NULL COMMENT '工艺id',
    `batch_record_version`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批记录版本',
    `batch_record_id`         bigint                                                       NOT NULL COMMENT '批记录id',
    `batch_record_version_id` bigint                                                       NOT NULL COMMENT '批记录版本id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='工艺关联记录中间表';


--
-- Dumping data for table `bm_process_batch_record`
--



--
-- Table structure for table `bm_process_record_order`
--

DROP TABLE IF EXISTS `bm_process_record_order`;


CREATE TABLE `bm_process_record_order`
(
    `id`                 bigint                                                       NOT NULL COMMENT '等于批记录版本的id',
    `record_item_id`     bigint                                                       NOT NULL COMMENT '记录项id',
    `record_item_order`  bigint                                                       NOT NULL COMMENT '记录项顺序',
    `process_version_id` bigint                                                       NOT NULL COMMENT '工艺版本id',
    `process_id`         bigint                                                       NOT NULL COMMENT '工艺id',
    `process_version`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工艺版本',
    `record_version_id`  bigint                                                       NOT NULL COMMENT '记录版本',
    `reusable`           tinyint(1)                                                   NOT NULL COMMENT '是否复用',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='工艺批记录归档顺序';


--
-- Dumping data for table `bm_process_record_order`
--



--
-- Table structure for table `bm_process_relation`
--

DROP TABLE IF EXISTS `bm_process_relation`;


CREATE TABLE `bm_process_relation`
(
    `id`                  bigint NOT NULL,
    `process_id`          bigint NOT NULL COMMENT '工艺id',
    `relation_process_id` bigint NOT NULL COMMENT '关联工艺id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='关联工艺';


--
-- Dumping data for table `bm_process_relation`
--



--
-- Table structure for table `bm_process_relation_material`
--

DROP TABLE IF EXISTS `bm_process_relation_material`;


CREATE TABLE `bm_process_relation_material`
(
    `id`                  bigint NOT NULL,
    `process_relation_id` bigint NOT NULL COMMENT '工艺关联id',
    `material_id`         bigint NOT NULL COMMENT '物料id',
    `process_id`          bigint NOT NULL COMMENT '工艺id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='工艺关联物料';


--
-- Dumping data for table `bm_process_relation_material`
--



--
-- Table structure for table `bm_process_version`
--

DROP TABLE IF EXISTS `bm_process_version`;


CREATE TABLE `bm_process_version`
(
    `id`                         bigint                                                       NOT NULL COMMENT '主键id',
    `process_id`                 bigint                                                                DEFAULT NULL,
    `process_instance_id`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '审批流程实例id',
    `version`                    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本号',
    `product_formula_version_id` bigint                                                                DEFAULT NULL COMMENT '配方版本id',
    `description`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '描述',
    `action_state`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '状态：1:编辑；2：审核；3：确定',
    `state`                      tinyint                                                      NOT NULL DEFAULT '1' COMMENT '1:启用；2：停用',
    `process_model_id`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程模型id',
    `relations`                  longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '关联工艺',
    `create_time`                datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `update_time`                datetime                                                              DEFAULT NULL COMMENT '修改时间',
    `create_by`                  varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT NULL COMMENT '创建人',
    `update_by`                  varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT NULL COMMENT '修改人',
    `is_deleted`                 tinyint                                                               DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_process_version` (`process_id`, `version`) USING BTREE COMMENT '工艺版本唯一索引',
    KEY `idx_process_instance_id` (`process_instance_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='工艺版本';


--
-- Dumping data for table `bm_process_version`
--



--
-- Table structure for table `bm_product_formula`
--

DROP TABLE IF EXISTS `bm_product_formula`;


CREATE TABLE `bm_product_formula`
(
    `id`                    bigint NOT NULL,
    `name`                  varchar(255) DEFAULT NULL COMMENT '配方名称',
    `product_id`            bigint       DEFAULT NULL COMMENT '产品id',
    `product_name`          varchar(255) DEFAULT NULL COMMENT '产品名称',
    `product_merge_code`    varchar(255) DEFAULT NULL COMMENT '产品合并编码',
    `product_specification` varchar(255) DEFAULT NULL COMMENT '产品规格',
    `unit_id`               bigint       DEFAULT NULL COMMENT '单位id',
    `create_time`           datetime     DEFAULT NULL,
    `update_time`           datetime     DEFAULT NULL,
    `create_by`             varchar(64)  DEFAULT NULL,
    `update_by`             varchar(64)  DEFAULT NULL,
    `is_deleted`            tinyint(1)   DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='产品配方';


--
-- Dumping data for table `bm_product_formula`
--



--
-- Table structure for table `bm_product_formula_material`
--

DROP TABLE IF EXISTS `bm_product_formula_material`;


CREATE TABLE `bm_product_formula_material`
(
    `id`                             bigint          NOT NULL,
    `version_id`                     bigint          NOT NULL COMMENT '配方版本id',
    `material_type`                  tinyint(1)      DEFAULT NULL COMMENT '物料类型',
    `material_id`                    bigint          NOT NULL COMMENT '物料id',
    `material_name`                  varchar(255)    DEFAULT NULL COMMENT '物料名称',
    `material_merge_code`            varchar(255)    DEFAULT NULL COMMENT '物料合并编码',
    `material_specification`         varchar(255)    DEFAULT NULL COMMENT '物料规格',
    `unit_id`                        bigint          NOT NULL COMMENT '单位id',
    `quantity`                       decimal(20, 10) NOT NULL COMMENT '数量',
    `quantity_type`                  tinyint(1)      DEFAULT NULL COMMENT '数量类型',
    `rounding`                       varchar(255)    DEFAULT NULL COMMENT '修约规则',
    `scale`                          decimal(20, 10) DEFAULT NULL COMMENT '物料精度',
    `scale_length`                   int             DEFAULT NULL COMMENT '精度长度',
    `dry_pure_type`                  tinyint         DEFAULT NULL COMMENT '折干折纯类型',
    `dry_pure_param`                 decimal(20, 10) DEFAULT NULL COMMENT '折干折纯参数',
    `unpacking_tolerance_type`       tinyint(1)      DEFAULT NULL COMMENT '拆包允差类型',
    `unpacking_tolerance_upper`      decimal(20, 10) DEFAULT NULL COMMENT '拆包允差上限',
    `unpacking_tolerance_lower`      decimal(20, 10) DEFAULT NULL COMMENT '拆包允差下限',
    `charge_mixture_tolerance_type`  tinyint(1)      DEFAULT NULL COMMENT '配料允差类型',
    `charge_mixture_tolerance_upper` decimal(20, 10) DEFAULT NULL COMMENT '配料允差上限',
    `charge_mixture_tolerance_lower` decimal(20, 10) DEFAULT NULL COMMENT '配料允差下限',
    `oddment_tolerance_type`         tinyint(1)      DEFAULT NULL COMMENT '余料允差类型',
    `oddment_tolerance_upper`        decimal(20, 10) DEFAULT NULL COMMENT '余料允差上限',
    `oddment_tolerance_lower`        decimal(20, 10) DEFAULT NULL COMMENT '余料允差下限',
    `create_time`                    datetime        DEFAULT NULL,
    `update_time`                    datetime        DEFAULT NULL,
    `create_by`                      varchar(64)     DEFAULT NULL,
    `update_by`                      varchar(64)     DEFAULT NULL,
    `is_deleted`                     tinyint(1)      DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='产品配方物料';


--
-- Dumping data for table `bm_product_formula_material`
--



--
-- Table structure for table `bm_product_formula_version`
--

DROP TABLE IF EXISTS `bm_product_formula_version`;


CREATE TABLE `bm_product_formula_version`
(
    `id`                  bigint NOT NULL,
    `version_no`          varchar(255)    DEFAULT NULL COMMENT '版本号',
    `product_formula_id`  bigint          DEFAULT NULL COMMENT '产品配方id',
    `description`         varchar(255)    DEFAULT NULL COMMENT '版本描述',
    `status`              tinyint(1)      DEFAULT NULL COMMENT '状态',
    `enable`              tinyint(1)      DEFAULT NULL COMMENT '启停状态',
    `batch_quantity`      decimal(20, 10) DEFAULT NULL COMMENT '批量',
    `unit_id`             bigint          DEFAULT NULL COMMENT '单位id',
    `process_instance_id` varchar(255)    DEFAULT NULL COMMENT '流程实例id',
    `create_time`         datetime        DEFAULT NULL,
    `update_time`         datetime        DEFAULT NULL,
    `create_by`           varchar(64)     DEFAULT NULL,
    `update_by`           varchar(64)     DEFAULT NULL,
    `is_deleted`          tinyint(1)      DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='产品配方版本';


--
-- Dumping data for table `bm_product_formula_version`
--



--
-- Table structure for table `bm_product_instruction`
--

DROP TABLE IF EXISTS `bm_product_instruction`;


CREATE TABLE `bm_product_instruction`
(
    `id`                   bigint                                                       NOT NULL COMMENT '主键',
    `product_plan_id`      bigint                                                       NOT NULL COMMENT '生产计划id',
    `node_id`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '生产工序节点id',
    `procedure_id`         bigint                                                       NOT NULL COMMENT '历史工序id(以此判断多给版本的节点是否是同一工序)',
    `procedure_model_id`   bigint                                                       NOT NULL COMMENT '生产工序id',
    `procedure_model_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '生产工序名称',
    `procedure_model_code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '生产工序阶段编码',
    `principal`            bigint                                                       NOT NULL COMMENT '负责人',
    `status`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'RESOLVE' COMMENT '指令单状态 已分解 RESOLVE已确认 CONFIRM',
    `sort`                 int(4) unsigned zerofill                                     NOT NULL COMMENT '排序',
    `create_time`          datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `update_time`          datetime                                                              DEFAULT NULL COMMENT '更新时间',
    `create_by`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '创建人',
    `update_by`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '更新人',
    `is_deleted`           tinyint(1)                                                            DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_productPlanId_nodeId` (`product_plan_id`, `node_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='生产计划指令单表';


--
-- Dumping data for table `bm_product_instruction`
--



--
-- Table structure for table `bm_product_instruction_team`
--

DROP TABLE IF EXISTS `bm_product_instruction_team`;


CREATE TABLE `bm_product_instruction_team`
(
    `id`                        bigint                                                        NOT NULL COMMENT '主键',
    `instruction_id`            bigint                                                        NOT NULL COMMENT '指令单id',
    `product_plan_id`           bigint                                                        NOT NULL COMMENT '生产计划id',
    `node_id`                   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '生产工序节点id',
    `procedure_id`              bigint                                                        NOT NULL COMMENT '历史工序id(以此判断多给版本的节点是否是同一工序)',
    `procedure_model_id`        bigint                                                        NOT NULL COMMENT '生产工序id',
    `node_step_id`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '生产工序步骤节点id',
    `procedure_step_id`         bigint                                                        NOT NULL COMMENT '历史工序id(以此判断多给版本的节点是否是同一工序)',
    `procedure_step_model_id`   bigint                                                        NOT NULL COMMENT '生产工序步骤id',
    `procedure_step_model_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '生产工序步骤名称',
    `procedure_step_time`       int                                                           NOT NULL COMMENT '执行时长',
    `procedure_step_time_unit`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行时长单位',
    `team_ids`                  longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '班组id列表',
    `sort`                      int(4) unsigned zerofill                                      NOT NULL COMMENT '排序',
    `create_time`               datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_time`               datetime                                                     DEFAULT NULL COMMENT '更新时间',
    `create_by`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
    `is_deleted`                tinyint(1)                                                   DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_instructionId_nodeStepId` (`instruction_id`, `node_step_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='生产计划指令单班组表';


--
-- Dumping data for table `bm_product_instruction_team`
--



--
-- Table structure for table `bm_product_plan`
--

DROP TABLE IF EXISTS `bm_product_plan`;


CREATE TABLE `bm_product_plan`
(
    `id`                          bigint                                                       NOT NULL COMMENT '主键',
    `plan_no`                     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计划编号',
    `batch_no`                    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '生产批号',
    `product_date`                date                                                         NOT NULL COMMENT '生产时间',
    `type`                        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计划类型',
    `product_id`                  bigint                                                       NOT NULL COMMENT '产品Id',
    `product_name`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品名称',
    `product_merge_code`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品编码',
    `product_specification`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品规格',
    `inner_packing_specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '内包规格',
    `packing_specification`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '包装规格',
    `process_id`                  bigint                                                       NOT NULL COMMENT '生产工艺id',
    `process_name`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '生产工艺名称',
    `process_num`                 int                                                          NOT NULL COMMENT '工艺下节点数量',
    `process_version`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '生产工艺版本',
    `status`                      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'EDIT' COMMENT '状态 编辑EDIT 审批AUDIT 确认CONFIRM 废弃DISCARD',
    `instruct_status`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'WAIT_DECOMPOSE' COMMENT '状态 待分解WAIT_DECOMPOSE 待确认WAIT_CONFIRM 待下发WAIT_SEND 已下发 SEND',
    `is_relation`                 varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'FALSE' COMMENT '是否被其他批次关联 未关联FALSE 已关联TRUE',
    `process_instance_id`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '流程实例',
    `is_start`                    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'WAIT' COMMENT '是否开始生产 等待WAIT 开始STARTING 结束 END',
    `start_time`                  datetime                                                              DEFAULT NULL COMMENT '生产计划开始时间',
    `end_time`                    datetime                                                              DEFAULT NULL COMMENT '生产计划结束时间',
    `execute_process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '生产执行流程实例',
    `confirm_time`                datetime                                                              DEFAULT NULL COMMENT '确认时间',
    `batch_quantity`              decimal(20, 10)                                                       DEFAULT NULL COMMENT '生产批量',
    `unit_id`                     bigint                                                                DEFAULT NULL COMMENT '生产批量单位id',
    `create_time`                 datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `update_time`                 datetime                                                              DEFAULT NULL COMMENT '更新时间',
    `create_by`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '创建人',
    `update_by`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '更新人',
    `is_deleted`                  tinyint(1)                                                            DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_planNo` (`plan_no`) USING BTREE,
    UNIQUE KEY `uk_processId_batchNo` (`process_id`, `batch_no`) USING BTREE,
    KEY `idx_isStart` (`is_start`) USING BTREE,
    KEY `idx_processInstanceId` (`process_instance_id`) USING BTREE,
    KEY `idx_executeProcessInstanceId` (`execute_process_instance_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='生产计划表';


--
-- Dumping data for table `bm_product_plan`
--



--
-- Table structure for table `bm_product_plan_code_rule`
--

DROP TABLE IF EXISTS `bm_product_plan_code_rule`;


CREATE TABLE `bm_product_plan_code_rule`
(
    `id`             bigint                                                        NOT NULL COMMENT '主键',
    `type`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '生产计划编码规则分类',
    `process_id`     bigint                                                        NOT NULL COMMENT '生产工艺id',
    `code_rule_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码规则code',
    `code_rule_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码规则名称',
    `create_time`    datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime                                                     DEFAULT NULL COMMENT '更新时间',
    `create_by`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
    `is_deleted`     tinyint(1)                                                   DEFAULT '0' COMMENT '是否删除',
    UNIQUE KEY `uk_type_processId` (`type`, `process_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='生产计划编码规则';


--
-- Dumping data for table `bm_product_plan_code_rule`
--



--
-- Table structure for table `bm_product_plan_relation`
--

DROP TABLE IF EXISTS `bm_product_plan_relation`;


CREATE TABLE `bm_product_plan_relation`
(
    `id`                       bigint                                                       NOT NULL COMMENT '主键',
    `product_plan_id`          bigint                                                       NOT NULL COMMENT '生产计划id',
    `process_id`               bigint                                                                DEFAULT NULL COMMENT '工序id',
    `relation_product_plan_id` bigint                                                       NOT NULL COMMENT '关联生产计划id',
    `is_direct_relation`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'FALSE' COMMENT '是否直接关联',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_processId_batchNo` (`product_plan_id`, `relation_product_plan_id`) USING BTREE,
    KEY `idx_relationProductPlanId` (`relation_product_plan_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='生产计划关联关系表';


--
-- Dumping data for table `bm_product_plan_relation`
--



--
-- Table structure for table `bm_product_plan_team`
--

DROP TABLE IF EXISTS `bm_product_plan_team`;


CREATE TABLE `bm_product_plan_team`
(
    `id`          bigint                                                        NOT NULL COMMENT '主键',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班组名称',
    `code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班组编码',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班组描述',
    `people`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '班组人员 json数据',
    `status`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'FALSE' COMMENT '状态 TRUE 启用 FALSE 禁用',
    `create_time` datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                                     DEFAULT NULL COMMENT '更新时间',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
    `is_deleted`  tinyint(1)                                                   DEFAULT '0' COMMENT '是否删除',
    UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='生产计划班组';


--
-- Dumping data for table `bm_product_plan_team`
--



--
-- Table structure for table `bm_resource_permission`
--

DROP TABLE IF EXISTS `bm_resource_permission`;


CREATE TABLE `bm_resource_permission`
(
    `resource_id` bigint NOT NULL,
    `dept_id`     bigint NOT NULL,
    UNIQUE KEY `uk_resource_dept` (`resource_id`, `dept_id`) USING BTREE COMMENT '资源-部门唯一索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='数据权限表';


--
-- Dumping data for table `bm_resource_permission`
--



--
-- Table structure for table `bm_storage`
--

DROP TABLE IF EXISTS `bm_storage`;


CREATE TABLE `bm_storage`
(
    `id`          bigint NOT NULL AUTO_INCREMENT COMMENT '物理主键',
    `parent_id`   bigint                                  DEFAULT NULL COMMENT '上级区域id',
    `name`        varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '区域名称',
    `level`       int                                     DEFAULT NULL COMMENT '层级',
    `create_time` datetime                                DEFAULT NULL,
    `update_time` datetime                                DEFAULT NULL,
    `create_by`   varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`   varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `is_deleted`  tinyint(1)                              DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='暂存间表';


--
-- Dumping data for table `bm_storage`
--



--
-- Table structure for table `bm_storage_material`
--

DROP TABLE IF EXISTS `bm_storage_material`;


CREATE TABLE `bm_storage_material`
(
    `id`                        bigint NOT NULL AUTO_INCREMENT COMMENT '物理主键',
    `material_id`               bigint                                  DEFAULT NULL COMMENT '物料id',
    `storage_material_batch_id` bigint                                  DEFAULT NULL COMMENT '物料批次id',
    `material_position_id`      bigint                                  DEFAULT NULL COMMENT '暂存货位id',
    `no`                        varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料件号',
    `unit_id`                   bigint                                  DEFAULT NULL COMMENT '标准单位id',
    `unit_extend_id`            bigint                                  DEFAULT NULL COMMENT '扩展单位id',
    `init_quantity`             varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '初始量',
    `consume_quantity`          varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消耗量',
    `available_quantity`        varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '可用量',
    `reserve_quantity`          varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预订量',
    `container`                 varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '容器',
    `create_time`               datetime                                DEFAULT NULL,
    `update_time`               datetime                                DEFAULT NULL,
    `create_by`                 varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`                 varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `is_deleted`                tinyint(1)                              DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='物料件信息';


--
-- Dumping data for table `bm_storage_material`
--



--
-- Table structure for table `bm_storage_material_batch`
--

DROP TABLE IF EXISTS `bm_storage_material_batch`;


CREATE TABLE `bm_storage_material_batch`
(
    `id`                bigint NOT NULL AUTO_INCREMENT COMMENT '物理主键',
    `material_id`       bigint                                  DEFAULT NULL COMMENT '物料id',
    `material_batch_no` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料批号',
    `original_batch_no` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '原始批号',
    `expired_date`      date                                    DEFAULT NULL COMMENT '有效日期',
    `unit_id`           bigint                                  DEFAULT NULL COMMENT '标准单位id',
    `unit_extend_id`    bigint                                  DEFAULT NULL COMMENT '扩展单位id',
    `available`         tinyint(1)                              DEFAULT NULL COMMENT '是否可用',
    `link_explain`      varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '来源/去向',
    `sender_id`         varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '递交人id',
    `receiver_id`       varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接收人id',
    `create_time`       datetime                                DEFAULT NULL,
    `update_time`       datetime                                DEFAULT NULL,
    `create_by`         varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`         varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `is_deleted`        tinyint(1)                              DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='物料批次';


--
-- Dumping data for table `bm_storage_material_batch`
--



--
-- Table structure for table `bm_storage_material_position_log`
--

DROP TABLE IF EXISTS `bm_storage_material_position_log`;


CREATE TABLE `bm_storage_material_position_log`
(
    `id`                     bigint NOT NULL AUTO_INCREMENT COMMENT '物理主键',
    `storage_id`             bigint                                  DEFAULT NULL COMMENT '暂存间id',
    `material_position_id`   bigint                                  DEFAULT NULL COMMENT '暂存货位id',
    `material_position_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '暂存货位',
    `material_position_code` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货位编码',
    `material_position_path` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所属位置',
    `material_id`            bigint                                  DEFAULT NULL COMMENT '物料id',
    `material_name`          varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料名称',
    `material_code`          varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料编码',
    `material_batch_no`      varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料批号',
    `operate_time`           datetime                                DEFAULT NULL COMMENT '操作时间',
    `operation_type`         varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '具体操作',
    `operate_detail`         varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '具体操作',
    `operator_name`          varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '操作人员',
    `material_no`            varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料件号',
    `quantity`               varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料量',
    `unit`                   varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单位',
    `product_name`           varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品名称',
    `product_code`           varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品编码',
    `product_batch_no`       varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品批号',
    `remark`                 varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注/来源去向说明',
    `create_time`            datetime                                DEFAULT NULL,
    `update_time`            datetime                                DEFAULT NULL,
    `create_by`              varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_by`              varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `is_deleted`             tinyint(1)                              DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='货位日志';


--
-- Dumping data for table `bm_storage_material_position_log`
--



--
-- Table structure for table `bm_storage_material_reserve`
--

DROP TABLE IF EXISTS `bm_storage_material_reserve`;


CREATE TABLE `bm_storage_material_reserve`
(
    `id`                  bigint NOT NULL COMMENT '物理主键',
    `storage_material_id` bigint                                  DEFAULT NULL COMMENT '暂存物料件id',
    `product_id`          bigint                                  DEFAULT NULL COMMENT '预定产品id',
    `process_id`          bigint                                  DEFAULT NULL COMMENT '预定工艺id',
    `batch_id`            bigint                                  DEFAULT NULL COMMENT '预定生产批次id',
    `reserve_quantity`    varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预订量',
    `reserve_remark`      varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预定备注',
    `reserve_time`        datetime                                DEFAULT NULL COMMENT '预定时间',
    `reserve_user_id`     varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预定人id（操作人id）',
    `create_time`         datetime                                DEFAULT NULL,
    `update_time`         datetime                                DEFAULT NULL,
    `create_by`           varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `update_by`           varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `is_deleted`          tinyint(1)                              DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='暂存物料预定记录';


--
-- Dumping data for table `bm_storage_material_reserve`
--



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
-- Table structure for table `inf_de_deployment`
--

DROP TABLE IF EXISTS `inf_de_deployment`;


CREATE TABLE `inf_de_deployment`
(
    `id`                    int NOT NULL AUTO_INCREMENT,
    `rev`                   int                                                           DEFAULT NULL,
    `version`               int                                                           DEFAULT NULL,
    `name`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `business_key`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `category`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `deployment_id`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `deployment_version_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `meta_info`             longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
    `element_info`          longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
    `deploy_by`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `deploy_time`           datetime                                                      DEFAULT NULL,
    `deploy_status`         tinyint(1)                                                    DEFAULT NULL,
    `create_by`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `create_time`           datetime                                                      DEFAULT NULL,
    `update_by`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_time`           datetime                                                      DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_deployment_version` (`deployment_version_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2393
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='流程部署表';


--
-- Dumping data for table `inf_de_deployment`
--



--
-- Table structure for table `inf_de_message`
--

DROP TABLE IF EXISTS `inf_de_message`;


CREATE TABLE `inf_de_message`
(
    `id`            int NOT NULL AUTO_INCREMENT,
    `deployment_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `message_key`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `element_type`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `element_key`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `inf_de_message`
--



--
-- Table structure for table `inf_hi_execution`
--

DROP TABLE IF EXISTS `inf_hi_execution`;


CREATE TABLE `inf_hi_execution`
(
    `id`                    int NOT NULL AUTO_INCREMENT,
    `element_key`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `element_type`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `execution_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `process_instance_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `deployment_version_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `state`                 tinyint                                                       DEFAULT NULL,
    `start_time`            datetime                                                      DEFAULT NULL,
    `end_time`              datetime                                                      DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1014
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `inf_hi_execution`
--



--
-- Table structure for table `inf_hi_process_instance`
--

DROP TABLE IF EXISTS `inf_hi_process_instance`;


CREATE TABLE `inf_hi_process_instance`
(
    `id`                        int NOT NULL AUTO_INCREMENT,
    `name`                      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `deployment_id`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `deployment_version_id`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `process_instance_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `super_process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `root_process_instance_id`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `start_by`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `start_time`                datetime                                                     DEFAULT NULL,
    `end_time`                  datetime                                                     DEFAULT NULL,
    `process_state`             tinyint                                                      DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 121
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `inf_hi_process_instance`
--



--
-- Table structure for table `inf_hi_task_instance`
--

DROP TABLE IF EXISTS `inf_hi_task_instance`;


CREATE TABLE `inf_hi_task_instance`
(
    `id`                    int NOT NULL AUTO_INCREMENT,
    `task_id`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `execution_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `process_instance_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `deployment_version_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_type`          varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_key`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_name`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `owner`                 varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `assignee`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `assignee_type`         varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `completed_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `state`                 tinyint                                                       DEFAULT NULL,
    `start_time`            datetime                                                      DEFAULT NULL,
    `end_time`              datetime                                                      DEFAULT NULL,
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 76
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `inf_hi_task_instance`
--



--
-- Table structure for table `inf_hi_variable`
--

DROP TABLE IF EXISTS `inf_hi_variable`;


CREATE TABLE `inf_hi_variable`
(
    `id`                  int NOT NULL AUTO_INCREMENT,
    `name`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `type`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `category`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `value`               varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `execution_id`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `create_time`         datetime                                                       DEFAULT NULL,
    `update_time`         datetime                                                       DEFAULT NULL,
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `inf_hi_variable`
--



--
-- Table structure for table `inf_hi_variable_execution`
--

DROP TABLE IF EXISTS `inf_hi_variable_execution`;


CREATE TABLE `inf_hi_variable_execution`
(
    `id`                        bigint NOT NULL AUTO_INCREMENT,
    `var_name`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `process_instance_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `super_process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `execution_id`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `root_process_instance_id`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `inf_hi_variable_execution`
--



--
-- Table structure for table `inf_job_instance`
--

DROP TABLE IF EXISTS `inf_job_instance`;


CREATE TABLE `inf_job_instance`
(
    `id`                  int NOT NULL AUTO_INCREMENT,
    `type`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `biz_id`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `next_triggered_time` bigint                                                       DEFAULT NULL,
    `triggered_count`     int                                                          DEFAULT NULL,
    `create_time`         datetime                                                     DEFAULT NULL,
    `update_time`         datetime                                                     DEFAULT NULL,
    `status`              tinyint                                                      DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `inf_job_instance`
--



--
-- Table structure for table `inf_re_variable_execution`
--

DROP TABLE IF EXISTS `inf_re_variable_execution`;


CREATE TABLE `inf_re_variable_execution`
(
    `id`                        bigint NOT NULL AUTO_INCREMENT,
    `var_name`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `process_instance_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `super_process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `execution_id`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `root_process_instance_id`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `inf_re_variable_execution`
--



--
-- Table structure for table `inf_ru_execution`
--

DROP TABLE IF EXISTS `inf_ru_execution`;


CREATE TABLE `inf_ru_execution`
(
    `id`                    int                                                          NOT NULL AUTO_INCREMENT,
    `element_key`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `element_name`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `element_type`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `super_execution_id`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `execution_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `process_instance_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `deployment_version_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `state`                 tinyint                                                      DEFAULT NULL,
    `start_time`            datetime                                                     DEFAULT NULL,
    `end_time`              datetime                                                     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_execution` (`execution_id`) USING BTREE,
    KEY `idx_process_instance_id` (`process_instance_id`) USING BTREE,
    KEY `idx_element` (`element_key`, `element_type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2410
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='每个节点的流程实例表';


--
-- Dumping data for table `inf_ru_execution`
--



--
-- Table structure for table `inf_ru_process_instance`
--

DROP TABLE IF EXISTS `inf_ru_process_instance`;


CREATE TABLE `inf_ru_process_instance`
(
    `id`                        int NOT NULL AUTO_INCREMENT,
    `category`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `business_key`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `name`                      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `deployment_id`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `deployment_version_id`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `process_instance_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `super_execution_id`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `super_process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `root_process_instance_id`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `start_by`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `start_time`                datetime                                                     DEFAULT NULL,
    `end_time`                  datetime                                                     DEFAULT NULL,
    `process_state`             tinyint                                                      DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_process_instance_id` (`process_instance_id`) USING BTREE,
    KEY `idx_business_key` (`business_key`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 303
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='整个流程实例表';


--
-- Dumping data for table `inf_ru_process_instance`
--



--
-- Table structure for table `inf_ru_task_instance`
--

DROP TABLE IF EXISTS `inf_ru_task_instance`;


CREATE TABLE `inf_ru_task_instance`
(
    `id`                    int NOT NULL AUTO_INCREMENT,
    `task_id`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `execution_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `process_instance_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `deployment_version_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_type`          varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_key`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `element_name`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `owner`                 varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `assignee`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `assignee_type`         varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `completed_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `state`                 tinyint                                                       DEFAULT NULL,
    `start_time`            datetime                                                      DEFAULT NULL,
    `end_time`              datetime                                                      DEFAULT NULL,
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_proc_inst_execution_task_state` (`process_instance_id`, `execution_id`, `task_id`, `state`) USING BTREE,
    KEY `idx_assignee` (`assignee`, `assignee_type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 459
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `inf_ru_task_instance`
--



--
-- Table structure for table `inf_ru_variable`
--

DROP TABLE IF EXISTS `inf_ru_variable`;


CREATE TABLE `inf_ru_variable`
(
    `id`                  int NOT NULL AUTO_INCREMENT,
    `name`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `type`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `category`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `value`               varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `execution_id`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `create_time`         datetime                                                       DEFAULT NULL,
    `update_time`         datetime                                                       DEFAULT NULL,
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;


--
-- Dumping data for table `inf_ru_variable`
--












-- Dump completed on 2024-04-09 10:43:15
