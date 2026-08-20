import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';

// 按批次量领料-物料批次子节点
export const BATCH_QUANTITY_PICK_BATCH_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  BATCH_QUANTITY_PICK_BATCH_NAME: {
    componentType: 'BATCH_QUANTITY_PICK_BATCH_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  BATCH_QUANTITY_PICK_BATCH_CODE: {
    componentType: 'BATCH_QUANTITY_PICK_BATCH_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  BATCH_QUANTITY_PICK_BATCH_SPECIFICATION: {
    componentType: 'BATCH_QUANTITY_PICK_BATCH_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  BATCH_QUANTITY_PICK_BATCH_BATCHNO: {
    componentType: 'BATCH_QUANTITY_PICK_BATCH_BATCHNO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  BATCH_QUANTITY_PICK_PLAN_PICK: {
    componentType: 'BATCH_QUANTITY_PICK_PLAN_PICK',
    componentName: t('计划领料量'),
    node_type: '',
    icon: 'NUMBER',
  },
  BATCH_QUANTITY_PICK_THEORY_MATERIAL: {
    componentType: 'BATCH_QUANTITY_PICK_THEORY_MATERIAL',
    componentName: t('理论物料量'),
    node_type: '',
    icon: 'NUMBER',
  },
  BATCH_QUANTITY_PICK_UNIT: {
    componentType: 'BATCH_QUANTITY_PICK_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  BATCH_QUANTITY_PICK_MOISTURE: {
    componentType: 'BATCH_QUANTITY_PICK_MOISTURE',
    componentName: t('水分') + '(%)',
    node_type: '',
    icon: 'NUMBER',
  },
  //含量%
  BATCH_QUANTITY_PICK_CONTENT: {
    componentType: 'BATCH_QUANTITY_PICK_CONTENT',
    componentName: t('含量') + '(%)',
    node_type: '',
    icon: 'NUMBER',
  },
  // 供应商
  BATCH_QUANTITY_PICK_SUPPLIER: {
    componentType: 'BATCH_QUANTITY_PICK_SUPPLIER',
    componentName: t('供应商'),
    node_type: '',
    icon: 'TEXT',
  },
  // 生产商
  BATCH_QUANTITY_PICK_PRODUCER: {
    componentType: 'BATCH_QUANTITY_PICK_PRODUCER',
    componentName: t('生产商'),
    node_type: '',
    icon: 'TEXT',
  },
  // 原厂批号
  BATCH_QUANTITY_PICK_ORIGIN_BATCHNO: {
    componentType: 'BATCH_QUANTITY_PICK_ORIGIN_BATCHNO',
    componentName: t('原厂批号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 原始编码
  BATCH_QUANTITY_PICK_ORIGIN_CODE: {
    componentType: 'BATCH_QUANTITY_PICK_ORIGIN_CODE',
    componentName: t('原始编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 有效期至
  BATCH_QUANTITY_PICK_EXPIRATION_DATE: {
    componentType: 'BATCH_QUANTITY_PICK_EXPIRATION_DATE',
    componentName: t('有效期至'),
    node_type: '',
    icon: 'DATE',
  },
};

// 按批次量领料-物料汇总子节点
export const BATCH_QUANTITY_PICK_SUMMARY_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  // 物料名称
  BATCH_QUANTITY_PICK_BATCH_SUMMARY_NAME: {
    componentType: 'BATCH_QUANTITY_PICK_BATCH_SUMMARY_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料编码
  BATCH_QUANTITY_PICK_BATCH_SUMMARY_CODE: {
    componentType: 'BATCH_QUANTITY_PICK_BATCH_SUMMARY_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料规格
  BATCH_QUANTITY_PICK_BATCH_SUMMARY_SPECIFICATION: {
    componentType: 'BATCH_QUANTITY_PICK_BATCH_SUMMARY_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 计划量合计
  BATCH_QUANTITY_PICK_BATCH_SUMMARY_PLAN_PICK: {
    componentType: 'BATCH_QUANTITY_PICK_BATCH_SUMMARY_PLAN_PICK',
    componentName: t('计划量合计'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 理论量合计
  BATCH_QUANTITY_PICK_BATCH_SUMMARY_THEORY_MATERIAL: {
    componentType: 'BATCH_QUANTITY_PICK_BATCH_SUMMARY_THEORY_MATERIAL',
    componentName: t('理论量合计'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  BATCH_QUANTITY_PICK_BATCH_SUMMARY_UNIT: {
    componentType: 'BATCH_QUANTITY_PICK_BATCH_SUMMARY_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
};

// 按批次量领料子节点
export const BATCH_QUANTITY_PICK_BATCH_SUMMARY: BUSINESS_NODE_INFO_TYPE = {
  BATCH_QUANTITY_PICK_BATCH: {
    componentType: 'BATCH_QUANTITY_PICK_BATCH',
    componentName: t('物料批次'),
    node_type: '',
    children: objectToArray(BATCH_QUANTITY_PICK_BATCH_CHILDREN) as BUSINESS_NODE[],
  },
  BATCH_QUANTITY_PICK_BATCH_BUTTON: {
    componentType: 'BATCH_QUANTITY_PICK_BATCH_BUTTON',
    componentName: t('新建物料批次组'),
    node_type: '',
    icon: 'Add',
  },
  BATCH_QUANTITY_PICK_SUMMARY: {
    componentType: 'BATCH_QUANTITY_PICK_SUMMARY',
    componentName: t('物料汇总'),
    node_type: '',
    children: objectToArray(BATCH_QUANTITY_PICK_SUMMARY_CHILDREN) as BUSINESS_NODE[],
  },
  BATCH_QUANTITY_PICK_SUMMARY_BUTTON: {
    componentType: 'BATCH_QUANTITY_PICK_SUMMARY_BUTTON',
    componentName: t('新建物料汇总组'),
    node_type: '',
    icon: 'Add',
  },
  BATCH_RECEIVE_BUTTON: {
    componentType: 'BATCH_RECEIVE_BUTTON',
    componentName: t('领料按钮'),
    node_type: '',
    icon: 'TEXT',
  },
  ADD_BATCH_RECEIVE_BUTTON: {
    componentType: 'ADD_BATCH_RECEIVE_BUTTON',
    componentName: t('新增领料按钮'),
    node_type: '',
    icon: 'Add',
  },
};
