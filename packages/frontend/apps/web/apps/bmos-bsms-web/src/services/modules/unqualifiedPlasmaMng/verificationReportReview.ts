import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------不合格核查报告审核---------------

/**
 * @description: 不合格核查报告审核列表 /unqualified/plasma/report/audit/page
 */
export const unqualifiedPlasmaReportAuditList = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/report/audit/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 审核/退回 /unqualified/plasma/report/audit/audit
 */
export const unqualifiedPlasmaReportAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/report/audit/audit`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 内容预览 /unqualified/plasma/report/audit/preview/{reportBillNo}
 */
export const unqualifiedPlasmaReportAuditPreview = (reportBillNo: string) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/report/audit/preview/${reportBillNo}`,
    method: 'GET',
  });
};
