import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------检验过程检查---------------

/**
 * @description: 检验报告检查 /report/check
 */
export const checkReport = (data: any) => {
  return request({
    url: `${BASE_URL}/report/check`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 打印控制点记录 /report/check-report
 */
export const printCheckReport = (data: any) => {
  return request({
    url: `${BASE_URL}/report/check-report`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};
