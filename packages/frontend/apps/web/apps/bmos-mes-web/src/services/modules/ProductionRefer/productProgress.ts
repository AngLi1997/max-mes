import request from '../../service';

/**
 * @description 获取生产管理
 */
export const reqFlowPlanProgressPage = (params: any) => {
  return request({
    url: '/app/mes/flow/plan/progress/page',
    method: 'get',
    params,
  });
};

/**
 * @description 查询工序节点 /api/app/mes/flow/procedures/{processInstanceId}
 * @param {string} processInstanceId 流程实例id
 * @param {string} processVersionId 工艺版本id
 */
export const reqFlowProcedures = (processInstanceId: string, processVersionId: string) => {
  return request({
    url: `/app/mes/flow/procedures`,
    method: 'get',
    params: {
      processInstanceId,
      processVersionId,
    },
  });
};

/**
 * @description 查询工序步骤节点 /api/app/mes/flow/steps/{executionId}
 * @param {string} processInstanceId 流程实例id
 * @param {string} planId 计划id
 */
export const reqFlowSteps = (params: any) => {
  return request({
    url: `/app/mes/flow/steps`,
    method: 'get',
    params,
  });
};

/**
 * @description 查询生产计划修订记录 /api/app/mes/execute/plan/modify/list
 */
export const reqExecutePlanModifyList = (params: any) => {
  return request({
    url: `/app/mes/execute/plan/modify/list`,
    method: 'GET',
    params,
  });
};

/**
 * @description 查询工序生产进度 /api/app/mes/flow/procedure/progress
 * @param {string} processInstanceId 流程实例id
 */
export const reqFlowProcedureProgress = (processInstanceId: string) => {
  return request({
    url: `/app/mes/flow/procedure/progress`,
    method: 'get',
    params: {
      processInstanceId,
    },
  });
};

/**
 * @description 查询工步生产进度 /api/app/mes/flow/list/step/progress
 */
export const reqFlowListStepProgress = (params: any) => {
  return request({
    url: `/app/mes/flow/list/step/progress`,
    method: 'get',
    params,
  });
};
/**
 * @description 查询工步换班信息 /api/app/mes/flow/list/change/team
 */
export const reqFlowListChangeTeam = (params: any) => {
  return request({
    url: `/app/mes/flow/list/change/team`,
    method: 'get',
    params,
  });
};
