import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';

// 清场检查子节点
export const CLEAN_CHECK_GROUP_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  CLEAN_CHECK_BATCHNO: {
    componentType: 'CLEAN_CHECK_BATCHNO',
    componentName: t('生产批号'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_CHECK_PRODUCT_NAME: {
    componentType: 'CLEAN_CHECK_PRODUCT_NAME',
    componentName: t('产品名称'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_CHECK_PRODUCT_NO: {
    componentType: 'CLEAN_CHECK_PRODUCT_NO',
    componentName: t('产品编码'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_CHECK_PROCEDURE: {
    componentType: 'CLEAN_CHECK_PROCEDURE',
    componentName: t('工序'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_CHECK_ROOM_NAME: {
    componentType: 'CLEAN_CHECK_ROOM_NAME',
    componentName: t('房间名称'),
    node_type: '',
    icon: 'SELECT',
  },
  CLEAN_CHECK_ROOM_CODE: {
    componentType: 'CLEAN_CHECK_ROOM_CODE',
    componentName: t('房间编号'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_CHECK_CLEAN_PERSON: {
    componentType: 'CLEAN_CHECK_CLEAN_PERSON',
    componentName: t('清场人'),
    node_type: '',
    icon: 'SUBMIT_SIGN',
  },
  CLEAN_CHECK_CLEAN_DATE: {
    componentType: 'CLEAN_CHECK_CLEAN_DATE',
    componentName: t('清场日期'),
    node_type: '',
    icon: 'DATE',
  },
  CLEAN_CHECK_QUALITY_INSPECTION_PERSON: {
    componentType: 'CLEAN_CHECK_QUALITY_INSPECTION_PERSON',
    componentName: t('复核人'),
    node_type: '',
    icon: 'SUBMIT_SIGN',
  },
  CLEAN_CHECK_ROOM_QUALITY_INSPECTION_DATE: {
    componentType: 'CLEAN_CHECK_ROOM_QUALITY_INSPECTION_DATE',
    componentName: t('复核日期'),
    node_type: '',
    icon: 'DATE',
  },
  CLEAN_CHECK_START_TIME: {
    componentType: 'CLEAN_CHECK_START_TIME',
    componentName: t('清场开始时间'),
    node_type: '',
    icon: 'DATE',
  },
  CLEAN_CHECK_END_TIME: {
    componentType: 'CLEAN_CHECK_END_TIME',
    componentName: t('清场结束时间'),
    node_type: '',
    icon: 'DATE',
  },
  CLEAN_CHECK_EXPIRATION_DATE: {
    componentType: 'CLEAN_CHECK_EXPIRATION_DATE',
    componentName: t('有效期至'),
    node_type: '',
    icon: 'DATE',
  },
};

// 清场检查
export const CLEAN_CHECK: BUSINESS_NODE_INFO_TYPE = {
  CLEAN_CHECK_GROUP: {
    componentType: 'CLEAN_CHECK_GROUP',
    componentName: t('检查组'),
    node_type: '',
    children: objectToArray(CLEAN_CHECK_GROUP_CHILDREN) as BUSINESS_NODE[],
  },
  CLEAN_CHECK_GROUP_BUTTON: {
    componentType: 'CLEAN_CHECK_GROUP_BUTTON',
    componentName: t('检查组'),
    node_type: '',
    icon: 'Add',
  },
};
