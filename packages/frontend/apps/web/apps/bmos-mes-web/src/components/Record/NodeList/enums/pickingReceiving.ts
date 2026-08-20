import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';

// 领料接收-物料批次子节点
export const PICKING_RECEIVING_BATCH_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  PICKING_RECEIVING_BATCH_NAME: {
    componentType: 'PICKING_RECEIVING_BATCH_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  PICKING_RECEIVING_BATCH_CODE: {
    componentType: 'PICKING_RECEIVING_BATCH_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  PICKING_RECEIVING_BATCH_SPECIFICATION: {
    componentType: 'PICKING_RECEIVING_BATCH_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  PICKING_RECEIVING_BATCH_BATCHNO: {
    componentType: 'PICKING_RECEIVING_BATCH_BATCHNO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  PICKING_RECEIVING_BATCH_PICK: {
    componentType: 'PICKING_RECEIVING_BATCH_PICK',
    componentName: t('领料量'),
    node_type: '',
    icon: 'NUMBER',
  },
  PICKING_RECEIVING_BATCH_UNIT: {
    componentType: 'PICKING_RECEIVING_BATCH_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 供应商
  PICKING_RECEIVING_BATCH_SUPPLIER: {
    componentType: 'PICKING_RECEIVING_BATCH_SUPPLIER',
    componentName: t('供应商'),
    node_type: '',
    icon: 'TEXT',
  },
  // 生产商
  PICKING_RECEIVING_BATCH_PRODUCER: {
    componentType: 'PICKING_RECEIVING_BATCH_PRODUCER',
    componentName: t('生产商'),
    node_type: '',
    icon: 'TEXT',
  },
  // 原厂批号
  PICKING_RECEIVING_BATCH_ORIGINAL_BATCHNO: {
    componentType: 'PICKING_RECEIVING_BATCH_ORIGINAL_BATCHNO',
    componentName: t('原厂批号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 原始编码
  PICKING_RECEIVING_BATCH_ORIGINAL_CODE: {
    componentType: 'PICKING_RECEIVING_BATCH_ORIGINAL_CODE',
    componentName: t('原始编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 有效期至
  PICKING_RECEIVING_BATCH_EXPIRATION_DATE: {
    componentType: 'PICKING_RECEIVING_BATCH_EXPIRATION_DATE',
    componentName: t('有效期至'),
    node_type: '',
    icon: 'DATE',
  },
};

// 领料接收-物料汇总子节点
export const PICKING_RECEIVING_SUMMARY_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  PICKING_RECEIVING_SUMMARY_NAME: {
    componentType: 'PICKING_RECEIVING_SUMMARY_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  PICKING_RECEIVING_SUMMARY_CODE: {
    componentType: 'PICKING_RECEIVING_SUMMARY_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  PICKING_RECEIVING_SUMMARY_SPECIFICATION: {
    componentType: 'PICKING_RECEIVING_SUMMARY_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  PICKING_RECEIVING_SUMMARY_PICK: {
    componentType: 'PICKING_RECEIVING_SUMMARY_PICK',
    componentName: t('领料量'),
    node_type: '',
    icon: 'NUMBER',
  },
  PICKING_RECEIVING_SUMMARY_UNIT: {
    componentType: 'PICKING_RECEIVING_SUMMARY_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
};

// 领料接收子节点
export const PICKING_RECEIVING_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  PICKING_RECEIVING_BATCH: {
    componentType: 'PICKING_RECEIVING_BATCH',
    componentName: t('物料批次'),
    node_type: '',
    icon: 'TEXT',
    children: objectToArray(PICKING_RECEIVING_BATCH_CHILDREN) as BUSINESS_NODE[],
  },
  // 新建物料批次
  PICKING_RECEIVING_BATCH_BUTTON: {
    componentType: 'PICKING_RECEIVING_BATCH_BUTTON',
    componentName: t('新建物料批次'),
    node_type: '',
    icon: 'Add',
  },
  PICKING_RECEIVING_SUMMARY: {
    componentType: 'PICKING_RECEIVING_SUMMARY',
    componentName: t('物料汇总'),
    node_type: '',
    icon: 'TEXT',
    children: objectToArray(PICKING_RECEIVING_SUMMARY_CHILDREN) as BUSINESS_NODE[],
  },
  // 新建物料汇总
  PICKING_RECEIVING_SUMMARY_BUTTON: {
    componentType: 'PICKING_RECEIVING_SUMMARY_BUTTON',
    componentName: t('新建物料汇总'),
    node_type: '',
    icon: 'Add',
  },
  PICKING_RECEIVE_BUTTON: {
    componentType: 'PICKING_RECEIVE_BUTTON',
    componentName: t('接收按钮'),
    node_type: '',
    icon: 'TEXT',
  },
  ADD_PICKING_RECEIVE_BUTTON: {
    componentType: 'ADD_PICKING_RECEIVE_BUTTON',
    componentName: t('新增接收按钮'),
    node_type: '',
    icon: 'Add',
  },
};
