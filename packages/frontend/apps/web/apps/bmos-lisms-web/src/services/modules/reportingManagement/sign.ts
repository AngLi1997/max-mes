import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------检验报告签发---------------

/**
 * @description: 签发检验报告 /report/sign
 */
export const signReport = (data: any) => {
  return request({
    url: `${BASE_URL}/report/sign`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 再次签发 /report/resign
 */
export const reSignReport = (data: any) => {
  return request({
    url: `${BASE_URL}/report/resign`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 撤销检验报告 /report/sign-back
 */
export const backReport = (data: any) => {
  return request({
    url: `${BASE_URL}/report/sign-back`,
    method: 'POST',
    data,
  });
};
