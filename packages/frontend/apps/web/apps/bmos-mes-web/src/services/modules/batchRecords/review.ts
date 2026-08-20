import request from '../../service';

/**
 * @description /api/app/mes/plan/archive/flow/page 查询批记录审批流程页
 */
export const reqPlanArchiveFlowPage = (params: any) => {
  return request({
    url: '/app/mes/plan/archive/flow/page',
    method: 'GET',
    params,
  });
};
