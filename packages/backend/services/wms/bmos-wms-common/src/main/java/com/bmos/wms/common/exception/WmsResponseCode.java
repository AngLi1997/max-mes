package com.bmos.wms.common.exception;

import com.bmos.common.response.ResponseItem;

/**
 * WMS异常码
 * 83 =》 wms
 * 01 =》 货品分类模块
 */
public interface WmsResponseCode {

    ResponseItem NOT_ACTIVE = ResponseItem.from(83_00_0001, "WMS未授权,请联系管理员", "bmosPlatform");
    ResponseItem ACTIVE_ERROR = ResponseItem.from(83_00_0002, "激活码错误", "bmosPlatform");

    ResponseItem CARGO_CATEGORY_NOT_EXIST = ResponseItem.from(83_01_0001, "货品分类不存在", "bmosWms");
    ResponseItem CARGO_CATEGORY_EXIST = ResponseItem.from(83_01_0002, "货品分类已存在", "bmosWms");
    ResponseItem CARGO_CATEGORY_CODE_EXIST = ResponseItem.from(83_01_0003, "货品分类编码已存在", "bmosWms");
    ResponseItem CARGO_CATEGORY_NAME_EXIST = ResponseItem.from(83_01_0004, "货品分类名称已存在", "bmosWms");
    ResponseItem CARGO_CATEGORY_CODE_NOT_EXIST = ResponseItem.from(83_01_0005, "货品分类编码不存在", "bmosWms");
    ResponseItem CARGO_CATEGORY_NAME_NOT_EXIST = ResponseItem.from(83_01_0006, "货品分类名称不存在", "bmosWms");
    ResponseItem CARGO_CATEGORY_CODE_NOT_EMPTY = ResponseItem.from(83_01_0007, "货品分类编码不能为空", "bmosWms");
    ResponseItem CARGO_CATEGORY_NAME_NOT_EMPTY = ResponseItem.from(83_01_0008, "货品分类名称不能为空", "bmosWms");
    ResponseItem CARGO_CATEGORY_PARENT_NOT_EXIST = ResponseItem.from(83_01_0009, "货品分类父级不存在", "bmosWms");
    ResponseItem CARGO_CATEGORY_PARENT_NOT_SELF = ResponseItem.from(83_01_0010, "货品分类父级不能是自己", "bmosWms");


    ResponseItem STORAGE_NOT_EXIST = ResponseItem.from(83_09_001, "存储区域不存在", "bmosWms");
    ResponseItem STORAGE_NAME_EXISTED = ResponseItem.from(83_09_002, "存储区域名称已存在", "bmosWms");
    ResponseItem STORAGE_OVER_LEVEL = ResponseItem.from(83_09_003, "存储区域层级超过上限", "bmosWms");

    ResponseItem STORAGE_NOT_ALLOWED_DELETE_WITH_CHILDREN = ResponseItem.from(83_09_004, "分类下存在子级，无法删除", "bmosWms");
    ResponseItem STORAGE_NOT_ALLOWED_DELETE_WITH_STORAGE = ResponseItem.from(83_09_005, "该分类下已有存储货位存在,无法删除", "bmosWms");
    ResponseItem STORAGE_NOT_ALLOWED_DELETE_WITH_STORAGE_LOG = ResponseItem.from(83_09_006, "已产生货位日志,不能删除", "bmosWms");
    ResponseItem CARGO_POSITION_NOT_EXIST = ResponseItem.from(83_09_007, "货位不存在", "bmosWms");
    ResponseItem CARGO_POSITION_EXIST = ResponseItem.from(83_09_008, "货位已存在", "bmosWms");
    ResponseItem CARGO_POSITION_CODE_EXIST = ResponseItem.from(83_09_009, "货位编码已存在", "bmosWms");
    ResponseItem CARGO_POSITION_ENABLED = ResponseItem.from(83_09_010, "货位已启用", "bmosWms");
    ResponseItem CARGO_POSITION_DISABLED = ResponseItem.from(83_09_011, "货位已停用", "bmosWms");
    ResponseItem CARGO_POSITION_NOT_ALLOWED_DISABLE_WITH_MATERIAL = ResponseItem.from(83_09_012, "货位中存在货品件，不允许停用", "bmosWms");
    ResponseItem STORAGE_MATERIAL_BATCH_NOT_EXIST = ResponseItem.from(83_09_013, "货品批次不存在", "bmosWms");

    ResponseItem STORAGE_MATERIAL_NOT_EXIST = ResponseItem.from(83_09_014, "货品不存在", "bmosWms");
    ResponseItem STORAGE_MATERIAL_QUANTITY_ZERO = ResponseItem.from(83_09_015, "货品已失效", "bmosWms");

    ResponseItem STORAGE_MATERIAL_OUTBOUND_NOT_ENOUGH = ResponseItem.from(83_09_016, "货品可用量不足，无法出库", "bmosWms");
    ResponseItem STORAGE_MATERIAL_BATCH_EXIST = ResponseItem.from(83_09_017, "该货品批次已存在，不能创建相同的货品批次", "bmosWms");
    ResponseItem STORAGE_MATERIAL_BATCH_INBOUND_SIZE_EXCEED = ResponseItem.from(83_09_018, "超过单次入库上限", "bmosWms");
    ResponseItem STORAGE_MATERIAL_CHECK_QUANTITY_ERROR = ResponseItem.from(83_09_019, "货品检验数量错误", "bmosWms");
    ResponseItem STORAGE_MATERIAL_POSITION_NOT_MATCH = ResponseItem.from(83_09_020, "当前货品件不在该货位中", "bmosWms");
    ResponseItem STORAGE_MATERIAL_POSITION_EXIST = ResponseItem.from(83_09_021, "当前货品件已在货位中", "bmosWms");
    ResponseItem STORAGE_MATERIAL_RESERVED = ResponseItem.from(83_09_022, "货品已被预订", "bmosWms");
    ResponseItem STORAGE_MATERIAL_NOT_RESERVED_IN_PRODUCT_AND_BATCH = ResponseItem.from(83_09_023, "该产品和生产批次下无货品被预订", "bmosWms");
    ResponseItem STORAGE_MATERIAL_NOT_RESERVED = ResponseItem.from(83_09_024, "货品未被预订", "bmosWms");
    ResponseItem STORAGE_MATERIAL_RESERVE_EXIST = ResponseItem.from(83_09_025, "货品件已被预定", "bmosWms");

    ResponseItem PLATFORM_CHECK_CODE_ERROR = ResponseItem.from(83_09_026, "向平台校验编码错误", "bmosWms");
    ResponseItem PLATFORM_MATERIAL_CATEGORY_CODE_EXISTED = ResponseItem.from(83_09_027, "分类编码重复", "bmosWms");
    ResponseItem CARGO_CATEGORY_HAS_CHILDREN = ResponseItem.from(83_09_028, "该分类下存在子级，无法删除", "bmosWms");
    ResponseItem CARGO_CATEGORY_HAS_CARGO = ResponseItem.from(83_09_029, "该分类下存在货品，无法删除", "bmosWms");
    ResponseItem PLATFORM_MATERIAL_CATEGORY_SAVE_FAIL = ResponseItem.from(83_09_030, "向平台保存物料分类失败", "bmosWms");
    ResponseItem PLATFORM_MATERIAL_SAVE_FAIL = ResponseItem.from(83_09_031, "向平台保存物料失败", "bmosWms");
    ResponseItem CARGO_NOT_EXIST = ResponseItem.from(83_09_032, "货品不存在", "bmosWms");
    ResponseItem CARGO_ALREADY_ENABLED = ResponseItem.from(83_09_033, "该货品已启用", "bmosWms");
    ResponseItem CARGO_ALREADY_DISABLED = ResponseItem.from(83_09_034, "该货品已停用", "bmosWms");
    ResponseItem SUB_MATERIAL_NOT_EXIST = ResponseItem.from(83_09_035, "所属物料不存在", "bmosWms");
    ResponseItem CARGO_HAS_SUB_MATERIAL_CAN_NOT_DISABLE = ResponseItem.from(83_09_036, "物料信息已关联成员物料，不允许停用", "bmosWms");

    ResponseItem PLATFORM_GET_SYNC_ERROR = ResponseItem.from(83_09_037, "平台物料获取失败", "bmosWms");

    ResponseItem INVENTORY_TOO_MANY_BATCH = ResponseItem.from(83_09_038, "出库物料件存在不同批次", "bmosWms");
    ResponseItem MATERIAL_SYNC_ERROR_CHOSE_NOT_PARENT = ResponseItem.from(83_09_039, "物料分类同步异常,无父级信息", "bmosWms");
    ResponseItem CARGO_HAS_BATCH_CAN_NOT_DELETE = ResponseItem.from(83_09_0040, "货品信息已产生物料批次，无法删除", "bmosWms");
    ResponseItem CARGO_POSITION_HAS_LOG = ResponseItem.from(83_09_0041, "已产生货位日志，无法删除", "bmosWms");
    ResponseItem CARGO_POSITION_PERMISSION_DENIED = ResponseItem.from(83_09_0042, "无货位权限", "bmosWms");


    // 发料相关
    ResponseItem REQUISITION_PLAN_ID_EXIST = ResponseItem.from(83_10_001, "领料计划ID已存在", "bmosWms");
    ResponseItem INVENTORY_QUANTITY_NOT_ENOUGH_BY_BATCH = ResponseItem.from(83_10_002, "{0}物料批次库存量不满足计划领料量", "bmosWms");
    ResponseItem INVENTORY_QUANTITY_NOT_ENOUGH_BY_CARGO = ResponseItem.from(83_10_003, "{0}物料库存量不满足计划领料量", "bmosWms");
    ResponseItem SEND_ORDER_NOT_EXIST = ResponseItem.from(83_10_004, "发料工单不存在", "bmosWms");
    ResponseItem SEND_ORDER_NOT_ALLOWED_CANCEL = ResponseItem.from(83_10_005, "发料工单无法取消", "bmosWms");
    ResponseItem SEND_ORDER_NOT_ALLOWED_SEND = ResponseItem.from(83_10_006, "发料工单无法发料", "bmosWms");
    ResponseItem SEND_ORDER_NOT_ALLOWED_SEND_OTHER_CARGO = ResponseItem.from(83_10_007, "货品{0}库存量无法满足其他领料计划", "bmosWms");
    ResponseItem SEND_ORDER_NOT_ALLOWED_SEND_OTHER_BATCH = ResponseItem.from(83_10_008, "货品批次{0}库存量无法满足其他领料计划", "bmosWms");
    ResponseItem SEND_ORDER_ADD_NOT_FINISHED_CARGO = ResponseItem.from(83_10_009, "货品发料未完成，请继续添加", "bmosWms");
    ResponseItem SEND_ORDER_ADD_NOT_FINISHED_BATCH = ResponseItem.from(83_10_010, "批次发料未完成，请继续添加", "bmosWms");
}
