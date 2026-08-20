import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';


// ---------------供应商信息---------------

/**
 * @description: 分页查询 /supplier/page
 */
export const getSupplierList = (data: any) => {
  return request({
    url: `${BASE_URL}/supplier/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 下拉列表 /supplier/list
 */
export const getSupplierSelectList = () => {
  return request({
    url: `${BASE_URL}/supplier/list`,
    method: 'GET',
  });
}

/**
 * @description: 详情 /supplier/{id}
 */
export const getSupplierDetail = (id: number) => {
  return request({
    url: `${BASE_URL}/supplier/${id}`,
    method: 'GET',
  });
}

/**
 * @description: 新增 /supplier/create
 */
export const createSupplier = (data: any) => {
  return request({
    url: `${BASE_URL}/supplier/create`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 编辑 /supplier/update
 */
export const updateSupplier = (data: any) => {
  return request({
    url: `${BASE_URL}/supplier/update`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 删除 /supplier/{id}
 */
export const deleteSupplier = (id: number) => {
  return request({
    url: `${BASE_URL}/supplier/${id}`,
    method: 'DELETE',
  });
}