import { t } from '@bmos/i18n';
export enum DatasetType {
  // 批记录数据
  POINT = 'POINT',
  // 动态填报数据
  DYNAMIC_REPORT = 'DYNAMIC_REPORT',
  // 批签发引用
  LOT_RELEASE_LINK = 'LOT_RELEASE_LINK',
}

export const DatasetTypeMap: Map<DatasetType, string> = new Map([
  [DatasetType.POINT, t('批记录数据')],
  [DatasetType.DYNAMIC_REPORT, t('动态填报数据')],
  [DatasetType.LOT_RELEASE_LINK, t('批签发引用')],
]);

export enum OperationType {
  // 新增
  Add = 'add',
  // 编辑
  Edit = 'edit',
  // 查看
  View = 'view',
}

import { ComponentNode } from '@/components/Record';

export enum FormulaFieldType {
  JOIN = 'JOIN',
  DATE = 'DATE',
  STRING = 'STRING',
  NUMBER = 'NUMBER',
  DATEFORMAT = 'DATEFORMAT',
}

export interface FormulaComponentNode extends ComponentNode {
  recordItemId?: any;
  reusable?: boolean;
  procedureStepId?: string;
}

export interface FormulaParsesType {
  index: number;
  key: string;
  showName: string;
  type: string;
  multiple: boolean;
  manualInput: boolean;
  target?: FormulaComponentNode[];
  curSelectTemplateItem: any;
}

export const ERROR_MESSAGE: Record<string, string> = {
  DATE: t('请选择日期组件'),
  TEXT: t('请选择文字组件'),
  NUMBER: t('请选择数字组件'),
};
