import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------物料入库---------------

/**
 * @description: 分页查询 /material/inWarehouse/page
 */
export const getMaterialInWarehousePage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/inWarehouse/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 物料接收详情 /material/inWarehouse/detail/{identify}
 */
export const getMaterialInWarehouseDetail = (identify: string) => {
  return request({
    url: `${BASE_URL}/material/inWarehouse/detail/${identify}`,
    method: 'GET',
  });
};

/**
 * @description: 物料接收编辑 /material/inWarehouse/edit
 */
export const editMaterialInWarehouse = (data: any) => {
  return request({
    url: `${BASE_URL}/material/inWarehouse/edit`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 物料入库 /material/inWarehouse
 */
export const materialInWarehouse = (data: any) => {
  return request({
    url: `${BASE_URL}/material/inWarehouse`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 物料撤销接收 /material/receive/cancel
 */
export const materialReceiveCancel = (data: any) => {
  return request({
    url: `${BASE_URL}/material/receive/cancel`,
    method: 'DELETE',
    data,
  });
};
