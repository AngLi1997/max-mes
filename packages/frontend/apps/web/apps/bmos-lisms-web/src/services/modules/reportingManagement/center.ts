import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------检验报告中心---------------

/**
 * @description: 一级列表分页 /report/page
 */
export const getReportCenterPage = (data: any) => {
  return request({
    url: `${BASE_URL}/report/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 二级列表分页 /report/child-page
 */
export const getReportCenterChildPage = (data: any) => {
  return request({
    url: `${BASE_URL}/report/child-page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 检验报告审核 /report/audit
 */
export const auditReport = (data: any) => {
  return request({
    url: `${BASE_URL}/report/audit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 打印检测报告 /report/preview
 */
export const printReport = (data: any) => {
  return request({
    url: `${BASE_URL}/report/preview`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 查看已审核次数 /report/audit-record/{{sampleBatchNo}}
 */
export const getAuditRecord = (sampleBatchNo: string) => {
  return request({
    url: `${BASE_URL}/report/audit-record/${sampleBatchNo}`,
    method: 'GET',
  });
};
