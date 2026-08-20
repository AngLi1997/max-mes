import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------标本交接---------------

/**
 * @description: 分页查询 /sample/batch/page
 */
export const getSampleBatchList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/batch/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 二级列表 /sample/page
 */
export const getSampleBatchInfoList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 批量接收 /sample/batch/accept
 */
export const acceptSampleBatch = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/batch/accept`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 接收血源系统标本请验数据 /sample/batch/receive
 */
export const receiveSampleBatch = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/batch/receive`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 导出 /sample/export
 */
export const sampleDataExport = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/export`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
    // headers: {
    //   'Content-Type': 'multipart/form-data',
    // }
  });
};