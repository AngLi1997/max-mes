import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------供应商信息---------------

/**
 * @description: 分页查询供应商 /material/supplier/page
 */
export const getMaterialSupplierPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/supplier/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 新增供应商 /material/supplier/save
 */
export const addMaterialSupplier = (data: any) => {
  return request({
    url: `${BASE_URL}/material/supplier/save`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 修改供应商 /material/supplier/update
 */
export const updateMaterialSupplier = (data: any) => {
  return request({
    url: `${BASE_URL}/material/supplier/update`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 删除供应商 /material/supplier/remove
 */
export const deleteMaterialSupplier = (data: any) => {
  return request({
    url: `${BASE_URL}/material/supplier/remove`,
    method: 'DELETE',
    data,
  });
};

/**
 * @description: 获取供应商下拉列表 /material/supplier/list
 */
export const getMaterialSupplierList = () => {
  return request({
    url: `${BASE_URL}/material/supplier/list`,
    method: 'GET',
  });
};
