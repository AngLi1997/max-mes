export interface TableParams {
  pageRef: Ref<any>;
  firstRowData: Ref<any>;
  updateTableData?: () => void;
}
/* 
  @description  菜单标识枚举
*/
export enum MenuIdentifyEnum {
  GLOBAL_PARAMETER_SETTING = 'GM020', // 全局参数设置
  INSPECTION_PARAMETER_SETTING = 'GM021', // 检验参数设置
  RECEIVING_LIBRARY_SETTING = 'GM022', // 领用库设置
  MATERIAL_PARAMETER_SETTING = 'GM023', // 物料参数设置
  SINGLE_PLASMA_STATION_SETTING = 'GM024', // 单采血浆站设置
  ROUNDING_RULE_SETTING = 'GM025', // 修约规则设置
  ROUNDING_PARAMETER_SETTING = 'GM026', // 修约参数设置
}

// 枚举值类型枚举
export enum enumsTypeEnum {
  NAME = 'NAME',
  SWITCH = 'SWITCH',
  DATE = 'DATE',
  HOUR = 'HOUR',
  COLOUR = 'COLOUR',
  NUMBER = 'NUMBER',
}
