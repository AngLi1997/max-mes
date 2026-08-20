import { BASE_URL } from '@/services/baseUrl';
import { getItem } from '@/utils';
import axios from '@bmos/axios';
import request from '../../service';

// ---------------放行单管理---------------

/**
 * @description: 分页查询 /quality-guarantee/release-note/page
 */
export const getReleaseManagementList = (data: any) => {
  return request({
    url: `${BASE_URL}/quality-guarantee/release-note/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 创建放行单 /quality-guarantee/release-note
 */
export const createReleaseNote = (data: any) => {
  return request({
    url: `${BASE_URL}/quality-guarantee/release-note`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 打印放行单 /quality-guarantee/release-note/print
 */
export const printReleaseNote = (data: any) => {
  const config = {
    headers: {
      'Content-Type': 'multipart/form-data',
      token: getItem('BMOS-ACCESS-TOKEN') || '1433e2d1-d9f8-4481-85a8-b8a8979a285b',
      'bmos-access-token': getItem('BMOS-ACCESS-TOKEN') || '1433e2d1-d9f8-4481-85a8-b8a8979a285b',
    },
  };
  return axios.post(`${BASE_URL}/quality-guarantee/release-note/print`, data, config);
};
