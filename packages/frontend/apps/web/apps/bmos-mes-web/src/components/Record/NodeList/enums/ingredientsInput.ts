import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';

// 配料投入-投料详情子节点
export const INGREDIENTS_INPUT_FEEDING_DETAILS_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  // 物料名称
  INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_NAME: {
    componentType: 'INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料编码
  INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_CODE: {
    componentType: 'INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料规格
  INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_SPECIFICATION: {
    componentType: 'INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料批号
  INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_BATCHNO: {
    componentType: 'INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_BATCHNO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料件号
  INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_PARTNO: {
    componentType: 'INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_PARTNO',
    componentName: t('物料件号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料量
  INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_QUANTITY: {
    componentType: 'INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_QUANTITY',
    componentName: t('物料量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  INGREDIENTS_INPUT_FEEDING_DETAILS_UNIT: {
    componentType: 'INGREDIENTS_INPUT_FEEDING_DETAILS_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 投料人
  INGREDIENTS_INPUT_FEEDING_DETAILS_FEEDER: {
    componentType: 'INGREDIENTS_INPUT_FEEDING_DETAILS_FEEDER',
    componentName: t('投料人'),
    node_type: '',
    icon: 'TEXT',
  },
  // 投料时间
  INGREDIENTS_INPUT_FEEDING_DETAILS_FEEDING_TIME: {
    componentType: 'INGREDIENTS_INPUT_FEEDING_DETAILS_FEEDING_TIME',
    componentName: t('投料时间'),
    node_type: '',
    icon: 'DATE',
  },
  // 设备名称
  INGREDIENTS_INPUT_FEEDING_DETAILS_DEVICE_NAME: {
    componentType: 'INGREDIENTS_INPUT_FEEDING_DETAILS_DEVICE_NAME',
    componentName: t('设备名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 设备编号
  INGREDIENTS_INPUT_FEEDING_DETAILS_DEVICE_CODE: {
    componentType: 'INGREDIENTS_INPUT_FEEDING_DETAILS_DEVICE_CODE',
    componentName: t('设备编号'),
    node_type: '',
    icon: 'TEXT',
  },
};

// 配料投入-物料汇总子节点
export const INGREDIENTS_INPUT_SUMMARY_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  // 物料名称
  INGREDIENTS_INPUT_SUMMARY_MATERIAL_NAME: {
    componentType: 'INGREDIENTS_INPUT_SUMMARY_MATERIAL_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料编码
  INGREDIENTS_INPUT_SUMMARY_MATERIAL_CODE: {
    componentType: 'INGREDIENTS_INPUT_SUMMARY_MATERIAL_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料规格
  INGREDIENTS_INPUT_SUMMARY_MATERIAL_SPECIFICATION: {
    componentType: 'INGREDIENTS_INPUT_SUMMARY_MATERIAL_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 投料总量
  INGREDIENTS_INPUT_SUMMARY_TOTAL_QUANTITY: {
    componentType: 'INGREDIENTS_INPUT_SUMMARY_TOTAL_QUANTITY',
    componentName: t('投料总量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  INGREDIENTS_INPUT_SUMMARY_UNIT: {
    componentType: 'INGREDIENTS_INPUT_SUMMARY_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 总件数
  INGREDIENTS_INPUT_SUMMARY_TOTAL_NUMBER: {
    componentType: 'INGREDIENTS_INPUT_SUMMARY_TOTAL_NUMBER',
    componentName: t('总件数'),
    node_type: '',
    icon: 'NUMBER',
  },
};

// 配料投入
export const INGREDIENTS_INPUT: BUSINESS_NODE_INFO_TYPE = {
  INGREDIENTS_INPUT_FEEDING_DETAILS: {
    componentType: 'INGREDIENTS_INPUT_FEEDING_DETAILS',
    componentName: t('投料详情'),
    node_type: '',
    children: objectToArray(INGREDIENTS_INPUT_FEEDING_DETAILS_CHILDREN) as BUSINESS_NODE[],
  },
  INGREDIENTS_INPUT_FEEDING_DETAILS_BUTTON: {
    componentType: 'INGREDIENTS_INPUT_FEEDING_DETAILS_BUTTON',
    componentName: t('新建投料详情组'),
    node_type: '',
    icon: 'Add',
  },
  INGREDIENTS_INPUT_SUMMARY: {
    componentType: 'INGREDIENTS_INPUT_SUMMARY',
    componentName: t('物料汇总'),
    node_type: '',
    children: objectToArray(INGREDIENTS_INPUT_SUMMARY_CHILDREN) as BUSINESS_NODE[],
  },
  INGREDIENTS_INPUT_SUMMARY_BUTTON: {
    componentType: 'INGREDIENTS_INPUT_SUMMARY_BUTTON',
    componentName: t('新建物料汇总组'),
    node_type: '',
    icon: 'Add',
  },
  INGREDIENTS_INPUT_BUTTON: {
    componentType: 'INGREDIENTS_INPUT_BUTTON',
    componentName: t('投料按钮'),
    node_type: '',
    icon: 'TEXT',
  },
  ADD_INGREDIENTS_INPUT_BUTTON: {
    componentType: 'ADD_INGREDIENTS_INPUT_BUTTON',
    componentName: t('新增投料按钮'),
    node_type: '',
    icon: 'Add',
  },
};
