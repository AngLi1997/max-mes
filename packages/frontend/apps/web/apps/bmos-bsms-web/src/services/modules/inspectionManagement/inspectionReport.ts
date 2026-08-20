import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 检验报告 -----------------

/**
 * @description: 分页查询 /examination-report/page-list
 */
export const getInspectionReportList = (data: any) => {
  return request({
    url: `${BASE_URL}/examination-report/page-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 接收实验室报告 /examination-report/receive
 */
export const receiveInspectionReport = (data: any) => {
  return request({
    url: `${BASE_URL}/examination-report/receive`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 不合格详情列表 /examination-report/detail/{inspectionBatchNo}
 */
export const getInspectionReportDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/examination-report/detail/${data.inspectionBatchNo}`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 打印检验报告 /examination-report/print/{inspectionBatchNo}
 */
export const printInspectionReport = (data: any) => {
  return request({
    url: `${BASE_URL}/examination-report/print/${data.inspectionBatchNo}`,
    method: 'GET',
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: C3_检验报告详情 /examination-detail/examination-report/detail
 */
export const getInspectionReportDetail = (params: any) => {
  return request({
    url: `${BASE_URL}/examination-detail/examination-report/detail`,
    method: 'GET',
    params,
  });
};
