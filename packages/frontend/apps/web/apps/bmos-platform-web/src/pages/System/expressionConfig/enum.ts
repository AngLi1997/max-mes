import { t } from '@bmos/i18n';
//弹窗名称
export const modelName: Record<string | string, string> = {
  add: t('新增公式'),
  edit: t('编辑公式'),
  view: t('查看公式'),
};
//弹窗类型数据名称
export enum modalStatus {
  Add = 'add',
  Edit = 'edit',
  View = 'view',
}

//编辑新增删除名称类型
export enum modelType {
  editTree = 'editTree',
  addTree = 'addTree',
  deleteTree = 'deleteTree',
}

export type ActionType = Record<string, Function>;
