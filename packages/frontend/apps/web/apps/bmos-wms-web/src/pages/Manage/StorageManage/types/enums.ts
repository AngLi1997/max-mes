export enum Materials {
  Text = 'Text',
}

export enum StorageLevel {
  WORKSHOP = 1, // 车间
  AREA = 2, // 区域
  STORAGE = 3, // 暂存间
  POSITION = 4, // 货位
}

export interface DataItem {
  id: string;
  quantity: string;
  availableQuantity: string;
  consumeQuantity: string;
  expiredDate: string;
  initQuantity: string;
  key: number;
  materialBatchNo: string;
  materialCode: string;
  materialName: string;
  materialNo: string;
  materialPositionId: string;
  materialSpecification: string;
  unit: string;
}
