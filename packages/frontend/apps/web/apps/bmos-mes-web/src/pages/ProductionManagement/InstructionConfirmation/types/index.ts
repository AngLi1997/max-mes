export interface descriptionsDataType {
  productName: string; //产品名称
  productMergeCode: string; //产品编码
  productSpecification: string; //产品规格
  processName: string; //生产工艺
  productLineName: string; //产线
  productionBatch: string; //生产批量
  planNo: string; //计划编号
  batchNo: string; //生产批号
  productDate: string; //计划生产时间
  type: string; //指令单类型
}
export interface descriptionsDomType {
  label: string;
  key:
  | 'productName'
  | 'productMergeCode'
  | 'productSpecification'
  | 'processName'
  | 'productLineName'
  | 'productionBatch'
  | 'planNo'
  | 'batchNo'
  | 'productDate'
  | 'type';
}
