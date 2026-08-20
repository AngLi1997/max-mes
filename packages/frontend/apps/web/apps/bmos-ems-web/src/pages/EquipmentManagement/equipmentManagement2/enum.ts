import { t } from '@bmos/i18n';

//状态
export enum modalStatus {
  Add = 'add',
  Edit = 'edit',
  View = 'view',
  Delete = 'delete',
}

//获取emit数据
export type UseTableParams = {
  emits: any;
  UseColumns: any;
  props: any;
};

//名称
export const modelName: Record<string | string, string> = {
  add: t('新增设备'),
  edit:t('编辑设备'),
  view:t( '查看设备'),
};

export type ActionType = Record<string, Function>;

//标签属性状态
export const tagStatus: Record<string | string, string> = {
  CLEAN_001: t('清洁'),
  DISINFECT_002: t('消毒'),
  CALIBRATION_003: t('校准'),
};

//标签属性KEY
export type tagKey = {
  equipmentPropertyVOList: Array<object>;
  equipmentStatusVOList: Array<object>;
};

//采集点类型
export const pointType: Record<string | string, string> = {
  ATTR: t('属性'),
  SERVICE: t('服务'),
  EVENT: t('事件'),
};
