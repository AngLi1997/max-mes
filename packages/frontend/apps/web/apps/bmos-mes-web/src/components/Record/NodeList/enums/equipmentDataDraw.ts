// 设备数采绘图  组件配置
import { BUSINESS_NODE_INFO_TYPE } from '../enum';

export const EQUIPMENT_DATA_DRAW: BUSINESS_NODE_INFO_TYPE = {
  EQUIPMENT_DATA_DRAW: {
    componentType: 'EQUIPMENT_DATA_DRAW',
    componentName: t('设备数采绘图'),
    node_type: '',
    icon: 'Frame',
  },
};

// 设备数采绘图
export const EQUIPMENT_DATA_DRAW_LIST: BUSINESS_NODE_INFO_TYPE = {
  ...EQUIPMENT_DATA_DRAW,
  EQUIPMENT_DATA_DRAW_BUTTON: {
    componentType: 'EQUIPMENT_DATA_DRAW_BUTTON',
    componentName: t('新建数采绘图'),
    node_type: '',
    icon: 'Add',
  },
};
