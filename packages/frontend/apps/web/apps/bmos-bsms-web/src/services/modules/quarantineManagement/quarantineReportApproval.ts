import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------检疫期报告送审---------------

/**
 * @description: 分页查询 /quarantine/report/submit-audit/page
 */
export const getQuarantineReportSubmitAuditList = (data: any) => {
  return request({
    url: `${BASE_URL}/quarantine/report/submit-audit/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 送审 /quarantine/report/submit-audit
 */
export const submitQuarantineReportAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/quarantine/report/submit-audit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 撤销 /quarantine/report/cancel
 */
export const cancelQuarantineReportAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/quarantine/report/cancel`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 审核 /quarantine/report/audit
 */
export const auditQuarantineReportAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/quarantine/report/audit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 导出 /quarantine/report/export
 */
export const exportQuarantineReportAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/quarantine/report/export`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 打印检疫期核查报告 /quarantine/report/print-report
 */
export const printQuarantineReportAudit = (params: any) => {
  return request({
    url: `${BASE_URL}/quarantine/report/print-report`,
    method: 'POST',
    params,
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 打印检疫期核查结果 /quarantine/report/print-result
 */
export const printQuarantineReportAuditResult = (params: any) => {
  return request({
    url: `${BASE_URL}/quarantine/report/print-result`,
    method: 'POST',
    params,
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 打印检疫期核查记录 /quarantine/report/print-record
 */
export const printQuarantineReportAuditRecord = (params: any) => {
  return request({
    url: `${BASE_URL}/quarantine/report/print-record`,
    method: 'POST',
    params,
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 检疫期核查报告预览 /quarantine/report/preview-report
 */
export const previewQuarantineReportAudit = (params: any) => {
  return request({
    url: `${BASE_URL}/quarantine/report/preview-report`,
    method: 'POST',
    params,
  });
};
