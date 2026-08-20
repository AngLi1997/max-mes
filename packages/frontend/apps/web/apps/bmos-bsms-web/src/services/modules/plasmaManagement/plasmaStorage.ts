import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------血浆入库---------------

/**
 * @description: 分页查询 /plasma-in-warehouse/page
 */
export const getPlasmaInStorageList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-in-warehouse/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 入库列表数量详情 /plasma-in-warehouse/detail/page
 */
export const getPlasmaInStorageDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-in-warehouse/detail/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 整批入库 /plasma-in-warehouse/batch/in
 */
export const plasmaInStorageBatchIn = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-in-warehouse/batch/in`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 入库血浆核对列表 /plasma-in-warehouse/verify/page
 */
export const getPlasmaInStorageVerifyList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-in-warehouse/verify/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 血浆核对扫描 /plasma-in-warehouse/verify/scan
 */
export const plasmaInStorageVerifyScan = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-in-warehouse/verify/scan`,
    method: 'PUT',
    data,
  });
}