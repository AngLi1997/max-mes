import { BUSINESS_NODE_INFO_TYPE } from '../enum';
import { CUSTOM_FIELD_BUTTON } from './basic';

export const MATERIAL_INFO_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  MATERIAL_INFO_MATERIAL_NAME: {
    componentType: 'MATERIAL_INFO_MATERIAL_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_INFO_MATERIAL_CODE: {
    componentType: 'MATERIAL_INFO_MATERIAL_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_INFO_MATERIAL_BATCHNO: {
    componentType: 'MATERIAL_INFO_MATERIAL_BATCHNO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_INFO_MATERIAL_PARTNO: {
    componentType: 'MATERIAL_INFO_MATERIAL_PARTNO',
    componentName: t('物料件号'),
    node_type: '',
    icon: 'TEXT',
  },
  MATERIAL_INFO_MATERIAL_QUANTITY: {
    componentType: 'MATERIAL_INFO_MATERIAL_QUANTITY',
    componentName: t('物料量'),
    node_type: '',
    icon: 'NUMBER',
  },
  MATERIAL_INFO_NET_WEIGHT: {
    componentType: 'MATERIAL_INFO_NET_WEIGHT',
    componentName: t('净重'),
    node_type: '',
    icon: 'NUMBER',
  },
  MATERIAL_INFO_TARE_WEIGHT: {
    componentType: 'MATERIAL_INFO_TARE_WEIGHT',
    componentName: t('皮重'),
    node_type: '',
    icon: 'NUMBER',
  },
  MATERIAL_INFO_GROSS_WEIGHT: {
    componentType: 'MATERIAL_INFO_GROSS_WEIGHT',
    componentName: t('毛重'),
    node_type: '',
    icon: 'NUMBER',
  },
  MATERIAL_INFO_UNIT: {
    componentType: 'MATERIAL_INFO_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
};

// 物料件信息
export const MATERIAL_INFO: BUSINESS_NODE_INFO_TYPE = {
  ...MATERIAL_INFO_CHILDREN,
  CUSTOM_FIELD_BUTTON,
};
