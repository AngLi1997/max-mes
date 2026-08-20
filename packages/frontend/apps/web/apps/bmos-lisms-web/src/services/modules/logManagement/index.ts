import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

/**
 * @description: 检验数据记录分页 /log/inspect/record-page
 */
export const postLogInspectRecordPage = (data: any) => {
  return request({
    url: `${BASE_URL}/log/inspect/record-page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 修约日志分页 /log/rounding/page
 */
export const postLogRoundingPage = (data: any) => {
  return request({
    url: `${BASE_URL}/log/rounding/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 审核日志 /log/audit/page
 */
export const postLogAuditPage = (data: any) => {
  return request({
    url: `${BASE_URL}/log/audit/page`,
    method: 'POST',
    data,
  });
};
