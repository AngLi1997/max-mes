import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';

// 清场执行子节点
export const CLEAN_IMPLEMENT_GROUP_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  CLEAN_IMPLEMENT_BATCHNO: {
    componentType: 'CLEAN_IMPLEMENT_BATCHNO',
    componentName: t('生产批号'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_IMPLEMENT_PRODUCT_NAME: {
    componentType: 'CLEAN_IMPLEMENT_PRODUCT_NAME',
    componentName: t('产品名称'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_IMPLEMENT_PRODUCT_NO: {
    componentType: 'CLEAN_IMPLEMENT_PRODUCT_NO',
    componentName: t('产品编码'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_IMPLEMENT_PROCEDURE: {
    componentType: 'CLEAN_IMPLEMENT_PROCEDURE',
    componentName: t('工序'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_IMPLEMENT_ROOM_NAME: {
    componentType: 'CLEAN_IMPLEMENT_ROOM_NAME',
    componentName: t('房间名称'),
    node_type: '',
    icon: 'SELECT',
  },
  CLEAN_IMPLEMENT_ROOM_CODE: {
    componentType: 'CLEAN_IMPLEMENT_ROOM_CODE',
    componentName: t('房间编号'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_IMPLEMENT_CLEAN_PERSON: {
    componentType: 'CLEAN_IMPLEMENT_CLEAN_PERSON',
    componentName: t('清场人'),
    node_type: '',
    icon: 'SUBMIT_SIGN',
  },
  CLEAN_IMPLEMENT_CLEAN_DATE: {
    componentType: 'CLEAN_IMPLEMENT_CLEAN_DATE',
    componentName: t('清场日期'),
    node_type: '',
    icon: 'DATE',
  },
  CLEAN_IMPLEMENT_QUALITY_INSPECTION_PERSON: {
    componentType: 'CLEAN_IMPLEMENT_QUALITY_INSPECTION_PERSON',
    componentName: t('复核人'),
    node_type: '',
    icon: 'SUBMIT_SIGN',
  },
  CLEAN_IMPLEMENT_ROOM_QUALITY_INSPECTION_DATE: {
    componentType: 'CLEAN_IMPLEMENT_ROOM_QUALITY_INSPECTION_DATE',
    componentName: t('复核日期'),
    node_type: '',
    icon: 'DATE',
  },
  CLEAN_IMPLEMENT_START_TIME: {
    componentType: 'CLEAN_IMPLEMENT_START_TIME',
    componentName: t('清场开始时间'),
    node_type: '',
    icon: 'DATE',
  },
  CLEAN_IMPLEMENT_END_TIME: {
    componentType: 'CLEAN_IMPLEMENT_END_TIME',
    componentName: t('清场结束时间'),
    node_type: '',
    icon: 'DATE',
  },
  CLEAN_IMPLEMENT_EXPIRATION_DATE: {
    componentType: 'CLEAN_IMPLEMENT_EXPIRATION_DATE',
    componentName: t('有效期至'),
    node_type: '',
    icon: 'DATE',
  },
};

//  清场执行
export const CLEAN_IMPLEMENT: BUSINESS_NODE_INFO_TYPE = {
  CLEAN_IMPLEMENT_GROUP: {
    componentType: 'CLEAN_IMPLEMENT_GROUP',
    componentName: t('执行组'),
    node_type: '',
    children: objectToArray(CLEAN_IMPLEMENT_GROUP_CHILDREN) as BUSINESS_NODE[],
  },
  CLEAN_IMPLEMENT_GROUP_BUTTON: {
    componentType: 'CLEAN_IMPLEMENT_GROUP_BUTTON',
    componentName: t('新建执行组'),
    node_type: '',
    icon: 'Add',
  },
};
