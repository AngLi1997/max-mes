import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------拒收审核---------------

/**
 * @description: 分页查询 /sample/reject/audit/page
 */
export const getSampleRejectAuditPage = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/reject/audit/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 审核 /sample/reject/audit
 */
export const getSampleRejectAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/reject/audit`,
    method: 'POST',
    data,
  });
};
