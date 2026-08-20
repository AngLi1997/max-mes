import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 合并标本 ---------------

/**
 * @description: 根据箱号获取标本列表 /sample/merge/list
 */
export const getSampleAmalgamationList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/merge/list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 扫描 /sample/merge/scan
 */
export const sampleAmalgamationScan = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/merge/scan`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 提交合并 /sample/merge/merge
 */
export const sampleAmalgamationMerge = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/merge/merge`,
    method: 'POST',
    data,
  });
};
