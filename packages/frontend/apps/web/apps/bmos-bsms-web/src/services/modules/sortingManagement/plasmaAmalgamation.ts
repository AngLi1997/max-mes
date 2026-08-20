import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 合并血浆 ---------------

/**
 * @description: 根据箱号获取血浆列表 /plasma/merge/list
 */
export const getPlasmaAmalgamationList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma/merge/list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 扫描 /plasma/merge/scan
 */
export const plasmaAmalgamationScan = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma/merge/scan`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 提交合并 /plasma/merge/merge
 */
export const plasmaAmalgamationMerge = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma/merge/merge`,
    method: 'POST',
    data,
  });
};
