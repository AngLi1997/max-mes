import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------标本数据同步---------------

/**
 * @description: 分页查询 /sample-data-sync/page-list
 */
export const getSampleDataSyncList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-data-sync/page-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 确认接收 /sample-data-sync/receive
 */
export const sampleDataSyncReceive = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-data-sync/receive`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 撤销同步 /sample-data-sync/revocation
 *
 */
export const sampleDataSyncRevocation = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-data-sync/revocation`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 成功/失败详情列表查询 /sample-data-sync/detail-list
 */
export const sampleDataSyncDetailQuery = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-data-sync/detail-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 修改同步记录数据回显 /sample-data-sync/{syncBatchNo}
 */
export const getSampleDataSyncEcho = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-data-sync/${data.syncBatchNo}`,
    method: 'GET',
  });
};

/**
 * @description: 修改同步记录入库仓库 /sample-data-sync/update/{syncBatchNo}/{warehouseId}
 */
export const sampleDataSyncUpdateInfo = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-data-sync/update/${data.syncBatchNo}/${data.warehouseId}`,
    method: 'PUT',
  });
};

/**
 * @description: 导入标本信息 /sample-data-sync/import
 */
export const sampleDataSyncImport = (data: FormData) => {
  return request({
    url: `${BASE_URL}/sample-data-sync/import`,
    method: 'POST',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

/**
 * @description: 导出标本信息 /sample-data-sync/export
 */
export const sampleDataSyncExport = (params: any) => {
  return request({
    url: `${BASE_URL}/sample-data-sync/export`,
    method: 'GET',
    params,
    responseType: 'arraybuffer',
    original: true,
  });
};
