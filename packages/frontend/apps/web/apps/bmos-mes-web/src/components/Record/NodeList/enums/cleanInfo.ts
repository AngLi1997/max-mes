import { BUSINESS_NODE_INFO_TYPE } from '../enum';
// 清场信息子节点
export const CLEAN_INFO_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  CLEAN_INFO_BATCHNO: {
    componentType: 'CLEAN_INFO_BATCHNO',
    componentName: t('生产批号'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_INFO_PRODUCT_NAME: {
    componentType: 'CLEAN_INFO_PRODUCT_NAME',
    componentName: t('产品名称'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_INFO_PRODUCT_NO: {
    componentType: 'CLEAN_INFO_PRODUCT_NO',
    componentName: t('产品编码'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_INFO_PROCEDURE: {
    componentType: 'CLEAN_INFO_PROCEDURE',
    componentName: t('工序'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_INFO_ROOM_NAME: {
    componentType: 'CLEAN_INFO_ROOM_NAME',
    componentName: t('房间名称'),
    node_type: '',
    icon: 'SELECT',
  },
  CLEAN_INFO_ROOM_CODE: {
    componentType: 'CLEAN_INFO_ROOM_CODE',
    componentName: t('房间编号'),
    node_type: '',
    icon: 'TEXT',
  },
  CLEAN_INFO_CLEAN_PERSON: {
    componentType: 'CLEAN_INFO_CLEAN_PERSON',
    componentName: t('清场人'),
    node_type: '',
    icon: 'SUBMIT_SIGN',
  },
  CLEAN_INFO_CLEAN_DATE: {
    componentType: 'CLEAN_INFO_CLEAN_DATE',
    componentName: t('清场日期'),
    node_type: '',
    icon: 'DATE',
  },
  CLEAN_INFO_QUALITY_INSPECTION_PERSON: {
    componentType: 'CLEAN_INFO_QUALITY_INSPECTION_PERSON',
    componentName: t('复核人'),
    node_type: '',
    icon: 'SUBMIT_SIGN',
  },
  CLEAN_INFO_ROOM_QUALITY_INSPECTION_DATE: {
    componentType: 'CLEAN_INFO_ROOM_QUALITY_INSPECTION_DATE',
    componentName: t('复核日期'),
    node_type: '',
    icon: 'DATE',
  },
  CLEAN_INFO_START_TIME: {
    componentType: 'CLEAN_INFO_START_TIME',
    componentName: t('清场开始时间'),
    node_type: '',
    icon: 'DATE',
  },
  CLEAN_INFO_END_TIME: {
    componentType: 'CLEAN_INFO_END_TIME',
    componentName: t('清场结束时间'),
    node_type: '',
    icon: 'DATE',
  },
  CLEAN_INFO_EXPIRATION_DATE: {
    componentType: 'CLEAN_INFO_EXPIRATION_DATE',
    componentName: t('有效期至'),
    node_type: '',
    icon: 'DATE',
  },
};
