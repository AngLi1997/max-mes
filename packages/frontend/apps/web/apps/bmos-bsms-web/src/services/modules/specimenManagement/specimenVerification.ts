import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------入库标本核对---------------

/**
 * @description: 分页查询 /sample-in-warehouse-verify/page-list
 */
export const getSampleInStorageVerifyList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-in-warehouse-verify/page-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 扫描核对 /sample-in-warehouse-verify/scan
 */
export const sampleInStorageVerifyScan = (params: any) => {
  return request({
    url: `${BASE_URL}/sample-in-warehouse-verify/scan`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 完成核对 /sample-in-warehouse-verify/submit
 */
export const sampleInStorageVerifySubmit = (params: any) => {
  return request({
    url: `${BASE_URL}/sample-in-warehouse-verify/submit`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 清除缓存 /sample-in-warehouse-verify/clear-cache
 */
export const getSampleInStorageVerifyClearCache = () => {
  return request({
    url: `${BASE_URL}/sample-in-warehouse-verify/clear-cache`,
    method: 'DELETE',
  });
}