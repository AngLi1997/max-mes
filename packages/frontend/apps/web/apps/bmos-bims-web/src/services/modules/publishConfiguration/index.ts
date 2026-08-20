import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';


// ---------------总发布校验配置---------------

/**
 * @description: 查询 /validator/list
 */
export const getValidatorList = () => {
  return request({
    url: `${BASE_URL}/validator/list`,
    method: 'GET',
  });
}

/**
 * @description: 更新 /validator/update
 */
export const updateValidator = (data: any) => {
  return request({
    url: `${BASE_URL}/validator/update`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 用户下拉列表 /user/pullList
 */
export const getUserList = (params?: any) => {
  return request({
    url: `${BASE_URL}/user/pullList`,
    method: 'GET',
    params
  });
}