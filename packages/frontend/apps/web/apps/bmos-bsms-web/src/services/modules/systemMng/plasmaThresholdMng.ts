import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------在库血浆阈值管理---------------

/**
 * @description: 分页查询 /threshold/page
 */
export const getPlasmaThresholdList = (data: any) => {
  return request({
    url: `${BASE_URL}/threshold/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 根据id查询 /threshold/{id}
 */
export const getPlasmaThresholdById = (id: string) => {
  return request({
    url: `${BASE_URL}/threshold/${id}`,
    method: 'GET',
  });
};

/**
 * @description: 编辑 /threshold/update
 */
export const updatePlasmaThreshold = (data: any) => {
  return request({
    url: `${BASE_URL}/threshold/update`,
    method: 'PUT',
    data,
  });
};
