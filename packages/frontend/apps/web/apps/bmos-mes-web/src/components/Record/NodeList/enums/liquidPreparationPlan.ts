import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE, CUSTOM_FIELD_BUTTON } from '../enum';

//配液计划-物料批次子节点
export const LIQUID_PREPARATION_PLAN_BATCH_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  LIQUID_PREPARATION_PLAN_BATCH_NAME: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_PLAN_BATCH_CODE: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_PLAN_BATCH_SPECIFICATION: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_PLAN_BATCH_BATCHNO: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH_BATCHNO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 批次配液量
  LIQUID_PREPARATION_PLAN_BATCH_QUANTITY: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH_QUANTITY',
    componentName: t('批次配液量'),
    node_type: '',
    icon: 'NUMBER',
  },
  //单位
  LIQUID_PREPARATION_PLAN_BATCH_UNIT: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 有效期至
  LIQUID_PREPARATION_PLAN_BATCH_EXPIRY_DATE: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH_EXPIRY_DATE',
    componentName: t('有效期至'),
    node_type: '',
    icon: 'DATE',
  },
  // 供应商
  LIQUID_PREPARATION_PLAN_BATCH_SUPPLIER: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH_SUPPLIER',
    componentName: t('供应商'),
    node_type: '',
    icon: 'TEXT',
  },
  // 生产商
  LIQUID_PREPARATION_PLAN_BATCH_MANUFACTURER: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH_MANUFACTURER',
    componentName: t('生产商'),
    node_type: '',
    icon: 'TEXT',
  },
  // 原厂批号
  LIQUID_PREPARATION_PLAN_BATCH_ORIGINAL_BATCHNO: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH_ORIGINAL_BATCHNO',
    componentName: t('原厂批号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 原始编码
  LIQUID_PREPARATION_PLAN_BATCH_ORIGINAL_CODE: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH_ORIGINAL_CODE',
    componentName: t('原始编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 报告单编号
  LIQUID_PREPARATION_PLAN_BATCH_REPORT_NO: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH_REPORT_NO',
    componentName: t('报告单编号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 放行单编号
  LIQUID_PREPARATION_PLAN_BATCH_RELEASE_NO: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH_RELEASE_NO',
    componentName: t('放行单编号'),
    node_type: '',
    icon: 'TEXT',
  },
};

// 配液计划-物料汇总子节点
export const LIQUID_PREPARATION_PLAN_SUMMARY_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  // 物料名称
  LIQUID_PREPARATION_PLAN_SUMMARY_NAME: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料编码
  LIQUID_PREPARATION_PLAN_SUMMARY_CODE: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料规格
  LIQUID_PREPARATION_PLAN_SUMMARY_SPECIFICATION: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料批号
  LIQUID_PREPARATION_PLAN_SUMMARY_BATCHNO: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_BATCHNO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 批次配液量
  LIQUID_PREPARATION_PLAN_SUMMARY_QUANTITY: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_QUANTITY',
    componentName: t('批次配液量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 物料配液总量
  LIQUID_PREPARATION_PLAN_SUMMARY_TOTAL_QUANTITY: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_TOTAL_QUANTITY',
    componentName: t('物料配液总量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  LIQUID_PREPARATION_PLAN_SUMMARY_UNIT: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 有效期至
  LIQUID_PREPARATION_PLAN_SUMMARY_EXPIRY_DATE: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_EXPIRY_DATE',
    componentName: t('有效期至'),
    node_type: '',
    icon: 'DATE',
  },
  // 供应商
  LIQUID_PREPARATION_PLAN_SUMMARY_SUPPLIER: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_SUPPLIER',
    componentName: t('供应商'),
    node_type: '',
    icon: 'TEXT',
  },
  // 生产商
  LIQUID_PREPARATION_PLAN_SUMMARY_MANUFACTURER: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_MANUFACTURER',
    componentName: t('生产商'),
    node_type: '',
    icon: 'TEXT',
  },
  // 原厂批号
  LIQUID_PREPARATION_PLAN_SUMMARY_ORIGINAL_BATCHNO: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_ORIGINAL_BATCHNO',
    componentName: t('原厂批号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 原始编码
  LIQUID_PREPARATION_PLAN_SUMMARY_ORIGINAL_CODE: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_ORIGINAL_CODE',
    componentName: t('原始编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 报告单编号
  LIQUID_PREPARATION_PLAN_SUMMARY_REPORT_NO: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_REPORT_NO',
    componentName: t('报告单编号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 放行单编号
  LIQUID_PREPARATION_PLAN_SUMMARY_RELEASE_NO: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_RELEASE_NO',
    componentName: t('放行单编号'),
    node_type: '',
    icon: 'TEXT',
  },
};

// 配液计划-配液计划子节点
export const LIQUID_PREPARATION_PLAN: BUSINESS_NODE_INFO_TYPE = {
  LIQUID_PREPARATION_PLAN_BATCH: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH',
    componentName: t('物料批次'),
    node_type: '',
    children: [...objectToArray(LIQUID_PREPARATION_PLAN_BATCH_CHILDREN) as BUSINESS_NODE[], CUSTOM_FIELD_BUTTON],
  },
  LIQUID_PREPARATION_PLAN_BATCH_BUTTON: {
    componentType: 'LIQUID_PREPARATION_PLAN_BATCH_BUTTON',
    componentName: t('新建物料批次组'),
    node_type: '',
    icon: 'Add',
  },
  LIQUID_PREPARATION_PLAN_SUMMARY: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY',
    componentName: t('物料汇总'),
    node_type: '',
    children: [...objectToArray(LIQUID_PREPARATION_PLAN_SUMMARY_CHILDREN) as BUSINESS_NODE[], CUSTOM_FIELD_BUTTON],
  },
  LIQUID_PREPARATION_PLAN_SUMMARY_BUTTON: {
    componentType: 'LIQUID_PREPARATION_PLAN_SUMMARY_BUTTON',
    componentName: t('新建物料汇总组'),
    node_type: '',
    icon: 'Add',
  },
  LIQUID_PLAN_BUTTON: {
    componentType: 'LIQUID_PLAN_BUTTON',
    componentName: t('配液计划按钮'),
    node_type: '',
    icon: 'TEXT',
  },
  ADD_LIQUID_PLAN_BUTTON: {
    componentType: 'ADD_LIQUID_PLAN_BUTTON',
    componentName: t('新增配液计划按钮'),
    node_type: '',
    icon: 'Add',
  },
};
