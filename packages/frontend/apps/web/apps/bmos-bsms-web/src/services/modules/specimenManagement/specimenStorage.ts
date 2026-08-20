import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------标本入库---------------

/**
 * @description: 分页查询 /sample-in-storage/page-list
 */
export const getInStorageList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-in-storage/page-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 入库核对标本详情列表查询 /sample-in-storage/check-detail-list
 */
export const getInStorageCheckDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-in-storage/check-detail-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 入库核对扫描 /sample-in-storage/scan/{batchNo}/{no}
 */
export const getInStorageScan = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-in-storage/scan/${data.batchNo}/${data.no}`,
    method: 'GET',
  });
};

/**
 * @description: 样本批量入库 /sample-in-storage/batch/in
 */
export const batchInStorage = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-in-storage/batch/in`,
    method: 'POST',
    data,
  });
};