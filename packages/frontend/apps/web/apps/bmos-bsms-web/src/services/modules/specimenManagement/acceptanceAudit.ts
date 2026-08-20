import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------验收审核---------------

/**
 * @description: 分页查询 /sample-acceptance-audit/page-list
 */
export const getSpecimenAcceptanceAuditList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-acceptance-audit/page-list`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 验收审核 /sample-acceptance-audit/audit
 */
export const specimenAcceptanceAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-acceptance-audit/audit`,
    method: 'PUT',
    data,
  });
}