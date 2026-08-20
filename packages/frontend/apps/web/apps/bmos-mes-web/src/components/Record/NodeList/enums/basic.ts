import { NODE_INFO_TYPE } from './type';

export const NODE_INFO: NODE_INFO_TYPE = {
  TEXT: {
    componentType: 'TEXT',
    componentName: t('文字'),
    node_type: '',
    icon: 'TEXT',
  },
  NUMBER: {
    componentType: 'NUMBER',
    componentName: t('数值'),
    node_type: '',
    icon: 'NUMBER',
  },
  RADIO: {
    componentType: 'RADIO',
    componentName: t('单选'),
    node_type: '',
    icon: 'RADIO',
  },
  CHECKBOX: {
    componentType: 'CHECKBOX',
    componentName: t('多选'),
    node_type: '',
    icon: 'CHECKBOX',
  },
  SELECT: {
    componentType: 'SELECT',
    componentName: t('选择'),
    node_type: '',
    icon: 'SELECT',
  },
  DATE: {
    componentType: 'DATE',
    componentName: t('日期'),
    node_type: '',
    icon: 'DATE',
  },
  TIME: {
    componentType: 'TIME',
    componentName: t('时间'),
    node_type: '',
    icon: 'TIME',
  },
  // ATTACHMENT: {
  //   componentType: 'ATTACHMENT',
  //   componentName: t('附件'),
  //   node_type: '',
  //   icon: 'ATTACHMENT',
  // },
  HANDLE_SUBMIT_SIGN: {
    componentType: 'HANDLE_SUBMIT_SIGN',
    componentName: t('手写签名'),
    node_type: '',
    icon: 'HANDLE_SUBMIT_SIGN',
  },
  HANDLE_REVIEW_SIGN: {
    componentType: 'HANDLE_REVIEW_SIGN',
    componentName: t('手写签名'),
    node_type: '',
    icon: 'HANDLE_REVIEW_SIGN',
  },
  SUBMIT_SIGN: {
    componentType: 'SUBMIT_SIGN',
    componentName: t('电子签名'),
    node_type: '',
    icon: 'SUBMIT_SIGN',
  },
  REVIEW_SIGN: {
    componentType: 'REVIEW_SIGN',
    componentName: t('电子签名'),
    node_type: '',
    icon: 'REVIEW_SIGN',
  },
  PHOTO: {
    componentType: 'PHOTO',
    componentName: t('照相'),
    node_type: '',
    icon: 'PHOTO',
  },
};

//新建自定义字段
export const CUSTOM_FIELD_BUTTON = {
  componentType: 'CUSTOM_FIELD_BUTTON',
  componentName: t('新建自定义字段'),
  node_type: '',
  icon: 'Add',
};
// 自定义字段
export const CUSTOM_FIELD = {
  componentType: 'CUSTOM_FIELD',
  componentName: t('自定义字段'),
  node_type: '',
  icon: 'CUSTOM',
};
