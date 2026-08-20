import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------抽检申请审核---------------

/**
 * @description: 分页列表 /material/use/spot-check/audit/page
 */
export const getMaterialUseSpotCheckAuditPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/spot-check/audit/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 审核 /material/use/spot-check/audit
 */
export const materialUseSpotCheckAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/spot-check/audit`,
    method: 'POST',
    data,
  });
};
