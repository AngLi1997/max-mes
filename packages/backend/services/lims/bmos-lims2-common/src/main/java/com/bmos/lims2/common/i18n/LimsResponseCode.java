package com.bmos.lims2.common.i18n;

import com.bmos.common.response.ResponseItem;

/**
 * Lims业务错误码
 *
 * 04 检验班组
 * 05 检验方案
 * 06 请验单配置
 */
public interface LimsResponseCode {

    ResponseItem NOT_ACTIVE = ResponseItem.from(81_01_0001, "Lims未授权", "bmosLims");

    ResponseItem ACTIVE_ERROR = ResponseItem.from(81_01_0002, "激活码错误", "bmosLims");

    /**
     * ------------------------- 通用错误码 ----------------------------
     */
    ResponseItem INVALID_PARAM = ResponseItem.from(81_00_0001, "参数无效", "bmosLims");
    ResponseItem INVALID_PARAMETER_EMPTY = ResponseItem.from(81_00_0005, "参数【{0}】不能为空", "bmosLims");
    ResponseItem DATA_NOT_EXISTS = ResponseItem.from(81_00_0002, "数据不存在", "bmosLims");
    ResponseItem BUSINESS_ERROR = ResponseItem.from(81_00_0003, "业务处理异常", "bmosLims");
    ResponseItem SYSTEM_ERROR = ResponseItem.from(81_00_0004, "系统异常", "bmosLims");

    ResponseItem ORCHESTRATOR_ENGINE_ERROR = ResponseItem.from(83_10_001, "流程控制器未配置完成", "bmosLims");
    ResponseItem PLATFORM_GET_SYNC_ERROR = ResponseItem.from(83_03_002, "从平台获取同步信息错误", "bmosLims");
    ResponseItem INSPECTION_CATEGORY_NOT_EXISTED = ResponseItem.from(83_03_003, "检品分类已不存在，请刷新后重新试", "bmosLims");
    ResponseItem CODE_EXISTED = ResponseItem.from(83_03_004, "编码已存在, 无法操作", "bmosLims");
    ResponseItem CATEGORY_EXIST_CHILD = ResponseItem.from(83_03_005, "该分类存在子级,无法删除", "bmosLims");
    ResponseItem CATEGORY_EXIST_PRODUCTS = ResponseItem.from(83_03_006, "该分类存在检品,无法删除", "bmosLims");
    ResponseItem CATEGORY_NOT_EXISTS = ResponseItem.from(83_03_007, "该分类不存在", "bmosLims");
    ResponseItem CODE_NOT_EXISTS = ResponseItem.from(83_03_009, "编码不存在,无法操作", "bmosLims");
    ResponseItem ANALYZE_PROGRAM_BIND_INSPECT = ResponseItem.from(83_03_010, "该分析项已绑定检验项,无法删除", "bmosLims");
    ResponseItem INSPECT_PROGRAM_BIND_PACKAGE = ResponseItem.from(83_03_011, "该检验项已绑定实验包,无法删除", "bmosLims");
    ResponseItem CHOOSE_NOT_EXIST = ResponseItem.from(83_03_012, "该选项不存在,无法操作", "bmosLims");
    ResponseItem PACKAGE_BIND_PRODUCTS = ResponseItem.from(83_03_013, "该实验包已绑定检品,无法删除", "bmosLims");
    ResponseItem REPORT_ANALYZE_STANDARD_NOT_EMPTY = ResponseItem.from(83_03_014, "报告分析项中,标准不能为空", "bmosLims");
    ResponseItem EXPORT_LOG_FAIL = ResponseItem.from(83_03_015, "导出操作日志记录失败", "bmosLims");
    ResponseItem ORDER_CODE_FAIL = ResponseItem.from(83_03_016, "请验单编号生成失败", "bmosLims");
    ResponseItem PRODUCTS_NOT_EXIST = ResponseItem.from(83_03_017, "检品不存在", "bmosLims");
    ResponseItem PRODUCTS_PACKAGE_UN_BIND = ResponseItem.from(83_03_018, "当前实验包已与产品解绑", "bmosLims");

    ResponseItem PACKAGE_NOT_BIND_INSPECT = ResponseItem.from(83_03_019, "当前实验包未与检验项目绑定", "bmosLims");
    ResponseItem PACKAGE_INSPECT_NOT_BIND_ANALYZE = ResponseItem.from(83_03_020, "当前实验包中某个检验项目不存在分析项", "bmosLims");
    ResponseItem CHECK_ORDER_ALREADY_FINISHED = ResponseItem.from(83_03_021, "当前请验单已完成", "bmosLims");
    ResponseItem CHECK_ORDER_PROCESS_ERROR = ResponseItem.from(83_03_022, "当前请验单已完成该流程", "bmosLims");
    ResponseItem ORDER_PROCESS_FAIL = ResponseItem.from(83_03_023, "请验单流程执行失败", "bmosLims");
    ResponseItem CHECK_ORDER_ALREADY_TERMINATE = ResponseItem.from(83_03_024, "当前请验单已终止", "bmosLims");
    ResponseItem PRODUCTS_ALREADY_CHECK = ResponseItem.from(83_03_025, "当前检品已生成请验单", "bmosLims");
    ResponseItem CHECK_ORDER_NOT_FOUND = ResponseItem.from(83_03_026, "检验单不存在", "bmosLims");
    ResponseItem PRODUCT_STATUS_ALREADY_UPDATED = ResponseItem.from(83_03_027, "检品状态已更新,请重试", "bmosLims");
    ResponseItem CHECK_ANALYZE_RESULT_INCOMPLETE = ResponseItem.from(83_03_028, "分析项结果录入不完整", "bmosLims");

    /**
     * 合并编码已存在
     */
    ResponseItem MERGE_CODE_EXISTED = ResponseItem.from(83_03_030, "合并编码已存在", "bmosLims");
    ResponseItem MATERIAL_CATEGORY_EXISTED = ResponseItem.from(82_03_001, "生产物料分类已存在", "bmosLims");
    ResponseItem MATERIAL_CATEGORY_EXISTED_IN_PLATFORM = ResponseItem.from(82_03_002, "生产物料分类在平台已存在", "bmosLims");
    ResponseItem MATERIAL_CATEGORY_EXISTS_CHILD = ResponseItem.from(82_03_003, "该分类存在子级,无法删除", "bmosLims");
    ResponseItem MATERIAL_CATEGORY_LINKED_PRODUCT = ResponseItem.from(82_03_004, "该分类下已有物料存在,无法删除", "bmosLims");
    ResponseItem MATERIAL_ENABLED = ResponseItem.from(82_03_005, "物料已启用", "bmosLims");
    ResponseItem MATERIAL_EXISTED_IN_PLATFORM = ResponseItem.from(82_03_006, "物料编码已存在", "bmosLims");
    ResponseItem PLATFORM_MATERIAL_REGISTER_ERROR = ResponseItem.from(82_03_007, "向平台注册物料错误", "bmosLims");
    ResponseItem MATERIAL_NOT_EXISTED = ResponseItem.from(82_03_008, "物料信息不存在", "bmosLims");
    ResponseItem SUB_MATERIAL_MUST_HAS_PRINCIPAL = ResponseItem.from(82_03_009, "成员物料必须选择所属物料", "bmosLims");
    ResponseItem MATERIAL_CATEGORY_NOT_EXISTED = ResponseItem.from(82_03_010, "生产物料分类不存在,请检查", "bmosLims");
    ResponseItem PLATFORM_MATERIAL_CATEGORY_REGISTER_ERROR = ResponseItem.from(82_03_011, "向平台注册物料分类错误", "bmosLims");
    ResponseItem PLATFORM_CHECK_CODE_ERROR = ResponseItem.from(82_03_012, "向平台校验编码错误", "bmosLims");
    ResponseItem PLATFORM_GET_UNIT_ERROR = ResponseItem.from(82_03_014, "从平台获取单位错误", "bmosLims");
    ResponseItem PLATFORM_MATERIAL_CATEGORY_CODE_EXISTED = ResponseItem.from(82_03_015, "分类编码重复", "bmosLims");
    ResponseItem PRODUCT_MATERIAL_EXISTED_MEMBER_MATERIAL = ResponseItem.from(82_03_016, "产品信息已关联成员产品,不允许停用", "bmosLims");
    ResponseItem ORIGINAL_MATERIAL_EXISTED_BATCH = ResponseItem.from(82_03_017, "该原辅包信息已产生物料批次,无法删除", "bmosLims");
    ResponseItem MATERIAL_EXISTED_MEMBER_MATERIAL = ResponseItem.from(82_03_018, "物料信息已关联成员物料,不允许停用", "bmosLims");
    ResponseItem MATERIAL_EXISTED_SCHEME = ResponseItem.from(82_03_019, "该检品已配置检验方案，禁止停用", "bmosLims");


    /**
     * 实验包删除校验：被方案绑定
     */
    ResponseItem PACKAGE_BIND_SCHEMES = ResponseItem.from(83_03_029, "实验包已被{0}方案绑定，无法删除", "bmosLims");


    /**
     * ------------------------- 流程配置 ----------------------------
     */

    ResponseItem FLOW_PAYLOAD_ERROR = ResponseItem.from(83_06_001, "流程参数不能为空", "bmosLims");
    ResponseItem FLOW_AUDIT_STATE_ERROR = ResponseItem.from(83_06_002, "流程版本数据状态异常", "bmosLims");
    ResponseItem FLOW_AUDIT_SELECT_ERROR = ResponseItem.from(83_06_003, "流程数据查询异常", "bmosLims");
    ResponseItem FLOW_AUDIT_CREATE_ERROR = ResponseItem.from(83_06_004, "流程保存异常", "bmosLims");
    int FLOW_AUDIT_ERROR = 83_06_005;
    ResponseItem FLOW_AUDIT_NOT_DELETE = ResponseItem.from(83_06_006, "流程不允许删除", "bmosLims");
    ResponseItem FLOW_AUDIT_PARAMETER_ERROR = ResponseItem.from(83_06_007, "参数不能为空", "bmosLims");
    ResponseItem FLOW_AUDIT_NOT_ERROR = ResponseItem.from(83_06_008, "无启用的审核流程", "bmosLims");
    ResponseItem FLOW_AUDIT_STATER_ERROR = ResponseItem.from(83_06_009, "流程发起失败", "bmosLims");
    ResponseItem FLOW_AUDIT_USER_ERROR = ResponseItem.from(83_06_010, "流程未找到人员", "bmosLims");
    ResponseItem FLOW_AUDIT_COMPLETE_ERROR = ResponseItem.from(83_06_011, "流程处理失败", "bmosLims");
    ResponseItem FLOW_AUDIT_MESSAGE_USER_ERROR = ResponseItem.from(83_06_012, "未配置抄送人，校验不通过", "bmosLims");
    ResponseItem FLOW_AUDIT_ROLE_USER_ERROR = ResponseItem.from(83_06_013, "未配置审核角色，不可配置角色会签策略", "bmosLims");
    ResponseItem FLOW_AUDIT_All_USER_ERROR = ResponseItem.from(83_06_014, "未配置审核人员，不可配置人员会签策略", "bmosLims");
    ResponseItem FLOW_AUDIT_NAME_ERROR = ResponseItem.from(83_06_015, "流程模型名称重复", "bmosLims");
    ResponseItem FLOW_AUDIT_EXPORT_ERROR = ResponseItem.from(83_06_016, "流程追溯导出失败", "bmosLims");
    ResponseItem FLOW_AUDIT_START_USER_ERROR = ResponseItem.from(83_06_017, "根据用户id查询用户失败", "bmosLims");
    ResponseItem FLOW_HAVE_PUBLISHED = ResponseItem.from(83_06_018, "该流程模型已完成发布，不允许再次进行保存，请退出当前操作页面", "bmosLims");
    ResponseItem FLOW_DEPLOY_HAVE_PUBLISHED = ResponseItem.from(83_06_019, "该流程模型已完成发布，不允许再次进行发布，请退出当前操作页面", "bmosLims");
    ResponseItem PROCESS_NOT_FINISH = ResponseItem.from(83_06_020, "工序信息未配置完成", "bmosLims");
    ResponseItem PROCESS_STEP_NOT_FINISH = ResponseItem.from(83_06_021, "步骤信息未配置完成", "bmosLims");
    ResponseItem AUDIT_NO_PERMISSION = ResponseItem.from(83_06_022, "无数据权限", "bmosLims");
    ResponseItem PROCESS_TASK_NOT_FINISH = ResponseItem.from(83_06_023, "任务信息未配置完成", "bmosLims");
    ResponseItem FLOW_AUDIT_PROCESS_BIND_OTHER_AUDIT = ResponseItem.from(83_06_024, "工艺【{0}】已绑定【{1}】流程中，请解绑后再进行操作", "bmosLims");
    ResponseItem AUDIT_VERSION_EXISTS = ResponseItem.from(83_06_025, "流程版本已存在", "bmosLims");
    
    /**
     * ------------------------- 流程配置结束 ----------------------------
     */

    /**
     * ------------------------- 结论组件判定 ----------------------------
     */
    ResponseItem CONCLUSION_CONTEXT_MISSING = ResponseItem.from(83_15_001, "结论组件缺少上下文参数", "bmosLims");
    ResponseItem CONCLUSION_COMPONENT_INVALID = ResponseItem.from(83_15_002, "结论组件配置异常，缺少字段ID", "bmosLims");
    ResponseItem CONCLUSION_RESULT_NULL = ResponseItem.from(83_15_003, "结论判定结果为空", "bmosLims");
    ResponseItem CONCLUSION_MAPPING_EMPTY = ResponseItem.from(83_15_004, "结论组件未配置选项映射", "bmosLims");
    ResponseItem CONCLUSION_MAPPING_NOT_FOUND = ResponseItem.from(83_15_005, "未匹配到对应的结论选项: 期望值={0}", "bmosLims");

    /**
     * ------------------------- 班组 ----------------------------
     */
    ResponseItem TEAM_CODE_EXISTS = ResponseItem.from(83_04_01, "检验班组编码已存在", "bmosLims");
    ResponseItem INSPECTION_TEAM_NOT_EXISTS = ResponseItem.from(83_04_02, "检验班组不存在", "bmosLims");
    ResponseItem INSPECTION_TEAM_STATUS_UPDATED = ResponseItem.from(83_04_03, "检验班组启停状态已更新", "bmosLims");
    ResponseItem TEAM_BINDING_SCHEME_NOT_ALLOW_DISABLE = ResponseItem.from(83_04_04, "该班组已绑定方案【{0}】，不允许停用", "bmosLims");

    /**
     * ------------------------ 检验方案 ---------------------------
     */
    ResponseItem INSPECTION_SCHEME_NAME_EXISTS = ResponseItem.from(83_05_01, "检验方案名称已存在", "bmosLims");
    ResponseItem INSPECTION_SCHEME_NOT_EXIST = ResponseItem.from(83_05_02, "检验方案不存在", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_NOT_EXIST = ResponseItem.from(83_05_03, "检验版本不存在", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_EXISTS = ResponseItem.from(83_05_04, "检验方案版本号已存在", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_STATE_ERROR = ResponseItem.from(83_05_05, "检验方案版本状态错误,请重试", "bmosLims");
    ResponseItem INSPECTION_SCHEME_NO_VALID_VERSION = ResponseItem.from(83_05_06, "检验方案没有启用的版本", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_AUDIT_RUNNING = ResponseItem.from(83_05_07, "检验方案版本审批正在进行中", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_NOT_EDITABLE = ResponseItem.from(83_05_08, "检验方案版本不可启用", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_HAS_APPROVING = ResponseItem.from(83_05_09, "检验方案存在其他待审批版本", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_NOT_ACTIVE = ResponseItem.from(83_05_10, "检验方案版本不是激活状态", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_NOT_EDITING = ResponseItem.from(83_05_10, "检验方案版本不是编辑状态", "bmosLims");
    ResponseItem EXPRESSION_INVALID = ResponseItem.from(83_05_11, "表达式错误", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_NO_EXISTS = ResponseItem.from(83_05_12, "不存在该版本", "bmosLims");
    ResponseItem INSPECTION_SCHEME_DATA_POINT_EMPTY = ResponseItem.from(83_05_13, "数据点不能为空\n", "bmosLims");
    /**
     * 启用校验：方案中存在未配置班组的检验项目
     */
    ResponseItem INSPECTION_SCHEME_ITEM_TEAM_NOT_CONFIGURED = ResponseItem.from(83_05_13, "方案中存在未配置班组的检验项目，无法启用", "bmosLims");

    /**
     * 判定条件引用了已删除的数据点
     */
    ResponseItem JUDGMENT_REFERENCE_DATA_POINT_DELETED = ResponseItem.from(83_05_14, "判定条件引用了已删除的数据点", "bmosLims");
    /**
     * 判定条件引用的数据点类型已发生变更
     */
    ResponseItem JUDGMENT_REFERENCE_DATA_POINT_TYPE_CHANGED = ResponseItem.from(83_05_15, "判定条件引用的数据点【{0}】类型已变更，请检查", "bmosLims");
    /**
     * 判定条件引用的数据点与记录组件的绑定关系不存在
     */
    ResponseItem JUDGMENT_REFERENCE_DATA_POINT_BINDING_MISSING = ResponseItem.from(83_05_16, "判定条件引用的数据点【{0}】与记录组件的关联关系不存在", "bmosLims");
    /**
     * 判定条件引用的选项未在组件选项中配置
     */
    ResponseItem JUDGMENT_REFERENCE_DATA_POINT_OPTION_INVALID = ResponseItem.from(83_05_17, "判定条件引用的选项【{0}】未在记录组件中配置", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_NO_ITEM = ResponseItem.from(83_05_18, "方案版本中至少要有一个检验项目", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_ITEM_NO_PARAMETER = ResponseItem.from(83_05_19, "检验项目【{0}】中至少要有一个分析项", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_PARAMETER_MISSING_REQUIRED = ResponseItem.from(83_05_20, "检验项目【{0}】的分析项存在必填字段未填写", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_NO_SAMPLING = ResponseItem.from(83_05_21, "方案版本至少要有一条取样信息", "bmosLims");
    ResponseItem INSPECTION_SCHEME_CODE_EXIST = ResponseItem.from(83_05_22, "检验方案编码已存在", "bmosLims");
    ResponseItem INSPECTION_SCHEME_MATERIAL_EXIST = ResponseItem.from(83_05_23, "该检品已配置检验方案", "bmosLims");
    ResponseItem INSPECTION_SCHEME_HAS_ACTIVE_VERSION = ResponseItem.from(83_05_24, "方案存在生效版本，无法删除", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_STATUS_ERROR = ResponseItem.from(83_05_25, "版本状态错误", "bmosLims");
    ResponseItem INSPECTION_SCHEME_VERSION_ACTIVE_CANNOT_VOID = ResponseItem.from(83_05_26, "生效版本无法作废", "bmosLims");

    /**
     * ------------------------ 稳定性方案 ---------------------------
     */
    ResponseItem STABILITY_SCHEME_NAME_EXISTS = ResponseItem.from(83_30_01, "稳定性方案名称已存在", "bmosLims");
    ResponseItem STABILITY_SCHEME_NOT_EXIST = ResponseItem.from(83_30_02, "稳定性方案不存在", "bmosLims");
    ResponseItem STABILITY_SCHEME_VERSION_NOT_EXIST = ResponseItem.from(83_30_03, "稳定性方案版本不存在", "bmosLims");
    ResponseItem STABILITY_SCHEME_VERSION_EXISTS = ResponseItem.from(83_30_04, "稳定性方案版本号已存在", "bmosLims");
    ResponseItem STABILITY_SCHEME_CODE_EXIST = ResponseItem.from(83_30_05, "稳定性方案编码已存在", "bmosLims");
    ResponseItem STABILITY_SCHEME_MATERIAL_EXIST = ResponseItem.from(83_30_06, "该检品已配置稳定性方案", "bmosLims");
    ResponseItem STABILITY_SCHEME_HAS_ACTIVE_VERSION = ResponseItem.from(83_30_07, "方案存在生效版本，无法删除", "bmosLims");
    ResponseItem STABILITY_SCHEME_VERSION_STATUS_ERROR = ResponseItem.from(83_30_08, "版本状态错误", "bmosLims");
    ResponseItem STABILITY_SCHEME_VERSION_ACTIVE_CANNOT_VOID = ResponseItem.from(83_30_09, "生效版本无法作废", "bmosLims");
    ResponseItem STABILITY_SCHEME_ITEM_NOT_EXIST = ResponseItem.from(83_30_10, "稳定性方案检验项目配置不存在", "bmosLims");
    ResponseItem STABILITY_SCHEME_PARAMETER_NOT_EXIST = ResponseItem.from(83_30_11, "稳定性方案分析项配置不存在", "bmosLims");
    ResponseItem STABILITY_SCHEME_PLAN_NOT_EXIST = ResponseItem.from(83_30_12, "稳定性方案检验计划不存在", "bmosLims");
    ResponseItem STABILITY_SCHEME_PLAN_TIMEPOINT_NOT_EXIST = ResponseItem.from(83_30_13, "稳定性方案时间点不存在", "bmosLims");
    ResponseItem STABILITY_SCHEME_DATA_POINT_NOT_EXIST = ResponseItem.from(83_30_14, "稳定性方案数据点配置不存在", "bmosLims");
    ResponseItem STABILITY_SCHEME_JUDGMENT_NOT_EXIST = ResponseItem.from(83_30_15, "稳定性方案判定配置不存在", "bmosLims");
    ResponseItem STABILITY_SCHEME_VERSION_NO_ITEM = ResponseItem.from(83_30_16, "版本下未配置检验项目", "bmosLims");
    ResponseItem STABILITY_SCHEME_VERSION_ITEM_NO_PARAMETER = ResponseItem.from(83_30_17, "检验项目下未配置分析项", "bmosLims");
    ResponseItem STABILITY_SCHEME_DATA_POINT_EMPTY = ResponseItem.from(83_30_18, "数据点不能为空", "bmosLims");
    ResponseItem STABILITY_SCHEME_PLAN_TIMEPOINT_AMOUNT_INSUFFICIENT = ResponseItem.from(83_30_19, "检验计划时间点取样量合计不能小于总取样量", "bmosLims");
    ResponseItem STABILITY_SCHEME_PLAN_TIMEPOINT_AMOUNT_EXCEED = ResponseItem.from(83_30_20, "合计取样量不能小于各个时间点取样量之和", "bmosLims");

    /**
     * ------------------------ 稳定性考察计划 ---------------------------
     */
    ResponseItem STABILITY_INSPECT_PLAN_NOT_EXIST = ResponseItem.from(83_31_01, "稳定性考察计划不存在", "bmosLims");
    ResponseItem STABILITY_INSPECT_PLAN_CODE_EXIST = ResponseItem.from(83_31_02, "稳定性考察计划编号已存在", "bmosLims");
    ResponseItem STABILITY_INSPECT_PLAN_STATUS_ERROR = ResponseItem.from(83_31_03, "稳定性考察计划状态错误", "bmosLims");
    ResponseItem STABILITY_INSPECT_PLAN_CANNOT_PAUSE = ResponseItem.from(83_31_04, "当前状态无法暂停", "bmosLims");
    ResponseItem STABILITY_INSPECT_PLAN_CANNOT_RESUME = ResponseItem.from(83_31_05, "当前状态无法恢复", "bmosLims");
    ResponseItem STABILITY_INSPECT_PLAN_BATCH_NOT_EXIST = ResponseItem.from(83_31_06, "稳定性考察计划批次不存在", "bmosLims");
    ResponseItem STABILITY_INSPECT_PLAN_NO_ACTIVE_SCHEME_VERSION = ResponseItem.from(83_31_07, "稳定性方案无生效版本，无法创建考察计划", "bmosLims");
    ResponseItem STABILITY_INSPECT_PLAN_SCHEME_NO_PLAN = ResponseItem.from(83_31_08, "稳定性方案版本未配置检验计划", "bmosLims");
    ResponseItem STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST = ResponseItem.from(83_31_09, "稳定性考察计划样品不存在", "bmosLims");
    ResponseItem STABILITY_INSPECT_PLAN_SAMPLE_ALREADY_RECEIVED = ResponseItem.from(83_31_10, "样品已接收，无需重复操作", "bmosLims");
    ResponseItem STABILITY_INSPECT_PLAN_SAMPLE_ALREADY_SAMPLED = ResponseItem.from(83_31_11, "样品已取样，无需重复操作", "bmosLims");
    ResponseItem STABILITY_INSPECT_PLAN_SAMPLE_NOT_SAMPLED = ResponseItem.from(83_31_12, "样品尚未取样，无法接收", "bmosLims");
    ResponseItem STABILITY_SAMPLE_ALREADY_DESTROYED = ResponseItem.from(83_31_13, "样品已销毁，无法再次操作", "bmosLims");
    ResponseItem STABILITY_SAMPLE_STATUS_NOT_ALLOW_DESTROY = ResponseItem.from(83_31_14, "当前状态不允许销毁，仅待销毁状态可执行销毁操作", "bmosLims");
    ResponseItem STABILITY_INSPECT_PLAN_CANNOT_WITHDRAW = ResponseItem.from(83_31_15, "仅待开始状态的考察计划可撤销", "bmosLims");
    ResponseItem STABILITY_TIMEPOINT_TASK_NOT_EXIST = ResponseItem.from(83_31_16, "稳定性时间点任务不存在", "bmosLims");
    ResponseItem STABILITY_INSPECT_PLAN_SAMPLE_NOT_MANUAL = ResponseItem.from(83_31_17, "仅手动新增的样品可删除", "bmosLims");
    ResponseItem STABILITY_INSPECT_PLAN_SAMPLE_CANNOT_DELETE = ResponseItem.from(83_31_18, "仅待取样状态的样品可删除", "bmosLims");
    ResponseItem STABILITY_TIMEPOINT_ACTUAL_AMOUNT_EXCEED_PLAN = ResponseItem.from(83_31_19, "实际取样量不能大于计划取样量", "bmosLims");
    ResponseItem STABILITY_TIMEPOINT_ACTUAL_AMOUNT_EXCEED_SOURCE_REMAINING = ResponseItem.from(83_31_20, "实际取样量不能大于取样对象剩余样品数量", "bmosLims");
    ResponseItem STABILITY_OVERALL_ACTUAL_AMOUNT_LESS_THAN_PLAN = ResponseItem.from(83_31_21, "实际取样量不能小于计划取样量", "bmosLims");

    /**
     * ------------------------ 稳定性结果审核 ---------------------------
     */
    ResponseItem STABILITY_RESULT_AUDIT_PASSWORD_WRONG = ResponseItem.from(83_32_01, "密码验证失败，请重新输入", "bmosLims");
    ResponseItem STABILITY_RESULT_AUDIT_ORDER_STATUS_ERROR = ResponseItem.from(83_32_02, "检验单当前状态不允许该审核操作", "bmosLims");

    /**
     * ------------------------ 请验单配置 ---------------------------
     */
    ResponseItem INSPECTION_CONFIG_NAME_EXISTS = ResponseItem.from(83_06_01, "请验单名称已存在", "bmosLims");
    ResponseItem INSPECTION_CONFIG_DATA_CODE_EXIST = ResponseItem.from(83_06_02, "请验单数据重复", "bmosLims");
    ResponseItem INSPECTION_CONFIG_DATA_CANT_BE_EMPTY = ResponseItem.from(83_06_03, "请验单缺少请验单数据无法保存", "bmosLims");
    ResponseItem INSPECTION_CONFIG_NOT_EXISTS = ResponseItem.from(83_06_04, "请验单配置不存在", "bmosLims");
    ResponseItem INSPECTION_CONFIG_ENABLE_CANT_OPERATE = ResponseItem.from(83_06_05, "请验单配置已启用", "bmosLims");
    /**
     * 请验单配置被占用：已被请验单使用，禁止停用
     */
    ResponseItem INSPECTION_CONFIG_USED_CANT_DISABLE = ResponseItem.from(83_06_06, "该请验单配置已被请验单使用，禁止停用", "bmosLims");


    /**
     * -------------------------- fen xi xiang --------------------------
     *
     */
    ResponseItem PARAMETER_NOT_FOUND = ResponseItem.from(83_07_01, "分析项不存在", "bmosLims");
    ResponseItem PARAMETER_CODE_EXISTED = ResponseItem.from(83_07_02, "分析项编码已存在", "bmosLims");
    ResponseItem DATA_POINT_NAME_DUPLICATE = ResponseItem.from(83_07_03, "数据点名称在同一分析项中重复【{0}】", "bmosLims");

    /**
     * 分析项删除校验：被检验项目绑定
     */
    ResponseItem PARAMETER_BIND_INSPECT_ITEMS = ResponseItem.from(83_07_04, "分析项已被{0}绑定，无法删除", "bmosLims");

    /**
     * -------------------------- 请验 --------------------------
     */
    ResponseItem CONFIRM_ORDER_STATUS_ERROR = ResponseItem.from(83_18_01, "请验单状态错误，不允许确认", "bmosLims");
    ResponseItem RETENTION_EXPIRY_DATE_REQUIRED = ResponseItem.from(83_18_02, "选择留样时，有效期至不能为空", "bmosLims");
    ResponseItem RETENTION_SAMPLING_REQUIRED = ResponseItem.from(83_18_03, "选择留样时，必须添加留样的取样信息", "bmosLims");
    ResponseItem RETENTION_INSPECT_ITEM_REQUIRED = ResponseItem.from(83_18_04, "选择留样时，取样信息中必须包含留样检验项目", "bmosLims");
    ResponseItem RETENTION_INSPECT_ITEM_NOT_CONFIGURED = ResponseItem.from(83_18_05, "系统未配置留样检验项目，请联系管理员", "bmosLims");

    /**
     * -------------------------- 留样样品管理 --------------------------
     */
    ResponseItem RETENTION_SAMPLE_NOT_RETENTION = ResponseItem.from(83_18_06, "样品不是留样样品", "bmosLims");
    ResponseItem RETENTION_SAMPLE_NOT_RECEIVED_CANNOT_EXTEND = ResponseItem.from(83_18_07, "样品未接收，无法延期", "bmosLims");
    ResponseItem RETENTION_SAMPLE_DESTROYED_CANNOT_EXTEND = ResponseItem.from(83_18_08, "样品已销毁，无法延期", "bmosLims");
    ResponseItem RETENTION_SAMPLE_NOT_RECEIVED_CANNOT_COLLECT = ResponseItem.from(83_18_09, "样品未接收，无法领用", "bmosLims");
    ResponseItem RETENTION_SAMPLE_DESTROYED_CANNOT_COLLECT = ResponseItem.from(83_18_10, "样品已销毁，无法领用", "bmosLims");
    ResponseItem RETENTION_SAMPLE_NOT_RECEIVED_CANNOT_DESTROY = ResponseItem.from(83_18_11, "样品未接收，无法销毁", "bmosLims");
    ResponseItem RETENTION_SAMPLE_ALREADY_DESTROYED = ResponseItem.from(83_18_12, "样品已销毁，无法重复销毁", "bmosLims");
    ResponseItem RETENTION_EXPIRY_MUST_AFTER_OLD = ResponseItem.from(83_18_13, "新的留样期限必须大于原留样期限", "bmosLims");
    ResponseItem RETENTION_COLLECT_QUANTITY_EXCEED = ResponseItem.from(83_18_14, "领用数量不能大于当前样品数量", "bmosLims");
    ResponseItem RETENTION_SAMPLE_IDS_REQUIRED = ResponseItem.from(83_18_15, "样品ID列表不能为空", "bmosLims");
    ResponseItem RETENTION_SAMPLE_ID_REQUIRED = ResponseItem.from(83_18_16, "样品ID不能为空", "bmosLims");

    /**
     * -------------------------- 留样观察 --------------------------
     */
    ResponseItem RETENTION_OBSERVATION_TASK_ID_REQUIRED = ResponseItem.from(83_18_17, "任务ID不能为空", "bmosLims");
    ResponseItem RETENTION_OBSERVATION_TASK_NOT_EXIST = ResponseItem.from(83_18_18, "任务不存在", "bmosLims");
    ResponseItem RETENTION_OBSERVATION_TASK_ALREADY_COMPLETED = ResponseItem.from(83_18_19, "该任务已完成，不能重复提交", "bmosLims");
    ResponseItem RETENTION_OBSERVATION_EARLIER_TASK_UNCOMPLETED = ResponseItem.from(83_18_20, "存在更早日期的留样观察任务未完成，请按照顺序完成", "bmosLims");
    ResponseItem RETENTION_OBSERVATION_BATCH_SUBMIT_PARTIAL_FAILED = ResponseItem.from(83_18_21, "批量提交完成，成功{0}个，失败{1}个。失败详情：{2}", "bmosLims");
    ResponseItem RETENTION_LEDGER_EXPORT_ERROR = ResponseItem.from(83_18_22, "留样台账导出失败", "bmosLims");

    /**
     * -------------------------- 取样登记 --------------------------
     *
     */
    ResponseItem INVALID_PARAMETER_ORDER_NOT_EXITS = ResponseItem.from(83_08_01, "请验单不存在", "bmosLims");
    ResponseItem INVALID_PARAMETER_SCHEME_VERSION_NOT_EXITS = ResponseItem.from(83_08_02, "方案版本不存在", "bmosLims");

    /**
     * ---------------------------取样-----------------------------
     */

    ResponseItem INVALID_SAMPLE_NOT_EXITS = ResponseItem.from(83_09_01, "样品不存在【{0}】", "bmosLims");
    ResponseItem INVALID_SAMPLE_QUANTITY_NOT_ENOUGH = ResponseItem.from(83_09_03, "样品【{0}】取样量为空", "bmosLims");
    ResponseItem INVALID_SAMPLE_ALREADY_SAMPLED = ResponseItem.from(83_09_04, "样品【{0}】已取样", "bmosLims");

    /**
     * ---------------------------接收-----------------------------
     */
    ResponseItem RECEIVE_ERROR_SAMPLE_NOT_SAMPLED = ResponseItem.from(83_10_01, "样品【{0}】尚未取样，无法接收", "bmosLims");
    ResponseItem RECEIVE_ERROR_SAMPLE_ALREADY_RECEIVED = ResponseItem.from(83_10_02, "样品【{0}】已接收，请勿重复接收", "bmosLims");
    /**
     * ---------------------------分样-----------------------------
     */
    ResponseItem DIVIDE_ERROR_SAMPLE_NOT_RECEIVED = ResponseItem.from(83_11_01, "样品【{0}】尚未接收，无法分样", "bmosLims");
    ResponseItem DIVIDE_ERROR_SAMPLE_ALREADY_DISCARD = ResponseItem.from(83_11_03, "样品【{0}】已经作废，无法分样", "bmosLims");
    ResponseItem DIVIDE_ERROR_SAMPLE_ALREADY_DIVIDED = ResponseItem.from(83_11_02, "样品【{0}】已分样，请勿重复分样", "bmosLims");
    ResponseItem DIVIDE_ERROR_SAMPLE_DIVISION_QUANTITY_ERROR = ResponseItem.from(83_11_03, "经过换算后样品【{0}】分样总数量【{1}】不等于原始数量【{2}】", "bmosLims");
    ResponseItem DIVIDED_ERROR_DATA_EMPTY = ResponseItem.from(83_11_04, "分样数据为空", "bmosLims");
    ResponseItem DIVIDE_ERROR_RESULT_EMPTY = ResponseItem.from(83_11_05, "分样结果为空", "bmosLims");
    ResponseItem DIVIDE_ERROR_RESULT_QUANTITY_ERROR = ResponseItem.from(83_11_06, "分样数量必须大于0", "bmosLims");
    ResponseItem DIVIDE_ERROR_RESULT_UNIT_ERROR = ResponseItem.from(83_11_07, "分样单位不能为空", "bmosLims");
    ResponseItem DIVIDE_ERROR_SAMPLE_NOT_SAMPLED = ResponseItem.from(83_11_08, "样品【{0}】尚未取样，无法分样", "bmosLims");
    ResponseItem DIVIDE_ERROR_SAMPLE_DIVISION_QUANTITY_EXCEED = ResponseItem.from(83_11_09, "经过换算后样品【{0}】分样总数量【{1}】超过原始数量【{2}】", "bmosLims");

    /**
     * ---------------------------领样-----------------------------
     */

    ResponseItem COLLECT_ERROR_SAMPLE_NOT_RECEIVED= ResponseItem.from(83_12_01, "样品【{0}】尚未接收，无法领取", "bmosLims");
    ResponseItem COLLECT_ERROR_SAMPLE_ALREADY_COLLECTED = ResponseItem.from(83_12_02, "样品【{0}】已领取，请勿重复领取", "bmosLims");
    ResponseItem COLLECT_ERROR_SAMPLE_DISCARDED = ResponseItem.from(83_12_03, "样品【{0}】已作废，无法领取", "bmosLims");
    ResponseItem COLLECT_ERROR_SAMPLE_NOT_SAMPLED = ResponseItem.from(83_12_04, "样品【{0}】尚未取样，无法领取", "bmosLims");
    /**
     * ---------------------------任务分配-----------------------------
     */
    ResponseItem PERMISSION_DENIED = ResponseItem.from(83_13_01, "您没有权限操作部分任务，请检查任务是否属于您所在的检验班组", "bmosLims");
    ResponseItem TASK_ERROR_STATUS_NOT_MATCH = ResponseItem.from(83_13_02, "任务状态不是待分配，无法分配", "bmosLims");
    ResponseItem TASK_ERROR_STATUS_NOT_MATCH_CLAIM= ResponseItem.from(83_13_02, "任务状态不是待分配，无法领取", "bmosLims");
    ResponseItem TASK_ERROR_ID_EMPTY = ResponseItem.from(83_13_03, "任务ID不能为空", "bmosLims");
    ResponseItem TASK_ERROR_STATUS_NOT_MATCH_RETURN = ResponseItem.from(83_13_04, "任务状态不是待完成，无法退回", "bmosLims");
    ResponseItem TASK_ERROR_OWNER_NOT_MATCH_RETURN = ResponseItem.from(83_13_05, "只有任务所有人才能退回任务", "bmosLims");
    ResponseItem TASK_ERROR_APPROVE_STATUS_NOT_MATCH = ResponseItem.from(83_13_06, "任务状态不是退回待审批，无法审批", "bmosLims");
    ResponseItem TASK_ERROR_STATUS_NOT_MATCH_TERMINATE = ResponseItem.from(83_13_07, "已终止或已完成，无法终止", "bmosLims");
    ResponseItem TASK_STATUS_CHANGE_ERROR = ResponseItem.from(83_13_08, "任务状态转换错误:【{0}】->【{1}】", "bmosLims");
    /**
     * ---------------------------数据录入-----------------------------
     */
    ResponseItem DATA_ENTRY_TASK_NOT_FOUND = ResponseItem.from(83_14_01, "任务不存在", "bmosLims");
    ResponseItem DATA_ENTRY_ORDER_NOT_FOUND = ResponseItem.from(83_14_02, "检验单不存在", "bmosLims");
    ResponseItem DATA_ENTRY_DATAPOINT_NOT_FOUND = ResponseItem.from(83_14_03, "数据点不存在", "bmosLims");
    ResponseItem DATA_ENTRY_DATA_NOT_EMPTY = ResponseItem.from(83_14_04, "录入数据不能为空", "bmosLims");
    ResponseItem DATA_ENTRY_TASK_EMPTY = ResponseItem.from(83_14_05, "任务不能为空", "bmosLims");
    ResponseItem DATA_ENTRY_DATAPOINT_TYPE_EMPTY = ResponseItem.from(83_14_06, "数据点类型不能为空", "bmosLims");
    ResponseItem DATA_ENTRY_DATAPOINT_DATAPOINT_NAME_EMPTY = ResponseItem.from(83_14_07, "数据点名称不能为空", "bmosLims");
    ResponseItem DATA_ENTRY_TASK_STATUS_NOT_MATCH = ResponseItem.from(83_14_08, "任务状态错误，无法录入", "bmosLims");
    ResponseItem DATA_ENTRY_ORDER_STATUS_NOT_MATCH = ResponseItem.from(83_14_09, "检验单状态错误，无法录入", "bmosLims");
    ResponseItem REVIEW_DATA_ERROR = ResponseItem.from(83_14_10, "存在错误数据，不允许复核通过", "bmosLims");
    ResponseItem REVIEW_OPERATOR_ERROR = ResponseItem.from(83_14_11, "录入人不得复核本人录入的数据", "bmosLims");
    ResponseItem REVIEW_TASK_STATUS_ERROR = ResponseItem.from(83_14_12, "任务状态不是待复核，无法复核", "bmosLims");
    ResponseItem PERMISSION_DENIED_AUTO_JUDGMENT = ResponseItem.from(83_14_13, "已配置判定规则，禁止人工录入判定结论", "bmosLims");
    /**
     * APP-ELN 相关
     */
    ResponseItem APP_ELN_TASK_ONLY_ELN = ResponseItem.from(83_14_14, "仅ELN任务支持完成操作", "bmosLims");
    ResponseItem APP_ELN_SIGN_REQUIRED = ResponseItem.from(83_14_15, "请先完成签名", "bmosLims");
    ResponseItem APP_ELN_TASK_STATUS_NOT_ALLOWED = ResponseItem.from(83_14_16, "任务状态不允许完成", "bmosLims");
    ResponseItem APP_ELN_REVIEW_SIGN_REQUIRED = ResponseItem.from(83_14_17, "请先完成复核签名", "bmosLims");


    /**
     * ---------------------------样品审核-----------------------------
     */
    ResponseItem SAMPLE_AUDIT_START_ERROR = ResponseItem.from(83_15_01, "样品审核流程发起失败", "bmosLims");

    /**
     * ---------------------------报告模板-----------------------------
     *
     */

    ResponseItem REPORT_TEMPLATE_VERSION_EXISTED = ResponseItem.from(83_16_01, "报告模板版本已存在", "bmosLims");

    ResponseItem REPORT_TEMPLATE_NAME_EXISTS = ResponseItem.from(83_16_02, "报告模板名称不能重复", "bmosLims");

    /**
     * ---------------------------回收-----------------------------
     */
    ResponseItem RECYCLE_QUANTITY_EXCEED_SAMPLE_QUANTITY = ResponseItem.from(83_17_01, "回收余量不允许大于样品数量", "bmosLims");

    /**
     * ---------------------------编码规则-----------------------------
     */
    ResponseItem CODERULE_ERROR = ResponseItem.from(83_18_01, "编码规则错误: 【{0}】", "bmosLims");
    /**
     * ---------------------------记录-----------------------------
     */
    ResponseItem EXPRESS_EXISTS_DUPLICATION = ResponseItem.from(83_19_001, "公式配置存在重复元素", "bmosLims");
    ResponseItem EXPRESS_EXISTS_CYCLE = ResponseItem.from(83_19_002, "公式配置存在环状节点", "bmosLims");
    ResponseItem COMPONENT_NOT_EXIST =  ResponseItem.from(82_19_003, "组件不存在", "bmosLims");
    ResponseItem RECORD_CATEGORY_NAME_ERROR = ResponseItem.from(82_19_004, "分类名称已存在", "bmosLims");
    ResponseItem RECORD_CATEGORY_UPDATE_ERROR = ResponseItem.from(82_19_005, "分类更新失败", "bmosLims");
    ResponseItem RECORD_BOUND_CATEGORY = ResponseItem.from(82_19_006, "分类存在子集，不允许删除", "bmosLims");
    ResponseItem RECORD_PRODUCTION_ID_ERROR = ResponseItem.from(82_19_007, "批量生成id失败", "bmosLims");
    ResponseItem RECORD_ITEM_NOT_EXIST = ResponseItem.from(82_19_008, "记录项不存在", "bmosLims");
    ResponseItem FILE_DOWNLOAD_FAILED = ResponseItem.from(82_19_010, "文件下载失败", "bmosLims");
    ResponseItem FILE_ANALYSIS_FILE_TYPE_ERROR = ResponseItem.from(82_19_011, "文件类型错误", "bmosLims");
    ResponseItem RECORD_FILE_UPLOAD_ERROR = ResponseItem.from(82_19_012, "文件上传失败", "bmosLims");
    ResponseItem FILE_ANALYSIS_ERROR = ResponseItem.from(82_19_013, "文件解析失败", "bmosLims");
    ResponseItem RECORD_ITEM_UPDATE_ERROR = ResponseItem.from(82_19_014, "记录项更新失败", "bmosLims");
    ResponseItem PARAMETER_QUERY_NOT_EXISTS = ResponseItem.from(82_19_015, "参数不存在", "bmosLims");
    ResponseItem RECORD_VERSION_SAVE_ERROR = ResponseItem.from(82_19_016, "记录版本号已存在", "bmosLims");
    ResponseItem RECORD_STATE_ABNORMAL = ResponseItem.from(82_19_017, "记录存在未确认的版本", "bmosLims");
    ResponseItem RECORD_COPY_ERROR = ResponseItem.from(82_19_018, "记录复制失败", "bmosLims");
    ResponseItem EDIT_STATUS_CAN_AUDIT = ResponseItem.from(82_19_019, "编辑状态方可提交审批", "bmosLims");
    ResponseItem RECORD_VERSION_NOT_EXIST = ResponseItem.from(82_19_020, "记录版本不存在", "bmosLims");
    ResponseItem RECORD_SORT_CHANGE_ERROR = ResponseItem.from(82_19_021, "存在记录项无顺序配置", "bmosLims");
    ResponseItem TIME_DIFF_FORMULA_PARAM_HAS_NO_TIMESTAMP = ResponseItem.from(82_19_022, "时间差公式参数无拓展时间戳信息", "bmosLims");
    ResponseItem CODE_RULE_TYPE_ERROR = ResponseItem.from(82_19_023, "类型传输错误", "bmosLims");
    ResponseItem RECORD_CODE_DUPLICATE = ResponseItem.from(82_19_024, "方法编码重复，请重新输入", "bmosLims");
    ResponseItem RECORD_VERSION_AUDIT_RUNNING = ResponseItem.from(82_19_025, "存在审批中的版本，不允许发起审批", "bmosLims");
    /**
     * ---------------------------报告生成-----------------------------
     */
    ResponseItem REPORT_START_ORDER_STATUS_ERROR = ResponseItem.from(83_20_01, "请验单的状态不是已完成或已终止", "bmosLims");
    ResponseItem REPORT_NOT_GENERATED = ResponseItem.from(83_20_02, "报告尚未生成成功，无法确认", "bmosLims");
    ResponseItem REPORT_CONFIRM_STATUS_ERROR = ResponseItem.from(83_20_03, "报告当前状态不支持确认操作", "bmosLims");


    /**
     * ---------------------------请验单状态-----------------------------
     */

    ResponseItem ORDER_STATUS_CHANGE_ERROR = ResponseItem.from(83_21_01, "请验单状态转换错误:【{0}】->【{1}】", "bmosLims");

    /**
     * ---------------------------附件-----------------------------
     */
    ResponseItem ATTACHMENT_NOT_EXIST = ResponseItem.from(83_22_01, "附件不存在", "bmosLims");
    /**
     * ---------------------------操作规程-----------------------------
     */
    ResponseItem OPERATE_UPLOAD_ERROR = ResponseItem.from(83_23_01, "文件上传错误", "bmosLims");
    ResponseItem CATEGORY_PARENT_ERROR = ResponseItem.from(83_23_02, "分类下存在子级，无法删除", "bmosLims");
    ResponseItem CATEGORY_OPERATE_ERROR = ResponseItem.from(83_23_03, "分类下存在文件信息，无法删除", "bmosLims");
    ResponseItem CATEGORY_NAME_ERROR = ResponseItem.from(83_23_04, "分类名称已存在", "bmosLims");
    ResponseItem OPERATE_CODE_ERROR = ResponseItem.from(83_23_05, "文件编号已存在", "bmosLims");
    ResponseItem OPERATE_VERSION_ERROR = ResponseItem.from(83_23_06, "文件版本已存在", "bmosLims");
    ResponseItem OPERATE_VERSION_FLOW_ERROR = ResponseItem.from(83_23_07, "版本启用或停用失败", "bmosLims");
    ResponseItem OPERATE_UPDATE_VALID_ERROR = ResponseItem.from(83_23_08, "版本非确认或编辑状态，无法直接生效", "bmosLims");
    ResponseItem OPERATE_VERSION_FLOW_START_ERROR = ResponseItem.from(83_23_09, "无法发起启用审批，已存在审核中版本", "bmosLims");


    /**
     * ---------------------------数据录入-----------------------------
     */

    ResponseItem PROCEDURE_EXPRESS_LOCKED = ResponseItem.from(82_24_001, "网络波动，请稍后重试", "bmosLims");

    ResponseItem EXECUTE_DATA_EXIST = ResponseItem.from(82_24_002, "记录数据已存在", "bmosLims");

    ResponseItem CALCULATE_RESULT_TOO_LONG_FOR_COLUMN = ResponseItem.from(82_24_003, "计算结果过长,无法保存", "bmosLims");


    ResponseItem ATTACHMENT_FILE_ERROR = ResponseItem.from(82_24_004, "拍照上传失败", "bmosLims");

    /**
     * ---------------------------ELN-方法配置-----------------------------
     */
    ResponseItem SCHEME_PARAMETER_NOT_EXIST = ResponseItem.from(82_25_001, "方案不存在", "bmosLims");
    ResponseItem PARAMETER_ELN_MISSING_PARAMETER = ResponseItem.from(82_25_002, "ELN执行方式需要提供记录ID、记录版本ID、记录编码", "bmosLims");
    ResponseItem METHOD_ALREADY_BOUND = ResponseItem.from(82_25_003, "方法【{0}】已绑定到分析项", "bmosLims");
    ResponseItem METHOD_ITEM_COUNT_ERROR = ResponseItem.from(82_25_004,"记录版本配置异常：存在多个记录项","bmosLims");
    ResponseItem METHOD_NO_VALID_VERSION = ResponseItem.from(82_25_005, "方法【{0}】没有启用的版本", "bmosLims");


    /**
     * ---------------------------ELN-签名-----------------------------
     */

    ResponseItem USER_SIGN_UPLOAD_FAIL = ResponseItem.from(82_26_001, "用户签名上传失败", "bmosLims");
    ResponseItem USER_SIGN_SAVE_FAIL = ResponseItem.from(82_26_002, "用户签名保存失败", "bmosLims");
    ResponseItem USER_HANDLE_SIGN_ERROR = ResponseItem.from(82_26_003, "用户手写签名组件回填值失败", "bmosLims");
    ResponseItem USER_SIGN_NOT_EXIST = ResponseItem.from(82_26_004, "当前用户手写签名不存在", "bmosLims");


    /**
     * ---------------------------ELN-时间格式-----------------------------
     */
    ResponseItem PARAM_TIME_FORMAT_NOT_EXIST = ResponseItem.from(82_27_001, "日期格式参数配置不存在", "bmosLims");
    ResponseItem TIME_BUSINESS_COMPONENT_HAS_NO_TIMESTAMP = ResponseItem.from(82_27_002, "组件【{0}】无拓展时间戳信息", "bmosLims");
    ResponseItem DATA_POINT_NOT_BIND_RECORD_COMPONENT = ResponseItem.from(82_27_003, "方案启用校验失败：{0}-{1} 为 ELN 执行方式，存在未绑定记录组件的数据点【{2}】", "bmosLims");
}
