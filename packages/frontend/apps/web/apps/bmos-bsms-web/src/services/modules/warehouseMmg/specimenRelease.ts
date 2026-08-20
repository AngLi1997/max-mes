import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------标本库存管理---------------

/**
 * @description: 样本库存管理一级列表查询 /wms-sample/page-list
 */
export const getWmsQualifiedSampleList = async (data: any) => {
  return await request({
    url: `${BASE_URL}/wms-sample/page-list`,
    method: 'POST',
    data
  })
}

/**
 * @description: 样本库存管理二级列表查询 /wms-sample/second-list
 */
export const getWmsQualifiedSampleSecondList = async (data: any) => {
  return await request({
    url: `${BASE_URL}/wms-sample/second-list`,
    method: 'POST',
    data
  })
}

/**
 * @description: 样本出库任务下发 /wms-sample/out
 */
export const qualifiedSampleOutWarehouseOut = (data: any) => {
  return request({
    url: `${BASE_URL}/wms-sample/out`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 样本回库任务下发 /wms-sample/back
 */
export const qualifiedSampleBackWarehouseOut = (data: any) => {
  return request({
    url: `${BASE_URL}/wms-sample/back`,
    method: 'POST',
    data,
  });
}