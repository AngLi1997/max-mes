export enum NotifyMessageType {
  ALL = 'ALL',
  AUDIT = 'AUDIT',
  WARNING = 'WARNING',
  ALARM = 'ALARM',
}

export enum MessageTabType {
  NOT_READ = 'NOT_READ',
  READ = 'READ',
}

export const NotifyMessageTypeMap: Map<NotifyMessageType, Array<string>> = new Map([
  [NotifyMessageType.AUDIT, ['AUDIT']],
  [
    NotifyMessageType.WARNING,
    [
      'MATERIAL_EXPIRE_FORE_WARNING',
      'LISMS_MATERIAL_EXPIRE_WARNING',
      'LISMS_MATERIAL_INVENTORY_WARNING',
      'LISMS_SUPPLIER_EXPIRE_WARNING',
    ],
  ],
  [NotifyMessageType.ALARM, ['DATA_OUT_LIMIT_WARNING', 'PRODUCT_MODIFY_ABNORMAL_WARNING', 'EQUIPMENT_DEFAULT_WARNING']],
]);

export interface NotifyMessageItemType {
  id: string;
  title: string;
  msgContent: string;
  remark: string;
  updateTime: string;
  type: NotifyMessageType;
  isRead?: boolean;
}
