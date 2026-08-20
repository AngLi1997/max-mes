import { ComponentNode } from '../type';

export enum NODE_TYPE {
  TEXT = 'TEXT',
  NUMBER = 'NUMBER',
  RADIO = 'RADIO',
  DATE = 'DATE',
  SUBMIT_SIGN = 'SUBMIT_SIGN',
  HANDLE_SUBMIT_SIGN = 'HANDLE_SUBMIT_SIGN',
  ATTACHMENT = 'ATTACHMENT',
  CHECKBOX = 'CHECKBOX',
  SELECT = 'SELECT',
  REVIEW_SIGN = 'REVIEW_SIGN',
  HANDLE_REVIEW_SIGN = 'HANDLE_REVIEW_SIGN',
  TIME = 'TIME',
  PHOTO = 'PHOTO',
}

export type NODE = Pick<ComponentNode, 'componentType' | 'componentName' | 'node_type'> & {
  icon?: string;
};

export type NODE_INFO_TYPE = {
  [p in keyof typeof NODE_TYPE as string]: NODE;
};

export enum BUSINESS_NODE_TYPE {
  BUSINESS_PRODUCT_INFO = 'BUSINESS_PRODUCT_INFO',
  BUSINESS_FORMULA_INFO = 'BUSINESS_FORMULA_INFO',
}

export type BUSINESS_NODE_INFO_TYPE = {
  [p in keyof typeof BUSINESS_NODE_TYPE as string]: BUSINESS_NODE;
};

export type BUSINESS_NODE = Pick<ComponentNode, 'componentType' | 'componentName' | 'children' | 'node_type' | 'permission'> & {
  icon?: string;
};
