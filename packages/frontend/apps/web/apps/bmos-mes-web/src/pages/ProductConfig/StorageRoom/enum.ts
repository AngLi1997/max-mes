//弹窗名称
export const modelName: Record<string | string, string> = {
  add: '新增暂存货位',
  edit: '编辑暂存货位',
  view: '查看暂存货位',
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

export enum materials {
  Text = 'Text',
}

export type ActionType = Record<string, Function>;
