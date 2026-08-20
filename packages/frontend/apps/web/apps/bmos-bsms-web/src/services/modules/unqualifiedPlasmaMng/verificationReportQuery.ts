import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------不合格核查报告查询---------------

/**
 * @description: 不合格核查报告查询列表 /unqualified/plasma/report/query/page
 */
export const unqualifiedPlasmaReportQueryList = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/report/query/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 打印不合格核查报告 /unqualified/plasma/report/query/print/{reportBillNo}
 */
export const unqualifiedPlasmaReportPrint = (reportBillNo: string) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/report/query/print/${reportBillNo}`,
    method: 'GET',
    responseType: 'arraybuffer',
    original: true,
  });
};
