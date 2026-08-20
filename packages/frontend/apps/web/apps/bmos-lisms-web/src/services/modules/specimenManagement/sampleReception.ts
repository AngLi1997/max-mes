import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------标本接收---------------

/**
 * @description: 分页查询 /sample/receive/one/page
 */
export const getSampleReceivePage = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/receive/one/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 二级列表 /sample/receive/two/page
 */
export const getSampleReceiveTwoPage = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/receive/two/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 标本数量详情 /sample/receive/get/detail/{batchNo}
 */
export const getSampleReceiveCntDetail = (batchNo: string) => {
  return request({
    url: `${BASE_URL}/sample/receive/get/detail/${batchNo}`,
    method: 'GET',
  });
};

/**
 * @description: 标本接收申请 /sample/receive/apply
 */
export const getSampleReceiveApply = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/receive/apply`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 再次接收 /sample/station/receive
 */
export const sampleReceiveAgain = (params: any) => {
  return request({
    url: `${BASE_URL}/sample/station/receive`,
    method: 'POST',
    params,
  });
};

/**
 * @description: 扫码接收 /sample/receive/scan
 */
export const sampleReceiveScan = (params: any) => {
  return request({
    url: `${BASE_URL}/sample/receive/scan`,
    method: 'POST',
    params,
  });
};

/**
 * @description: 扫码接收分页 /sample/receive/scan/page
 */
export const getSampleReceiveScanPage = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/receive/scan/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 标本接收/标本拒收：查询运输状态信息 /sample/receive/query/{batchNo}
 */
export const getSampleTransQuery = (batchNo: string) => {
  return request({
    url: `${BASE_URL}/sample/receive/query/${batchNo}`,
    method: 'GET',
  });
};

/**
 * @description: 标本接收/接收审核：查询检验项目 /sample/query/inspectItems
 */
export const getInspectItems = (params: { sampleNo: string }) => {
  return request({
    url: `${BASE_URL}/sample/query/inspectItems`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 标本接收：打印送检交接记录 /sample/report/file
 */
export const getSampleReceiveReport = (params: any) => {
  return request({
    url: `${BASE_URL}/sample/report/file`,
    method: 'GET',
    params,
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 标本接收：打印标本清单 /sample/download
 */
export const getSampleReceiveDownload = (params: any) => {
  return request({
    url: `${BASE_URL}/sample/download`,
    method: 'GET',
    params,
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 标本接收：导出Excel /sample/export
 */
export const getSampleReceiveExport = (params: any) => {
  return request({
    url: `${BASE_URL}/sample/export`,
    method: 'GET',
    params,
    responseType: 'arraybuffer',
    original: true,
  });
};
