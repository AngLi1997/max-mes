import { t } from '@bmos/i18n';
//状态
export enum modalStatus {
  Add = 'add',
  Edit = 'edit',
  View = 'view',
}
//名称
export const modelName: Record<string | string, string> = {
  add: t('新增标签'),
  edit: t('编辑标签'),
  view: t('查看标签'),
};
//获取emit数据
export type UseTableParams = {
  emits: any;
};

//From表单字段
export interface User {
  label: string; //名称
  dataSourceField: string; //下拉框ID
  defineField: string; //id
}

export type ActionType = Record<string, Function>;