import { BASE_URL } from '@/services/baseUrl';
import { getItem } from '@/utils';
import axios from '@bmos/axios';
import request from '../../service';

// ---------------免疫类型管理---------------

/**
 * @description: 分页查询 /immunity/page
 */
export const getImmunetypeList = (data: any) => {
  return request({
    url: `${BASE_URL}/immunity/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 根据id查询 /immunity/{id}/{id}
 */
export const getImmunetypeById = (id: string) => {
  return request({
    url: `${BASE_URL}/immunity/${id}`,
    method: 'GET',
  });
};

/**
 * @description: 编辑 /immunity/update
 */
export const updateImmunetype = (data: any) => {
  return request({
    url: `${BASE_URL}/immunity//update`,
    method: 'PUT',
    data,
  });
};