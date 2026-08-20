import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';
import { CUSTOM_FIELD_BUTTON } from './basic';

// 设备数采-数采点
export const EQUIPMENT_DATA_ACQUISITION_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  EQUIPMENT_DATA_ACQUISITION_TIME: {
    componentType: 'EQUIPMENT_DATA_ACQUISITION_TIME',
    componentName: t('采集时间'),
    node_type: '',
    icon: 'DATE',
  },
};
// 设备数采 新建设备数采(表格)
export const EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE: BUSINESS_NODE_INFO_TYPE = {
  EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE: {
    componentType: 'EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE',
    componentName: t('设备数采(表格)'),
    node_type: '',
    icon: 'DynamicTable',
  },
};

// 设备数采
export const EQUIPMENT_DATA_ACQUISITION: BUSINESS_NODE_INFO_TYPE = {
  // 数采时间
  EQUIPMENT_DATA_ACQUISITION_GROUP: {
    componentType: 'EQUIPMENT_DATA_ACQUISITION_GROUP',
    componentName: t('设备'),
    node_type: '',
    children: [...objectToArray(EQUIPMENT_DATA_ACQUISITION_CHILDREN), CUSTOM_FIELD_BUTTON] as BUSINESS_NODE[],
  },
  EQUIPMENT_DATA_ACQUISITION_GROUP_BUTTON: {
    componentType: 'EQUIPMENT_DATA_ACQUISITION_GROUP_BUTTON',
    componentName: t('新增设备组'),
    node_type: '',
    icon: 'Add',
  },
  EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE_BUTTON: {
    componentType: 'EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE_BUTTON',
    componentName: t('新建设备数采(表格)'),
    node_type: '',
    icon: 'Add',
  },
};
