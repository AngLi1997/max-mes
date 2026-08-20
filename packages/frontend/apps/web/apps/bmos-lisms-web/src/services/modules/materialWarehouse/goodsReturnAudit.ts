import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------物料退货审核---------------

/**
 * @description: 审核分页列表 /material/use/return/audit/page
 */
export const getMaterialUseReturnAuditPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/return/audit/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 审核 /material/use/return/audit
 */
export const materialUseReturnAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/return/audit`,
    method: 'POST',
    data,
  });
};
