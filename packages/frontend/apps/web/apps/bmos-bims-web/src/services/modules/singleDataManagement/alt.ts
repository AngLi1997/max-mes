import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------ALT---------------

/**
 * @description: 分页查询 /alt/page
 */
export const getALTList = (data: any) => {
  return request({
    url: `${BASE_URL}/alt/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 二级列表 /alt/second-page
 */
export const getALTSecondList = (data: any) => {
  return request({
    url: `${BASE_URL}/alt/second-page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 批次号发布 /alt/batch-publish
 */
export const batchPublishALT = (params: any) => {
  return request({
    url: `${BASE_URL}/alt/batch-publish`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 发布 /alt/publish
 */
export const publishALT = (data: any) => {
  return request({
    url: `${BASE_URL}/alt/publish`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 批次号核对 /alt/batch-check
 */
export const batchCheckALT = (params: any) => {
  return request({
    url: `${BASE_URL}/alt/batch-check`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 核对 /alt/check
 */
export const checkALT = (data: any) => {
  return request({
    url: `${BASE_URL}/alt/check`,
    method: 'POST',
    data,
  });
};
