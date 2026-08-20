import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------物料库存管理---------------

/**
 * @description: 分页查询 /material/inventory/first/page
 */
export const getMaterialInventoryPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/inventory/first/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 二级分页查询 /material/inventory/second/page
 */
export const getMaterialInventorySecondPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/inventory/second/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 入库物料详情 /material/inventory/detail
 */
export const getMaterialInventoryDetail = (data: any) => {
  return request({
    url: `${BASE_URL}/material/inventory/detail`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 入库物料详情-入库记录分页查询 /material/inventory/record/page
 */
export const getMaterialInventoryRecordPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/inventory/record/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 编辑质控品含量 /material/inventory/edit
 */
export const editMaterialInventory = (params: any) => {
  return request({
    url: `${BASE_URL}/material/inventory/edit`,
    method: 'PUT',
    params,
  });
};

/**
 * @description: 货位卡 /material/inventory/storageCard/{inWarehouseNo}
 */
export const getMaterialInventoryStorageCard = (inWarehouseNo: any) => {
  return request({
    url: `${BASE_URL}/material/inventory/storageCard/${inWarehouseNo}`,
    method: 'GET',
  });
};

/**
 * @description: 物料操作数据回显 /material/inventory/operation/{inWarehouseNo}
 */
export const getMaterialInventoryOperation = (inWarehouseNo: any) => {
  return request({
    url: `${BASE_URL}/material/inventory/operation/${inWarehouseNo}`,
    method: 'GET',
  });
};

/**
 * @description: 物料领用前校验 /material/use/valid/receive/{materialInstanceIdentify}
 */
export const getMaterialUseValidReceive = (materialInstanceIdentify: any) => {
  return request({
    url: `${BASE_URL}/material/use/valid/receive/${materialInstanceIdentify}`,
    method: 'GET',
  });
};

/**
 * @description: 物料领用 /material/use/receive
 */
export const materialUseReceive = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/receive`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 物料报废 /material/use/scrap
 */
export const materialUseScrap = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/scrap`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 物料退货 /material/use/return-material
 */
export const materialUseReturn = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/return-material`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 物料抽检 /material/use/spot-check
 */
export const materialUseSpotCheck = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/spot-check`,
    method: 'PUT',
    data: {
      materialInstanceIdentify: data.materialInstanceIdentify,
      inWarehouseNo: data.inWarehouseNo,
    },
  });
};
