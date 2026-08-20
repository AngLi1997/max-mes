import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';

// 成品产出
export const PRODUCT_OUTPUT_DETAILS_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  PRODUCT_OUTPUT_DETAILS_NAME: {
    componentType: 'PRODUCT_OUTPUT_DETAILS_NAME',
    componentName: t('成品名称'),
    node_type: '',
    icon: 'TEXT',
  },
  PRODUCT_OUTPUT_DETAILS_CODE: {
    componentType: 'PRODUCT_OUTPUT_DETAILS_CODE',
    componentName: t('成品编码'),
    node_type: '',
    icon: 'TEXT',
  },
  PRODUCT_OUTPUT_DETAILS_SPECIFICATION: {
    componentType: 'PRODUCT_OUTPUT_DETAILS_SPECIFICATION',
    componentName: t('成品规格'),
    node_type: '',
    icon: 'TEXT',
  },
  PRODUCT_OUTPUT_DETAILS_BATCH_NO: {
    componentType: 'PRODUCT_OUTPUT_DETAILS_BATCH_NO',
    componentName: t('成品批号'),
    node_type: '',
    icon: 'TEXT',
  },
  PRODUCT_OUTPUT_DETAILS_SINGLE_QUANTITY: {
    componentType: 'PRODUCT_OUTPUT_DETAILS_SINGLE_QUANTITY',
    componentName: t('单件量'),
    node_type: '',
    icon: 'NUMBER',
  },
  PRODUCT_OUTPUT_DETAILS_UNIT: {
    componentType: 'PRODUCT_OUTPUT_DETAILS_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  PRODUCT_OUTPUT_DETAILS_SIZE: {
    componentType: 'PRODUCT_OUTPUT_DETAILS_SIZE',
    componentName: t('产出件数'),
    node_type: '',
    icon: 'NUMBER',
  },
  PRODUCT_OUTPUT_DETAILS_OPERATOR: {
    componentType: 'PRODUCT_OUTPUT_DETAILS_OPERATOR',
    componentName: t('操作人'),
    node_type: '',
    icon: 'TEXT',
  },
  PRODUCT_OUTPUT_DETAILS_OPERATE_TIME: {
    componentType: 'PRODUCT_OUTPUT_DETAILS_OPERATE_TIME',
    componentName: t('操作时间'),
    node_type: '',
    icon: 'DATE',
  },
};

// 成品产出-成品汇总子节点
export const PRODUCT_OUTPUT_SUMMARY_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  // 成品名称
  PRODUCT_OUTPUT_SUMMARY_NAME: {
    componentType: 'PRODUCT_OUTPUT_SUMMARY_NAME',
    componentName: t('成品名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 成品编码
  PRODUCT_OUTPUT_SUMMARY_CODE: {
    componentType: 'PRODUCT_OUTPUT_SUMMARY_CODE',
    componentName: t('成品编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 成品规格
  PRODUCT_OUTPUT_SUMMARY_SPECIFICATION: {
    componentType: 'PRODUCT_OUTPUT_SUMMARY_SPECIFICATION',
    componentName: t('成品规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 产出总量
  PRODUCT_OUTPUT_SUMMARY_OUTPUT_TOTAL: {
    componentType: 'PRODUCT_OUTPUT_SUMMARY_OUTPUT_TOTAL',
    componentName: t('产出总量'),
    node_type: '',
    icon: 'TEXT',
  },
  // 总件数
  PRODUCT_OUTPUT_SUMMARY_SIZE_TOTAL: {
    componentType: 'PRODUCT_OUTPUT_SUMMARY_SIZE_TOTAL',
    componentName: t('总件数'),
    node_type: '',
    icon: 'NUMBER',
  },
};

// 按批次量领料子节点
export const PRODUCT_OUTPUT_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  PRODUCT_OUTPUT_DETAILS: {
    componentType: 'PRODUCT_OUTPUT_DETAILS',
    componentName: t('产出详情'),
    node_type: '',
    children: objectToArray(PRODUCT_OUTPUT_DETAILS_CHILDREN) as BUSINESS_NODE[],
  },
  PRODUCT_OUTPUT_DETAILS_BUTTON: {
    componentType: 'PRODUCT_OUTPUT_DETAILS_BUTTON',
    componentName: t('新建成品详情组'),
    node_type: '',
    icon: 'Add',
  },
  PRODUCT_OUTPUT_SUMMARY: {
    componentType: 'PRODUCT_OUTPUT_SUMMARY',
    componentName: t('成品汇总'),
    node_type: '',
    children: objectToArray(PRODUCT_OUTPUT_SUMMARY_CHILDREN) as BUSINESS_NODE[],
  },
  PRODUCT_OUTPUT_SUMMARY_BUTTON: {
    componentType: 'PRODUCT_OUTPUT_SUMMARY_BUTTON',
    componentName: t('新建成品汇总组'),
    node_type: '',
    icon: 'Add',
  },
  PRODUCT_OUTPUT_BUTTON: {
    componentType: 'PRODUCT_OUTPUT_BUTTON',
    componentName: t('产出按钮'),
    node_type: '',
    icon: 'TEXT',
  },
  PRODUCT_ADD_BUTTON: {
    componentType: 'PRODUCT_ADD_BUTTON',
    componentName: t('新增产出按钮'),
    node_type: '',
    icon: 'Add',
  },
};
