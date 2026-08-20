import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------不合格血浆库存管理---------------

/**
 * @description: 待入库血浆一级列表 /wms-unqualified-plasma/out/page
 */
export const getWmsUnqualifiedPlasmaOutList = async (data: any) => {
  return await request({
    url: `${BASE_URL}/wms-unqualified-plasma/out/page`,
    method: 'POST',
    data
  })
}

/**
 * @description: 待入库血浆二级列表 /wms-unqualified-plasma/out/detail/page
 */
export const getWmsUnqualifiedPlasmaOutDetailList = async (data: any) => {
  return await request({
    url: `${BASE_URL}/wms-unqualified-plasma/out/detail/page`,
    method: 'POST',
    data
  })
}

/**
 * @description: 血浆回库 /wms-unqualified-plasma/in/{batchNo}
 */
export const unqualifiedPlasmaInWarehouseIn = (batchNo: any) => {
  return request({
    url: `${BASE_URL}/wms-unqualified-plasma/in/${batchNo}`,
    method: 'PUT',
  });
}

/**
 * @description: 已入库血浆一级列表 /wms-unqualified-plasma/in/page
 */
export const getWmsUnqualifiedPlasmaInList = async (data: any) => {
  return await request({
    url: `${BASE_URL}/wms-unqualified-plasma/in/page`,
    method: 'POST',
    data
  })
}

/**
 * @description: 已入库血浆二级列表 /wms-unqualified-plasma/in/detail/page
 */
export const getWmsUnqualifiedPlasmaInDetailList = async (data: any) => {
  return await request({
    url: `${BASE_URL}/wms-unqualified-plasma/in/detail/page`,
    method: 'POST',
    data
  })
}

/**
 * @description: 合并出库 /wms-unqualified-plasma/out
 */
export const unqualifiedPlasmaOutWarehouseOut = (params: any) => {
  return request({
    url: `${BASE_URL}/wms-unqualified-plasma/out`,
    method: 'PUT',
    params
  });
}