import { BMStateTagEnum } from '@bmos/components';

// 启用状态枚举
export enum EnableStatusMap {
  OFF = '0', // 停用
  ON = '1', // 启用
}

// 检验进程枚举
export enum InspectionProcessMap {
  SAMPLE_RECEIVING, // 标本待接收
  INSPECTION_PENDING, // 检验待执行
  INSPECTION_PENDED, // 检验执行中
  DATA_ISSUING, // 数据待签发
  REPORT_ISSUING, // 报告待签发
  REPORT_ISSUED, // 报告已签发
}

/**
 *  @description: 操作状态枚举 编辑 新增
 */
export enum OperationStatusMap {
  EDIT, // 编辑
  ADD, // 新增
}

/**
 * @description: 是否枚举
 */
export enum yesOrNoEnum {
  // 是
  YES = 'TRUE',
  // 否
  NO = 'FALSE',
}

// BMStateTag状态枚举
export enum StatusType {
  // 正常
  NORMAL = BMStateTagEnum.PRIMARY,
  // 拒收/退回
  REJECT = BMStateTagEnum.DANGER,
  // 超期
  OVERTIME = BMStateTagEnum.DANGER,
  // 待接收
  RECEIVING = BMStateTagEnum.WARNING,
  // 已接收
  RECEIVED = BMStateTagEnum.SUCCESS,
  // 接收待审核
  RECEIVE_AUDITING = BMStateTagEnum.PRIMARY,
  // 拒收待审核
  REJECTING = BMStateTagEnum.PRIMARY,
  // 已拒收
  REJECTED = BMStateTagEnum.DANGER,
  // 待审核
  AUDITING = BMStateTagEnum.PRIMARY,
  // 通过
  APPROVE = BMStateTagEnum.SUCCESS,
  // 签发通过
  RESULT_PASS = BMStateTagEnum.SUCCESS,
  // 签发退回
  RESULT_BACK = BMStateTagEnum.DANGER,
  // 不予放行
  NO_PASS = BMStateTagEnum.DANGER,
  // 准予放行
  PASS = BMStateTagEnum.SUCCESS,
}

// 物料接收结果枚举
export enum MaterialReceiveResultEnum {
  // 验收合格
  PASS = 'PASS',
  // 验收不合格
  NO_PASS = 'NO_PASS',
}

/**
 * @description: 物料仓库区域对应颜色
 */
export enum MaterialWarehouseAreaEnum {
  // 待检区
  WAITING = '#F69936',
  // 合格区
  PASS = '#59BF78',
  // 不合格区
  NOPASS = '#FF0000',
}

/**
 * @description: 使用类别对应颜色
 */
export const UseCategoryEnum = {
  // 报废
  SCRAP: 'red',
  // 退货
  RETURN: 'red',
  // 领用
  RECEIVE: '#59BF78',
};

/**
 * @description: 物料库存操作类型枚举
 */
export enum MaterialModelTypeEnum {
  RECEIVE, // 领用
  SCRAP, // 报废
  SPOT_CHECK, // 抽检
  RETURN, // 退货
}

/**
 * @description: 预警类型枚举
 */
export enum WarningTypeEnum {
  // 物料到期预警
  MATERIAL_EXPIRY_WARNING,
  // 物料最低库存预警
  MATERIAL_LOW_STOCK_WARNING,
  // 供应商到期预警
  SUPPLIER_EXPIRY_WARNING,
}

/**
 * @description: 抽检状态枚举
 */
export enum SpotCheckStatusEnum {
  // 待提交
  WAIT_SUBMIT = 'WAIT_SUBMIT',
  // 已提交
  SUBMITED = 'SUBMITED',
}

/**
 * @description: 放行结果枚举
 */
export enum PassResultEnum {
  PASS = 'PASS', // 准予放行
  NO_PASS = 'NO_PASS', // 不予放行
}

/**
 * @description: 领用库审核类型枚举
 */
export enum RecordSourceAuditTypeEnum {
  /**
   * @description: 物料消耗
   */
  OUT_CONSUME = 'OUT_CONSUME',
  /**
   * @description: 物料报废
   */
  OUT_SCRAP = 'OUT_SCRAP',
}

// 是 否 枚举
export enum YesOrNoEnum {
  NO = 0,
  YES = 1,
}

// 通过标准类型 数值型 文本型 枚举
export enum PassStandardTypeEnum {
  NUMBER = 'NUMBER',
  TEXT = 'TEXT',
}

// 配置的 noType 类型 枚举 静态数据新增STATIC_DATA_NO 领用库新增RECEIVE_STORE_CODE
export enum NoTypeEnum {
  STATIC_DATA_NO = 'STATIC_DATA_NO',
  RECEIVE_STORE_CODE = 'RECEIVE_STORE_CODE',
}

// RR001: NONE  RR002: HLJZ/2-09-006 修约规则枚举
export enum RoundingRuleEnum {
  RR001 = 'NONE',
  RR002 = 'HLJZ/2-09-006',
}

// 10 MB 文件大小
export const TENMB = 10 * 1024 * 1024;

// 检验项目枚举
export enum InspectionProjectEnum {
  // 蛋白质含量
  ProteinContent = 'IP001',
  // ALT
  ALT = 'IP002',
  // HBsAg
  HBsAg = 'IP003',
  // 抗-HCV
  AntiHCV = 'IP004',
  // HIV-Ag/Ab
  HIVAgAb = 'IP005',
  // 抗-TP
  AntiTP = 'IP006',
  // 蛋白电泳
  ProteinElectrophoresis = 'IP010',
}
// 检验状态
export enum InspectionStatusEnum {
  // 待检验
  TO_INSPECT = 'TO_INSPECT',
  // 检验中
  INSPECTING = 'INSPECTING',
  // 已完成
  PUBLISHED = 'PUBLISHED',
}

export enum ProjectTypeEnum {
  // 固定项目
  FIXED_TIEM = 'FIXED_TIEM',
  // 特殊项目
  SPECIAL_TIEM = 'SPECIAL_TIEM',
}

// 检验结果:QUALIFIED合格/UNQUALIFIED不合格
export enum InspectionResultEnum {
  // 合格
  QUALIFIED = 'QUALIFIED',
  // 不合格
  UNQUALIFIED = 'UNQUALIFIED',
}

export enum SpecimenTypeEnum {
  // 血浆标本
  PLASMA_SPECIMEN = 'PLASMA_SPECIMEN',
  // 血清标本
  SERUM_SPECIMEN = 'SERUM_SPECIMEN',
}

/**
 * @description: 审核通过 退回 颜色
 */
export enum AuditStatusEnum {
  // 审核通过
  APPROVE = '#59BF78',
  // 退回
  REJECT = '#FF0000',
}

// 发布状态
export enum PublishStatusEnum {
  // 待检验
  TO_INSPECT = 'TO_INSPECT',
  // 待发布
  TO_PUBLISH = 'TO_PUBLISH',
  // 待审核
  TO_AUDIT = 'TO_AUDIT',
  // 审核通过
  PUBLISHED = 'PUBLISHED',
}

// 检验次数：INITIAL_INSPECT初检/RE_INSPECT复检
export enum InspectionCountEnum {
  // 初检
  INITIAL_INSPECT = 'INITIAL_INSPECT',
  // 复检
  RE_INSPECT = 'RE_INSPECT',
}

// 物料类型 CORE_MATERIAL NORMAL_MATERIAL
export enum MaterialTypeEnum {
  // 关键物料
  CORE_MATERIAL = 'CORE_MATERIAL',
  // 普通物料
  NORMAL_MATERIAL = 'NORMAL_MATERIAL',
}

export enum ChartTypeEnum {
  // 当日送检 批次
  INSPECT_SUBMISSION_BATCH_DAY = 'INSPECT_SUBMISSION_BATCH_DAY',
  // 当日接收 批次
  RECEIVED_BATCH_DAY = 'RECEIVED_BATCH_DAY',
  // 当日待检 份
  TO_SINGLE_BATCH_DAY = 'TO_SINGLE_BATCH_DAY',
  // 待接收 批次
  TO_RECEIVE_BATCH_DAY = 'TO_RECEIVE_BATCH_DAY',
  // 数据待签发 批次
  TO_PUBLISH_DATA_BATCH_DAY = 'TO_PUBLISH_DATA_BATCH_DAY',
  // 报告待签发 批次
  TO_PUBLISH_REPORT_BATCH_DAY = 'TO_PUBLISH_REPORT_BATCH_DAY',
  // 当日送检 份
  INSPECT_SUBMISSION_SINGLE_DAY = 'INSPECT_SUBMISSION_SINGLE_DAY',
  // 当日接收 份
  RECEIVED_SINGLE_DAY = 'RECEIVED_SINGLE_DAY',
  // 当日已检份
  INSPECTED_SINGLE_DAY = 'INSPECTED_SINGLE_DAY',
  // 待接收 份
  TO_RECEIVE_SINGLE_DAY = 'TO_RECEIVE_SINGLE_DAY',
  // 数据待签发 份
  TO_PUBLISH_DATA_SINGLE_DAY = 'TO_PUBLISH_DATA_SINGLE_DAY',
  // 业务待审核
  TO_AUDIT_SINGLE_DAY = 'TO_AUDIT_SINGLE_DAY',
  // 检验趋势 周
  INSPECTED_SINGLE_WEEK = 'INSPECTED_SINGLE_WEEK',
  // 检验趋势 月
  INSPECTED_SINGLE_MONTH = 'INSPECTED_SINGLE_MONTH',
  // 检验趋势 年
  INSPECTED_SINGLE_YEAR = 'INSPECTED_SINGLE_YEAR',
  // 请验统计 周
  INSPECT_SUBMISSION_BATCH_WEEK = 'INSPECT_SUBMISSION_BATCH_WEEK',
  // 请验统计 月
  INSPECT_SUBMISSION_BATCH_MONTH = 'INSPECT_SUBMISSION_BATCH_MONTH',
  // 请验统计 年
  INSPECT_SUBMISSION_BATCH_YEAR = 'INSPECT_SUBMISSION_BATCH_YEAR',
  // 物料统计
  WAREHOUSE_STATISTICS = 'WAREHOUSE_STATISTICS',
  // 待检总数 批 日
  TO_INSPECT_BATCH_DAY = 'TO_INSPECT_BATCH_DAY',
  // 已检总数 批 日
  INSPECTED_BATCH_DAY = 'INSPECTED_BATCH_DAY',
  // 待检数 份 日
  TO_INSPECT_SINGLE_DAY = 'TO_INSPECT_SINGLE_DAY',
  // 检验中 份 日
  INSPECTING_SINGLE_DAY = 'INSPECTING_SINGLE_DAY',
  // 待检总数 批 月
  TO_INSPECT_BATCH_MONTH = 'TO_INSPECT_BATCH_MONTH',
  // 已检总数 批 月
  INSPECTED_BATCH_MONTH = 'INSPECTED_BATCH_MONTH',
  // 待检数 份 月
  TO_INSPECT_SINGLE_MONTH = 'TO_INSPECT_SINGLE_MONTH',
  // 检验中 份 月
  INSPECTING_SINGLE_MONTH = 'INSPECTING_SINGLE_MONTH',
  // 已检验 份 月
  INSPECTED_SINGLE_WHOLE_MONTH = 'INSPECTED_SINGLE_WHOLE_MONTH',
  // 待检总数 批 年
  TO_INSPECT_BATCH_YEAR = 'TO_INSPECT_BATCH_YEAR',
  // 已检总数 批 年
  INSPECTED_BATCH_YEAR = 'INSPECTED_BATCH_YEAR',
  // 待检数 份 年
  TO_INSPECT_SINGLE_YEAR = 'TO_INSPECT_SINGLE_YEAR',
  // 检验中 份 年
  INSPECTING_SINGLE_YEAR = 'INSPECTING_SINGLE_YEAR',
  // 已检验 份 年
  INSPECTED_SINGLE_WHOLE_YEAR = 'INSPECTED_SINGLE_WHOLE_YEAR',
}
