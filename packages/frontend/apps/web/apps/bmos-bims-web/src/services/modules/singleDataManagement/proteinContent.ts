import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------蛋白质含量---------------

/**
 * @description: 分页查询 /protein/page
 */
export const getProteinContentList = (data: any) => {
  return request({
    url: `${BASE_URL}/protein/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 批次号发布 /protein/batch-publish
 */
export const batchPublishProteinContent = (params: any) => {
  return request({
    url: `${BASE_URL}/protein/batch-publish`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 发布 /protein/publish
 */
export const publishProteinContent = (data: any) => {
  return request({
    url: `${BASE_URL}/protein/publish`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 批次号核对 /protein/batch-check
 */
export const batchCheckProteinContent = (params: any) => {
  return request({
    url: `${BASE_URL}/protein/batch-check`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 核对 /protein/check
 */
export const checkProteinContent = (data: any) => {
  return request({
    url: `${BASE_URL}/protein/check`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 文件上传 /protein/import
 */
export const uploadProteinContent = (data: FormData) => {
  return request({
    url: `${BASE_URL}/protein/import`,
    method: 'POST',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};
