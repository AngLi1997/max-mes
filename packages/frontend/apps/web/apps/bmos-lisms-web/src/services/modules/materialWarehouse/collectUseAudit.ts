import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------物料领用审核---------------

/**
 * @description: 审核分页列表 /material/use/receive/audit/page
 */
export const getMaterialUseReceiveAuditPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/receive/audit/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 审核 /material/use/receive/audit
 */
export const materialUseReceiveAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/receive/audit`,
    method: 'POST',
    data,
  });
};
