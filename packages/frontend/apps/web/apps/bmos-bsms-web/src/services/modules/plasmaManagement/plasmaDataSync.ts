import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------血浆数据同步---------------

/**
 * @description: 分页查询 /plasma-data-sync/list
 */
export const getPlasmaDataSyncList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-data-sync/list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 确认接收 /plasma-data-sync/receive
 */
export const plasmaDataSyncReceive = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-data-sync/receive`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 修改信息 /plasma-data-sync/update
 */
export const plasmaDataSyncUpdate = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-data-sync/update`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 撤销同步 /plasma-data-sync/revocation
 */
export const plasmaDataSyncCancel = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-data-sync/revocation`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 成功/失败详情列表查询 /plasma-data-sync/detail
 */
export const plasmaDataSyncDetail = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-data-sync/detail`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 手动导入血浆数据 /plasma-data-sync/manual/sync
 */
export const plasmaDataSyncManualSync = (data: FormData) => {
  return request({
    url: `${BASE_URL}/plasma-data-sync/manual/sync`,
    method: 'POST',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

/**
 * @description: 导出 /plasma-data-sync/export
 */
export const plasmaDataSyncExport = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-data-sync/export`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};
