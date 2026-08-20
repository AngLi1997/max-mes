import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE, CUSTOM_FIELD_BUTTON } from '../enum';

//配液量取-物料批次子节点
export const LIQUID_PREPARATION_MEASURE_DETAIL_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  LIQUID_PREPARATION_MEASURE_DETAIL_NAME: {
    componentType: 'LIQUID_PREPARATION_MEASURE_DETAIL_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_MEASURE_DETAIL_CODE: {
    componentType: 'LIQUID_PREPARATION_MEASURE_DETAIL_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_MEASURE_DETAIL_SPECIFICATION: {
    componentType: 'LIQUID_PREPARATION_MEASURE_DETAIL_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_MEASURE_DETAIL_BATCHNO: {
    componentType: 'LIQUID_PREPARATION_MEASURE_DETAIL_BATCHNO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料件号
  LIQUID_PREPARATION_MEASURE_DETAIL_PARTNO: {
    componentType: 'LIQUID_PREPARATION_MEASURE_DETAIL_PARTNO',
    componentName: t('物料件号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料量
  LIQUID_PREPARATION_MEASURE_DETAIL_QUANTITY: {
    componentType: 'LIQUID_PREPARATION_MEASURE_DETAIL_QUANTITY',
    componentName: t('物料量'),
    node_type: '',
    icon: 'NUMBER',
  },
  //单位
  LIQUID_PREPARATION_MEASURE_DETAIL_UNIT: {
    componentType: 'LIQUID_PREPARATION_MEASURE_DETAIL_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 操作人
  LIQUID_PREPARATION_MEASURE_DETAIL_OPERATOR: {
    componentType: 'LIQUID_PREPARATION_MEASURE_DETAIL_OPERATOR',
    componentName: t('操作人'),
    node_type: '',
    icon: 'TEXT',
  },
  // 复核人
  LIQUID_PREPARATION_MEASURE_DETAIL_REVIEWER: {
    componentType: 'LIQUID_PREPARATION_MEASURE_DETAIL_REVIEWER',
    componentName: t('复核人'),
    node_type: '',
    icon: 'TEXT',
  },
  // 操作时间
  LIQUID_PREPARATION_MEASURE_DETAIL_OPERATION_TIME: {
    componentType: 'LIQUID_PREPARATION_MEASURE_DETAIL_OPERATION_TIME',
    componentName: t('操作时间'),
    node_type: '',
    icon: 'DATE',
  },
};

// 配液量取-物料汇总子节点
export const LIQUID_PREPARATION_MEASURE_SUMMARY_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  // 物料名称
  LIQUID_PREPARATION_MEASURE_SUMMARY_NAME: {
    componentType: 'LIQUID_PREPARATION_MEASURE_SUMMARY_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料编码
  LIQUID_PREPARATION_MEASURE_SUMMARY_CODE: {
    componentType: 'LIQUID_PREPARATION_MEASURE_SUMMARY_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料规格
  LIQUID_PREPARATION_MEASURE_SUMMARY_SPECIFICATION: {
    componentType: 'LIQUID_PREPARATION_MEASURE_SUMMARY_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料总量
  LIQUID_PREPARATION_MEASURE_SUMMARY_TOTAL_QUANTITY: {
    componentType: 'LIQUID_PREPARATION_MEASURE_SUMMARY_TOTAL_QUANTITY',
    componentName: t('物料总量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  LIQUID_PREPARATION_MEASURE_SUMMARY_UNIT: {
    componentType: 'LIQUID_PREPARATION_MEASURE_SUMMARY_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 总件数
  LIQUID_PREPARATION_MEASURE_SUMMARY_TOTAL_COUNT: {
    componentType: 'LIQUID_PREPARATION_MEASURE_SUMMARY_TOTAL_COUNT',
    componentName: t('总件数'),
    node_type: '',
    icon: 'NUMBER',
  },
};

// 配液量取-配液量取子节点
export const LIQUID_PREPARATION_MEASURE: BUSINESS_NODE_INFO_TYPE = {
  LIQUID_PREPARATION_MEASURE_DETAIL: {
    componentType: 'LIQUID_PREPARATION_MEASURE_DETAIL',
    componentName: t('量取详情'),
    node_type: '',
    children: [...objectToArray(LIQUID_PREPARATION_MEASURE_DETAIL_CHILDREN) as BUSINESS_NODE[], CUSTOM_FIELD_BUTTON],
  },
  LIQUID_PREPARATION_MEASURE_DETAIL_BUTTON: {
    componentType: 'LIQUID_PREPARATION_MEASURE_DETAIL_BUTTON',
    componentName: t('新建量取详情组'),
    node_type: '',
    icon: 'Add',
  },
  LIQUID_PREPARATION_MEASURE_SUMMARY: {
    componentType: 'LIQUID_PREPARATION_MEASURE_SUMMARY',
    componentName: t('物料汇总'),
    node_type: '',
    children: objectToArray(LIQUID_PREPARATION_MEASURE_SUMMARY_CHILDREN) as BUSINESS_NODE[],
  },
  LIQUID_PREPARATION_MEASURE_SUMMARY_BUTTON: {
    componentType: 'LIQUID_PREPARATION_MEASURE_SUMMARY_BUTTON',
    componentName: t('新建物料汇总组'),
    node_type: '',
    icon: 'Add',
  },
  LIQUID_MEASURE_BUTTON: {
    componentType: 'LIQUID_MEASURE_BUTTON',
    componentName: t('配液量取按钮'),
    node_type: '',
    icon: 'TEXT',
  },
  ADD_LIQUID_MEASURE_BUTTON: {
    componentType: 'ADD_LIQUID_MEASURE_BUTTON',
    componentName: t('新增配液量取按钮'),
    node_type: '',
    icon: 'Add',
  },
};
