import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------接收审核---------------

/**
 * @description: 接收审核：一级列表页 /sample/receive/audit/one/page
 */
export const getSampleReceiveAuditPage = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/receive/audit/one/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 接收审核：二级列表页 /sample/receive/audit/two/page
 */
export const getSampleReceiveTwoAuditPage = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/receive/audit/two/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 接收审核：审核 /sample/receive/audit
 */
export const getSampleReceiveAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/receive/audit`,
    method: 'POST',
    data,
  });
};
