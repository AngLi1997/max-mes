import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------物料报废审核---------------

/**
 * @description: 审核分页列表 /material/use/scrap/audit/page
 */
export const getMaterialUseScrapAuditPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/scrap/audit/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 审核 /material/use/scrap/audit
 */
export const materialUseScrapAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/scrap/audit`,
    method: 'POST',
    data,
  });
};
