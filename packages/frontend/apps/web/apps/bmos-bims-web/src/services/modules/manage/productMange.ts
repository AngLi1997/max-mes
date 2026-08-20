import request from '../../service';

/**
 * @description: 仓库货品 - 分页查询货品 /api/app/wms/inventory/inventoryPageByCargoId
 */
export const reqInventoryInventoryPageByCargoId = (params: any) => {
  return request({
    url: `/app/wms/inventory/inventoryPageByCargoId`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 仓库货品 - 分页查询货品批次（包含可用量为0的） /api/app/wms/inventory/batchPageByCargoIds
 */
export const reqInventoryBatchPageByCargoIds = (params: any) => {
  return request({
    url: `/app/wms/inventory/batchPageByCargoIds`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 仓库货品 - 编辑批次 /api/app/wms/inventory/editInventoryBatch
 */
export const reqInventoryEditInventoryBatch = (data: any) => {
  return request({
    url: `/app/wms/inventory/editInventoryBatch`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 仓库货品 - 新增批次 /api/app/wms/inventory/addInventoryBatch
 */
export const reqInventoryAddInventoryBatch = (data: any) => {
  return request({
    url: `/app/wms/inventory/addInventoryBatch`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 仓库货品 - 根据批次id查询批次详情 /api/app/wms/inventory/queryInventoryBatchById
 * @param {string} inventoryBatchId 批次id
 */
export const reqInventoryQueryInventoryBatchById = (inventoryBatchId: string) => {
  return request({
    url: `/app/wms/inventory/queryInventoryBatchById`,
    method: 'GET',
    params: { inventoryBatchId },
  });
};

/**
 * @description: 仓库货品 - 新增货品件 /api/app/wms/inventory/addInventory
 */
export const reqInventoryAddInventory = (data: any) => {
  return request({
    url: `/app/wms/inventory/addInventory`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 仓库货品 - 根据批次id查询批次详情 /api/app/wms/inventory/listByCargoIdAndBatchId
 * @param {string} cargoId 货品id
 * @param {string} inventoryBatchId 批次id
 * @return {Promise<any>}
 */
export const reqInventoryListByCargoIdAndBatchId = (cargoId: string, inventoryBatchId?: string) => {
  return request({
    url: `/app/wms/inventory/listByCargoIdAndBatchId`,
    method: 'GET',
    params: { cargoId, ...(inventoryBatchId && { inventoryBatchId }) },
  });
};
