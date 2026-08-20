import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------标本出库---------------

/**
 * @description: B11-B12_列表分页查询--一级列表 /sample-out-warehouse/page-list
 */
export const getSampleOutWarehouseList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-warehouse/page-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 列表分页查询--二级列表查询 /sample-out-warehouse/{outPlanBatchNo}
 */
export const getSampleOutWarehouseByBatchNo = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-warehouse/${data.outPlanBatchNo}`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 样本出库任务下发WMS /sample-out-warehouse/out
 */
export const sampleOutWarehouseOut = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-warehouse/out`,
    method: 'POST',
    data,
  });
};

/**
 * @description: B11-B12_出库详情列表 /sample-out-warehouse/detail
 */
export const getSampleOutWarehouseDetail = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-warehouse/detail`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 重新核对 /sample-out-verify/recheck/{outPlanBatchNo}
 */
export const sampleOutVerifyRecheck = (outPlanBatchNo: string) => {
  return request({
    url: `${BASE_URL}/sample-out-verify/recheck/${outPlanBatchNo}`,
    method: 'GET',
  });
};

/**
 * @description: 出库扫描 /sample-out-verify/scan
 */
export const sampleOutVerifyScan = (params: any) => {
  return request({
    url: `${BASE_URL}/sample-out-verify/scan`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 标本出库（状态更改） /sample-out-verify/out/{outPlanBatchNo}
 */
export const sampleOutVerifyOut = (outPlanBatchNo: string) => {
  return request({
    url: `${BASE_URL}/sample-out-verify/out/${outPlanBatchNo}`,
    method: 'GET',
  });
};

/**
 * @description: 导出 /sample-out-warehouse/export
 */
export const sampleOutWarehouseExport = (params: any) => {
  return request({
    url: `${BASE_URL}/sample-out-warehouse/export`,
    method: 'GET',
    params,
    responseType: 'arraybuffer',
    original: true,
  });
};
