import { t } from '@bmos/i18n';
//弹窗名称
export const modelName: Record<string | string, string> = {
  add: t('新增角色'),
  edit: t('编辑角色'),
  view: t('查看角色'),
};
//编辑新增删除名称类型
export enum modelType {
  editTree = 'editTree',
  addTree = 'addTree',
  deleteTree = 'deleteTree',
}
//弹窗类型数据名称
export enum modalStatus {
  Add = 'add',
  Edit = 'edit',
  View = 'view',
}

export type ActionType = Record<string, Function>;
