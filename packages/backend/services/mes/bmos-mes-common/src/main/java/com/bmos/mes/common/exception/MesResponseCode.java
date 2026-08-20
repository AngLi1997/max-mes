package com.bmos.mes.common.exception;

import com.bmos.common.response.ResponseItem;

/**
 * 异常码
 * 82 =》 mes
 * 01 =》 工艺配置模块
 * 02 =》 批记录配置模块
 * 05 => 生产计划
 * 06 => 审核流
 * 08 => 生产执行
 * 09 => 暂存间相关
 * 11 => 配方相关
 * 12 => 审批结论
 * 13 => 领料配料投料回收相关
 * 14 => 设备相关
 * 16 => 清场信息
 * 17 => 计划归档开始
 * 18 => 操作规程
 * 19 => 称量中心
 * 20 => 用户相关
 * 30 => 用户相关
 *
 * 21 => 配液
 * 22 => 拍照上传、异常管理
 * 23 => 批签发相关
 * 24 => 计划管理
 * 25 => 批次摘要相关
 * 26 => 皮重配置相关
 * 27 => 物料追溯模板相关
 * 28 => 消息中心
 * 29 => 批记录/批签发生成
 *
 * 31 => 请验单配置
 */
public interface MesResponseCode {
    ResponseItem NOT_ACTIVE = ResponseItem.from(82_00_0001, "MES未授权,请联系管理员", "bmosPlatform");
    ResponseItem ACTIVE_ERROR = ResponseItem.from(82_00_0002, "激活码错误", "bmosPlatform");
    ResponseItem PARAM_TIME_FORMAT_NOT_EXIST = ResponseItem.from(82_00_0003, "日期格式参数配置不存在", "bmosPlatform");

    ResponseItem PARAMETER_NOT_FULL = ResponseItem.from(81_08_011, "参数未全部传递", "bmosPlatform");

    ResponseItem EXPRESS_EXISTS_CYCLE = ResponseItem.from(82_00_001, "公式配置存在环状节点", "bmosMes");
    ResponseItem EXPRESS_EXISTS_DUPLICATION = ResponseItem.from(82_00_002, "公式配置存在重复元素", "bmosMes");

    ResponseItem PROCESS_ENABLED = ResponseItem.from(82_01_001, "工艺已有启用的版本", "bmosMes");
    ResponseItem PROCESS_NAME_EXIST = ResponseItem.from(82_01_002, "工艺名称已存在", "bmosMes");
    ResponseItem PROCESS_VERSION_EXIST = ResponseItem.from(82_01_003, "工艺版本号已存在", "bmosMes");
    ResponseItem PROCESS_NOT_CONFIRM = ResponseItem.from(82_01_004, "工艺未处于确认状态", "bmosMes");
    ResponseItem PROCESS_NOT_EDITING = ResponseItem.from(82_01_005, "工艺未处于编辑状态", "bmosMes");
    ResponseItem PROCESS_VERSION_NOT_EXIST = ResponseItem.from(82_01_006, "工艺版本不存在", "bmosMes");
    ResponseItem PROCESS_NOT_EXIST = ResponseItem.from(82_01_007, "工艺不存在", "bmosMes");
    ResponseItem PROCEDURE_NOT_EXIST = ResponseItem.from(82_01_008, "工序不存在", "bmosMes");
    ResponseItem PROCEDURE_EXIST = ResponseItem.from(82_01_009, "工序已存在", "bmosMes");
    ResponseItem PROCESS_VERSION_EMPTY = ResponseItem.from(82_01_010, "工艺版本号不能为空", "bmosMes");
    ResponseItem PROCESS_VERSION_ID_EMPTY = ResponseItem.from(82_01_011, "工艺版本ID不能为空", "bmosMes");
    ResponseItem PROCEDURE_STEP_EXIST = ResponseItem.from(82_01_012, "工序步骤已存在", "bmosMes");
    ResponseItem PROCESS_ACTION_STATE_ERROR = ResponseItem.from(82_01_013, "工艺非编辑状态,无法修改", "bmosMes");
    ResponseItem PROCESS_RELATION_RECORD_NON_CONFIRM = ResponseItem.from(82_01_014, "工艺绑定的记录版本未确认", "bmosMes");
    ResponseItem PROCESS_RELATION_CIRCULAR = ResponseItem.from(82_01_015, "工艺循环关联", "bmosMes");
    ResponseItem PROCEDURE_EXIST_EMPTY_GRAPH = ResponseItem.from(82_01_016, "工艺存在未配置工序步骤流程的工序节点", "bmosMes");
    ResponseItem PROCEDURE_ITEM_ERROR = ResponseItem.from(82_01_017, "【{0}】的【{1}】记录项配置与记录版本不符合，请检查配置", "bmosMes");
    ResponseItem PROCEDURE_PRINCIPAL_NOT_EXIST = ResponseItem.from(82_01_018, "工序负责人未找到", "bmosMes");
    ResponseItem PROCEDURE_STEP_NOT_EXIST = ResponseItem.from(82_01_019, "工序步骤不存在", "bmosMes");

    ResponseItem PROCEDURE_STEP_CONFIG_REQUIRED = ResponseItem.from(82_01_020, "工序【{0}】步骤/任务【{1}】记录配置组件【{2}】存在未填必填项", "bmosMes");
    ResponseItem PROCEDURE_FORMULA_MATERIAL_NOT_MATCH = ResponseItem.from(82_01_021, "工序【{0}】中配方物料【{1}】在当前版本不存在", "bmosMes");
    ResponseItem PROCEDURE_STEP_CONFIG_MATERIAL_NOT_MATCH = ResponseItem.from(82_01_022, "工序【{0}】工步/任务【{1}】组件配置中配方物料【{2}】在当前版本不存在", "bmosMes");
    ResponseItem EQUIPMENT_PICTURE_CONFIG_UNCOMPLETED = ResponseItem.from(82_01_023, "设备数采绘图组件配置不存在", "bmosMes");
    ResponseItem NO_ACTIVE_PROCESS = ResponseItem.from(82_01_024, "无启用的工艺版本", "bmosMes");
    ResponseItem HISTORY_PROCEDURE_DUPLICATE = ResponseItem.from(82_01_025, "历史工序重复", "bmosMes");
    ResponseItem PROCEDURE_DUPLICATE_NAME = ResponseItem.from(82_01_026, "工序名称重复", "bmosMes");
    ResponseItem HISTORY_PROCEDURE_STEP_DUPLICATE = ResponseItem.from(82_01_027, "历史工序步骤/任务重复", "bmosMes");
    ResponseItem PROCEDURE_STEP_DUPLICATE_NAME = ResponseItem.from(82_01_028, "步骤/任务名称重复", "bmosMes");

    ResponseItem RECORD_BOUND_CATEGORY = ResponseItem.from(82_02_001, "存在绑定关系", "bmosMes");
    ResponseItem FILE_ANALYSIS_ERROR = ResponseItem.from(82_02_002, "记录缺少一级大纲解析失败", "bmosMes");
    ResponseItem RECORD_VERSION_SAVE_ERROR = ResponseItem.from(82_02_003, "记录版本号已存在", "bmosMes");
    ResponseItem RECORD_STATE_ABNORMAL = ResponseItem.from(82_02_004, "记录存在未确认的版本", "bmosMes");
    ResponseItem RECORD_COPY_ERROR = ResponseItem.from(82_02_005, "批记录复制失败", "bmosMes");
    ResponseItem RECORD_ITEM_UPDATE_ERROR = ResponseItem.from(82_02_006, "记录项更新失败", "bmosMes");
    ResponseItem RECORD_ITEM_SAVE_ERROR = ResponseItem.from(82_02_007, "记录项添加失败", "bmosMes");
    ResponseItem RECORD_CATEGORY_UPDATE_ERROR = ResponseItem.from(82_02_008, "分类更新失败", "bmosMes");
    ResponseItem RECORD_PRODUCTION_ID_ERROR = ResponseItem.from(82_02_009, "批量生成id失败", "bmosMes");
    ResponseItem RECORD_SAVE_ERROR = ResponseItem.from(82_02_010, "记录名称已存在", "bmosMes");
    ResponseItem RECORD_ITEM_NOT_EXIST = ResponseItem.from(82_02_011, "记录项不存在", "bmosMes");
    ResponseItem RECORD_CATEGORY_NAME_ERROR = ResponseItem.from(82_02_012, "分类名称已存在", "bmosMes");
    ResponseItem RECORD_FILE_UPLOAD_ERROR = ResponseItem.from(82_02_013, "批记录源文件上传失败", "bmosMes");
    ResponseItem COMPONENT_NOT_EXIST =  ResponseItem.from(82_02_014, "组件不存在", "bmosMes");
    ResponseItem FILE_ANALYSIS_FILE_TYPE_ERROR =  ResponseItem.from(82_02_015, "批记录上传非docx格式文件，解析失败", "bmosMes");
    ResponseItem RECORD_UPLOAD_LOCKED = ResponseItem.from(82_02_016, "当前批记录上传任务繁忙，请稍候再试", "bmosMes");
    ResponseItem RECORD_UPLOAD_FAILED = ResponseItem.from(82_02_017, "批记录上传失败", "bmosMes");
    ResponseItem RECORD_ITEM_UPLOAD_FAILED = ResponseItem.from(82_02_018, "记录项上传失败", "bmosMes");
    ResponseItem RECORD_VERSION_NOT_EXIST =  ResponseItem.from(82_02_019, "批记录版本不存在", "bmosMes");
    ResponseItem RECORD_SORT_CHANGE_ERROR =  ResponseItem.from(82_02_020, "存在记录项无顺序配置,请刷新重试", "bmosMes");
    ResponseItem FUNCTION_PREVIEW_PARAM_NOT_COMPLETE =  ResponseItem.from(82_02_021, "公式配置未完成,无法计算", "bmosMes");

    ResponseItem MATERIAL_CATEGORY_EXISTED = ResponseItem.from(82_03_001, "生产物料分类已存在", "bmosMes");
    ResponseItem MATERIAL_CATEGORY_EXISTED_IN_PLATFORM = ResponseItem.from(82_03_002, "生产物料分类在平台已存在", "bmosMes");
    ResponseItem MATERIAL_CATEGORY_EXISTS_CHILD = ResponseItem.from(82_03_003, "该分类存在子级,无法删除", "bmosMes");
    ResponseItem MATERIAL_CATEGORY_LINKED_PRODUCT = ResponseItem.from(82_03_004, "该分类下已有物料存在,无法删除", "bmosMes");
    ResponseItem MATERIAL_ENABLED = ResponseItem.from(82_03_005, "物料已启用", "bmosMes");
    ResponseItem MATERIAL_EXISTED_IN_PLATFORM = ResponseItem.from(82_03_006, "物料编码已存在", "bmosMes");
    ResponseItem PLATFORM_MATERIAL_REGISTER_ERROR = ResponseItem.from(82_03_007, "向平台注册物料错误", "bmosMes");
    ResponseItem MATERIAL_NOT_EXISTED = ResponseItem.from(82_03_008, "物料信息不存在", "bmosMes");
    ResponseItem SUB_MATERIAL_MUST_HAS_PRINCIPAL = ResponseItem.from(82_03_009, "成员物料必须选择所属物料", "bmosMes");
    ResponseItem MATERIAL_CATEGORY_NOT_EXISTED = ResponseItem.from(82_03_010, "生产物料分类不存在,请检查", "bmosMes");
    ResponseItem PLATFORM_MATERIAL_CATEGORY_REGISTER_ERROR = ResponseItem.from(82_03_011, "向平台注册物料分类错误", "bmosMes");
    ResponseItem PLATFORM_CHECK_CODE_ERROR = ResponseItem.from(82_03_012, "向平台校验编码错误", "bmosMes");
    ResponseItem PLATFORM_GET_SYNC_ERROR = ResponseItem.from(82_03_013, "从平台获取同步信息错误", "bmosMes");
    ResponseItem PLATFORM_GET_UNIT_ERROR = ResponseItem.from(82_03_014, "从平台获取单位错误", "bmosMes");
    ResponseItem PLATFORM_MATERIAL_CATEGORY_CODE_EXISTED = ResponseItem.from(82_03_015, "分类编码重复", "bmosMes");
    ResponseItem PRODUCT_MATERIAL_EXISTED_MEMBER_MATERIAL = ResponseItem.from(82_03_016, "产品信息已关联成员产品,不允许停用", "bmosMes");
    ResponseItem ORIGINAL_MATERIAL_EXISTED_BATCH = ResponseItem.from(82_03_017, "该原辅包信息已产生物料批次,无法删除", "bmosMes");

    ResponseItem MATERIAL_EXISTED_MEMBER_MATERIAL = ResponseItem.from(82_03_018, "物料信息已关联成员物料,不允许停用", "bmosMes");

    ResponseItem MIDDLE_MATERIAL_EXISTED_BATCH = ResponseItem.from(82_03_019, "该中间品信息已产生物料批次,无法删除", "bmosMes");
    ResponseItem MATERIAL_SYNC_ERROR_CHOSE_NOT_PARENT = ResponseItem.from(82_03_020, "物料分类同步异常,无父级信息", "bmosMes");
    ResponseItem PRODUCT_HAS_BOUND_FORMULA = ResponseItem.from(82_03_021, "产品信息已绑定配方,不允许停用", "bmosMes");
    ResponseItem MATERIAL_BOUND_PRODUCT_FORMULA = ResponseItem.from(82_03_022, "{0}已绑定配方,不允许停用", "bmosMes");

    ResponseItem PLATFORM_GET_ROLE_ERROR = ResponseItem.from(82_04_001, "从平台获取角色错误", "bmosMes");

    ResponseItem PRODUCT_PLAN_NO_DUPLICATE = ResponseItem.from(82_05_001, "指令单编号已存在", "bmosMes");
    ResponseItem PRODUCT_PLAN_BATCH_NO_DUPLICATE = ResponseItem.from(82_05_002, "计划生产批号已存在", "bmosMes");
    ResponseItem EDIT_STATUS_CAN_EDIT = ResponseItem.from(82_05_003, "编辑状态方可编辑", "bmosMes");
    ResponseItem EDIT_AND_CONFIRM_STATUS_CAN_DISCARD = ResponseItem.from(82_05_004, "编辑状态和确认状态方可废弃", "bmosMes");
    ResponseItem START_PRODUCT_NOT_DISCARD = ResponseItem.from(82_05_005, "计划批次已开始生产，不能作废", "bmosMes");
    ResponseItem PARAMETER_QUERY_NOT_EXISTS = ResponseItem.from(82_05_006, "参数未查询到", "bmosMes");
    ResponseItem EDIT_STATUS_CAN_AUDIT = ResponseItem.from(82_05_007, "编辑状态方可提交审批", "bmosMes");
    ResponseItem WAIT_DECOMPOSE_CAN_OPERATOR = ResponseItem.from(82_05_008, "生产计划待分解状态方可操作指令单", "bmosMes");
    ResponseItem CONFIRM_AND_WAIT_DECOMPOSE = ResponseItem.from(82_05_009, "已确认状态生产计划且待分解方可生成指令单", "bmosMes");
    ResponseItem INSTRUCTION_NOT_DECOMPOSE_FINISH = ResponseItem.from(82_05_010, "指令单未分解完成", "bmosMes");
    ResponseItem INSTRUCTION_RESOLVED = ResponseItem.from(82_05_011, "指令单已分解", "bmosMes");

    ResponseItem STATUS_ERROR = ResponseItem.from(82_05_012, "状态流转错误", "bmosMes");
    ResponseItem INSTRUCTION_NOT_EXISTS = ResponseItem.from(82_05_013, "指令单不存在", "bmosMes");
    ResponseItem INSTRUCTION_RESOLVE_CAN_CONFIRM = ResponseItem.from(82_05_014, "指令单状态已分解方可确认", "bmosMes");

    ResponseItem CONFIRM_AND_WAIT_SEND = ResponseItem.from(82_05_015, "已确认状态生产计划且待下发方可下发指令单", "bmosMes");
    ResponseItem PRODUCT_PLAN_CAN_NOT_CONFIRM = ResponseItem.from(82_05_016, "生产计划不能重复确认", "bmosMes");
    ResponseItem SEND_INTRUCTION_CAN_CONFIRM = ResponseItem.from(82_05_017, "已下发指令单方可生产前确认", "bmosMes");
    ResponseItem PRODUCT_PLAN_NOT_EXISTS = ResponseItem.from(82_05_018, "生产计划不存在", "bmosMes");
    ResponseItem NO_PROCESS_NOT_BIND_CODE_RULE = ResponseItem.from(82_05_019, "计划编号未绑定编号规则", "bmosMes");
    ResponseItem TEAM_CODE_EXISTS = ResponseItem.from(82_05_020, "班组编码已存在", "bmosMes");
    ResponseItem TEAM_NOT_EXISTS = ResponseItem.from(82_05_021, "生产计划班组不存在", "bmosMes");
    ResponseItem TEAM_STATUS_FALSE_CAN_EDIT = ResponseItem.from(82_05_022, "停用状态班组才可编辑", "bmosMes");
    ResponseItem BATCH_NO_PROCESS_NOT_BIND_CODE_RULE = ResponseItem.from(82_05_023, "生产批号未绑定编号规则", "bmosMes");
    ResponseItem CODE_RULE_TYPE_ERROR = ResponseItem.from(82_05_024, "类型传输错误", "bmosMes");
    ResponseItem PRODUCT_PLAN_CODE_RULE_EXISTS = ResponseItem.from(82_05_025, "生产计划工序已绑定编码规则", "bmosMes");
    ResponseItem PLAN_NOT_BIND_LINE = ResponseItem.from(82_05_026, "生产计划未绑定产线", "bmosMes");
    ResponseItem INSTRUCTION_TEAM_CANT_BE_EMPTY = ResponseItem.from(82_05_027, "指令单班组不能为空", "bmosMes");
    ResponseItem PLAN_CODE_RULE_NOT_EXIST = ResponseItem.from(82_05_028, "指令单编号编码规则【{0}】不存在，请检查字典配置", "bmosMes");
    ResponseItem BATCH_NO_CODE_RULE_NOT_EXIST = ResponseItem.from(82_05_029, "生产批号编码规则【{0}】不存在，请检查字典配置", "bmosMes");

    ResponseItem FLOW_PAYLOAD_ERROR = ResponseItem.from(82_06_001, "流程参数不能为空", "bmosMes");
    ResponseItem FLOW_AUDIT_STATE_ERROR = ResponseItem.from(82_06_002, "流程版本数据状态异常", "bmosMes");
    ResponseItem FLOW_AUDIT_SELECT_ERROR = ResponseItem.from(82_06_003, "流程数据查询异常", "bmosMes");
    ResponseItem FLOW_AUDIT_CREATE_ERROR = ResponseItem.from(82_06_004, "流程保存异常", "bmosMes");
    int FLOW_AUDIT_ERROR = 82_06_005;
    ResponseItem FLOW_AUDIT_NOT_DELETE = ResponseItem.from(82_06_006, "流程不允许删除", "bmosMes");
    ResponseItem FLOW_AUDIT_PARAMETER_ERROR = ResponseItem.from(82_06_007, "参数不能为空", "bmosMes");
    ResponseItem FLOW_AUDIT_NOT_ERROR = ResponseItem.from(82_06_008, "无启用的审核流程", "bmosMes");
    ResponseItem FLOW_AUDIT_STATER_ERROR = ResponseItem.from(82_06_009, "流程发起失败", "bmosMes");
    ResponseItem FLOW_AUDIT_USER_ERROR = ResponseItem.from(82_06_010, "流程未找到人员", "bmosMes");
    ResponseItem FLOW_AUDIT_COMPLETE_ERROR = ResponseItem.from(82_06_011, "流程处理失败", "bmosMes");
    ResponseItem FLOW_AUDIT_MESSAGE_USER_ERROR = ResponseItem.from(82_06_012, "未配置抄送人，校验不通过", "bmosMes");
    ResponseItem FLOW_AUDIT_ROLE_USER_ERROR = ResponseItem.from(82_06_013, "未配置审核角色，不可配置角色会签策略", "bmosMes");
    ResponseItem FLOW_AUDIT_All_USER_ERROR = ResponseItem.from(82_06_014, "未配置审核人员，不可配置人员会签策略", "bmosMes");
    ResponseItem FLOW_AUDIT_NAME_ERROR = ResponseItem.from(82_06_015, "流程模型名称重复", "bmosMes");
    ResponseItem FLOW_AUDIT_EXPORT_ERROR = ResponseItem.from(82_06_016, "流程追溯导出失败", "bmosMes");
    ResponseItem FLOW_AUDIT_START_USER_ERROR = ResponseItem.from(82_06_017, "根据用户id查询用户失败", "bmosMes");
    ResponseItem FLOW_HAVE_PUBLISHED = ResponseItem.from(82_06_018, "该流程模型已完成发布，不允许再次进行保存，请退出当前操作页面", "bmosMes");
    ResponseItem FLOW_DEPLOY_HAVE_PUBLISHED = ResponseItem.from(82_06_019, "该流程模型已完成发布，不允许再次进行发布，请退出当前操作页面", "bmosMes");
    ResponseItem PROCESS_NOT_FINISH = ResponseItem.from(82_06_020, "工序信息未配置完成", "bmosMes");
    ResponseItem PROCESS_STEP_NOT_FINISH = ResponseItem.from(82_06_021, "步骤信息未配置完成", "bmosMes");
    ResponseItem AUDIT_NO_PERMISSION = ResponseItem.from(82_06_022, "无数据权限", "bmosMes");
    ResponseItem PROCESS_TASK_NOT_FINISH = ResponseItem.from(82_06_023, "任务信息未配置完成", "bmosMes");
    ResponseItem FLOW_AUDIT_PROCESS_BIND_OTHER_AUDIT = ResponseItem.from(82_06_024, "工艺【{0}】已绑定【{1}】流程中，请解绑后再进行操作", "bmosMes");
    ResponseItem AUDIT_VERSION_EXISTS = ResponseItem.from(82_06_025, "流程版本已存在", "bmosMes");


    ResponseItem DATASET_VERSION_NOT_EXISTED = ResponseItem.from(82_07_001, "数据集版本不存在", "bmosMes");
    ResponseItem DATAPOINT_GROUP_EXISTS_CHILD = ResponseItem.from(82_07_002, "数据点分组下存在子分组", "bmosMes");
    ResponseItem DATAPOINT_GROUP_EXISTS_POINT = ResponseItem.from(82_07_003, "数据点分组下存在数据点", "bmosMes");
    ResponseItem DATASET_TYPE_PROCESS_EXISTED = ResponseItem.from(82_07_004, "该工艺已经有同类型数据集创建", "bmosMes");
    ResponseItem DATAPOINT_NAME_EXISTED = ResponseItem.from(82_07_005, "数据点名称重复", "bmosMes");
    ResponseItem TEMPLATE_VERSION_NOT_EXISTED = ResponseItem.from(82_07_006, "模板版本不存在", "bmosMes");
    ResponseItem BATCH_RELEASE_NOT_EXISTED = ResponseItem.from(82_07_007, "批签发记录不存在", "bmosMes");
    ResponseItem GENERATE_BATCH_RELEASE_ERROR = ResponseItem.from(82_07_008, "生成批签发异常", "bmosMes");
    ResponseItem BATCH_RELEASE_AUDIT_NOT_EXISTED = ResponseItem.from(82_07_009, "批签发审核流程不存在", "bmosMes");
    ResponseItem TEMPLATE_VERSION_USING = ResponseItem.from(82_07_010, "该版本模板使用中,无法停用", "bmosMes");
    ResponseItem DATASET_NAME_EXISTED = ResponseItem.from(82_07_011, "该产品同数据集类型下存在相同的名称", "bmosMes");
    ResponseItem DATAPOINT_NOT_EXIST = ResponseItem.from(82_07_012, "数据点不存在", "bmosMes");
    ResponseItem DATAPOINT_GROUP_NOT_EXIST = ResponseItem.from(82_07_013, "数据点分组不存在", "bmosMes");
    ResponseItem TEMPLATE_NAME_EXISTED = ResponseItem.from(82_07_014, "模板名称已存在", "bmosMes");
    ResponseItem BATCH_RELEASE_AUDITING = ResponseItem.from(82_07_015, "批签发审核中,无法重复提交", "bmosMes");
    ResponseItem BATCH_RELEASE_NOT_MATCH = ResponseItem.from(82_07_016, "批签发文件数据不匹配, 请修改数据集后重新生成!(模板生产数据缺失)", "bmosMes");


    ResponseItem PROCEDURE_STEP_LOCKED = ResponseItem.from(82_08_001, "当前工序步骤已被锁定，请稍后再试", "bmosMes");
    ResponseItem PROCEDURE_EXPRESS_LOCKED = ResponseItem.from(82_08_002, "网络波动，请稍后重试", "bmosMes");
    ResponseItem EXECUTE_DATA_EXIST = ResponseItem.from(82_08_003, "记录数据已存在", "bmosMes");

    ResponseItem REQUIRED_DATA_COMPONENT_HAS_NOTHING = ResponseItem.from(82_08_004, "存在必填组件未填写", "bmosMes");
    ResponseItem CALCULATE_RESULT_TOO_LONG_FOR_COLUMN = ResponseItem.from(82_08_005, "计算结果过长,无法保存", "bmosMes");
    ResponseItem TIME_DIFF_FORMULA_PARAM_HAS_NO_TIMESTAMP = ResponseItem.from(82_08_006, "时间差公式参数无拓展时间戳信息", "bmosMes");

    ResponseItem TIME_BUSINESS_COMPONENT_HAS_NO_TIMESTAMP = ResponseItem.from(82_08_007, "组件【{0}】无拓展时间戳信息", "bmosMes");
    ResponseItem ATTACHMENT_NOT_EXIST = ResponseItem.from(82_08_008, "附件信息不存在", "bmosMes");
    ResponseItem ORCHESTRATOR_ENGINE_ERROR = ResponseItem.from(82_10_001, "流程控制器未配置完成", "bmosMes");
    ResponseItem STORAGE_NOT_EXIST = ResponseItem.from(82_09_001, "暂存间不存在", "bmosMes");
    ResponseItem STORAGE_NAME_EXISTED = ResponseItem.from(82_09_002, "暂存间名称已存在", "bmosMes");
    ResponseItem STORAGE_OVER_LEVEL = ResponseItem.from(82_09_003, "暂存间层级超过上限", "bmosMes");

    ResponseItem STORAGE_NOT_ALLOWED_DELETE_WITH_CHILDREN = ResponseItem.from(82_09_004, "分类下存在子级，无法删除", "bmosMes");
    ResponseItem STORAGE_NOT_ALLOWED_DELETE_WITH_STORAGE = ResponseItem.from(82_09_005, "该分类下已有存储货位存在,无法删除", "bmosMes");
    ResponseItem STORAGE_NOT_ALLOWED_DELETE_WITH_STORAGE_LOG = ResponseItem.from(82_09_006, "已产生货位日志,不能删除", "bmosMes");
    ResponseItem CARGO_POSITION_NOT_EXIST = ResponseItem.from(82_09_007, "暂存货位不存在", "bmosMes");
    ResponseItem CARGO_POSITION_EXIST = ResponseItem.from(82_09_008, "暂存货位已存在", "bmosMes");
    ResponseItem CARGO_POSITION_CODE_EXIST = ResponseItem.from(82_09_009, "暂存货位编码已存在", "bmosMes");
    ResponseItem CARGO_POSITION_ENABLED = ResponseItem.from(82_09_010, "暂存货位已启用", "bmosMes");
    ResponseItem CARGO_POSITION_DISABLED = ResponseItem.from(82_09_011, "暂存货位已停用", "bmosMes");
    ResponseItem CARGO_POSITION_NOT_ALLOWED_DISABLE_WITH_MATERIAL = ResponseItem.from(82_09_012, "暂存货位中存在物料件，不允许停用", "bmosMes");
        ResponseItem STORAGE_MATERIAL_BATCH_NOT_EXIST = ResponseItem.from(82_09_013, "暂存物料批次不存在", "bmosMes");

    ResponseItem STORAGE_MATERIAL_NOT_EXIST = ResponseItem.from(82_09_014, "物料件不存在，请扫描物料件标签或容器标签", "bmosMes");
    ResponseItem STORAGE_MATERIAL_QUANTITY_ZERO = ResponseItem.from(82_09_015, "物料可用量为0", "bmosMes");

    ResponseItem STORAGE_MATERIAL_OUTBOUND_NOT_ENOUGH = ResponseItem.from(82_09_016, "暂存物料可用量不足，无法出库", "bmosMes");
    ResponseItem STORAGE_MATERIAL_BATCH_EXIST = ResponseItem.from(82_09_017, "该物料批次已存在，不能创建相同的物料批次", "bmosMes");
    ResponseItem STORAGE_MATERIAL_BATCH_INBOUND_SIZE_EXCEED = ResponseItem.from(82_09_018, "超过单次入库上限", "bmosMes");
    ResponseItem STORAGE_MATERIAL_CHECK_QUANTITY_ERROR = ResponseItem.from(82_09_019, "暂存物料检验数量错误", "bmosMes");
    ResponseItem STORAGE_MATERIAL_POSITION_NOT_MATCH = ResponseItem.from(82_09_020, "当前物料件不在该暂存货位中", "bmosMes");
    ResponseItem STORAGE_MATERIAL_POSITION_EXIST = ResponseItem.from(82_09_021, "当前物料件已在暂存货位中", "bmosMes");
    ResponseItem STORAGE_MATERIAL_RESERVED = ResponseItem.from(82_09_022, "暂存物料已被预订", "bmosMes");
    ResponseItem STORAGE_MATERIAL_NOT_RESERVED_IN_PRODUCT_AND_BATCH = ResponseItem.from(82_09_023, "该产品和生产批次下无暂存物料被预订", "bmosMes");
    ResponseItem STORAGE_MATERIAL_NOT_RESERVED = ResponseItem.from(82_09_024, "暂存物料未被预订", "bmosMes");
    ResponseItem STORAGE_MATERIAL_RESERVE_EXIST = ResponseItem.from(82_09_025, "物料件已被预定", "bmosMes");

    ResponseItem CARGO_POSITION_PERMISSION_DENIED = ResponseItem.from(82_09_026, "无货位权限", "bmosMes");

    ResponseItem STORAGE_MATERIAL_CONTAINER_OCCUPY = ResponseItem.from(82_09_027, "容器已被占用", "bmosMes");

    ResponseItem STORAGE_MATERIAL_CONTAINER_NOT_EXIST = ResponseItem.from(82_09_028, "容器不存在", "bmosMes");
    ResponseItem STORAGE_MATERIAL_CONTAINER_NOT_AVAILABLE = ResponseItem.from(82_09_029, "容器不可用", "bmosMes");
    ResponseItem STORAGE_MATERIAL_TAG_NOT_RESERVED = ResponseItem.from(82_09_030, "请扫描预定的物料件标签", "bmosMes");
    ResponseItem EQUIPMENT_NOT_CONTAINER = ResponseItem.from(82_09_031, "设备非容器", "bmosMes");
    ResponseItem STORAGE_MATERIAL_RESERVE_QUANTITY_ZERO = ResponseItem.from(82_09_032, "物料预定量为0", "bmosMes");
    ResponseItem STORAGE_MATERIAL_EXPIRED = ResponseItem.from(82_09_033, "物料件已超过有效期", "bmosMes");
    ResponseItem EQUIPMENT_CONTAINER_NO_MATERIAL = ResponseItem.from(82_09_034, "容器内无物料", "bmosMes");
    ResponseItem STORAGE_MATERIAL_BATCH_CANT_USE = ResponseItem.from(82_09_035, "物料批次处于{0}状态,无法使用", "bmosMes");

    ResponseItem PLEASE_CHECK_INPUT_NO = ResponseItem.from(82_09_036, "物料件不存在,请检查输入的物料件号或容器编号", "bmosMes");
    ResponseItem RESERVED_BY_UNRELATED_BATCH = ResponseItem.from(82_09_037, "已被非当前生产批次及关联批次预定", "bmosMes");
    ResponseItem STORAGE_MATERIAL_NOT_MATCH = ResponseItem.from(82_09_038, "物料件不是同一批次", "bmosMes");
    ResponseItem STORAGE_MATERIAL_NOT_IN_SAME_POSITION = ResponseItem.from(82_09_039, "物料件不在同一暂存货位中", "bmosMes");


    ResponseItem PRODUCT_FORMULA_NAME_EXISTED = ResponseItem.from(82_11_001, "配方名称已存在", "bmosMes");
    ResponseItem PRODUCT_FORMULA_VERSION_NOT_EXISTED = ResponseItem.from(82_11_002, "配方版本不存在", "bmosMes");
    ResponseItem PRODUCT_FORMULA_EXISTED_ENABLE_VERSION = ResponseItem.from(82_11_003, "配方已有启用的版本", "bmosMes");
    ResponseItem PRODUCT_FORMULA_VERSION_EXISTED = ResponseItem.from(82_11_004, "配方版本已存在", "bmosMes");
    ResponseItem PRODUCT_FORMULA_VERSION_NOT_EDIT = ResponseItem.from(82_11_005, "配方版本处于非编辑状态", "bmosMes");
    ResponseItem PRODUCT_FORMULA_MATERIAL_NOT_EXIST = ResponseItem.from(82_11_006, "配方物料不存在", "bmosMes");
    ResponseItem PRODUCT_FORMULA_MATERIAL_NOT_EXISTS = ResponseItem.from(82_11_007, "配方物料不存在", "bmosMes");
    ResponseItem PRODUCT_FORMULA_MATERIAL_RESERVE_NOT_MATCH = ResponseItem.from(82_11_008, "配方物料不存在该物料，无法预订", "bmosMes");

    ResponseItem PROCEDURE_CONDITION_ERROR = ResponseItem.from(82_12_001, "工序审批结束参数为空", "bmosMes");
    ResponseItem PROCESS_DATE_ERROR = ResponseItem.from(82_12_002, "数据为空", "bmosMes");
    ResponseItem PROCESS_NAME_ERROR = ResponseItem.from(82_12_003, "工艺参数不能为空", "bmosMes");
    ResponseItem PROCESS_CONDITION_ERROR = ResponseItem.from(82_12_004, "条件数量超过最大数量", "bmosMes");
    ResponseItem PROCESS_CONDITION_SIZE_ERROR = ResponseItem.from(82_12_005, "条件不能为空", "bmosMes");
    ResponseItem PROCESS_EXPRESSION_ERROR = ResponseItem.from(82_12_006, "表达式id不能为空", "bmosMes");
    ResponseItem CHECKOUT_EXPRESSION_ERROR = ResponseItem.from(82_12_007, "逻辑表达式配置错误", "bmosMes");
    ResponseItem TASK_UPDATE_ENABLE_ERROR = ResponseItem.from(82_12_008, "任务节点启动错误", "bmosMes");
    ResponseItem TASK_ERROR = ResponseItem.from(82_12_009, "任务节点完成未满足条件", "bmosMes");
    ResponseItem STEP_COMPLETE_ERROR = ResponseItem.from(82_12_010, "节点完成未满足条件 ", "bmosMes");
    ResponseItem STEP_ACTIVE_ERROR = ResponseItem.from(82_12_011, "节点激活未满足条件 ", "bmosMes");
    ResponseItem PROCEDURE_RESTART_ERROR = ResponseItem.from(82_12_012, "工序完成，无法重做", "bmosMes");
    ResponseItem MATERIAL_ERROR = ResponseItem.from(82_12_013, "物料预定量未满足条件", "bmosMes");
    ResponseItem BURDENING_ERROR = ResponseItem.from(82_12_014, "配料称量签名未满足条件", "bmosMes");
    ResponseItem OUTPUT_ERROR = ResponseItem.from(82_12_015, "中间品产出签名未满足条件", "bmosMes");
    ResponseItem TASK_COMPLETE_ERROR = ResponseItem.from(82_12_016, "任务节点完成错误", "bmosMes");
    ResponseItem PROCEDURE_NOT_ACTIVE_ERROR = ResponseItem.from(82_12_017, "工序未激活，无法重做", "bmosMes");
    ResponseItem STEP_MODEL_NOT_TEAM = ResponseItem.from(82_12_018, "当前节点非换班节点", "bmosMes");
    ResponseItem PROCEDURE_COMPLETE_ERROR = ResponseItem.from(82_12_019, "当前工序已完成，无法执行换班操作", "bmosMes");
    ResponseItem PROCEDURE_NOT_CHANGE_ERROR = ResponseItem.from(82_12_020, "当前工序未开始，无法执行换班操作", "bmosMes");
    ResponseItem STEP_MODLE_COMPLETE_ERROR = ResponseItem.from(82_12_021, "当前工序步骤/任务已完成", "bmosMes");
    ResponseItem STEP_NOT_ACTIVE_ERROR = ResponseItem.from(82_12_022, "工序未开始，无待执行的步骤", "bmosMes");
    ResponseItem STEP_NOT_JURISDICTION_ERROR = ResponseItem.from(82_12_023, "无符合班组执行权限的步骤", "bmosMes");
    ResponseItem PLAN_END_ERROR = ResponseItem.from(82_12_024, "当前计划已完结", "bmosMes");
    ResponseItem PROCESS_PROCEDURE_CONDITION_ERROR = ResponseItem.from(82_12_025, "工序：【{0}】的【{1}】配置错误，请检查配置;", "bmosMes");
    ResponseItem PROCESS_STEP_CONDITION_ERROR = ResponseItem.from(82_12_026, "工序：【{0}】下工步/任务：【{1}】的【{2}】配置错误，请检查配置;", "bmosMes");
    ResponseItem PROCEDURE_NOT_ACTIVE = ResponseItem.from(82_12_027, "工序非运行状态，不允许强制完成", "bmosMes");
    ResponseItem PROCEDURE_NOT_PAUSE = ResponseItem.from(82_12_028, "暂停节点不存在,无需强制完成", "bmosMes");
    ResponseItem PROCEDURE_END_ERROR = ResponseItem.from(82_12_029, "工序流程已完成", "bmosMes");
    ResponseItem PROCESS_CONFIG_ERROR = ResponseItem.from(82_12_030, "配置【{0}】失效或已删除，请检查配置", "bmosMes");
    ResponseItem PROCEDURE_CONFIG_ERROR = ResponseItem.from(82_12_031, "【{0}】,配置【{1}】失效或已删除，请检查配置", "bmosMes");



    ResponseItem REQUISITION_PLAN_COMPLETED = ResponseItem.from(82_13_001, "领料计划已完成,无法重复完成", "bmosMes");
    ResponseItem INGREDIENT_PLAN_COMPLETED = ResponseItem.from(82_13_002, "配料计划已完成,无法重复完成", "bmosMes");
    ResponseItem STORAGE_MATERIAL_BATCH_NOT_MATCH = ResponseItem.from(82_13_004, "物料件不符合配料批次", "bmosMes");
    ResponseItem STORAGE_MATERIAL_NOT_AVAILABLE = ResponseItem.from(82_13_005, "物料件未生效", "bmosMes");

    ResponseItem STORAGE_MATERIAL_NOT_OUTBOUND = ResponseItem.from(82_13_006, "物料件未出库", "bmosMes");

    ResponseItem INGREDIENT_PLAN_NOT_EXIST = ResponseItem.from(82_13_007, "配料计划不存在", "bmosMes");
    ResponseItem INGREDIENT_PLAN_BATCH_NOT_EXIST = ResponseItem.from(82_13_008, "配料计划批次不存在", "bmosMes");
    ResponseItem INGREDIENT_PLAN_BATCH_WEIGHER_EXIST = ResponseItem.from(82_13_009, "配料计划批次称量人已确认", "bmosMes");

    ResponseItem INGREDIENT_PLAN_BATCH_ENOUGH = ResponseItem.from(82_13_010, "已称量物料量已满足配料量", "bmosMes");

    ResponseItem INGREDIENT_PLAN_RECORD_SIGNED = ResponseItem.from(82_13_011, "称量物料件已签名", "bmosMes");

    ResponseItem REQUISITION_PLAN_NOT_EXISTS = ResponseItem.from(82_13_012, "领料计划不存在", "bmosMes");
    ResponseItem RECEIVED_INVENTORY_BATCH_NOT_EXISTS = ResponseItem.from(82_13_013, "预定仓库批次不存在", "bmosMes");
    ResponseItem REQUISITION_BIND_OTHER_COMPONENT = ResponseItem.from(82_13_014, "该领料单已被其他领料接收组件绑定", "bmosMes");
    ResponseItem REQUISITION_NOT_RECEIVE_ALL_MATERIAL = ResponseItem.from(82_13_015, "领料单中物料未接收完成", "bmosMes");

    ResponseItem INGREDIENT_WEIGHT_PROCESS_NOT_EXIST = ResponseItem.from(82_13_016, "配料称量流程不存在", "bmosMes");
    ResponseItem INGREDIENT_EMPTY_INPUT_LIST = ResponseItem.from(82_13_017, "配料计划下无投料信息", "bmosMes");
    ResponseItem INGREDIENT_STORAGE_MATERIAL_INPUTED = ResponseItem.from(82_13_018, "存在已投料的物料件", "bmosMes");
    ResponseItem INGREDIENT_INPUT_FINISHED = ResponseItem.from(82_13_019, "配料投入已完成所有物料投入", "bmosMes");

    ResponseItem NO_CHARGE_RECYCLE_INIT_DATA = ResponseItem.from(82_13_020, "该组件投料回收初始化数据异常", "bmosMes");
    ResponseItem RECYCLE_QUANTITY_MORE_THAN_CHARGE = ResponseItem.from(82_13_021, "回收量大于批次投料量", "bmosMes");
    ResponseItem NOT_FINISHED_PRODUCT_PROCESS = ResponseItem.from(82_13_022, "非成品工艺缺少批次信息", "bmosMes");
    ResponseItem FINISHED_PRODUCT_OUTPUT_INFO_ERROR = ResponseItem.from(82_13_023, "成品产出信息异常", "bmosMes");
    ResponseItem CANT_CHARGE_THIS_MATERIAL = ResponseItem.from(82_13_024, "不可投入该物料", "bmosMes");
    ResponseItem MATERIAL_ALREADY_CHARGE = ResponseItem.from(82_13_025, "物料件已添加", "bmosMes");
    ResponseItem PLEASE_SCAN_PENDING_MATERIAL = ResponseItem.from(82_13_026, "请扫描待投入的物料件", "bmosMes");
    ResponseItem MATERIAL_RESERVED_BY_OTHER_BATCH = ResponseItem.from(82_13_027, "{0}物料件已预定其他生产批次，请重新预定", "bmosMes");
    ResponseItem CANT_CHARGE_IN_THIS_DEVICE = ResponseItem.from(82_13_028, "不可投入该设备", "bmosMes");
    ResponseItem INPUT_COMPONENT_INSTANCE_NOT_EXISTS = ResponseItem.from(82_13_029, "配料投入组件实例不存在,请重新进入", "bmosMes");
    ResponseItem INGREDIENT_WEIGH_OVER_TARGET = ResponseItem.from(82_13_030, "超出批次目标量范围", "bmosMes");
    ResponseItem INGREDIENT_WEIGH_INPUT_NOT_ENOUGH = ResponseItem.from(82_13_031, "请添加物料", "bmosMes");
    ResponseItem NO_REQUISITION_PLAN_INFO = ResponseItem.from(82_13_032, "无领料计划信息", "bmosMes");

    ResponseItem REQUISITION_PLAN_COMPLETED_RECEIVED = ResponseItem.from(82_13_033, "领料接收已完成", "bmosMes");
    ResponseItem INPUT_EQUIPMENT_NOT_FOUND_ERROR = ResponseItem.from(82_13_034, "投入错误，工位没有绑定设备", "bmosMes");
    ResponseItem INPUT_EQUIPMENT_SCAN_ERROR = ResponseItem.from(82_13_035, "投入错误，该设备不属于工序配置的工位", "bmosMes");
    ResponseItem INPUT_EQUIPMENT_SCAN_PRODUCT_LINE_NOT_HAVE_EQUIPMENT_ERROR = ResponseItem.from(82_13_036,
            "投入错误，产线下没有绑定工位与设备",
            "bmosMes");
    ResponseItem INPUT_EQUIPMENT_SCAN_PRODUCT_LINE_NOT_BIND_EQUIPMENT_ERROR = ResponseItem.from(82_13_037,
            "投入错误，产线下没有该设备",
            "bmosMes");


    ResponseItem SCAN_RESERVE_TAG_ERROR = ResponseItem.from(82_13_038,
            "请扫描预定的物料件标签",
            "bmosMes");

    ResponseItem PLEASE_SCAN_PRODUCTION_LINE_CONTAINER = ResponseItem.from(82_13_039, "请扫描当前工艺产线下的容器", "bmosMes");
    ResponseItem CONTAINER_ALREADY_HAS_MATERIAL = ResponseItem.from(82_13_040, "容器已有物料件", "bmosMes");
    ResponseItem NO_ANY_INGREDIENT_INFO = ResponseItem.from(82_13_041, "无配料信息", "bmosMes");
    ResponseItem CONTAINER_NOT_BIND_STORAGE_MATERIAL = ResponseItem.from(82_13_042, "容器未绑定物料件", "bmosMes");
    ResponseItem PLEASE_SCAN_MATERIAL_OR_CONTAINER = ResponseItem.from(82_13_043, "请扫描物料件/容器", "bmosMes");
    ResponseItem RESERVE_COMPONENT_INSTANCE_NOT_EXIST = ResponseItem.from(82_13_044, "预定组件实例不存在,请重新进入", "bmosMes");
    ResponseItem CHARGE_QUANTITY_MORE_THAN_RESERVED = ResponseItem.from(82_13_045, "投料量大于物料件实际可用量", "bmosMes");
    ResponseItem MATERIAL_RESERVED_PRODUCT_PLAN_NOT_MATCH = ResponseItem.from(82_13_046, "物料件未预定到当前生产批次", "bmosMes");
    ResponseItem REQUISITION_PLAN_COMPLETED_SEND = ResponseItem.from(82_13_048, "发料已完成,无法重复发料", "bmosMes");
    ResponseItem STORAGE_MATERIAL_OUTBOUNDED = ResponseItem.from(82_13_049, "物料件已出库", "bmosMes");
    ResponseItem STORAGE_MATERIAL_AVAILABLE = ResponseItem.from(82_13_050, "物料件可用", "bmosMes");
    ResponseItem STORAGE_MATERIAL_RESERVE_BY_OTHER_PLAN = ResponseItem.from(82_13_051, "请投入预定的物料件或未被预定的物料件", "bmosMes");
    ResponseItem NEED_QUANTITY_NULL_PLEASE_CHECK_CONFIG = ResponseItem.from(82_13_052, "计算所需理论量为空,请检查配方物料配置", "bmosMes");
    ResponseItem PLAN_QUANTITY_CANT_BE_ZERO = ResponseItem.from(82_13_053, "计划量无法为0", "bmosMes");
    ResponseItem NET_WEIGH_MUST_GREATER_THAN_ZERO = ResponseItem.from(82_13_054, "称量结果必须大于0", "bmosMes");

    //todo-------------------------------- 设备提示信息开始 -----------------------------------------------//
    ResponseItem EQUIPMENT_INFO_COMPONENT_TYPE_ERROR = ResponseItem.from(82_14_001, "该组件不是设备信息组件", "bmosMes");
    ResponseItem EQUIPMENT_INFO_COMPONENT_EQUIPMENT_NOT_EXITS_ERROR = ResponseItem.from(82_14_002, "设备信息不存在",
            "bmosMes");
    ResponseItem EQUIPMENT_INFO_COMPONENT_ATTR_CONFIG_NOT_EXITS_ERROR = ResponseItem.from(82_14_003, "设备字段没有配置",
            "bmosMes");
    ResponseItem EQUIPMENT_ACQUISITION_COMPONENT_TYPE_ERROR = ResponseItem.from(82_14_004, "该组件不是设备采数组件", "bmosMes");
    ResponseItem EQUIPMENT_NOT_EXIST = ResponseItem.from(82_14_005, "设备不存在", "bmosMes");
    ResponseItem CAN_NOT_CHOSE_THIS_EQUIPMENT = ResponseItem.from(82_14_006, "不可选择该设备", "bmosMes");
    //todo-------------------------------- 设备提示信息结束 -----------------------------------------------//

    // 产出称量
    ResponseItem OUTPUT_WEIGH_PROCESS_WEIGHER_EXIST = ResponseItem.from(82_15_001, "本次产出称量已确认称量人员", "bmosMes");
    ResponseItem OUTPUT_WEIGH_PROCESS_WEIGHER_NOT_EXIST = ResponseItem.from(82_15_002, "本次产出称量未确认称量人员", "bmosMes");
    ResponseItem OUTPUT_WEIGH_PROCESS_NOT_EXIST = ResponseItem.from(82_15_003, "产出称量信息不存在", "bmosMes");
    ResponseItem WEIGH_RECORD_EXIST_UNSINGED_RECORD = ResponseItem.from(82_15_004, "已称量物料件需签名后才能更换", "bmosMes");
    ResponseItem OUTPUT_WEIGH_RECORD_EXIST = ResponseItem.from(82_15_005, "产出称量已存在称量记录", "bmosMes");
    ResponseItem OUTPUT_LOGIN_USER_WEIGHER_NOT_MATCH = ResponseItem.from(82_15_006, "登录账号与称量人不符", "bmosMes");
    ResponseItem WEIGHER_NOT_MATCH = ResponseItem.from(82_15_007, "称量人信息不匹配", "bmosMes");
    ResponseItem RECHECKER_NOT_MATCH = ResponseItem.from(82_15_008, "复核人信息不匹配", "bmosMes");
    ResponseItem OUTPUT_WEIGH_RECORD_NO_UNSINGED_RECORD = ResponseItem.from(82_15_009, "未找未签名称量信息", "bmosMes");
    ResponseItem OUTPUT_CANT_SCRAP_NOT_UNSIGNED_RECORD = ResponseItem.from(82_15_010, "无法作废已签名的称量记录", "bmosMes");
    ResponseItem LACK_OF_WORKSTATION_OPERATION_PERMISSIONS = ResponseItem.from(82_15_011, "缺少工位操作权限", "bmosMes");
    // --------------------------------------------------清场信息------------------------------------------------------------------------------
    ResponseItem CLEAN_CHECK_ROOM_TYPE_ERROR = ResponseItem.from(82_16_001, "清场检测信息组件类型错误", "bmosMes");
    ResponseItem CLEAN_CHECK_ROOM_SAVE_DOUBLE = ResponseItem.from(82_16_002, "清场检测信息重复", "bmosMes");
    ResponseItem CLEAN_ROOM_NOT_EXIST = ResponseItem.from(82_16_003, "清场房间不存在", "bmosMes");
    ResponseItem CLEAN_ROOM_COMPONENT_NOT_CONFIG = ResponseItem.from(82_16_004, "清场房间组件未配置当前房间", "bmosMes");
    ResponseItem CLEAN_ROOM_COMPONENT_PERMISSION_ERROR = ResponseItem.from(82_16_005, "无当前房间清场权限", "bmosMes");
    ResponseItem CLEAN_CHECK_INFO_SAVE_DOUBLE = ResponseItem.from(82_16_006, "清场检测信息重复", "bmosMes");
    ResponseItem ROOM_COMPONENT_NOT_EXIST = ResponseItem.from(82_16_007, "清场组件不存在", "bmosMes");
    ResponseItem LINE_NOT_BIND_ROOM = ResponseItem.from(82_16_008, "当前清场房间不属于当前生产产线", "bmosMes");

    // todo -------------------------------- 计划归档开始 -----------------------------------------------//
    ResponseItem PLAN_ARCHIVE_NOT_EXIST = ResponseItem.from(82_17_001, "计划状态不正确，只有已完成或者已终止的计划允许归档", "bmosMes");
    ResponseItem PLAN_ARCHIVE_ERROR = ResponseItem.from(82_17_002, "归档失败，请联系管理员", "bmosMes");
    ResponseItem PLAN_ARCHIVE_CATEGORY_NOT_EXIST = ResponseItem.from(82_17_003, "当前模板分类已不存在，请刷新页面后重试", "bmosMes");
    ResponseItem PLAN_ARCHIVE_TEMPLATE_UPLOAD_FAIL = ResponseItem.from(82_17_004, "批记录模板上传失败", "bmosMes");
    ResponseItem PLAN_ARCHIVE_TEMPLATE_NOT_EXIST = ResponseItem.from(82_17_005, "当前批记录模板信息不存在", "bmosMes");
    ResponseItem PLAN_ARCHIVE_TEMPLATE_VERSION_EXIST = ResponseItem.from(82_17_006, "批记录模板信息【{0}】已存在版本【{1}】", "bmosMes");
    ResponseItem PLAN_ARCHIVE_TEMPLATE_VERSION_NOT_EXIST = ResponseItem.from(82_17_007, "批记录模板版本不存在", "bmosMes");
    ResponseItem PLAN_ARCHIVE_TEMPLATE_VERSION_NOT_EDIT = ResponseItem.from(82_17_008, "批记录模板版本【{0}】非编辑状态，无法编辑/确认", "bmosMes");
    ResponseItem PLAN_ARCHIVE_TEMPLATE_VERSION_DOWNLOAD_FAIL = ResponseItem.from(82_17_009, "批记录模板【{0}】的【{1}】版本下载失败", "bmosMes");
    ResponseItem PLAN_ARCHIVE_TEMPLATE_VERSION_NOT_CONFIRM = ResponseItem.from(82_17_010, "批记录模板版本【{0}】非确认状态，无法执行【{1}】操作", "bmosMes");
    ResponseItem PLAN_ARCHIVE_CATEGORY_HAS_CHILD = ResponseItem.from(82_17_011, "当前分类下存在子分类，无法删除", "bmosMes");
    ResponseItem PLAN_ARCHIVE_TEMPLATE_INFO_PROCESS_HAS_PROCESS = ResponseItem.from(82_17_012, "所选工艺已绑定模板信息,无需再次绑定", "bmosMes");
    ResponseItem PLAN_ARCHIVE_TEMPLATE_EXIST = ResponseItem.from(82_17_013, "当前模板信息【{0}】已存在", "bmosMes");
    ResponseItem BATCH_RECORD_ARCHIVE_NOT_EXISTS = ResponseItem.from(82_17_014, "当前批记录不存在", "bmosMes");
    ResponseItem BATCH_RECORD_ARCHIVE_EDIT_ALREADY_OPERATE = ResponseItem.from(82_17_015, "批记录【{0}】非编辑状态，无法进行{1}", "bmosMes");
    ResponseItem BATCH_TEMPLATE_VERSION_NOT_EXISTS = ResponseItem.from(82_17_016, "批记录模板版本不存在", "bmosMes");
    ResponseItem BATCH_RECORD_ARCHIVE_AUDIT_NOT_OPERATE = ResponseItem.from(82_17_017, "批记录【{0}】正在审批，无法进行{1}", "bmosMes");
    ResponseItem BATCH_RECORD_ARCHIVE_SCRAP_NOT_OPERATE =  ResponseItem.from(82_17_018, "批记录【{0}】已为作废状态，无法进行{1}", "bmosMes");
    ResponseItem PLAN_ARCHIVE_CATEGORY_NAME_EXIST = ResponseItem.from(82_17_019, "分类名称【{0}】已存在, 无法更改", "bmosMes");
    ResponseItem BATCH_TEMPLATE_INFO_NOT_EXISTS = ResponseItem.from(82_17_020, "批记录模板不存在", "bmosMes");
    ResponseItem BATCH_ARCHIVE_NOT_EFFECTIVE = ResponseItem.from(82_17_021, "批记录【{0}】非编辑状态，无法进行{1}", "bmosMes");

    ResponseItem PLAN_ARCHIVE_CATEGORY_HAS_TEMPLATE = ResponseItem.from(82_17_022, "当前分类下存在模板信息，无法删除", "bmosMes");
    // todo -------------------------------- 计划归档结束 -----------------------------------------------//

    //todo-------------------------------- 操作规程相关提示信息 -----------------------------------------------//
    ResponseItem CATEGORY_NAME_ERROR = ResponseItem.from(82_18_001, "分类名称已存在", "bmosMes");
    ResponseItem CATEGORY_PARENT_ERROR = ResponseItem.from(82_18_002, "分类下存在子级，无法删除", "bmosMes");
    ResponseItem CATEGORY_OPERATE_ERROR = ResponseItem.from(82_18_003, "分类下存在文件信息，无法删除", "bmosMes");
    ResponseItem OPERATE_CODE_ERROR = ResponseItem.from(82_18_004, "文件编号已存在", "bmosMes");
    ResponseItem OPERATE_VERSION_ERROR = ResponseItem.from(82_18_005, "文件版本已存在", "bmosMes");
    ResponseItem OPERATE_VERSION_FLOW_START_ERROR = ResponseItem.from(82_18_006, "无法发起启用审批，已存在待生效或启用审批的版本", "bmosMes");
    ResponseItem OPERATE_VERSION_FLOW_ERROR = ResponseItem.from(82_18_007, "版本启用或停用失败", "bmosMes");
    ResponseItem OPERATE_UPLOAD_ERROR = ResponseItem.from(82_18_008, "文件格式错误", "bmosMes");
    ResponseItem OPERATE_UPDATE_VALID_ERROR = ResponseItem.from(82_18_009, "版本非确认或编辑状态，无法直接生效", "bmosMes");

    // --------------------称量中心相关--------------------
    ResponseItem WEIGH_CENTRE_CATEGORY_NOT_EXIST = ResponseItem.from(82_19_001, "称量中心分类不存在", "bmosMes");
    ResponseItem WEIGH_CENTRE_CATEGORY_NAME_EXIST = ResponseItem.from(82_19_002, "分类名称已存在", "bmosMes");
    ResponseItem WEIGH_CENTRE_CATEGORY_NOT_ALLOWED_DELETED_WITH_CHILDREN = ResponseItem.from(82_19_003, "分类下存在子级，无法删除", "bmosMes");
    ResponseItem WEIGH_CENTRE_CATEGORY_NOT_ALLOWED_DELETED_WITH_CENTRE = ResponseItem.from(82_19_004, "分类下存在称量中心信息，无法删除", "bmosMes");
    ResponseItem WEIGH_CENTRE_NOT_EXIST = ResponseItem.from(82_19_005, "称量中心不存在", "bmosMes");
    ResponseItem WEIGH_CENTRE_CODE_EXIST = ResponseItem.from(82_19_006, "称量中心编码已存在", "bmosMes");
    ResponseItem WEIGH_CENTRE_ENABLED = ResponseItem.from(82_19_007, "称量中心已启用", "bmosMes");
    ResponseItem WEIGH_CENTRE_DISABLED = ResponseItem.from(82_19_008, "称量中心已停用", "bmosMes");
    ResponseItem WEIGH_CENTRE_NOT_ALLOWED_DISABLED_WITH_REQUIREMENT = ResponseItem.from(82_19_009, "称量中心存在物料称量需求，不允许停用", "bmosMes");
    ResponseItem WEIGH_CENTRE_NOT_ALLOWED_DELETE_WITH_REQUIREMENT = ResponseItem.from(82_19_010, "称量中心已产生历史数据，无法删除", "bmosMes");
    ResponseItem WEIGH_CENTRE_NO_REQUIREMENT = ResponseItem.from(82_19_011, "暂无称量需求", "bmosMes");
    ResponseItem WEIGH_CENTRE_REQUIREMENT_PROGRAM_EXIST = ResponseItem.from(82_19_012, "物料需求重复规划，无法保存", "bmosMes");
    ResponseItem WEIGH_CENTRE_TASK_NOT_EXIST = ResponseItem.from(82_19_013, "称量任务不存在", "bmosMes");
    ResponseItem WEIGH_CENTRE_TASK_NOT_ALLOWED_EDIT = ResponseItem.from(82_19_014, "称量任务不允许编辑", "bmosMes");
    ResponseItem WEIGH_CENTRE_TASK_NOT_ALLOWED_MAKE_SURE = ResponseItem.from(82_19_015, "称量任务不允许确认", "bmosMes");
    ResponseItem WEIGH_CENTRE_TASK_NOT_ALLOWED_SEND = ResponseItem.from(82_19_016, "称量任务不允许下发", "bmosMes");
    ResponseItem WEIGH_CENTRE_TASK_NOT_ALLOWED_CANCEL = ResponseItem.from(82_19_017, "称量任务不允许取消", "bmosMes");
    ResponseItem WEIGH_CENTRE_REQUIREMENT_NO_PROGRAM = ResponseItem.from(82_19_018, "称量需求未规划", "bmosMes");
    ResponseItem WEIGH_CENTRE_REQUIREMENT_NOT_EXIST = ResponseItem.from(82_19_019, "称量需求不存在", "bmosMes");
    ResponseItem WEIGH_CENTRE_REQUIREMENT_NOT_ALLOWED_MAKE_SURE = ResponseItem.from(82_19_020, "称量需求已完成，请勿重复确认", "bmosMes");
    ResponseItem WEIGH_CENTRE_MATERIAL_BATCH_NOT_DUPLICATE = ResponseItem.from(82_19_021, "物料件不是同一批次", "bmosMes");
    ResponseItem WEIGH_CENTRE_REQUIREMENT_NOT_ALLOW_CHANGE_BATCH = ResponseItem.from(82_19_022, "非物料称量阶段，无法进行批次切换", "bmosMes");
    ResponseItem WEIGH_CENTRE_REQUIREMENT_NOT_ALLOW_FINISH = ResponseItem.from(82_19_023, "无法完成该需求的称量", "bmosMes");
    ResponseItem WEIGH_CENTRE_REQUIREMENT_ENOUGH = ResponseItem.from(82_19_024, "已称量物料量已满足需求量", "bmosMes");
    ResponseItem WEIGH_CENTRE_REQUIREMENT_INPUT_NOT_ENOUGH = ResponseItem.from(82_19_025, "请添加物料", "bmosMes");
    ResponseItem WEIGH_CENTRE_STATION_BOUNDED = ResponseItem.from(82_19_026, "{0}工位已绑定称量中心", "bmosMes");
    ResponseItem WEIGH_CENTRE_STORAGE_MATERIAL_INPUTED = ResponseItem.from(82_19_027, "存在已投料的物料件", "bmosMes");
    ResponseItem WEIGH_CENTRE_MATERIAL_INPUT_FINISHED = ResponseItem.from(82_19_028, "物料投入已完成所有物料投入", "bmosMes");
    ResponseItem WEIGH_CENTRE_STORAGE_MATERIAL_NOT_IN_REQUIREMENT = ResponseItem.from(82_19_029, "物料件不在本批需求中", "bmosMes");
    ResponseItem WEIGH_REQUIREMENT_KEY_FINISHED = ResponseItem.from(82_19_030, "物料投入已完成", "bmosMes");
    ResponseItem WEIGH_CENTRE_REQUIREMENT_OVER_TARGET = ResponseItem.from(82_19_031, "超出需求目标量范围", "bmosMes");
    ResponseItem WEIGH_CENTRE_SELECT_UNRESERVED_MATERIAL = ResponseItem.from(82_19_032, "已预定生产批次，请选择未预定的物料件", "bmosMes");
    // --------------------称量中心相关--------------------

    ///todo-------------------------------- 用户相关提示信息 -----------------------------------------------//
    ResponseItem USER_SIGN_UPLOAD_FAIL = ResponseItem.from(82_30_001, "用户签名上传失败", "bmosMes");
    ResponseItem USER_SIGN_SAVE_FAIL = ResponseItem.from(82_30_002, "用户签名保存失败", "bmosMes");
    ResponseItem USER_HANDLE_SIGN_ERROR = ResponseItem.from(82_30_003, "用户手写签名组件回填值失败", "bmosMes");
    ResponseItem USER_SIGN_NOT_EXIST = ResponseItem.from(82_30_004, "当前用户手写签名不存在", "bmosMes");

    // ============================配液相关提示=======================================================//
    ResponseItem PREPARATION_COMPONENT_HAS_NO_CONFIG = ResponseItem.from(82_21_001, "该配液组件无配置信息", "bmosMes");
    ResponseItem PREPARATION_COMPONENT_CONFIG_NOT_ENOUGH = ResponseItem.from(82_21_002, "配液组件配置信息不足", "bmosMes");
    ResponseItem PREPARATION_PLAN_NOT_EXISTS = ResponseItem.from(82_21_003, "配液计划不存在", "bmosMes");
    ResponseItem NO_ANY_PREPARATION_INFO = ResponseItem.from(82_21_004, "无配液信息", "bmosMes");
    ResponseItem PREPARATION_MATERIAL_NOT_EXISTS = ResponseItem.from(82_21_006, "配液物料不存在", "bmosMes");
    ResponseItem LIQUID_MEASURE_INSTANCE_NOT_EXISTS = ResponseItem.from(82_21_007, "配液量取组件实例不存在", "bmosMes");
    ResponseItem MATERIAL_PIECE_NOT_MATCH_MEASURE_BATCH = ResponseItem.from(82_21_008, "物料件不符合配液批次", "bmosMes");
    ResponseItem FIRST_TIME_MEASURE_MUST_HAVE_MEASURER = ResponseItem.from(82_21_009, "首次量取必须有量取人", "bmosMes");
    ResponseItem LIQUID_PREPARATION_BATCH_NOT_EXISTS = ResponseItem.from(82_21_010, "配液批次不存在", "bmosMes");
    ResponseItem LIQUID_PREPARATION_MEASURE_BATCH_NOT_EXISTS = ResponseItem.from(82_21_011, "量取批次不存在", "bmosMes");
    ResponseItem LIQUID_MEASURE_BATCH_COMPLETED = ResponseItem.from(82_21_012, "量取批次已完成量取", "bmosMes");
    ResponseItem LIQUID_MEASURE_OVER_TARGET = ResponseItem.from(82_21_013, "超出批次目标量范围", "bmosMes");
    ResponseItem LIQUID_MEASURE_OVER_DIFF = ResponseItem.from(82_21_014, "请添加物料", "bmosMes");
    ResponseItem MEASURE_RECORD_EXIST_UNSINGED_RECORD = ResponseItem.from(82_21_015, "已量取物料件需签名后才能更换", "bmosMes");
    ResponseItem BATCH_PARAMS_NOT_EXISTS = ResponseItem.from(82_21_016, "批次浓度参数不存在", "bmosMes");
    ResponseItem PREPARATION_PLAN_HAS_INPUT = ResponseItem.from(82_21_017, "已确认配液单，无法切换", "bmosMes");
    ResponseItem PREPARATION_INPUT_NOT_BIND_PLAN = ResponseItem.from(82_21_018, "配液投入未绑定配液单，请先绑定配液单", "bmosMes");
    ResponseItem PREPARATION_INPUT_COMPLETE = ResponseItem.from(82_21_019, "配液投入已完成", "bmosMes");
    ResponseItem PREPARATION_INPUT_EQUIPMENT_NOT_ENOUGH = ResponseItem.from(82_21_020, "不可投入该设备", "bmosMes");
    ResponseItem PREPARATION_INPUT_NOT_FINISHED = ResponseItem.from(82_21_021, "物料件【{0}】配液投入未完成", "bmosMes");
    ResponseItem PREPARE_INPUT_MATERIAL_NO_ALREADY_INPUT = ResponseItem.from(82_21_022, "物料件【{0}】已投入/已失效, 请重新确认", "bmosMes");
    ResponseItem PREPARATION_PRODUCE_COMPONENT_PLAN_ALREADY_DELETE = ResponseItem.from(82_21_023, "配液产出组件绑定的配液单已被删除，无法进行配液产出", "bmosMes");
    ResponseItem PREPARATION_PRODUCE_USER_NOT_EXIST = ResponseItem.from(82_21_024, "配液产出确定的产出人员已不存在", "bmosMes");
    ResponseItem PREPARATION_PRODUCE_COMPONENT_FORMULA_MATERIAL_DELETE = ResponseItem.from(82_21_025, "配液产出组件的配液计划所配置的配方物料已被删除", "bmosMes");
    ResponseItem PREPARATION_PRODUCE_CREATE_MATERIAL = ResponseItem.from(82_21_026, "已确认配液单，无法切换", "bmosMes");
    ResponseItem PREPARATION_PRODUCE_NOT_BIND_PREPARATION_PLAN = ResponseItem.from(82_21_027, "配液产出未绑定配液单，请先绑定配液单", "bmosMes");
    ResponseItem PREPARATION_PRODUCE_NO_UNSIGNED_RECORD = ResponseItem.from(82_21_028, "当前产出的没有物料件待签名", "bmosMes");
    ResponseItem PREPARATION_PRODUCE_PRODUCER_NOT_MATCH = ResponseItem.from(82_21_029, "登录账号与产出人不符", "bmosMes");
    ResponseItem PREPARATION_PRODUCE_SIGNED_RECORD_NOT_CHANGE = ResponseItem.from(82_21_030, "物料件【{0}】未签名，不能更换", "bmosMes");
    ResponseItem PREPARATION_PRODUCE_CONTAINER_NOT_EXIST = ResponseItem.from(82_21_031, "容器【{0}】不存在", "bmosMes");
    ResponseItem PREPARATION_PRODUCE_CONTAINER_EXIST = ResponseItem.from(82_21_032, "容器【{0}】已装载物料件【{1}】", "bmosMes");
    ResponseItem PREPARATION_PRODUCE_CONTAINER_NOT_AVAILABLE = ResponseItem.from(82_21_033, "容器【{0}】状态不可用", "bmosMes");
    ResponseItem PREPARATION_PRODUCE_CARGO_NOT_EXIST = ResponseItem.from(82_21_034, "容器【{0}】不存在", "bmosMes");
    ResponseItem PREPARATION_PRODUCE_QUANTITY_MUST_GE_ZERO = ResponseItem.from(82_21_035, "产出结果必须大于0", "bmosMes");
    ResponseItem PREPARATION_PRODUCE_PRODUCT_PLAN_DELETE = ResponseItem.from(82_21_036, "当前组件对应的生产计划已被删除", "bmosMes");
    ResponseItem PREPARATION_INPUT_SCAN_MATERIAL_SCRAP = ResponseItem.from(82_21_038, "物料件未生效", "bmosMes");
    ResponseItem PREPARATION_INPUT_SCAN_MATERIAL_BATCH_EXPIRED = ResponseItem.from(82_21_039, "物料件已超过有效期", "bmosMes");
    ResponseItem PREPARATION_QUANTITY_ZERO = ResponseItem.from(82_21_040, "配液量不能为0", "bmosMes");
    ResponseItem PREPARATION_PLAN_ALREADY_COMPLETED = ResponseItem.from(82_21_041, "配液计划已完成", "bmosMes");

    ResponseItem PREPARATION_INPUT_SCAN_MATERIAL_NOT_EXIST = ResponseItem.from(82_21_042, "请扫描物料件/容器标签", "bmosMes");
    ResponseItem PREPARATION_INPUT_SCAN_MATERIAL_NOT_RESERVE = ResponseItem.from(82_21_043, "请扫描预定的物料件标签", "bmosMes");
    ResponseItem PLEASE_SCAN_CONTAINER_TAG = ResponseItem.from(82_21_044, "请扫描容器标签", "bmosMes");
    ResponseItem INPUT_EQUIPMENT_STATUS_UNAVAILABLE = ResponseItem.from(82_21_045, "设备状态不可用", "bmosMes");
    ResponseItem LOGIN_USER_NOT_MATCH_MEASURER = ResponseItem.from(82_21_046, "登录账号与操作人不符", "bmosMes");
    ResponseItem MEASURE_QUANTITY_MUST_GRATER_THAN_ZERO = ResponseItem.from(82_21_047, "量取结果必须大于0", "bmosMes");

    /* ---------------------------------- 异常管理 提示信息 ----------------------------------------------*/
    ResponseItem EXCEPTION_NOT_EXISTS = ResponseItem.from(82_22_001, "异常记录不存在", "bmosMes");
    ResponseItem EXCEPTION_CANCELED = ResponseItem.from(82_22_002, "异常记录已作废,请确认", "bmosMes");
    ResponseItem EXCEPTION_INVESTIGATING = ResponseItem.from(82_22_003, "异常记录已处于调查中,请确认", "bmosMes");
    ResponseItem EXCEPTION_ALREADY_HANDLED = ResponseItem.from(82_22_004, "该异常已处理，请确认", "bmosMes");
    //todo-------------------------------- 拍照上传提示信息 ----------------------------------------------//
    ResponseItem ATTACHMENT_FILE_ERROR = ResponseItem.from(82_22_005, "拍照上传失败", "bmosMes");
    // 批签发相关
    ResponseItem DATASET_CATEGORY_NOT_EXIST = ResponseItem.from(82_23_001, "数据集分类不存在", "bmosMes");
    ResponseItem DATASET_CATEGORY_NAME_EXIST = ResponseItem.from(82_23_002, "数据集分类名称已存在", "bmosMes");
    ResponseItem DATASET_CATEGORY_NOT_ALLOWED_DELETED_WITH_CHILDREN = ResponseItem.from(82_23_003, "数据集分类下存在子分类，不允许删除", "bmosMes");
    ResponseItem DATASET_CATEGORY_NOT_ALLOWED_DELETED_WITH_CENTRE = ResponseItem.from(82_23_004, "数据集分类下存在数据集，不允许删除", "bmosMes");
    ResponseItem DATASET_NAME_EXIST = ResponseItem.from(82_23_005, "数据集名称已存在", "bmosMes");
    ResponseItem DATASET_NOT_EXIST = ResponseItem.from(82_23_006, "数据集不存在", "bmosMes");
    ResponseItem DATASET_POINT_NOT_EXIST = ResponseItem.from(82_23_007, "数据点不存在", "bmosMes");
    ResponseItem LOT_RELEASE_TEMPLATE_CATEGORY_NOT_EXIST = ResponseItem.from(82_23_008, "批签发模版分类不存在", "bmosMes");
    ResponseItem LOT_RELEASE_TEMPLATE_CATEGORY_NAME_EXIST = ResponseItem.from(82_23_009, "批签发模版分类名称已存在", "bmosMes");
    ResponseItem LOT_RELEASE_TEMPLATE_CATEGORY_NOT_ALLOWED_DELETED_WITH_CHILDREN = ResponseItem.from(82_23_010, "批签发模版分类下存在子分类，不允许删除", "bmosMes");
    ResponseItem LOT_RELEASE_TEMPLATE_CATEGORY_NOT_ALLOWED_DELETED_WITH_CENTRE = ResponseItem.from(82_23_011, "批签发模版分类下存在数据集，不允许删除", "bmosMes");
    ResponseItem LOT_RELEASE_TEMPLATE_VERSION_EXIST = ResponseItem.from(82_23_012, "批签发模版版本号已存在", "bmosMes");
    ResponseItem LOT_RELEASE_TEMPLATE_NOT_EXIST = ResponseItem.from(82_23_013, "批签发模版不存在", "bmosMes");
    ResponseItem LOT_RELEASE_TEMPLATE_FILE_TYPE_ERROR = ResponseItem.from(82_23_014, "批签发模版格式不正确", "bmosMes");
    ResponseItem LOT_RELEASE_TEMPLATE_VERSION_NOT_EXIST = ResponseItem.from(82_23_015, "批签发模板版本不存在", "bmosMes");
    ResponseItem LOT_RELEASE_TEMPLATE_VERSION_CANT_MAKE_SURE = ResponseItem.from(82_23_016, "批签发模板版本无法确认", "bmosMes");
    ResponseItem LOT_RELEASE_TEMPLATE_VERSION_CANT_EDIT = ResponseItem.from(82_23_017, "批签发模板版本无法编辑", "bmosMes");
    ResponseItem LOT_RELEASE_TEMPLATE_VERSION_CANT_MAKE_DEFAULT = ResponseItem.from(82_23_018, "批签发模板版本无法设为默认", "bmosMes");
    ResponseItem LOT_RELEASE_NOT_EXISTS = ResponseItem.from(82_23_019, "批签发不存在", "bmosMes");
    ResponseItem LOT_RELEASE_CANT_EDIT = ResponseItem.from(82_23_020, "批签发不允许编辑", "bmosMes");
    ResponseItem LOT_RELEASE_CANT_SCRAP = ResponseItem.from(82_23_021, "批签发无法作废", "bmosMes");
    ResponseItem FILE_DOWNLOAD_FAILED = ResponseItem.from(82_23_022, "文件下载失败", "bmosMes");
    ResponseItem DATASET_DYNAMIC_REPORT_DATA_NAME_DUPLICATE = ResponseItem.from(82_23_023, "动态填报数据名称重复", "bmosMes");
    ResponseItem LOT_RELEASE_TEMPLATE_NAME_EXIST = ResponseItem.from(82_23_024, "批签发模板名称已存在", "bmosMes");

    /* ---------------------------------- 计划管理 提示信息 ----------------------------------------------*/
    ResponseItem PRODUCTION_PLAN_ERROR = ResponseItem.from(82_24_001, "计划信息为空", "bmosMes");
    ResponseItem PRODUCTION_PLAN_START_ERROR = ResponseItem.from(82_24_002, "指令单编号【{0}】已开始生产，不能作废", "bmosMes");
    ResponseItem PLAN_TEMPLATE_ERROR = ResponseItem.from(82_24_003, "模板数据为空", "bmosMes");
    ResponseItem PLAN_TEMPLATE_CONFIRMED_ERROR = ResponseItem.from(82_24_004, "工艺版本与当前生效版本不一致", "bmosMes");
    ResponseItem PRODUCTION_PLAN_NAME_EXISTED = ResponseItem.from(82_24_005, "生产计划名称已存在", "bmosMes");
    ResponseItem PRODUCTION_PLAN_TEMPLATE_NAME_EXISTED = ResponseItem.from(82_24_006, "生产计划模板名称已存在", "bmosMes");
    ResponseItem PRODUCTION_PLAN_TEMPLATE_NOT_EXISTS = ResponseItem.from(82_24_007, "生产计划模板不存在", "bmosMes");
    ResponseItem TEMPLATE_DISABLE_OR_DELETED = ResponseItem.from(82_24_008, "选择的生产计划模版已停用/删除,请确认", "bmosMes");
    ResponseItem PRODUCTION_PLAN_NULLIFY_ERROR = ResponseItem.from(82_24_009, "当前计划已作废", "bmosMes");
    ResponseItem PRODUCTION_PLAN_RELATION_ERROR = ResponseItem.from(82_24_010, "关联批次存在异常,请检查", "bmosMes");
    ResponseItem PRODUCTION_PLAN_ISSUE_PARAM_ERROR = ResponseItem.from(82_24_011, "生产计划下发参数异常", "bmosMes");
    ResponseItem PRODUCTION_PLAN_ISSUE_BATCH_PARAM_ERROR = ResponseItem.from(82_24_012, "指令单编号或生产批号不能为空", "bmosMes");
    ResponseItem ISSUE_BATCH_LIST_EMPTY = ResponseItem.from(82_24_013, "下发批次列表不能为空", "bmosMes");
    ResponseItem PRODUCTION_PLAN_BATCH_ERROR = ResponseItem.from(82_24_014, "未配置编号规则，请确认", "bmosMes");
    ResponseItem PLAN_NO_AND_BATCH_NO_ERROR = ResponseItem.from(82_24_015, "工艺【{0}】未配置生产批号编号规则、指令单编号规则，请检查配置;", "bmosMes");
    ResponseItem BATCH_NO_ERROR = ResponseItem.from(82_24_016, "工艺【{0}】未配置生产批号编号规则，请检查配置;", "bmosMes");
    ResponseItem PLAN_NO_ERROR = ResponseItem.from(82_24_017, "工艺【{0}】未配置指令单编号规则，请检查配置;", "bmosMes");
    ResponseItem PROCEDURE_ENDTIME_AFTER_PROCESS_ENDTIME = ResponseItem.from(82_24_018, "工艺结束时间不能早于下属工序最晚结束日期", "bmosMes");
    ResponseItem PROCEDURE_STARTTIME_BEFORE_PROCESS_STARTTIME = ResponseItem.from(82_24_019, "工序开始时间不能早于工艺开始时间", "bmosMes");
    ResponseItem PROCEDURE_STARTTIME_AFTER_PROCESS_ENDTIME = ResponseItem.from(82_24_020, "工序开始时间不能晚于工艺结束时间", "bmosMes");
    ResponseItem PLAN_ITEM_NOT_BIND_PRODUCTION_LINE = ResponseItem.from(82_24_021, "指令单【{0}】未选择产线", "bmosMes");



    ResponseItem LOT_SUMMARY_NAME_EXIST = ResponseItem.from(82_25_001, "批次摘要名称已存在", "bmosMes");;
    ResponseItem LOT_SUMMARY_NOT_EXIST = ResponseItem.from(82_25_002, "批次摘要不存在", "bmosMes");

    ResponseItem BATCH_GENERATE_FAIL = ResponseItem.from(82_29_001, "批记录/批签发生成失败", "bmosMes");


    // 皮重配置相关
    ResponseItem TARE_WEIGH_CONFIG_NOT_EXIST = ResponseItem.from(82_26_001, "皮重配置不存在", "bmosMes");
    ResponseItem TARE_WEIGH_UNIT_CONVERT_ERROR = ResponseItem.from(82_26_002, "单位无法转换，请扫描正确的皮重标签", "bmosMes");

    ResponseItem MATERIAL_TRACE_TEMPLATE_NOT_EXIST = ResponseItem.from(82_27_001, "物料追溯模板不存在", "bmosMes");
    ResponseItem MATERIAL_TRACE_TEMPLATE_ENABLED = ResponseItem.from(82_27_002, "物料追溯模板已启用", "bmosMes");
    ResponseItem MATERIAL_TRACE_TEMPLATE_DISABLED = ResponseItem.from(82_27_003, "物料追溯模板已停用", "bmosMes");
    ResponseItem MATERIAL_TRACE_TEMPLATE_NAME_EXIST = ResponseItem.from(82_27_004, "物料追溯模板名称已存在", "bmosMes");
    ResponseItem MATERIAL_TRACE_TEMPLATE_PROCESS_ENABLED = ResponseItem.from(82_27_005, "该工艺存在启用的物料追溯模板【{0}】", "bmosMes");
    ResponseItem MATERIAL_TRACE_PROCESS_NO_TEMPLATE = ResponseItem.from(82_27_006, "该工艺不存在启用的物料追溯模板", "bmosMes");
    ResponseItem MATERIAL_TRACE_PROCESS_MATERIAL_NO_RELATION = ResponseItem.from(82_27_007, "（{0}）的{1}信息工艺信息错误", "bmosMes");
    ResponseItem LOG_NOT_EXIST = ResponseItem.from(82_28_001, "日志不存在", "bmosMes");
    ResponseItem MESSAGE_ERROR = ResponseItem.from(82_28_002, "消息id不能为空", "bmosMes");
    ResponseItem MESSAGE_USER_ERROR = ResponseItem.from(82_28_003, "接收人不能为空", "bmosMes");
    ResponseItem MESSAGE_CONTENT_ERROR = ResponseItem.from(82_28_004, "消息参数不能为空", "bmosMes");

    // 大山包P1迭代中--->需要添加到国际化文件
    ResponseItem INSPECT_CONFIG_NAME_EXIST = ResponseItem.from(82_31_001, "请验单【{0}】名称已存在", "bmosMes");
    ResponseItem INSPECT_CONFIG_DATA_EMPTY = ResponseItem.from(82_31_002, "请验单缺少请验单数据无法保存", "bmosMes");
    ResponseItem INSPECT_CONFIG_DATA_CODE_EXIST = ResponseItem.from(82_31_003, "请验单数据重复", "bmosMes");
    ResponseItem INSPECT_CONFIG_NOT_EXIST = ResponseItem.from(82_31_004, "当前请验单已不存在", "bmosMes");
    ResponseItem INSPECT_CONFIG_ENABLE_NOT_OPERATE = ResponseItem.from(82_31_005, "请验单已启用，无法操作", "bmosMes");
    ResponseItem MATERIAL_NOT_BIND_INSPECT_CONFIG = ResponseItem.from(82_31_006, "物料【{0}】未绑定请验单配置", "bmosMes");
    ResponseItem INSPECT_NO_ALREADY_EXIST = ResponseItem.from(82_31_007, "请验单标号【{0}】已存在", "bmosMes");
    ResponseItem INSPECT_INFO_NOT_EXISTS = ResponseItem.from(82_31_008, "请验单信息不能为空", "bmosMes");
    ResponseItem INSPECT_INFO_NOT_EMPTY = ResponseItem.from(82_31_009, "【{0}】请验单信息必填", "bmosMes");
    ResponseItem INSPECT_NOT_EXIST = ResponseItem.from(82_31_010, "请验单不存在", "bmosMes");
    ResponseItem INSPECT_CONFIG_BIND_MATERIAL = ResponseItem.from(82_31_011, "当前请验单已绑定物料，请先解除绑定", "bmosMes");
    ResponseItem INSPECT_CONFIG_HAS_INSPECT = ResponseItem.from(82_31_012, "当前请验单已存在请验单, 无法停用", "bmosMes");
    ResponseItem INSPECT_REJECTED_STATUS_ERROR = ResponseItem.from(82_31_013, "当前请验单已重新发起请验，请等待结果完成", "bmosMes");
    ResponseItem INSPECT_STATUS_ERROR = ResponseItem.from(82_31_014, "请验单状态为【{0}】,无法再次接收结果", "bmosMes");
    ResponseItem INSPECT_RESULT_COMPONENT_INSTANCE_NOT_EXIST = ResponseItem.from(82_31_016, "请验结果组件实例不存在", "bmosMes");
    
    // -------------- centre2模块相关异常 -----------------
    ResponseItem WEIGH_TICKET_NOT_EXIST = ResponseItem.from(82_40_001, "工单不存在", "bmosMes");
    ResponseItem WEIGH_TICKET_NO_EXIST = ResponseItem.from(82_40_002, "工单编号已存在", "bmosMes");
    ResponseItem WEIGH_TICKET_CANNOT_MODIFY = ResponseItem.from(82_40_003, "工单已开始或已完成，不能修改", "bmosMes");
    ResponseItem WEIGH_TICKET_CANNOT_DELETE = ResponseItem.from(82_40_004, "工单已开始或已完成，不能删除", "bmosMes");
    ResponseItem WEIGH_TICKET_COMPLETED_CANNOT_MODIFY = ResponseItem.from(82_40_005, "已完成的工单不能修改状态", "bmosMes");
    
    ResponseItem WEIGH_REQUIREMENT_GROUP_NOT_EXIST = ResponseItem.from(82_40_006, "工单需求组不存在", "bmosMes");
    ResponseItem WEIGH_REQUIREMENT_CANNOT_MODIFY = ResponseItem.from(82_40_007, "已完成或处理中的需求不能修改", "bmosMes");
    ResponseItem WEIGH_REQUIREMENT_CANNOT_DELETE = ResponseItem.from(82_40_008, "已完成或处理中的需求不能删除", "bmosMes");
    ResponseItem WEIGH_REQUIREMENT_COMPLETED_CANNOT_MODIFY = ResponseItem.from(82_40_009, "已完成的需求不能修改状态", "bmosMes");
    ResponseItem WEIGH_REQUIREMENT_MATERIAL_NOT_EXIST = ResponseItem.from(82_40_010, "需求物料不存在", "bmosMes");
    ResponseItem WEIGH_REQUIREMENT_GROUP_NOT_ALLOWED_CANCEL = ResponseItem.from(82_40_011, "称量需求已规划工单，无法取消", "bmosMes");
    ResponseItem WEIGH_REQUIREMENT_GROUP_NOT_ALLOWED_MAKE_SURE = ResponseItem.from(82_40_012, "工单需求不允许确认", "bmosMes");

    ResponseItem WEIGH_REQUIREMENT_ANOTHER_WEIGHING = ResponseItem.from(82_40_013, "该工单需求正在称量中，请勿重复操作", "bmosMes");
    ResponseItem WEIGH_TICKET_NO_REQUIREMENT = ResponseItem.from(82_40_014, "暂无称量需求", "bmosMes");

    ResponseItem WEIGH_REQUIREMENT_NOT_SELECTABLE = ResponseItem.from(82_40_015, "当前需求不是未称量或称量中，无法选中", "bmosMes");
    ResponseItem WEIGH_REQUIREMENT_STATUS_ERROR = ResponseItem.from(82_40_016, "当前需求状态为【{0}】，无法操作", "bmosMes");
    ResponseItem WEIGH_ALREADY_BIND_OPERATOR = ResponseItem.from(82_40_017, "该工单需求已绑定操作人，请勿重复操作", "bmosMes");
    ResponseItem WEIGH_TICKET_NOT_BIND_OPERATOR = ResponseItem.from(82_40_018, "工单未绑定操作人", "bmosMes");
    ResponseItem USER_NOT_LOGIN = ResponseItem.from(82_40_019, "当前账号未登录", "bmosMes");
    ResponseItem WEIGH_TICKET_OPERATOR_NOT_MATCH = ResponseItem.from(82_40_020, "请使用绑定的操作人账号进行工单执行或更换操作人", "bmosMes");
    ResponseItem WEIGH_TICKET_REQUIREMENT_PROGRAM_EXIST = ResponseItem.from(82_40_021, "工单需求重复规划，无法保存", "bmosMes");
    ResponseItem WEIGH_TICKET_STATUS_ERROR = ResponseItem.from(82_40_022, "工单状态错误，只有编辑中状态的工单可以操作", "bmosMes");
    ResponseItem WEIGHT_TICKET_NO_REQUIREMENT = ResponseItem.from(82_40_023, "请添加称量需求", "bmosMes");


    ResponseItem WEIGH_REQUIREMENT_NOT_EXIST = ResponseItem.from(82_41_001, "工单需求不存在", "bmosMes");
    ResponseItem WEIGH_STORAGE_MATERIAL_QUALITY_EMPTY = ResponseItem.from(82_41_002, "物料件【{0}】可用量为0，无法添加", "bmosMes");
    ResponseItem WEIGH_STORAGE_MATERIAL_NOT_OUT = ResponseItem.from(82_41_003, "物料件【{0}】未出库无法添加", "bmosMes");
    ResponseItem WEIGH_STORAGE_MATERIAL_ID_NOT_EMPTY = ResponseItem.from(82_41_004, "物料件ID不能为空", "bmosMes");

    ResponseItem WEIGH_REQUIREMENT_NOT_BELONG_TO_TICKET = ResponseItem.from(82_19_035, "称量需求不属于当前工单", "bmosMes");
    ResponseItem WEIGH_REQUIREMENT_ALREADY_PLANNED = ResponseItem.from(82_19_036, "称量需求已被规划", "bmosMes");
    ResponseItem WEIGH_REQUIREMENT_MATERIAL_NOT_MATCH = ResponseItem.from(82_19_037, "称量需求物料与工单物料不匹配", "bmosMes");
    ResponseItem WEIGH_STORAGE_MATERIAL_NOT_BELONG_BATCH = ResponseItem.from(82_41_005, "物料件非对应物料批次，无法添加", "bmosMes");
    ResponseItem WEIGH_DEVICE_BIND_OTHER_STORAGE_MATERIAL = ResponseItem.from(82_41_006, "设备【{0}】已绑定其他物料件", "bmosMes");
    ResponseItem WEIGH_ODDMENT_NOT_ENOUGH = ResponseItem.from(82_41_007, "工单内的有待完成的称量需求，不能进行余料称量", "bmosMes");
    ResponseItem WEIGH_TICKET_HAS_NOT_SIGN_RECORD = ResponseItem.from(82_41_008, "工单内含有未签名的称量记录，请先签名", "bmosMes");
    ResponseItem WEIGH_REQUIREMENT_NOT_BIND_OPERATOR = ResponseItem.from(82_41_009, "工单未绑定操作人，请先绑定操作人", "bmosMes");
    ResponseItem WEIGH_REQUIREMENT_SIGN_BIND_NOT_EQUAL = ResponseItem.from(82_41_010, "工单签名人与工单绑定的签名人不一致", "bmosMes");
    ResponseItem WEIGH_TICKET_OPERATOR_SIGN_USER_NOT_EQUAL = ResponseItem.from(82_41_011, "工单操作人与工单的签名人不能相同", "bmosMes");
    ResponseItem WEIGH_TICKET_EXECUTED_OR_NOT_SEND = ResponseItem.from(82_41_012, "工单已执行或未下发", "bmosMes");
    ResponseItem WEIGH_REQUIREMENT_ALREADY_WEIGHED =  ResponseItem.from(82_41_013, "工单需求已称量完成，请刷新后重新选择", "bmosMes");
    ResponseItem WEIGH_STORAGE_MATERIAL_NOT_APPEND = ResponseItem.from(82_41_014, "物料件未添加, 请添加物料件", "bmosMes");
    ResponseItem WEIGH_ODDMENT_ALREADY_FINISH = ResponseItem.from(82_41_015, "已称量完成，无法继续称量", "bmosMes");
    ResponseItem WEIGH_TICKET_ODDMENT_TARGET_NOT_MANY = ResponseItem.from(82_41_016, "余料称量结果没有超出目标量允差范围，无需签名确认", "bmosMes");
    ResponseItem WEIGH_SCAN_STORAGE_MATERIAL_TYPE_ERROR = ResponseItem.from(82_41_017, "请扫描物料件标签", "bmosMes");
    ResponseItem WEIGH_SCAN_STORAGE_MATERIAL_BATCH_EXPIRED = ResponseItem.from(82_41_018, "物料件已超出有效期至", "bmosMes");
    ResponseItem WEIGH_STORAGE_MATERIAL_ALREADY_RESERVE = ResponseItem.from(82_41_019, "已预定生产批次，请选择未预定的物料件", "bmosMes");
    ResponseItem WEIGH_NO_RECORD_NEED_SIGN = ResponseItem.from(82_41_020, "所有称量都已签名，无需重复签名", "bmosMes");
    ResponseItem WEIGH_STORAGE_MATERIAL_NOT_ENOUGH = ResponseItem.from(82_41_021, "请添加物料", "bmosMes");
    ResponseItem WEIGH_ODDMENT_NOT_FINISH_SIGN = ResponseItem.from(82_41_022, "余料称量超出目标量，请进行签名确认", "bmosMes");
    ResponseItem WEIGH_STORAGE_MATERIAL_RESERVED = ResponseItem.from(82_41_023, "物料件已预定生产批次，请选择未预定的物料件", "bmosMes");
}