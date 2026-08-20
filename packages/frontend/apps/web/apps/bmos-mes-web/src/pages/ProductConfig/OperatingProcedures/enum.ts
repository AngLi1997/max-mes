//状态
export enum modalStatus {
  Add = 'add',
  Edit = 'edit',
  View = 'view',
  Delete = 'delete',
  Copy = 'copy',
}
//名称
export const modelName: Record<string | string, string> = {
  add: t('新增文件'),
  edit: t('编辑文件'),
  view: t('查看文件'),
  copy: t('新增版本'),
};
//版本状态
export const circularStatus: Record<string, string> = {
  edit: 'primary',
  confirm: 'primary',
  audit: 'warning',
  valid: 'success',
  invalid: 'default',
  wait_valid: 'warning',
};
//版本控制按钮
export enum VersionStatus {
  edit = 'edit',
  confirm = 'confirm',
  audit = 'audit',
  valid = 'valid',
  invalid = 'invalid',
  wait_valid = 'wait_valid',
}
export type ActionType = Record<string, Function>;
//上传参数
export interface UploadParams {
  file: string;
  type: string;
  streamingMedia: Object;
}
