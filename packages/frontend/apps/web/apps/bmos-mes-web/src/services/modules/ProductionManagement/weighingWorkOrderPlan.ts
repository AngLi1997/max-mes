import request from '../../service';

/**
 * @description: 分页查询工单 /api/app/mes/weigh/ticket/page
 * @param {any} params
 */
export const reqWeighingWorkOrderPlanPage = (params: any) => {
  return request({
    url: `/app/mes/weigh/ticket/page`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 查询需求列表 /api/app/mes/weigh/ticket/requirement/list
 * @param {any} data
 */
export const reqWeighingWorkOrderPlanRequirementList = (data: any) => {
  return request({
    url: `/app/mes/weigh/ticket/requirement/list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 手动规划 /api/app/mes/weigh/ticket/programManual
 * @param {any} data
 */
export const weighingWorkOrderPlanManual = (data: any) => {
  return request({
    url: `/app/mes/weigh/ticket/programManual`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 自动规划 /api/app/mes/weigh/ticket/programAuto
 */
export const weighingWorkOrderPlanAuto = () => {
  return request({
    url: `/app/mes/weigh/ticket/programAuto`,
    method: 'POST',
  });
};

/**
 * @description: 下发工单 /api/app/mes/weigh/ticket/issue
 * @param {any} params
 */
export const weighingWorkOrderPlanIssue = (params: any) => {
  return request({
    url: `/app/mes/weigh/ticket/issue`,
    method: 'POST',
    params,
  });
};

/**
 * @description: 编辑工单 /api/app/mes/weigh/ticket/edit
 * @param {any} data
 */
export const weighingWorkOrderPlanEdit = (data: any) => {
  return request({
    url: `/app/mes/weigh/ticket/edit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 取消工单 /api/app/mes/weigh/ticket/cancel
 * @param {any} params
 */
export const weighingWorkOrderPlanCancel = (params: any) => {
  return request({
    url: `/app/mes/weigh/ticket/cancel`,
    method: 'POST',
    params,
  });
};

/**
 * @description: 根据工单id查询称量详情 /api/app/mes/weigh/ticket/getWeighRecord
 * @param {any} params
 */
export const getWeighingWorkOrderPlanGetWeighRecord = (params: any) => {
  return request({
    url: `/app/mes/weigh/ticket/getWeighRecord`,
    method: 'POST',
    params,
  });
};
