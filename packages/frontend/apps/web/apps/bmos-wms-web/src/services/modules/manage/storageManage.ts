import request from '../../service';

/**
 * @description: 暂存间数据树带货位树 /api/app/wms/storage/config/queryTreeWithCargoPosition
 */
export const reqStorageConfigQueryAllTreeWithCargoPosition = () => {
  return request({
    url: `/app/wms/storage/config/queryTreeWithCargoPosition`,
    method: 'GET',
  });
};

/**
 * @description: 分页查询暂存货品批次 /api/app/wms/storage/cargo/batchPage
 * @param {any} params
 */
export const reqStorageCargoBatchPage = (params: any) => {
  return request({
    url: `/app/wms/inventory/batchPage`,
    method: 'get',
    params,
  });
};

/**
 * @description: 盘库 /api/app/wms/inventory/check
 * @param {any} data
 */
export const reqInventoryCheck = (data: any) => {
  return request({
    url: `/app/wms/inventory/check`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 货品入库 /api/app/wms/inventory/inbound
 * @param {any} data
 */
export const reqStorageInventoryInbound = (data: any) => {
  return request({
    url: `/app/wms/inventory/inbound`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 根据货品件id查询货品件详情 /api/app/wms/storage/cargo/info
 * @param {any} params
 */
export const reqStorageCargoInfo = (params: any) => {
  return request({
    url: `/app/wms/storage/cargo/info`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 移库 /api/app/wms/inventory/move
 * @param {any} data
 */
export const reqInventoryMove = (data: any) => {
  return request({
    url: `/app/wms/inventory/move`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 出库 /api/app/wms/inventory/outbound
 * @param {any} data
 */
export const reqInventoryOutbound = (data: any) => {
  return request({
    url: `/app/wms/inventory/outbound`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 分页查询货品件 /api/app/wms/storage/cargo/page
 * @param {any} params
 */
export const reqStorageCargoPage = (params: any) => {
  return request({
    url: `/app/wms/inventory/inventoryPageByBatchId`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 分页查询货品件 /api/app/wms/material/position/listBoundUser
 * @param {string} positionId 货位id
 */
export const reqMaterialPositionListBoundUser = (positionId: string) => {
  return request({
    url: `/app/wms/material/position/listBoundUser`,
    method: 'GET',
    params: {
      positionId,
    },
  });
};

/**
 * @description: 根据货品id查询货品批次列表 /api/app/wms/storage/cargo/batch/listByMaterialId
 * @param {string} materialId 货品id
 * @param {string} batchNo 批次号
 */
export const reqCargoBatchListByMaterialId = (materialId: string, batchNo?: string) => {
  return request({
    url: `/app/wms/storage/cargo/batch/listByMaterialId`,
    method: 'GET',
    params: {
      materialId,
      batchNo,
    },
  });
};
//货品件详情信息 /api/app/wms/inventory/listByBatchIdAndPositionId
export const reqInventoryListByBatchIdAndPositionId = (params: any) => {
  return request({
    url: `/app/wms/inventory/listByBatchIdAndPositionId`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 根据父级id查询存储区域数据树(带有货品列表) /api/app/wms/cargo/category/queryTreeWithCargo
 */
export const reqCargoCategoryQueryTreeWithCargo = () => {
  return request({
    url: `/app/wms/cargo/category/queryTreeWithCargo`,
    method: 'GET',
  });
};

/**
 * @description: 根据父级id查询存储区域数据树(带有货品列表) /api/app/wms/cargo/category/queryTree
 */
export const reqCargoCategoryQueryTree = () => {
  return request({
    url: `/app/wms/cargo/category/queryTree`,
    method: 'GET',
  });
};

/**
 * @description: 根据物料id查询货品批次列表 /api/app/wms/inventory/listByCargoIdAndBatchNo
 * @param {string} cargoId 货品id
 * @param {string} inventoryBatchNo 批次号 可选
 */
export const reqInventoryListByCargoIdAndBatchNo = (cargoId: string, inventoryBatchNo?: string) => {
  return request({
    url: `/app/wms/inventory/listByCargoIdAndBatchNo`,
    method: 'GET',
    params: {
      cargoId,
      ...(inventoryBatchNo && { inventoryBatchNo }),
    },
  });
};

/**
 * @description: 根据货品id查询货品详情 /api/app/wms/inventory/queryInventoryById
 * @param {string} inventoryId 货品件id
 */
export const reqInventoryQueryInventoryById = (inventoryId: string) => {
  return request({
    url: `/app/wms/inventory/queryInventoryById`,
    method: 'GET',
    params: {
      inventoryId,
    },
  });
};
