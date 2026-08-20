import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';

// 生产投料-投料详情子节点
export const FEED_RECYCLE_FEEDING_DETAILS_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  // 物料名称
  FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_NAME: {
    componentType: 'FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料编码
  FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_CODE: {
    componentType: 'FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料规格
  FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_SPECIFICATION: {
    componentType: 'FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料批号
  FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_BATCHNO: {
    componentType: 'FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_BATCHNO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料件号
  FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_PARTNO: {
    componentType: 'FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_PARTNO',
    componentName: t('物料件号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料量
  FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_QUANTITY: {
    componentType: 'FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_QUANTITY',
    componentName: t('物料量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  FEED_RECYCLE_FEEDING_DETAILS_UNIT: {
    componentType: 'FEED_RECYCLE_FEEDING_DETAILS_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 操作类型
  FEED_RECYCLE_FEEDING_DETAILS_OPERATION_TYPE: {
    componentType: 'FEED_RECYCLE_FEEDING_DETAILS_OPERATION_TYPE',
    componentName: t('操作类型'),
    node_type: '',
    icon: 'TEXT',
  },
  // 操作人
  FEED_RECYCLE_FEEDING_DETAILS_OPERATOR: {
    componentType: 'FEED_RECYCLE_FEEDING_DETAILS_OPERATOR',
    componentName: t('操作人'),
    node_type: '',
    icon: 'TEXT',
  },
  // 操作时间
  FEED_RECYCLE_FEEDING_DETAILS_OPERATION_TIME: {
    componentType: 'FEED_RECYCLE_FEEDING_DETAILS_OPERATION_TIME',
    componentName: t('操作时间'),
    node_type: '',
    icon: 'DATE',
  },
  // 设备名称
  FEED_RECYCLE_FEEDING_DETAILS_DEVICE_NAME: {
    componentType: 'FEED_RECYCLE_FEEDING_DETAILS_DEVICE_NAME',
    componentName: t('设备名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 设备编号
  FEED_RECYCLE_FEEDING_DETAILS_DEVICE_CODE: {
    componentType: 'FEED_RECYCLE_FEEDING_DETAILS_DEVICE_CODE',
    componentName: t('设备编号'),
    node_type: '',
    icon: 'TEXT',
  },
};

// 生产投料-物料汇总子节点
export const FEED_RECYCLE_SUMMARY_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  // 物料名称
  FEED_RECYCLE_SUMMARY_MATERIAL_NAME: {
    componentType: 'FEED_RECYCLE_SUMMARY_MATERIAL_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料编码
  FEED_RECYCLE_SUMMARY_MATERIAL_CODE: {
    componentType: 'FEED_RECYCLE_SUMMARY_MATERIAL_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料规格
  FEED_RECYCLE_SUMMARY_MATERIAL_SPECIFICATION: {
    componentType: 'FEED_RECYCLE_SUMMARY_MATERIAL_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 投料总量
  FEED_RECYCLE_SUMMARY_TOTAL_QUANTITY: {
    componentType: 'FEED_RECYCLE_SUMMARY_TOTAL_QUANTITY',
    componentName: t('投料总量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 回收总量
  FEED_RECYCLE_SUMMARY_RECYCLE_TOTAL_QUANTITY: {
    componentType: 'FEED_RECYCLE_SUMMARY_RECYCLE_TOTAL_QUANTITY',
    componentName: t('回收总量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 使用总量
  FEED_RECYCLE_SUMMARY_USE_TOTAL_QUANTITY: {
    componentType: 'FEED_RECYCLE_SUMMARY_USE_TOTAL_QUANTITY',
    componentName: t('使用总量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  FEED_RECYCLE_SUMMARY_UNIT: {
    componentType: 'FEED_RECYCLE_SUMMARY_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 投料件数
  FEED_RECYCLE_SUMMARY_TOTAL_NUMBER: {
    componentType: 'FEED_RECYCLE_SUMMARY_TOTAL_NUMBER',
    componentName: t('投料件数'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 回收件数
  FEED_RECYCLE_SUMMARY_RECYCLE_TOTAL_NUMBER: {
    componentType: 'FEED_RECYCLE_SUMMARY_RECYCLE_TOTAL_NUMBER',
    componentName: t('回收件数'),
    node_type: '',
    icon: 'NUMBER',
  },
};

// 生产投料
export const FEED_RECYCLE: BUSINESS_NODE_INFO_TYPE = {
  FEED_RECYCLE_FEEDING_DETAILS: {
    componentType: 'FEED_RECYCLE_FEEDING_DETAILS',
    componentName: t('投料详情'),
    node_type: '',
    children: objectToArray(FEED_RECYCLE_FEEDING_DETAILS_CHILDREN) as BUSINESS_NODE[],
  },
  FEED_RECYCLE_FEEDING_DETAILS_BUTTON: {
    componentType: 'FEED_RECYCLE_FEEDING_DETAILS_BUTTON',
    componentName: t('新建投料详情组'),
    node_type: '',
    icon: 'Add',
  },
  FEED_RECYCLE_SUMMARY: {
    componentType: 'FEED_RECYCLE_SUMMARY',
    componentName: t('物料汇总'),
    node_type: '',
    children: objectToArray(FEED_RECYCLE_SUMMARY_CHILDREN) as BUSINESS_NODE[],
  },
  FEED_RECYCLE_SUMMARY_BUTTON: {
    componentType: 'FEED_RECYCLE_SUMMARY_BUTTON',
    componentName: t('新建物料汇总组'),
    node_type: '',
    icon: 'Add',
  },
  FEED_RECYCLE_BUTTON: {
    componentType: 'FEED_RECYCLE_BUTTON',
    componentName: t('投料按钮'),
    node_type: '',
    icon: 'TEXT',
  },
  FEED_BUTTON: {
    componentType: 'FEED_BUTTON',
    componentName: t('新增投料按钮'),
    node_type: '',
    icon: 'Add',
  },
};
