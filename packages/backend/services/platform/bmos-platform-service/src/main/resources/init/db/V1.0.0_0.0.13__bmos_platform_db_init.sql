DROP TABLE bp_equipment_station_user;
CREATE TABLE `bp_equipment_station_user` (
  `id` bigint NOT NULL DEFAULT '0' COMMENT '主键id，工位人员绑定关系的唯一标识',
  `station_id` bigint NOT NULL COMMENT '工位id，关联到bp_equipment_station表中的id',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id，标识与工位绑定的用户',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除，0未删除，非0已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='工位人员绑定关系表，记录工位与用户之间的绑定关系';