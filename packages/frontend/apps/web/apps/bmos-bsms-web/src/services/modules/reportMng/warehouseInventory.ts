import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 立体库盘存 ---------------

/**
 * @description: 立体库盘存列表 /plasma/stereo/inventory/page
 */
export const getWarehouseInventoryList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma/stereo/inventory/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 立体库盘存二级列表 /plasma/stereo/inventory/page/detail
 */
export const getWarehouseInventoryListDetail = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma/stereo/inventory/page/detail`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 立体库盘存导出 /plasma/stereo/inventory/export
 */
export const exportWarehouseInventoryList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma/stereo/inventory/export`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};
