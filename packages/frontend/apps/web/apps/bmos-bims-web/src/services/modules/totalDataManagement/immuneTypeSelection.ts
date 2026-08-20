import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';


// ---------------免疫类型选择---------------

/**
 * @description: 免疫类型选择-一级列表 /titer/first-page
 */
export const getImmunityTypeList = (data: any) => {
  return request({
    url: `${BASE_URL}/titer/first-page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 免疫类型选择-二级列表 /titer/second-page
 */
export const getImmunityTypeSecondList = (data: any) => {
  return request({
    url: `${BASE_URL}/titer/second-page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 选择 /titer/select
 */
export const selectImmunityType = (params: any) => {
  return request({
    url: `${BASE_URL}/titer/select`,
    method: 'GET',
    params,
  });
}
