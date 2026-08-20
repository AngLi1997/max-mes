import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 标本请验 -----------------

/**
 * @description: 分页查询 /sample-examination-info/page-list
 */
export const getSampleExaminationInfoPageList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-examination-info/page-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 二级列表 /sample-examination-info/detail-list/inspectionBatchNo
 */
export const getSampleExaminationInfoDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-examination-info/detail-list/${data.inspectionBatchNo}`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 标本请验 /sample-examination-info/inspection
 */
export const sampleExaminationInfoInspection = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-examination-info/inspection`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 请验出库任务下发 /sample-examination-info/out/warehouse/{inspectionBatchNo}
 */
export const sampleExaminationInfoOutWarehouse = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-examination-info/out/warehouse/${data.inspectionBatchNo}`,
    method: 'GET',
  });
};

/**
 * @description: 数据同步至LIMS /sample-examination-info/sync-lims/{inspectionBatchNo}
 */
export const sampleExaminationInfoSyncLims = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-examination-info/sync-lims/${data.inspectionBatchNo}`,
    method: 'GET',
  });
};

/**
 * @description: C1_样本请验详情 /examination-detail/examination/detail
 */
export const getExaminationDetail = (params: any) => {
  return request({
    url: `${BASE_URL}/examination-detail/examination/detail`,
    method: 'GET',
    params,
  });
};
