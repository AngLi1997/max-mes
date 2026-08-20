import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';


// ---------------物料基础信息---------------

/**
 * @description: 分页查询 /material/page
 */
export const getMaterialList = (data: any) => {
  return request({
    url: `${BASE_URL}/material/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 下拉列表 /storage/list
 */
export const getStorageSelectList = (data?: any) => {
  return request({
    url: `${BASE_URL}/storage/list`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 下拉列表 /material/list
 */
export const getMaterialSelectList = (params?: any) => {
  return request({
    url: `${BASE_URL}/material/list`,
    method: 'GET',
    params,
  });
}

/**
 * @description: 详情 /material/{id}
 */
export const getMaterialDetail = (id: number) => {
  return request({
    url: `${BASE_URL}/material/${id}`,
    method: 'GET',
  });
}

/**
 * @description: 新增 /material/create
 */
export const createMaterial = (data: any) => {
  return request({
    url: `${BASE_URL}/material/create`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 编辑 /material/update
 */
export const updateMaterial = (data: any) => {
  return request({
    url: `${BASE_URL}/material/update`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 删除 /material/{id}
 */
export const deleteMaterial = (id: number) => {
  return request({
    url: `${BASE_URL}/material/${id}`,
    method: 'DELETE',
  });
}