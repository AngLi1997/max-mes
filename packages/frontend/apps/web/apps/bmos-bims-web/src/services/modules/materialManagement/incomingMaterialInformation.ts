import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';


// ---------------入库物料信息---------------

/**
 * @description: 分页查询 /storage/page
 */
export const getIncomingMaterialList = (data: any) => {
  return request({
    url: `${BASE_URL}/storage/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 详情 /storage/{id}
 */
export const getIncomingMaterialDetail = (id: number) => {
  return request({
    url: `${BASE_URL}/storage/${id}`,
    method: 'GET',
  });
}

/**
 * @description: 新增 /storage/create
 */
export const createIncomingMaterial = (data: any) => {
  return request({
    url: `${BASE_URL}/storage/create`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 编辑 /storage/update
 */
export const updateIncomingMaterial = (data: any) => {
  return request({
    url: `${BASE_URL}/storage/update`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 删除 /storage/{id}
 */
export const deleteIncomingMaterial = (id: number) => {
  return request({
    url: `${BASE_URL}/storage/${id}`,
    method: 'DELETE',
  });
}