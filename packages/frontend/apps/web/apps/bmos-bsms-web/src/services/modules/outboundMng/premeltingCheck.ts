import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 预融核对 -----------------

/**
 * @description: 预融核对分页 /prethawing/page
 */
export const getPrethawingPage = (data: any) => {
  return request({
    url: `${BASE_URL}/prethawing/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 预融核对-详情 /prethawing/info
 */
export const getPrethawingInfo = (data: any) => {
  return request({
    url: `${BASE_URL}/prethawing/info`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 预融核对-详情异常列表 /prethawing/errorPage
 */
export const getPrethawingErrorPage = (data: any) => {
  return request({
    url: `${BASE_URL}/prethawing/errorPage`,
    method: 'POST',
    data,
  });
};
