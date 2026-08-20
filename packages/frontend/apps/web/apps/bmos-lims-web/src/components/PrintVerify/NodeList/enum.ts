import { cloneDeep } from '@bmos/utils';
import { ComponentNode } from './type';
// 对象转数组方法
export const objectToArray = (obj: any) => {
  return Object.keys(obj).map(key => cloneDeep(obj[key]));
};

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

export type NODE = Pick<ComponentNode, 'componentType' | 'componentName' | 'node_type'> & {
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

export enum BUSINESS_NODE_TYPE {
  BUSINESS_PRODUCT_INFO = 'BUSINESS_PRODUCT_INFO',
  BUSINESS_FORMULA_INFO = 'BUSINESS_FORMULA_INFO',
}

export type BUSINESS_NODE = Pick<ComponentNode, 'componentType' | 'componentName' | 'children' | 'node_type'> & {
  icon?: string;
};

export type BUSINESS_NODE_INFO_TYPE = {
  [p in keyof typeof BUSINESS_NODE_TYPE as string]: BUSINESS_NODE;
};

export const BUSINESS_PRODUCT_INFO_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  BUSINESS_PRODUCT_INFO_NAME: {
    componentType: 'BUSINESS_PRODUCT_INFO_NAME',
    componentName: t('产品名称'),
    node_type: '',
    icon: 'TEXT',
  },
  BUSINESS_PRODUCT_INFO_CODE: {
    componentType: 'BUSINESS_PRODUCT_INFO_CODE',
    componentName: t('产品编码'),
    node_type: '',
    icon: 'TEXT',
  },
  BUSINESS_PRODUCT_INFO_SPECIFICATION: {
    componentType: 'BUSINESS_PRODUCT_INFO_SPECIFICATION',
    componentName: t('产品规格'),
    node_type: '',
    icon: 'TEXT',
  },
  BUSINESS_PRODUCT_INFO_PROCESS_NAME: {
    componentType: 'BUSINESS_PRODUCT_INFO_PROCESS_NAME',
    componentName: t('工艺名称'),
    node_type: '',
    icon: 'TEXT',
  },
  BUSINESS_PRODUCT_INFO_BATCHNO: {
    componentType: 'BUSINESS_PRODUCT_INFO_BATCHNO',
    componentName: t('生产批号'),
    node_type: '',
    icon: 'TEXT',
  },
  BUSINESS_PRODUCT_INFO_BATCH: {
    componentType: 'BUSINESS_PRODUCT_INFO_BATCH',
    componentName: t('生产批量'),
    node_type: '',
    icon: 'TEXT',
  },
  BUSINESS_PRODUCT_INFO_UNIT: {
    componentType: 'BUSINESS_PRODUCT_INFO_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
};

export const BUSINESS_FORMULA_INFO_MATERIAL_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  BUSINESS_FORMULA_INFO_MATERIAL_NAME: {
    componentType: 'BUSINESS_FORMULA_INFO_MATERIAL_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  BUSINESS_FORMULA_INFO_MATERIAL_CODE: {
    componentType: 'BUSINESS_FORMULA_INFO_MATERIAL_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  BUSINESS_FORMULA_INFO_MATERIAL_SPECIFICATION: {
    componentType: 'BUSINESS_FORMULA_INFO_MATERIAL_SPECIFICATION',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  BUSINESS_FORMULA_INFO_THEORETICAL_QUANTITY: {
    componentType: 'BUSINESS_FORMULA_INFO_THEORETICAL_QUANTITY',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  BUSINESS_FORMULA_INFO_UNIT: {
    componentType: 'BUSINESS_FORMULA_INFO_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
};

export const BUSINESS_FORMULA_INFO_MATERIAL: BUSINESS_NODE_INFO_TYPE = {
  BUSINESS_FORMULA_INFO_MATERIAL: {
    componentType: 'BUSINESS_FORMULA_INFO_MATERIAL',
    componentName: t('生产BOM物料'),
    node_type: '',
    children: objectToArray(BUSINESS_FORMULA_INFO_MATERIAL_CHILDREN) as BUSINESS_NODE[],
  },
};
export const BUSINESS_NODE_INFO: BUSINESS_NODE_INFO_TYPE = {
  BUSINESS_PRODUCT_INFO: {
    componentType: 'BUSINESS_PRODUCT_INFO',
    componentName: t('生产信息'),
    node_type: '',
    children: objectToArray(BUSINESS_PRODUCT_INFO_CHILDREN) as BUSINESS_NODE[],
  },
  BUSINESS_FORMULA_INFO: {
    componentType: 'BUSINESS_FORMULA_INFO',
    componentName: t('生产BOM信息'),
    node_type: '',
    children: objectToArray(BUSINESS_FORMULA_INFO_MATERIAL) as BUSINESS_NODE[],
  },
};
// 所有基础节点信息
export const ALL_NODE_INFO = {
  ...NODE_INFO,
  ...BUSINESS_PRODUCT_INFO_CHILDREN,
  ...BUSINESS_FORMULA_INFO_MATERIAL_CHILDREN,
};

// 可以编辑的节点类型
export const EDITABLE_NODE_TYPE = [NODE_TYPE.RADIO, NODE_TYPE.CHECKBOX];

// 业务组件父级节点（可以删除）
export const BUSINESS_NODE_PARENT = [
  BUSINESS_NODE_TYPE.BUSINESS_PRODUCT_INFO,
  BUSINESS_NODE_TYPE.BUSINESS_FORMULA_INFO,
];
