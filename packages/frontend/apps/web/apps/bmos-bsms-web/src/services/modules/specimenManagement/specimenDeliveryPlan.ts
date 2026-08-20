import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------标本出库计划---------------

/**
 * @description: 分页查询 /sample-out-plan/page-list
 */
export const getSampleDeliveryPlanList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-plan/page-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 标本出库审核二级列表 /sample-out-plan-audit/detail/{outPlanNo}
 */
export const getSampleDeliveryPlanAuditDetail = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-plan-audit/detail/${data.outPlanNo}`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 标本出库计划审核 /sample-out-plan-audit/audit
 */
export const auditSampleDeliveryPlan = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-plan-audit/audit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 创建出库计划 /sample-out-plan/create
 */
export const createSampleDeliveryPlan = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-plan/create`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 根据出库批号查询 /sample-out-plan/{outPlanBatchNo}
 */
export const getSampleDeliveryPlanByBatchNo = (outPlanBatchNo: string) => {
  return request({
    url: `${BASE_URL}/sample-out-plan/${outPlanBatchNo}`,
    method: 'GET',
  });
};

/**
 * @description: 出库计划申请 /sample-out-plan/attention
 */
export const attentionSampleDeliveryPlan = (params: any) => {
  return request({
    url: `${BASE_URL}/sample-out-plan/attention`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 更改出库计划批次 /sample-out-plan/updateNo/{oldNo}/{newNo}
 */
export const updateSampleDeliveryPlanBatchNo = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-plan/updateNo/${data.oldNo}/${data.newNo}`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 删除出库计划 /sample-out-plan/{outPlanBatchNo}
 */
export const deleteSampleDeliveryPlan = (outPlanBatchNo: string) => {
  return request({
    url: `${BASE_URL}/sample-out-plan/${outPlanBatchNo}`,
    method: 'DELETE',
  });
};

/**
 * @description: 出库计划编辑页面在库血浆列表--一级列表  /sample-out-plan/edit/page-list
 */
export const getSampleDeliveryPlanEditList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-plan/edit/page-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 出库计划编辑页面在库血浆列表--二级列表  /sample-out-plan/edit/page-detail-list/{sortingPlanBatchNo}
 */
export const getSampleDeliveryPlanEditDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-plan/edit/page-detail-list/${data.sortingPlanBatchNo}`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 出库计划编辑--已选择样本列表查询 /sample-out-plan/edit/choose-list/{outPlanBatchNo}
 */
export const getSampleDeliveryPlanEditChooseList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-plan/edit/choose-list/${data.outPlanBatchNo}`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 批量添加 /sample-out-plan/batch/insert
 */
export const batchInsertSampleDeliveryPlan = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-plan/batch/insert`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 更改出库计划信息（出库日期、备注） /sample-out-plan/update/info
 */
export const updateSampleDeliveryPlanInfo = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-plan/update/info`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 批量退回 /sample-out-plan/batch/back
 */
export const batchBackSampleDeliveryPlan = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-out-plan/batch/back`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 标本出库计划详情 /sample-detail/out-plan/detail
 */
export const getSampleDeliveryPlanDetail = (params: any) => {
  return request({
    url: `${BASE_URL}/sample-detail/out-plan/detail`,
    method: 'GET',
    params,
  });
};

/**
 * @description: B9-B10_出库计划样本明细列表查询 /sample-detail/out-plan/detail/list/{outPlanBatchNo}
 */
export const getSampleDeliveryPlanDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-detail/out-plan/detail/list/${data.outPlanBatchNo}`,
    method: 'POST',
    data,
  });
};
