import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';


// ---------------检验结果发布审核---------------

/**
 * @description: 分页查询 /summary/audit-page
 */
export const getTestResultReviewList = (data: any) => {
  return request({
    url: `${BASE_URL}/summary/audit-page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 审核 /summary/audit
 */
export const auditTestResultReview = (data: any) => {
  return request({
    url: `${BASE_URL}/summary/audit`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 批量审核 /summary/batch-audit
 */
export const batchAuditTestResultReview = (params: any) => {
  return request({
    url: `${BASE_URL}/summary/batch-audit`,
    method: 'GET',
    params,
  });
}