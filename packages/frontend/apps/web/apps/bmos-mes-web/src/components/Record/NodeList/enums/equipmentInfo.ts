import { BUSINESS_NODE_INFO_TYPE } from '../enum';
import { CUSTOM_FIELD_BUTTON } from './basic';

export const EQUIPMENT_INFO_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  EQUIPMENT_INFO_NAME: {
    componentType: 'EQUIPMENT_INFO_NAME',
    componentName: t('设备名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 设备编号
  EQUIPMENT_INFO_CODE: {
    componentType: 'EQUIPMENT_INFO_CODE',
    componentName: t('设备编号'),
    node_type: '',
    icon: 'TEXT',
  },
};

// 设备信息
export const EQUIPMENT_INFO: BUSINESS_NODE_INFO_TYPE = {
  ...EQUIPMENT_INFO_CHILDREN,
  CUSTOM_FIELD_BUTTON,
};
