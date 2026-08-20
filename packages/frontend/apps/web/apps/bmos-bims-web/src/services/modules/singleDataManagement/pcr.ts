import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';


// ---------------PCR---------------

/**
 * @description: 分页查询 /pcr/page
 */
export const getPCRList = (data: any) => {
  return request({
    url: `${BASE_URL}/pcr/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 批次号发布 /pcr/batch-publish
 */
export const batchPublishPCR = (params: any) => {
  return request({
    url: `${BASE_URL}/pcr/batch-publish`,
    method: 'GET',
    params,
  });
}

/**
 * @description: 发布 /pcr/publish
 */
export const publishPCR = (data: any) => {
  return request({
    url: `${BASE_URL}/pcr/publish`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 批次号核对 /pcr/batch-check
 */
export const batchCheckPCR = (params: any) => {
  return request({
    url: `${BASE_URL}/pcr/batch-check`,
    method: 'GET',
    params,
  });
}

/**
 * @description: 核对 /pcr/check
 */
export const checkPCR = (data: any) => {
  return request({
    url: `${BASE_URL}/pcr/check`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 读取 /pcr/data/read
 */
export const readPCR = (data: FormData) => {
  return request({
    url: `${BASE_URL}/pcr/data/read`,
    method: 'POST',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
    }
  });
}
