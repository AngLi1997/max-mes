import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------入库前外观检验 -- 标本---------------

/**
 * @description: 标本列表分页查询 /sample-appearance-check/page-list
 */
export const getSpecimenAppearanceBeforeList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-appearance-check/page-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 入库前外观扫描核对 /sample-appearance-check/scan
 */
export const getSpecimenAppearanceBeforeScan = (batchNo: any, no: any, boxId: any) => {
  const params = {
    batchNo,
    no,
    boxId,
  };
  return request({
    url: `${BASE_URL}/sample-appearance-check/scan`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 外观检验 /sample-appearance-check/update
 */
export const specimenAppearanceBeforeUpdate = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-appearance-check/update`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 外观检测提交 /sample-appearance-check/submit
 */
export const specimenAppearanceBeforeSubmit = (batchNo: any, boxId: any) => {
  const params = {
    batchNo,
    boxId,
  };
  return request({
    url: `${BASE_URL}/sample-appearance-check/submit`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 清除缓存 /sample-appearance-check/clear-cache
 */
export const specimenAppearanceBeforeClearCache = () => {
  return request({
    url: `${BASE_URL}/sample-appearance-check/clear-cache`,
    method: 'DELETE',
  });
};
