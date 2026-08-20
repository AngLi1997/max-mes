package com.bmos.platform.common.exception;

import com.bmos.common.response.ResponseItem;

import java.lang.reflect.Field;

/**
 * 异常码
 * 81 =》 platform
 * 01 =》 物料
 * 02 =》 公式
 * 03 =》 单位
 * 04 =》 用户
 * 05 =》 部门
 * 06 =》 角色
 * 07 =》 参数
 * 08 =》 编码规则
 * 09 =》 字典
 * 10 =》 日志相关
 * 11 =》 标签相关
 * 12 => 设备相关
 * 00 => 公共
 * 14 =》工厂建模
 */
public interface PlatformResponseCode {
    ResponseItem EXPORT_TEMPLATE_HEADER_ERROR = ResponseItem.from(81_00_0001, "与模板表头不匹配，请使用正确的模板导入", "bmosPlatform");
    ResponseItem EXPORT_TEMPLATE_NOT_MATCH = ResponseItem.from(81_00_0002, "请使用对应的模板导入", "bmosPlatform");
    ResponseItem EXPORT_TEMPLATE_ERROR = ResponseItem.from(81_00_0003, "模板错误，请检查导入模板", "bmosPlatform");
    ResponseItem EXPORT_TEMPLATE_TYPE_ERROR = ResponseItem.from(81_00_0004, "只支持上传{0}格式的文件", "bmosPlatform");
    ResponseItem EXPORT_TEMPLATE_FILE_SIZE_ERROR = ResponseItem.from(81_00_0005, "文件不允许超过{0}MB", "bmosPlatform");
    ResponseItem EXPORT_TEMPLATE_IMPORT_FILE_ERROR = ResponseItem.from(81_00_0006, "录入的Excel为空或格式非法", "bmosPlatform");
    ResponseItem EXPORT_TEMPLATE_EXPORT_ERROR = ResponseItem.from(81_00_0007, "导出出错，请联系管理员", "bmosPlatform");
    ResponseItem MINIO_DOWNLOAD_FILE_FAIL = ResponseItem.from(81_00_0008, "文件下载失败", "bmosPlatform");
    ResponseItem MINIO_UPLOAD_FILE_FAIL = ResponseItem.from(81_00_0009, "失败", "bmosPlatform");

    ResponseItem NOT_ACTIVE = ResponseItem.from(81_01_0001, "平台未授权", "bmosPlatform");
    ResponseItem ACTIVE_ERROR = ResponseItem.from(81_01_0002, "激活码错误", "bmosPlatform");

    ResponseItem MERGE_CODE_EXISTED = ResponseItem.from(81_01_001, "合并编码已存在", "bmosPlatform");
    ResponseItem MATERIAL_CATEGORY_NOT_EXIST = ResponseItem.from(81_01_002, "物料分类不存在", "bmosPlatform");
    ResponseItem MATERIAL_CODE_ERROR = ResponseItem.from(81_01_003, "物料编码必须以物料分类编码开始", "bmosPlatform");
    ResponseItem MATERIAL_CODE_EXIST = ResponseItem.from(81_01_004, "物料编码已存在", "bmosPlatform");
    ResponseItem MATERIAL_NOT_EXIST = ResponseItem.from(81_01_005, "物料不存在", "bmosPlatform");
    ResponseItem MATERIAL_ENABLED = ResponseItem.from(81_01_006, "物料已启用", "bmosPlatform");
    ResponseItem MATERIAL_ISSUED = ResponseItem.from(81_01_007, "物料已下发,不允许停用", "bmosPlatform");
    ResponseItem CATEGORY_EXISTS_MATERIAL = ResponseItem.from(81_01_008, "当前分类下存在物料信息,不允许删除", "bmosPlatform");

    ResponseItem SUB_MATERIAL_MUST_HAS_PRINCIPAL = ResponseItem.from(81_01_009, "成员物料必须选择所属物料", "bmosPlatform");
    ResponseItem MATERIAL_ASSOCIATED_WITH_MEMBER = ResponseItem.from(81_01_010, "物料信息已关联成员物料,不允许停用", "bmosPlatform");
    ResponseItem CATEGORY_EXISTS_CHILD = ResponseItem.from(81_01_011, "当前分类下存在子分类,不允许删除", "bmosPlatform");
    ResponseItem CATEGORY_ISSUED = ResponseItem.from(81_01_012, "分类已下发至{0}，不允许删除", "bmosPlatform");

    ResponseItem CATEGORY_ISSUED_CANT_UPDATE = ResponseItem.from(81_01_013, "分类已下发至{0}，不允许修改", "bmosPlatform");
    ResponseItem SUB_MATERIAL_UNIT_MISMATCH = ResponseItem.from(81_01_014, "成员物料和所属物料的标准单位需一致", "bmosPlatform");
    ResponseItem MATERIAL_NAME_ERROR = ResponseItem.from(81_01_015, "物料名称不能为空", "bmosPlatform");
    ResponseItem MATERIAL_CODE_NULL_ERROR = ResponseItem.from(81_01_016, "物料编码不能为空", "bmosPlatform");
    ResponseItem MATERIAL_SPECIFICATION_ERROR = ResponseItem.from(81_01_017, "物料规格不能为空", "bmosPlatform");
    ResponseItem MATERIAL_ERROR = ResponseItem.from(81_01_018, "成员物料需与当前物料分类一致", "bmosPlatform");
    ResponseItem WHAT_MATERIAL_ERROR = ResponseItem.from(81_01_019, "所属物料不能为空", "bmosPlatform");
    ResponseItem MATERIAL_STATUS_ERROR = ResponseItem.from(81_01_020, "所属物料未启动", "bmosPlatform");



    ResponseItem EXPRESSION_CATEGORY_CODE_EXIST = ResponseItem.from(81_02_001, "分类名称已存在", "bmosPlatform");
    ResponseItem EXPRESSION_CATEGORY_NOT_EXIST = ResponseItem.from(81_02_002, "分类不存在", "bmosPlatform");
    ResponseItem EXPRESSION_CATEGORY_ID_NOT_EXIST = ResponseItem.from(81_02_003, "编码父类不存在", "bmosPlatform");
    ResponseItem EXPRESSION_NAME_EXIST = ResponseItem.from(81_02_004, "公式名称已存在", "bmosPlatform");
    ResponseItem EXPRESSION_UPDATE_NOT_EXIST = ResponseItem.from(81_02_005, "更新数据不存在", "bmosPlatform");
    ResponseItem EXPRESSION_CONFIRM_STATUS_VALIDATED = ResponseItem.from(81_02_006, "确认状态时不可更新", "bmosPlatform");
    ResponseItem EXPRESSION_EXISTS_CATEGORY_NOT_DELETE = ResponseItem.from(81_02_007, "存在引用的公式数据", "bmosPlatform");
    ResponseItem CATEGORY_EXISTS_CHILD_NODE = ResponseItem.from(81_02_008, "分类下存在子分类数据", "bmosPlatform");
    ResponseItem EXPRESSION_NOT_VERIFIED = ResponseItem.from(81_02_009, "公式还未验证通过,无法确认", "bmosPlatform");


    ResponseItem MATERIAL_USE_UNIT_EXTEND = ResponseItem.from(81_03_001, "单位已在物料信息中配置,无法删除", "bmosPlatform");
    ResponseItem UNIT_USE_STATE = ResponseItem.from(81_03_002, "当前标准单位下存在已启用的扩展单位", "bmosPlatform");
    ResponseItem UNIT_NOT_STATE = ResponseItem.from(81_03_003, "标准单位未启用", "bmosPlatform");
    ResponseItem UNIT_NAME_UNIQUE = ResponseItem.from(81_03_004, "单位名称已存在", "bmosPlatform");
    ResponseItem UNIT_NAME_EXTEND_UNIQUE = ResponseItem.from(81_03_005, "扩展单位已存在", "bmosPlatform");
    ResponseItem USE_UNIT_EXTEND = ResponseItem.from(81_03_006, "标准单位下存在扩展单位", "bmosPlatform");
    ResponseItem UNIT_NOTFOUND = ResponseItem.from(81_03_007, "单位不存在", "bmosPlatform");


    ResponseItem USER_PASSWORD_ERROR = ResponseItem.from(81_04_001, "密码错误", "bmosPlatform");
    ResponseItem PASSWORD_EMPTY = ResponseItem.from(81_04_002, "密码不能为空", "bmosPlatform");
    ResponseItem PASSWORD_VALID_UPPERCASE = ResponseItem.from(81_04_003, "密码必须包含大写字母", "bmosPlatform");
    ResponseItem PASSWORD_VALID_LOWERCASE = ResponseItem.from(81_04_004, "密码必须包含小写字母", "bmosPlatform");
    ResponseItem PASSWORD_VALID_DIGIT = ResponseItem.from(81_04_005, "密码必须包含数字", "bmosPlatform");
    ResponseItem PASSWORD_VALID_MIN_LENGTH = ResponseItem.from(81_04_006, "密码长度过短", "bmosPlatform");
    ResponseItem PASSWORD_VALID_LOCKED = ResponseItem.from(81_04_007, "登录密码已锁定，请联系管理员，账号将在{0}自动解锁", "bmosPlatform");
    ResponseItem PASSWORD_VALID_EXPIRED = ResponseItem.from(81_04_008, "密码已过期", "bmosPlatform");
    ResponseItem USER_NOT_EXIST = ResponseItem.from(81_04_009, "用户不存在", "bmosPlatform");
    ResponseItem USER_STATE_FALSE = ResponseItem.from(81_04_010, "用户已停用,请联系管理员", "bmosPlatform");
    ResponseItem ADMIN_USER_CAN_NOT_BE_MODIFY = ResponseItem.from(81_04_011, "管理员账号不能被编辑或删除", "bmosPlatform");
    ResponseItem USER_OLD_PASSWORD_ERROR = ResponseItem.from(81_04_012, "当前密码不正确", "bmosPlatform");
    ResponseItem LOGIN_ERROR = ResponseItem.from(81_04_013, "账号或密码错误", "bmosPlatform");
    ResponseItem USER_EXISTS = ResponseItem.from(81_04_014, "用户已存在", "bmosPlatform");
    ResponseItem PASSWORD_VALID_SPECIAL = ResponseItem.from(81_04_015, "密码必须包含特殊字符{0}", "bmosPlatform");
    ResponseItem PASSWORD_VALID_HISTORY = ResponseItem.from(81_04_016, "密码不能与历史密码相同", "bmosPlatform");
    ResponseItem USER_SIGN_URL_ERROR = ResponseItem.from(81_04_017, "用户手写签名地址错误", "bmosPlatform");
    ResponseItem NO_SIGNATURE_PASSWORD = ResponseItem.from(81_04_018, "用户{0}未配置签名密码", "bmosPlatform");
    ResponseItem LOG_PASSWORD_ERROR = ResponseItem.from(81_04_019, "用户{0}密码错误", "bmosPlatform");
    ResponseItem USER_NAME_NULL_ERROR = ResponseItem.from(81_04_021, "用户名称不能为空", "bmosPlatform");
    ResponseItem LOGIN_NAME_NULL_ERROR = ResponseItem.from(81_04_022, "用户账号不能为空", "bmosPlatform");
    ResponseItem GENDER_NULL_ERROR = ResponseItem.from(81_04_023, "性别不能为空", "bmosPlatform");
    ResponseItem LOGIN_NAME_ERROR = ResponseItem.from(81_04_024, "用户账号只能包含英文字母、数字，长度限制2~18", "bmosPlatform");
    ResponseItem USER_NOT_LOCK = ResponseItem.from(81_04_020, "用户未锁定,无须执行解锁操作", "bmosPlatform");
    ResponseItem PASSWORD_VALID_LOCKED_LAST_ERROR_PWD = ResponseItem.from(81_04_025, "账户密码错误，超出重试次数，登录密码已锁定，请联系管理员，账号将在{0}自动解锁", "bmosPlatform");
    // 此为PASSWORD_VALID_LOCKED和PASSWORD_VALID_LOCKED_LAST_ERROR_PWD的参数
    ResponseItem USER_LOCK = ResponseItem.from(81_04_026, "永久锁定", "bmosPlatform");
    ResponseItem USER_PASSWORD_LOCK = ResponseItem.from(81_04_027, "密码已锁定，无法修改密码，请联系管理员", "bmosPlatform");

    ResponseItem DEPT_EXIST_CHILDREN = ResponseItem.from(81_05_001, "存在子部门无法删除", "bmosPlatform");
    ResponseItem DEPT_EXIST_USER = ResponseItem.from(81_05_002, "存在用户无法删除", "bmosPlatform");
    ResponseItem DEPT_NOT_EXIST = ResponseItem.from(81_05_003, "部门不存在", "bmosPlatform");


    ResponseItem ROLE_EXIST_USER = ResponseItem.from(81_06_001, "存在用户无法删除", "bmosPlatform");
    ResponseItem ADMIN_ROLE_CAN_NOT_BE_MODIFY = ResponseItem.from(81_06_002, "系统管理员角色不能被编辑或删除", "bmosPlatform");
    ResponseItem ROLE_EXIST_SUB_INFO = ResponseItem.from(81_06_003, "该分类下还有所属信息或子分类", "bmosPlatform");
    ResponseItem ROLE_TYPE_NOT_ALL = ResponseItem.from(81_06_004, "分类选择有误", "bmosPlatform");

    ResponseItem NUMBER_TYPE_NOT_MATCH_VALUE = ResponseItem.from(81_07_001, "值类型与值不匹配", "bmosPlatform");
    ResponseItem DETAIL_IS_NULL = ResponseItem.from(81_07_002, "更新数据不存在", "bmosPlatform");

    ResponseItem CODE_RULE_CODE_DUPLICATE = ResponseItem.from(81_08_001, "编号规则编码已存在", "bmosPlatform");
    ResponseItem TYPE_NOT_ALLOW = ResponseItem.from(81_08_002, "类型不在允许范围之内", "bmosPlatform");
    ResponseItem CONSTANT_NOT_EMPTY = ResponseItem.from(81_08_003, "常量不能为空", "bmosPlatform");
    ResponseItem PARAMETER_NOT_EMPTY = ResponseItem.from(81_08_004, "参数不能为空", "bmosPlatform");
    ResponseItem DATE_NOT_EMPTY = ResponseItem.from(81_08_005, "日期类型与格式不能为空", "bmosPlatform");
    ResponseItem SEQUENCE_NOT_EMPTY = ResponseItem.from(81_08_006, "流水号不能为空", "bmosPlatform");
    ResponseItem VERSION_EXISTS = ResponseItem.from(81_08_007, "编号规则版本已存在", "bmosPlatform");
    ResponseItem PLEASE_CHOOSE_EDIT_VERSION = ResponseItem.from(81_08_008, "请选择编辑版本", "bmosPlatform");
    ResponseItem EXISTS_ENABLED_VERSION = ResponseItem.from(81_08_009, "编号规则已有启用的版本", "bmosPlatform");
    ResponseItem CODE_RULE_NOT_EXISTS = ResponseItem.from(81_08_010, "编码规则不存在", "bmosPlatform");
    ResponseItem PARAMETER_NOT_FULL = ResponseItem.from(81_08_011, "参数未全部传递", "bmosPlatform");
    ResponseItem RULE_TYPE_NOT_EXISTS = ResponseItem.from(81_08_012, "规则类型不存在", "bmosPlatform");
    ResponseItem SEQUENCE_RULE_NOT_EXISTS = ResponseItem.from(81_08_013, "序列号规则不存在", "bmosPlatform");
    ResponseItem REST_RULE_DATA_ONLY_ONE = ResponseItem.from(81_08_014, "重置规则中日期类型仅有一个", "bmosPlatform");
    ResponseItem REST_RULE_SEQUENCR_NOT_EXISTS = ResponseItem.from(81_08_016, "重置规则中不能配置流水号", "bmosPlatform");
    ResponseItem COLD_RULE_NOT_CONFIRM = ResponseItem.from(81_08_015, "编号规则未确认,无法启用", "bmosPlatform");

    ResponseItem DICT_TO_USE = ResponseItem.from(81_09_001, "该字典为内置字典,无法删除", "bmosPlatform");

    ResponseItem DICT_TO_EXIST = ResponseItem.from(81_09_002, "字典数据已存在", "bmosPlatform");
    ResponseItem DICT_CODE_TO_EXIST = ResponseItem.from(81_09_003, "字典编码已存在", "bmosPlatform");
    ResponseItem LOGIN_SUCCESS = ResponseItem.from(81_10_001, "登录成功", "bmosPlatform");
    ResponseItem LOGOUT_SUCCESS = ResponseItem.from(81_10_002, "登出成功", "bmosPlatform");
    ResponseItem SECOND_LOGIN_AUTOMATIC_LOGOUT = ResponseItem.from(81_10_003, "账号二次登录,自动登出", "bmosPlatform");
    ResponseItem LOGOUT_PARAM_ERROR = ResponseItem.from(81_10_004, "登出参数异常", "bmosPlatform");
    ResponseItem USER_TO_ACTIVE = ResponseItem.from(81_10_005, "账号待激活", "bmosPlatform");

    ResponseItem TAG_INSTANCE_NOT_EXIST = ResponseItem.from(81_11_001, "标签不存在", "bmosPlatform");
    ResponseItem TAG_INSTANCE_ENABLED = ResponseItem.from(81_11_002, "标签已启用", "bmosPlatform");
    ResponseItem TAG_INSTANCE_EXIST_ENABLED_INSTANCE_IN_SCENE = ResponseItem.from(81_11_003, "该业务场景已存在启用的标签", "bmosPlatform");
    ResponseItem TAG_INSTANCE_DISABLED = ResponseItem.from(81_11_004, "标签已禁用", "bmosPlatform");
    ResponseItem TAG_TYPE_NOT_EXIST = ResponseItem.from(81_11_005, "标签类型不存在", "bmosPlatform");
    ResponseItem TAG_SCENE_NOT_EXIST = ResponseItem.from(81_11_006, "标签场景不存在", "bmosPlatform");
    ResponseItem TAG_DEFINE_NOT_EXIST = ResponseItem.from(81_11_007, "标签定义不存在", "bmosPlatform");
    ResponseItem TAG_NAME_EXIST = ResponseItem.from(81_11_008, "标签名称已存在", "bmosPlatform");
    ResponseItem NO_ENABLE_INSTANCE_WITH_SCENE_ID = ResponseItem.from(81_11_009, "该业务场景下未启用标签", "bmosPlatform");
    ResponseItem PRINTER_IP_ILLEGAL = ResponseItem.from(81_11_010, "打印机ip不合法", "bmosPlatform");
    ResponseItem PRINTER_SEND_ZPL_ERROR = ResponseItem.from(81_11_011, "发送zpl指令失败", "bmosPlatform");
    ResponseItem TAG_DATASOURCE_FILEDS_MIN_ONE = ResponseItem.from(81_11_012, "标签需至少包含一个标签字段", "bmosPlatform");
    ResponseItem DICT_NOT_EXIST = ResponseItem.from(81_11_013, "字典不不存在", "bmosPlatform");
    ResponseItem PRINTER_DPI_ILLEGAL = ResponseItem.from(81_11_014, "打印机dpi不合法", "bmosPlatform");
    //todo---------------------------设备相关编码开始------------------------------------------//
    ResponseItem EQUIPMENT_HUB_CONNECTION_ERROR = ResponseItem.from(81_12_001, "hub连接错误", "bmosPlatform");
    ResponseItem EQUIPMENT_HUB_RESPONSE_ERROR = ResponseItem.from(81_12_002, "hub响应错误", "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_ADDRESS_ERROR = ResponseItem.from(81_12_032, "设备数采地址为空", "bmosPlatform");
    ResponseItem EQUIPMENT_HUB_RESPONSE_STATUS_ERROR = ResponseItem.from(81_12_003, "hub响应错误{0}", "bmosPlatform");
    ResponseItem EQUIPMENT_DATA_POINT_VALUE_EMPTY = ResponseItem.from(81_12_004, "hub获取点位{0}值失败，请检查配置", "bmosPlatform");
    ResponseItem EQUIPMENT_DATA_POINT_WRITE_ERROR = ResponseItem.from(81_12_005, "点位{0}写入值失败，请检查配置", "bmosPlatform");
    ResponseItem PROPERTY_CODE_REPEAT = ResponseItem.from(81_12_022, "信息字段或数据字段【{0}】重复", "bmosPlatform");
    ResponseItem EQUIPMENT_ALREADY_OCCUPY = ResponseItem.from(81_12_023, "设备已占用", "bmosPlatform");
    ResponseItem EQUIPMENT_NOT_EXIST = ResponseItem.from(81_12_024, "设备不存在", "bmosPlatform");
    ResponseItem EQUIPMENT_STATUS_NOT_APLLY = ResponseItem.from(81_12_025, "设备状态不是占用,无法释放", "bmosPlatform");
    ResponseItem EQUIPMENT_TAG_NOT_EXIST = ResponseItem.from(81_12_026, "设备标签不存在", "bmosPlatform");
    ResponseItem EQUIPMENT_STATUS_NOT_FAULT = ResponseItem.from(81_12_027, "设备状态非故障,无需进行恢复", "bmosPlatform");
    ResponseItem EQUIPMENT_STATUS_ALREADY_FAULT = ResponseItem.from(81_12_028, "设备状态已是故障,无需进行重复操作", "bmosPlatform");
    ResponseItem EQUIPMENT_NOT_ENABLE = ResponseItem.from(81_12_044, "该设备未启用，请确认", "bmosPlatform");
    ResponseItem NOT_HAVE_EQUIPMENT_PERMISSION = ResponseItem.from(81_12_045, "无该设备数据权限，请确认", "bmosPlatform");
    ResponseItem THERE_IS_LOG_THAT_HAS_NOT_BEEN_FILLED_OUT = ResponseItem.from(81_12_046, "有未完成填报的设备日志", "bmosPlatform");
    //todo---------------------------设备相关编码结束------------------------------------------//

    //todo---------------------------采集点相关编码开始----------------------------------------//
    ResponseItem EQUIPMENT_ACQUISITION_POINT_CODE_REPEAT = ResponseItem.from(81_12_006, "采集点编码【{0}】已经存在",
            "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_NAME_REPEAT = ResponseItem.from(81_12_007, "采集点名称【{0}】已经存在",
            "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_NOT_EXITS = ResponseItem.from(81_12_008, "采集点不存在", "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_ENABLE_STATUS_ERROR = ResponseItem.from(81_12_009, "采集点【{0}】状态错误，不允许启用",
            "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_DISABLE_STATUS_ERROR = ResponseItem.from(81_12_010, "采集点【{0}】状态错误，不允许停用",
            "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_ADD_ACQUISITION_STATUS_ERROR = ResponseItem.from(81_12_011, "采集点【{0" +
            "}】不处于启用状态，添加失败", "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_CODE_SIZE_ERROR = ResponseItem.from(81_12_012, "采集点编码长度不能超过128", "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_NAME_SIZE = ResponseItem.from(81_12_013, "采集点名称长度不能超过255", "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_NAME_EMPTY = ResponseItem.from(81_12_014, "采集点名称不能为空", "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_CODE_EMPTY = ResponseItem.from(81_12_015, "采集点编码不能为空", "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_DATAPOINT_NAME_EMPTY = ResponseItem.from(81_12_016, "数据点位名称不能为空", "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_DATAPOINT_NAME_SIZE = ResponseItem.from(81_12_017, "数据点位长度不能超过255", "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_DESCRIPTION_SIZE = ResponseItem.from(81_12_018, "描述长度不能超过500", "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_DATATYPE_EMPTY = ResponseItem.from(81_12_019, "采集点数据类型不能为空",
            "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_TYPE_EMPTY = ResponseItem.from(81_12_020, "采集点类型不能为空", "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_ID_NULL = ResponseItem.from(81_12_021, "主键id不能为空", "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_NOT_EMPTY = ResponseItem.from(81_12_029, "点位数据不能为空", "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_EDIT_STATUS_ERROR = ResponseItem.from(81_12_030, "采集点状态错误，不允许编辑",
            "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_ADD_ACQUISITION_REPEAT = ResponseItem.from(81_12_031, "设备绑定的采集点【{0" +
                    "}】中存在同一个数采点位名称，请重新绑定",
            "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_HAS_BIND = ResponseItem.from(81_12_033, "绑定关系已经存在，无法更换关联设备数据,（采集点编码：设备编码）:;【{0}】", "bmosPlatform");
    //---------------------------采集点相关编码结束----------------------------------------//

    //---------------------------工厂建模&设备基础信息开始----------------------------------------//
    ResponseItem FACTORY_DELETE_ERROR = ResponseItem.from(81_14_001, "该节点存在启用数据，不可删除", "bmosPlatform");
    ResponseItem FACTORY_SAVE_ERROR = ResponseItem.from(81_14_002, "当前编码已存在，请编辑后重试", "bmosPlatform");
    ResponseItem STATION_DELETE_ERROR = ResponseItem.from(81_14_003, "当前工位已启用，不允许删除", "bmosPlatform");
    ResponseItem EQUIPMENT_CODE_ERROR = ResponseItem.from(81_14_004, "设备编码重复", "bmosPlatform");
    ResponseItem EQUIPMENT_NAME_ERROR = ResponseItem.from(81_14_005, "设备名称重复", "bmosPlatform");
    ResponseItem EQUIPMENT_DELETE_ERROR = ResponseItem.from(81_14_006, "启用状态下设备实例不可删除", "bmosPlatform");
    ResponseItem EQUIPMENT_ENABLE_ERROR = ResponseItem.from(81_14_007, "已绑定工位设备实例，不可进行停用", "bmosPlatform");
    ResponseItem FACTORY_MODULE_CODE_EXIST = ResponseItem.from(81_14_008, "模型编码重复", "bmosPlatform");
    ResponseItem FACTORY_MODULE_NOT_EXIST = ResponseItem.from(81_14_009, "模型不存在", "bmosPlatform");
    ResponseItem FACTORY_MODULE_HAS_CHILD = ResponseItem.from(81_14_010, "模型存在子节点", "bmosPlatform");
    ResponseItem FACTORY_MODULE_HAS_DATA = ResponseItem.from(81_14_011, "模型下已绑定数据", "bmosPlatform");
    ResponseItem FACTORY_STATION_NOT_EXIST = ResponseItem.from(81_14_012, "工位不存在", "bmosPlatform");
    ResponseItem FACTORY_MODULE_HAS_STATION = ResponseItem.from(81_14_013, "模型下包含工位", "bmosPlatform");
    ResponseItem FACTORY_MODULE_HAS_ROOM = ResponseItem.from(81_14_014, "模型下包含房间", "bmosPlatform");
    ResponseItem FACTORY_ROOM_CODE_REPEAT = ResponseItem.from(81_14_015, "房间编码重复", "bmosPlatform");
    ResponseItem FACTORY_ROOM_NOT_EXIST = ResponseItem.from(81_14_016, "房间不存在", "bmosPlatform");
    ResponseItem FACTORY_ROOM_ALREADY_OCCUPATION = ResponseItem.from(81_14_017, "房间已占用", "bmosPlatform");
    ResponseItem FACTORY_ROOM_ALREADY_BIND_LINE = ResponseItem.from(81_14_018, "房间已绑定产线", "bmosPlatform");
    ResponseItem FACTORY_LINE_CODE_EXISTS = ResponseItem.from(81_14_019, "产线编码重复", "bmosPlatform");
    ResponseItem FACTORY_LINE_NOT_EXISTS = ResponseItem.from(81_14_020, "产线不存在", "bmosPlatform");
    ResponseItem FACTORY_LINE_BIND_EXISTS = ResponseItem.from(81_14_021, "产线已绑定房间或工位", "bmosPlatform");
    ResponseItem FACTORY_LINE_ENABLE = ResponseItem.from(81_14_022, "产线已启用", "bmosPlatform");
    ResponseItem FACTORY_ROOM_NOT_PERMISSION = ResponseItem.from(81_14_023, "无该房间数据权限，请确认", "bmosPlatform");
    ResponseItem FACTORY_ROOM_NOT_ENABLE = ResponseItem.from(81_14_024, "该房间未启用，请确认", "bmosPlatform");
    ResponseItem FACTORY_ROOM_STATUS_CHANGE_PARAM_ERROR = ResponseItem.from(81_14_025, "房间状态修改参数错误", "bmosPlatform");
    ResponseItem FACTORY_ROOM_ALREADY_ENABLE = ResponseItem.from(81_14_026, "房间已停用", "bmosPlatform");
    ResponseItem FACTORY_ROOM_STATION_REPEAT_BIND = ResponseItem.from(81_14_027, "所选工位已绑定在其他房间下，请解绑后在进行绑定", "bmosPlatform");
    ResponseItem FACTORY_STATION_ALREADY_BIND_ROOM = ResponseItem.from(81_14_028, "工位已绑定房间", "bmosPlatform");
    ResponseItem FACTORY_STATION_ALREADY_BIND_LINE = ResponseItem.from(81_14_029, "工位已绑定产线", "bmosPlatform");
    ResponseItem FACTORY_ROOM_ALREADY_BIND_STATION = ResponseItem.from(81_14_030, "房间已绑定工位", "bmosPlatform");
    ResponseItem FACTORY_STATION_ALREADY_BIND_OTHER_LINE = ResponseItem.from(81_14_031, "所选工位已绑定其他产线", "bmosPlatform");
    ResponseItem FACTORY_STATION_ALREADY_BIND_OTHER_ROOM = ResponseItem.from(81_14_032, "所选工位已绑定其他房间", "bmosPlatform");
    ResponseItem FACTORY_MODULE_HAS_LINE = ResponseItem.from(81_14_033, "模型下包含产线", "bmosPlatform");
    ResponseItem FACTORY_LINE_PROCESS_EXISTS = ResponseItem.from(81_14_034, "产线已被业务配置绑定", "bmosPlatform");
    ResponseItem FACTORY_ROOM_ALREADY_USE = ResponseItem.from(81_14_035, "房间已被业务配置绑定", "bmosPlatform");
    ResponseItem FACTORY_STATION_ALREADY_BIND_EQUIPMENT = ResponseItem.from(81_14_036, "工位已绑定设备", "bmosPlatform");
    ResponseItem FACTORY_STATION_ALREADY_BIND_PERSON = ResponseItem.from(81_14_037, "工位已绑定人员", "bmosPlatform");
    ResponseItem EQUIPMENT_TAG_NAME_OR_CODE_EXISTS = ResponseItem.from(81_14_038, "设备类型名称或编码重复", "bmosPlatform");
    ResponseItem EQUIPMENT_TAG_NOT_EXISTS = ResponseItem.from(81_14_039, "设备类型不存在", "bmosPlatform");
    ResponseItem EQUIPMENT_TAG_EMBED_CANNOT_DELETE = ResponseItem.from(81_14_040, "内置属性不能删除", "bmosPlatform");
    ResponseItem EQUIPMENT_TAG_HAS_CHILDREN = ResponseItem.from(81_14_041, "该设备类型下存在子类，请先删除子类", "bmosPlatform");
    ResponseItem EQUIPMENT_DATA_TAG_EMPTY = ResponseItem.from(81_14_042, "设备没有配置任何数据属性", "bmosPlatform");
    ResponseItem EQUIPMENT_DATA_TAG_NOT_EXIST = ResponseItem.from(81_14_043, "设备没有配置【{0}】数据属性", "bmosPlatform");
    ResponseItem ACQUISITION_ENABLE_EMPTY = ResponseItem.from(81_14_044, "可以使用的采集点为空，请确认后重新绑定", "bmosPlatform");
    ResponseItem ACQUISITION_NOT_ENABLE = ResponseItem.from(81_14_045, "【{0}】绑定的采集点不存在或者已经被其他设备数据绑定，请确认后重新绑定",
            "bmosPlatform");
    ResponseItem EQUIPMENT_TAG_PROPERTY_CODE_REPEAT = ResponseItem.from(81_14_046, "设备类型信息属性编码【{0}】已经存在，请重新编辑",
            "bmosPlatform");
    ResponseItem EQUIPMENT_TAG_DATA_PROPERTY_CODE_REPEAT = ResponseItem.from(81_14_047, "设备数据属性编码【{0}】已经存在，请重新编辑",
            "bmosPlatform");
    ResponseItem EQUIPMENT_TAG_STATUS_PROPERTY_CODE_REPEAT = ResponseItem.from(81_14_048, "设备状态属性编码【{0}】已经存在，请重新编辑",
            "bmosPlatform");
    ResponseItem PROPERTY_CODE_NOT_EXIST = ResponseItem.from(81_14_050, "所选的类型中信息或数据【{0}】不存在", "bmosPlatform");
    ResponseItem PROPERTY_CODE_EMPTY = ResponseItem.from(81_14_051, "所选的类型中信息或数据属性为空", "bmosPlatform");
    ResponseItem EQUIPMENT_ACQUISITION_POINT_BIND_ERROR = ResponseItem.from(81_14_052, "采集点【{0}】已经被设备属性【{1" +
                    "}】绑定，不允许绑定其他设备数据",
            "bmosPlatform");
    ResponseItem FACTORY_STATION_ALREADY_BIND_LINE_TEMPLATE = ResponseItem.from(81_14_053, "工位【{0}】已绑定产线【{1}】，请先解绑", "bmosPlatform");
    ResponseItem FACTORY_STATION_ALREADY_BIND_ROOM_TEMPLATE = ResponseItem.from(81_14_054, "工位【{0}】已绑定房间【{1}】，请先解绑", "bmosPlatform");
    ResponseItem ACQUISITION_PLATFORM_NOT_MATCH = ResponseItem.from(81_14_055, "采集点【{0}】不属于该数采平台，请重新绑定", "bmosPlatform");
    ResponseItem TENEMENT_CODE_OR_NAME_EXISTS = ResponseItem.from(81_14_056, "楼栋名称或者编码重复", "bmosPlatform");
    ResponseItem TENEMENT_MUST_ONLY_ONE_LEVEL = ResponseItem.from(81_14_057, "楼栋层级只允许存在一级", "bmosPlatform");
    ResponseItem TENEMENT_DELETE_HAS_CHILDREN = ResponseItem.from(81_14_058, "楼栋存在子集不允许删除", "bmosPlatform");

    ResponseItem TENEMENT_FLOOR_CODE_OR_NAME_EXISTS = ResponseItem.from(81_14_059, "楼层名称或者编码重复", "bmosPlatform");
    ResponseItem TENEMENT_FLOOR_NOT_EXISTS = ResponseItem.from(81_14_060, "楼层不存在", "bmosPlatform");
    ResponseItem TENEMENT_NOT_EXISTS = ResponseItem.from(81_14_061, "楼栋不存在", "bmosPlatform");
    ResponseItem EQUIPMENT_DATA_PROPERTY_NOT_EXIST = ResponseItem.from(81_14_062, "设备【{0}】不存在设备数据【{1}】", "bmosPlatform");
    ResponseItem TENEMENT_FLOOR_NOT_ENABLE = ResponseItem.from(81_14_063, "楼层【{0}】未启用", "bmosPlatform");
    ResponseItem TENEMENT_FLOOR_BELONG_TO_TENEMENT = ResponseItem.from(81_14_064, "楼层【{0}】不属于【{1}】楼栋", "bmosPlatform");
    ResponseItem ROOM_3D_MODEL_EXIST = ResponseItem.from(81_14_065, "【{0}】-【{1}】已绑定该模型，请确认", "bmosPlatform");
    ResponseItem ROOM_NOT_EXIST = ResponseItem.from(81_14_066, "房间不存在", "bmosPlatform");
    ResponseItem FACTORY_ROOM_ENV_PROPERTY_REPEAT = ResponseItem.from(81_14_067, "设备数据【{0}】配置重复", "bmosPlatform");
    ResponseItem EQUIPMENT_USE_LOG_EXPORT_CHANGE_TYPE_MANUAL = ResponseItem.from(81_14_068, "手动记录", "bmosPlatform");
    ResponseItem EQUIPMENT_USE_LOG_EXPORT_CHANGE_TYPE_AUTO = ResponseItem.from(81_14_069, "自动记录", "bmosPlatform");
    ;
    //o---------------------------工厂建模&设备基础信息结束----------------------------------------//

    public static void main(String[] args) throws IllegalAccessException {
        Class<PlatformResponseCode> platformResponseCodeClass = PlatformResponseCode.class;
        Field[] declaredFields = platformResponseCodeClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            ResponseItem o = (ResponseItem) declaredField.get(ResponseItem.class);
            System.out.println("res_" + o.getCode() + "=" + o.getMessage());
        }
    }
}