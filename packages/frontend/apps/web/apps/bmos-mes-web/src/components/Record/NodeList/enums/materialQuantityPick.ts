import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';
// 按物料量领料子节点
export const MATERIAL_QUANTITY_PICK_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  MATERIAL_QUANTITY_PICK_MATERIAL_NAME: {
    componentType: 'MATERIAL_QUANTITY_PICK_MATERIAL_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_QUANTITY_PICK_MATERIAL_CODE: {
    componentType: 'MATERIAL_QUANTITY_PICK_MATERIAL_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_QUANTITY_PICK_MATERIAL_SPECIFICATION: {
    componentType: 'MATERIAL_QUANTITY_PICK_MATERIAL_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 领料量
  MATERIAL_QUANTITY_PICK_MATERIAL_PICK: {
    componentType: 'MATERIAL_QUANTITY_PICK_MATERIAL_PICK',
    componentName: t('领料量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  MATERIAL_QUANTITY_PICK_MATERIAL_UNIT: {
    componentType: 'MATERIAL_QUANTITY_PICK_MATERIAL_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 供应商
  MATERIAL_QUANTITY_PICK_MATERIAL_SUPPLIER: {
    componentType: 'MATERIAL_QUANTITY_PICK_MATERIAL_SUPPLIER',
    componentName: t('供应商'),
    node_type: '',
    icon: 'TEXT',
  },
  // 生产商
  MATERIAL_QUANTITY_PICK_MATERIAL_MANUFACTURER: {
    componentType: 'MATERIAL_QUANTITY_PICK_MATERIAL_MANUFACTURER',
    componentName: t('生产商'),
    node_type: '',
    icon: 'TEXT',
  },
};

// 按物料量领料
export const MATERIAL_QUANTITY_PICK_MATERIAL: BUSINESS_NODE_INFO_TYPE = {
  MATERIAL_QUANTITY_PICK: {
    componentType: 'MATERIAL_QUANTITY_PICK_MATERIAL',
    componentName: t('物料'),
    node_type: '',
    children: objectToArray(MATERIAL_QUANTITY_PICK_CHILDREN) as BUSINESS_NODE[],
  },
  MATERIAL_QUANTITY_PICK_BUTTON: {
    componentType: 'MATERIAL_QUANTITY_PICK_BUTTON',
    componentName: t('新建组'),
    node_type: '',
    icon: 'Add',
  },
  MATERIAL_RECEIVE_BUTTON: {
    componentType: 'MATERIAL_RECEIVE_BUTTON',
    componentName: t('领料按钮'),
    node_type: '',
    icon: 'TEXT',
  },
  ADD_MATERIAL_RECEIVE_BUTTON: {
    componentType: 'ADD_MATERIAL_RECEIVE_BUTTON',
    componentName: t('新增领料按钮'),
    node_type: '',
    icon: 'Add',
  },
};
