import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';


// ---------------检验结果汇总发布---------------

/**
 * @description: 分页查询 /summary/page
 */
export const getReleaseOfTestResultsList = (data: any) => {
  return request({
    url: `${BASE_URL}/summary/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 发布 /summary/publish
 */
export const publishReleaseOfTestResults = (data: any) => {
  return request({
    url: `${BASE_URL}/summary/publish`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 批量发布 /summary/batch-publish
 */
export const batchPublishReleaseOfTestResults = (params: any) => {
  return request({
    url: `${BASE_URL}/summary/batch-publish`,
    method: 'GET',
    params,
  });
}