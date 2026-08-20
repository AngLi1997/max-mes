import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------入库血浆核对---------------

/**
 * @description: 血浆列表 /plasma-in-warehouse-verify/page
 */
export const getPlasmaCheckStorageList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-in-warehouse-verify/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 血浆扫描 /plasma-in-warehouse-verify/scan
 */
export const plasmaCheckStorageScan = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-in-warehouse-verify/scan`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 完成核对 /plasma-in-warehouse-verify/submit
 */
export const plasmaCheckStorageSubmit = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-in-warehouse-verify/submit`,
    method: 'PUT',
    data,
  });
}

/**
 * @description: 清除缓存 /plasma-in-warehouse-verify/clear
 */
export const plasmaCheckStorageClear = () => {
  return request({
    url: `${BASE_URL}/plasma-in-warehouse-verify/clear`,
    method: 'DELETE',
  });
}