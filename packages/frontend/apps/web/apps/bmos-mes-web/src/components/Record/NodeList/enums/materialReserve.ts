import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';
// 物料预订 汇总子节点
export const MATERIAL_RESERVE_SUMMARY_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  MATERIAL_RESERVE_MATERIAL_NAME: {
    componentType: 'MATERIAL_RESERVE_MATERIAL_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_MATERIAL_CODE: {
    componentType: 'MATERIAL_RESERVE_MATERIAL_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_MATERIAL_SPECIFICATION: {
    componentType: 'MATERIAL_RESERVE_MATERIAL_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_BATCH_NO: {
    componentType: 'MATERIAL_RESERVE_BATCH_NO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_BATCH_RESERVE_QUANTITY: {
    componentType: 'MATERIAL_RESERVE_BATCH_RESERVE_QUANTITY',
    componentName: t('批次预定量'),
    node_type: '',
    icon: 'NUMBER',
  },
  MATERIAL_RESERVE_RESERVE_TOTAL_QUANTITY: {
    componentType: 'MATERIAL_RESERVE_RESERVE_TOTAL_QUANTITY',
    componentName: t('物料预定总量'),
    node_type: '',
    icon: 'NUMBER',
  },
  MATERIAL_RESERVE_BATCH_CURRENT_RESERVE_QUANTITY: {
    componentType: 'MATERIAL_RESERVE_BATCH_CURRENT_RESERVE_QUANTITY',
    componentName: t('批次当前预订量'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_CURRENT_RESERVE_TOTAL_QUANTITY: {
    componentType: 'MATERIAL_RESERVE_CURRENT_RESERVE_TOTAL_QUANTITY',
    componentName: t('物料当前预订总量'),
    node_type: '',
    icon: 'NUMBER',
  },
  MATERIAL_RESERVE_UNIT: {
    componentType: 'MATERIAL_RESERVE_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_MOISTURE: {
    componentType: 'MATERIAL_RESERVE_MOISTURE',
    componentName: t('水分') + '(%)',
    node_type: '',
    icon: 'NUMBER',
  },
  MATERIAL_RESERVE_CONTENT: {
    componentType: 'MATERIAL_RESERVE_CONTENT',
    componentName: t('含量') + '(%)',
    node_type: '',
    icon: 'NUMBER',
  },
  MATERIAL_RESERVE_SUPPLIER: {
    componentType: 'MATERIAL_RESERVE_SUPPLIER',
    componentName: t('供应商'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_MANUFACTURER: {
    componentType: 'MATERIAL_RESERVE_MANUFACTURER',
    componentName: t('生产商'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_ORIGINAL_BATCH_NO: {
    componentType: 'MATERIAL_RESERVE_ORIGINAL_BATCH_NO',
    componentName: t('原厂批号'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_ORIGINAL_CODE: {
    componentType: 'MATERIAL_RESERVE_ORIGINAL_CODE',
    componentName: t('原始编码'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_REPORT_NO: {
    componentType: 'MATERIAL_RESERVE_REPORT_NO',
    componentName: t('报告单编号'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_RELEASE_NO: {
    componentType: 'MATERIAL_RESERVE_RELEASE_NO',
    componentName: t('放行单编号'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_EXPIRATION_DATE: {
    componentType: 'MATERIAL_RESERVE_EXPIRATION_DATE',
    componentName: t('有效期至'),
    node_type: '',
    icon: 'DATE',
  },
};
// 物料预订 批次子节点
export const MATERIAL_RESERVE_BATCH_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  MATERIAL_RESERVE_BATCH_MATERIAL_NAME: {
    componentType: 'MATERIAL_RESERVE_BATCH_MATERIAL_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_BATCH_MATERIAL_CODE: {
    componentType: 'MATERIAL_RESERVE_BATCH_MATERIAL_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_BATCH_MATERIAL_SPECIFICATION: {
    componentType: 'MATERIAL_RESERVE_BATCH_MATERIAL_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_BATCH_BATCH_NO: {
    componentType: 'MATERIAL_RESERVE_BATCH_BATCH_NO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_BATCH_BATCH_RESERVE_QUANTITY: {
    componentType: 'MATERIAL_RESERVE_BATCH_BATCH_RESERVE_QUANTITY',
    componentName: t('批次预定量'),
    node_type: '',
    icon: 'NUMBER',
  },
  MATERIAL_RESERVE_BATCH_CURRENT_BATCH_RESERVE_QUANTITY: {
    componentType: 'MATERIAL_RESERVE_BATCH_CURRENT_BATCH_RESERVE_QUANTITY',
    componentName: t('批次当前预订量'),
    node_type: '',
    icon: 'NUMBER',
  },
  MATERIAL_RESERVE_BATCH_UNIT: {
    componentType: 'MATERIAL_RESERVE_BATCH_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_BATCH_MOISTURE: {
    componentType: 'MATERIAL_RESERVE_BATCH_MOISTURE',
    componentName: t('水分') + '(%)',
    node_type: '',
    icon: 'NUMBER',
  },
  MATERIAL_RESERVE_BATCH_CONTENT: {
    componentType: 'MATERIAL_RESERVE_BATCH_CONTENT',
    componentName: t('含量') + '(%)',
    node_type: '',
    icon: 'NUMBER',
  },
  MATERIAL_RESERVE_BATCH_SUPPLIER: {
    componentType: 'MATERIAL_RESERVE_BATCH_SUPPLIER',
    componentName: t('供应商'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_BATCH_MANUFACTURER: {
    componentType: 'MATERIAL_RESERVE_BATCH_MANUFACTURER',
    componentName: t('生产商'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_BATCH_ORIGINAL_BATCH_NO: {
    componentType: 'MATERIAL_RESERVE_BATCH_ORIGINAL_BATCH_NO',
    componentName: t('原厂批号'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_BATCH_ORIGINAL_CODE: {
    componentType: 'MATERIAL_RESERVE_BATCH_ORIGINAL_CODE',
    componentName: t('原始编码'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_BATCH_REPORT_NO: {
    componentType: 'MATERIAL_RESERVE_BATCH_REPORT_NO',
    componentName: t('报告单编号'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_BATCH_RELEASE_NO: {
    componentType: 'MATERIAL_RESERVE_BATCH_RELEASE_NO',
    componentName: t('放行单编号'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_RESERVE_BATCH_EXPIRATION_DATE: {
    componentType: 'MATERIAL_RESERVE_BATCH_EXPIRATION_DATE',
    componentName: t('有效期至'),
    node_type: '',
    icon: 'DATE',
  },
};

// 物料预订
export const MATERIAL_RESERVE: BUSINESS_NODE_INFO_TYPE = {
  MATERIAL_RESERVE_BATCH: {
    componentType: 'MATERIAL_RESERVE_BATCH',
    componentName: t('物料批次'),
    node_type: '',
    children: objectToArray(MATERIAL_RESERVE_BATCH_CHILDREN) as BUSINESS_NODE[],
  },
  MATERIAL_RESERVE_BATCH_BUTTON: {
    componentType: 'MATERIAL_RESERVE_BATCH_BUTTON',
    componentName: t('新建物料批次'),
    node_type: '',
    icon: 'Add',
  },
  MATERIAL_RESERVE_SUMMARY: {
    componentType: 'MATERIAL_RESERVE_SUMMARY',
    componentName: t('物料汇总'),
    node_type: '',
    children: objectToArray(MATERIAL_RESERVE_SUMMARY_CHILDREN) as BUSINESS_NODE[],
  },
  MATERIAL_RESERVE_SUMMARY_BUTTON: {
    componentType: 'MATERIAL_RESERVE_SUMMARY_BUTTON',
    componentName: t('新建物料汇总'),
    node_type: '',
    icon: 'Add',
  },
  MATERIAL_RESERVE_BUTTON: {
    componentType: 'MATERIAL_RESERVE_BUTTON',
    componentName: t('预定按钮'),
    node_type: '',
    icon: 'TEXT',
  },
  ADD_MATERIAL_RESERVE_BUTTON: {
    componentType: 'ADD_MATERIAL_RESERVE_BUTTON',
    componentName: t('新增预定按钮'),
    node_type: '',
    icon: 'Add',
  },
};
