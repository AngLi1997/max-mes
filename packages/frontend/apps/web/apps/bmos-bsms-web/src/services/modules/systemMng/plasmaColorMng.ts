import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------库存血浆颜色管理---------------

/**
 * @description: 分页查询 /colour/page
 */
export const getPlasmaColorList = (data: any) => {
  return request({
    url: `${BASE_URL}/colour/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 根据id查询 /colour/{id}
 */
export const getPlasmaColorById = (id: string) => {
  return request({
    url: `${BASE_URL}/colour/${id}`,
    method: 'GET',
  });
};

/**
 * @description: 编辑 /colour/update
 */
export const updatePlasmaColor = (data: any) => {
  return request({
    url: `${BASE_URL}/colour/update`,
    method: 'PUT',
    data,
  });
};
