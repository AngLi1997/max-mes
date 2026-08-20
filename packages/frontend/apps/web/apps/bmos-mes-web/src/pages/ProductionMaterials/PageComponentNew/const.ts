export enum MaterialTypeMap {
  RawMaterial = 0, // 原辅包
  MiddleProduct = 1, // 中间品
  Product = 2, // 产品
}
//  0 | 1 | 2
export type MaterialTypeValue = 0 | 1 | 2;

export type MaterialType = `${MaterialTypeMap}`;
