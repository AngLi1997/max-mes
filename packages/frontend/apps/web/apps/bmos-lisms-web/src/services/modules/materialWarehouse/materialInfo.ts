import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------物料信息---------------

/**
 * @description: 分页查询 /material/page
 */
export const getMaterialInfoPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 新增物料信息 /material/save
 */
export const addMaterialInfo = (data: any) => {
  return request({
    url: `${BASE_URL}/material/save`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 修改物料信息 /material/update
 */
export const updateMaterialInfo = (data: any) => {
  return request({
    url: `${BASE_URL}/material/update`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 删除物料信息 /material/remove
 */
export const deleteMaterialInfo = (data: any) => {
  return request({
    url: `${BASE_URL}/material/remove`,
    method: 'DELETE',
    data,
  });
};
