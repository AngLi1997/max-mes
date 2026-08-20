import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------不合格核查报告送审---------------

/**
 * @description: 不合格核查报告送审列表/unqualified/plasma/report/page
 */
export const unqualifiedPlasmaReportPage = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/report/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 撤销 /unqualified/plasma/report/revocation
 */
export const unqualifiedPlasmaReportRevocation = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/report/revocation`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 送审 /unqualified/plasma/report/send-to-audit
 */
export const unqualifiedPlasmaReportSendToAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/report/send-to-audit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 内容预览 /unqualified/plasma/report/preview/{reportBillNo}
 */
export const unqualifiedPlasmaReportPreview = (reportBillNo: string) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/report/preview/${reportBillNo}`,
    method: 'GET',
  });
};

/**
 * @description: 详情 /unqualified/plasma/report/detail/{reportBillNo}
 */
export const unqualifiedPlasmaReportDetail = (reportBillNo: string) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/report/detail/${reportBillNo}`,
    method: 'GET',
  });
};
