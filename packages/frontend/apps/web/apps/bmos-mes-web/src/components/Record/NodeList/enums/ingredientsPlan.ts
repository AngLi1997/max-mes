import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';
// 配料计划物料汇总子节点
export const INGREDIENTS_PLAN_MATERIAL_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  INGREDIENTS_PLAN_MATERIAL_NAME: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  INGREDIENTS_PLAN_MATERIAL_CODE: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  INGREDIENTS_PLAN_MATERIAL_SPECIFICATION: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料批号
  INGREDIENTS_PLAN_MATERIAL_BATCHNO: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_BATCHNO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 批次配料量
  INGREDIENTS_PLAN_MATERIAL_QUANTITY: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_QUANTITY',
    componentName: t('批次配料量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 物料配料总量
  INGREDIENTS_PLAN_MATERIAL_TOTAL: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_TOTAL',
    componentName: t('物料配料总量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  INGREDIENTS_PLAN_MATERIAL_UNIT: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 水分(%)
  INGREDIENTS_PLAN_MATERIAL_MOISTURE: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_MOISTURE',
    componentName: t('水分') + '(%)',
    node_type: '',
    icon: 'NUMBER',
  },
  // 含量(%)
  INGREDIENTS_PLAN_MATERIAL_CONTENT: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_CONTENT',
    componentName: t('含量') + '(%)',
    node_type: '',
    icon: 'NUMBER',
  },
  // 供应商
  INGREDIENTS_PLAN_MATERIAL_SUPPLIER: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_SUPPLIER',
    componentName: t('供应商'),
    node_type: '',
    icon: 'TEXT',
  },
  // 生产商
  INGREDIENTS_PLAN_MATERIAL_MANUFACTURER: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_MANUFACTURER',
    componentName: t('生产商'),
    node_type: '',
    icon: 'TEXT',
  },
  // 原厂批号
  INGREDIENTS_PLAN_MATERIAL_ORIGINAL_BATCHNO: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_ORIGINAL_BATCHNO',
    componentName: t('原厂批号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 原始编码
  INGREDIENTS_PLAN_MATERIAL_ORIGINAL_CODE: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_ORIGINAL_CODE',
    componentName: t('原始编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 报告单编号
  INGREDIENTS_PLAN_MATERIAL_REPORT_NO: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_REPORT_NO',
    componentName: t('报告单编号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 放行单编号
  INGREDIENTS_PLAN_MATERIAL_RELEASE_NO: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_RELEASE_NO',
    componentName: t('放行单编号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 有校期至
  INGREDIENTS_PLAN_MATERIAL_EXPIRATION_DATE: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_EXPIRATION_DATE',
    componentName: t('有效期至'),
    node_type: '',
    icon: 'DATE',
  },
};

// 配料计划
export const INGREDIENTS_PLAN_MATERIAL: BUSINESS_NODE_INFO_TYPE = {
  INGREDIENTS_PLAN_MATERIAL: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL',
    componentName: t('物料汇总'),
    node_type: '',
    children: objectToArray(INGREDIENTS_PLAN_MATERIAL_CHILDREN) as BUSINESS_NODE[],
  },
  INGREDIENTS_PLAN_MATERIAL_BUTTON: {
    componentType: 'INGREDIENTS_PLAN_MATERIAL_BUTTON',
    componentName: t('新建物料汇总组'),
    node_type: '',
    icon: 'Add',
  },
  INGREDIENTS_PLAN_BUTTON: {
    componentType: 'INGREDIENTS_PLAN_BUTTON',
    componentName: t('配料计划按钮'),
    node_type: '',
    icon: 'TEXT',
  },
  ADD_INGREDIENTS_PLAN_BUTTON: {
    componentType: 'ADD_INGREDIENTS_PLAN_BUTTON',
    componentName: t('新增配料计划按钮'),
    node_type: '',
    icon: 'Add',
  },
};
