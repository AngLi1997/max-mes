import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------血浆库存管理---------------

/**
 * @description: 待入库血浆一级列表 /wms-plasma/out/page
 */
export const getWmsPlasmaOutList = async (data: any) => {
  return await request({
    url: `${BASE_URL}/wms-plasma/out/page`,
    method: 'POST',
    data
  })
}

/**
 * @description: 待入库血浆二级列表 /wms-plasma/out/detail/page
 */
export const getWmsPlasmaOutDetailList = async (data: any) => {
  return await request({
    url: `${BASE_URL}/wms-plasma/out/detail/page`,
    method: 'POST',
    data
  })
}

/**
 * @description: 血浆回库 /wms-plasma/in/{batchNo}
 */
export const plasmaInWarehouseIn = (batchNo: any) => {
  return request({
    url: `${BASE_URL}/wms-plasma/in/${batchNo}`,
    method: 'PUT',
  });
}

/**
 * @description: 已入库血浆一级列表 /wms-plasma/in/page
 */
export const getWmsPlasmaInList = async (data: any) => {
  return await request({
    url: `${BASE_URL}/wms-plasma/in/page`,
    method: 'POST',
    data
  })
}

/**
 * @description: 已入库血浆二级列表 /wms-plasma/in/detail/page
 */
export const getWmsPlasmaInDetailList = async (data: any) => {
  return await request({
    url: `${BASE_URL}/wms-plasma/in/detail/page`,
    method: 'POST',
    data
  })
}

/**
 * @description: 合并出库 /wms-plasma/out
 */
export const plasmaOutWarehouseOut = (params: any) => {
  return request({
    url: `${BASE_URL}/wms-plasma/out`,
    method: 'PUT',
    params
  });
}