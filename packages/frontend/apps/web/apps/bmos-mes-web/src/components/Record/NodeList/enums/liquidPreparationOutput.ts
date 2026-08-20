import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';
import { CUSTOM_FIELD_BUTTON } from './basic';

// 配液产出-详情子节点
export const LIQUID_PREPARATION_OUTPUT_DETAILS_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  LIQUID_PREPARATION_OUTPUT_DETAILS_NAME: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_DETAILS_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_OUTPUT_DETAILS_CODE: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_DETAILS_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_OUTPUT_DETAILS_SPECIFICATION: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_DETAILS_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_OUTPUT_DETAILS_BATCHNO: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_DETAILS_BATCHNO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料件号
  LIQUID_PREPARATION_OUTPUT_DETAILS_PARTNO: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_DETAILS_PARTNO',
    componentName: t('物料件号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料量
  LIQUID_PREPARATION_OUTPUT_DETAILS_QUANTITY: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_DETAILS_QUANTITY',
    componentName: t('物料量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  LIQUID_PREPARATION_OUTPUT_DETAILS_UNIT: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_DETAILS_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 产出人
  LIQUID_PREPARATION_OUTPUT_DETAILS_OPERATOR: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_DETAILS_OPERATOR',
    componentName: t('产出人'),
    node_type: '',
    icon: 'TEXT',
  },
  // 复核人
  LIQUID_PREPARATION_OUTPUT_DETAILS_REVIEWER: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_DETAILS_REVIEWER',
    componentName: t('复核人'),
    node_type: '',
    icon: 'TEXT',
  },
  // 产出时间
  LIQUID_PREPARATION_OUTPUT_DETAILS_OUTPUT_TIME: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_DETAILS_OUTPUT_TIME',
    componentName: t('产出时间'),
    node_type: '',
    icon: 'DATE',
  },
};

// 配液产出-物料汇总子节点
export const LIQUID_PREPARATION_OUTPUT_SUMMARY_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  // 物料名称
  LIQUID_PREPARATION_OUTPUT_SUMMARY_NAME: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_SUMMARY_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料编码
  LIQUID_PREPARATION_OUTPUT_SUMMARY_CODE: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_SUMMARY_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料规格
  LIQUID_PREPARATION_OUTPUT_SUMMARY_SPECIFICATION: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_SUMMARY_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料总量
  LIQUID_PREPARATION_OUTPUT_SUMMARY_TOTAL_QUANTITY: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_SUMMARY_TOTAL_QUANTITY',
    componentName: t('物料总量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  LIQUID_PREPARATION_OUTPUT_SUMMARY_UNIT: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_SUMMARY_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 总件数
  LIQUID_PREPARATION_OUTPUT_SUMMARY_PARTNO: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_SUMMARY_PARTNO',
    componentName: t('总件数'),
    node_type: '',
    icon: 'NUMBER',
  },
};

// 配液产出
export const LIQUID_PREPARATION_OUTPUT: BUSINESS_NODE_INFO_TYPE = {
  LIQUID_PREPARATION_OUTPUT_DETAILS: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_DETAILS',
    componentName: t('产出详情'),
    node_type: '',
    children: [...objectToArray(LIQUID_PREPARATION_OUTPUT_DETAILS_CHILDREN), CUSTOM_FIELD_BUTTON] as BUSINESS_NODE[],
  },
  LIQUID_PREPARATION_OUTPUT_DETAILS_BUTTON: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_DETAILS_BUTTON',
    componentName: t('新建产出详情组'),
    node_type: '',
    icon: 'Add',
  },
  LIQUID_PREPARATION_OUTPUT_SUMMARY: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_SUMMARY',
    componentName: t('物料汇总'),
    node_type: '',
    children: objectToArray(LIQUID_PREPARATION_OUTPUT_SUMMARY_CHILDREN) as BUSINESS_NODE[],
  },
  LIQUID_PREPARATION_OUTPUT_SUMMARY_BUTTON: {
    componentType: 'LIQUID_PREPARATION_OUTPUT_SUMMARY_BUTTON',
    componentName: t('新建物料汇总组'),
    node_type: '',
    icon: 'Add',
  },
  LIQUID_OUTPUT_BUTTON: {
    componentType: 'LIQUID_OUTPUT_BUTTON',
    componentName: t('产出按钮'),
    node_type: '',
    icon: 'TEXT',
  },
  ADD_LIQUID_OUTPUT_BUTTON: {
    componentType: 'ADD_LIQUID_OUTPUT_BUTTON',
    componentName: t('新增产出按钮'),
    node_type: '',
    icon: 'Add',
  },
};
