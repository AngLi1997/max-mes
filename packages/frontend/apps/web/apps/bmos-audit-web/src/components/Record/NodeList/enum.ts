import { ComponentNode } from './type';
import { t } from '@bmos/i18n';

export enum NODE_TYPE {
  TEXT = 'TEXT',
  NUMBER = 'NUMBER',
  RADIO = 'RADIO',
  DATE = 'DATE',
  SUBMIT_SIGN = 'SUBMIT_SIGN',
  ATTACHMENT = 'ATTACHMENT',
  CHECKBOX = 'CHECKBOX',
  SELECT = 'SELECT',
  REVIEW_SIGN = 'REVIEW_SIGN',
  TIME = 'TIME',
}

export type NODE = Pick<
  ComponentNode,
  'componentType' | 'componentName' | 'node_type'
> & {
  icon?: string;
};

export type NODE_INFO_TYPE = {
  [p in keyof typeof NODE_TYPE as string]: NODE;
};

export const FONT_ICON: Record<string, string> = {
  TEXT: 'icon-wenzi',
  NUMBER: 'icon-shuzi',
  RADIO: 'icon-danxuan',
  CHECKBOX: 'icon-duoxuan',
  SELECT: 'icon-xuanze',
  DATE: 'icon-riqi',
  TIME: 'icon-shijian',
  ATTACHMENT: 'icon-fujian',
  SUBMIT_SIGN: 'icon-tijiaoqianming',
  REVIEW_SIGN: 'icon-fuheqianming',
};

export const NODE_INFO: NODE_INFO_TYPE = {
  TEXT: {
    componentType: 'TEXT',
    componentName: t('文字'),
    node_type: '',
    icon: 'TEXT',
  },
  NUMBER: {
    componentType: 'NUMBER',
    componentName: t('数字'),
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
  ATTACHMENT: {
    componentType: 'ATTACHMENT',
    componentName: t('附件'),
    node_type: '',
    icon: 'ATTACHMENT',
  },
  SUBMIT_SIGN: {
    componentType: 'SUBMIT_SIGN',
    componentName: t('提交'),
    node_type: '',
    icon: 'SUBMIT_SIGN',
  },
  REVIEW_SIGN: {
    componentType: 'REVIEW_SIGN',
    componentName: t('复核'),
    node_type: '',
    icon: 'REVIEW_SIGN',
  },
};
