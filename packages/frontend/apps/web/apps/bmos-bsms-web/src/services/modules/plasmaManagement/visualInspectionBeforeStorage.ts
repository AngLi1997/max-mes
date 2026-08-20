import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------入库前外观检验 -- 血浆---------------

/**
 * @description: 血浆列表分页查询 /plasma-appearance-before/page
 */
export const getAppearanceBeforeList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-appearance-before/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 血浆扫描 /plasma-appearance-before/scan
 */
export const getAppearanceBeforeScan = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-appearance-before/scan`,
    method: 'PUT',
    data
  });
};

/**
 * @description: 更改外观检验 /plasma-appearance-before/update
 */
export const appearanceBeforeUpdate = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-appearance-before/update`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 提交外观检验结果 /plasma-appearance-before/submit
 */
export const appearanceBeforeSubmit = (params: any) => {
  return request({
    url: `${BASE_URL}/plasma-appearance-before/submit`,
    method: 'PUT',
    params
  });
};

/**
 * @description: 清除缓存 /plasma-appearance-before/clear
 */
export const appearanceBeforeClearCache = () => {
  return request({
    url: `${BASE_URL}/plasma-appearance-before/clear`,
    method: 'DELETE',
  });
}