import request from '../../service';

/**
 * @description: 查询审核流程配置管理页 /api/app/mes/audit/flow/audit/page
 * @param {API.FlowAuditPageReq} params 默认参数 pageNum: 1, pageSize: 10
 * @param {string} systemName 系统名称 默认值 mes
 */

export const reqFlowAuditList = (
  params: API.FlowAuditPageReq = {
    pageNum: 1,
    pageSize: 10,
  },
) => {
  return request({
    url: `/app/mes/audit/flow/audit/page`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 删除流程 /api/app/mes/audit/delete/flow/audit
 * @param {string} versionId 流程版本id
 */

export const reqFlowAuditDelete = (versionId: string) => {
  return request({
    url: `/app/mes/audit/delete/flow/audit`,
    method: 'GET',
    params: {
      versionId,
    },
  });
};
/**
 * @description: 校验流程模型 /api/app/mes/audit/checkout/deployment
 * @param {API.AuditCheckoutDeploymentReq} data 流程模型
 */

export const reqAuditCheckoutDeploymentReq = (data: API.AuditCheckoutDeploymentReq) => {
  return request({
    url: `/app/mes/audit/checkout/deployment`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 保存流程模型 /api/app/mes/audit/save/flow/audit
 * @param {API.SaveFlowAuditReq} data 流程模型
 */

export const reqSaveFlowAuditReq = (data: API.SaveFlowAuditReq) => {
  return request({
    url: `/app/mes/audit/save/flow/audit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 发布流程模型 /api/app/mes/audit/deploy/flow/audit
 * @param {API.DeployFlowAuditReq} data 流程模型
 */

export const reqDeployFlowAuditReq = (data: API.DeployFlowAuditReq) => {
  return request({
    url: `/app/mes/audit/deploy/flow/audit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 根据流程版本id查询详情设计 /api/app/mes/audit/detail/flow/audit
 * @param {string} versionId 流程版本id
 */

export const reqDetailFlowAuditReq = (versionId: string) => {
  return request({
    url: `/app/mes/audit/detail/flow/audit`,
    method: 'GET',
    params: {
      versionId,
    },
  });
};
/**
 * @description: 获取编码 /api/app/mes/audit/get/flow/audit/code
 * @param {string} versionId 流程版本id
 */

export const reqGetCodeReq = () => {
  return request({
    url: `/app/mes/audit/get/flow/audit/code`,
    method: 'GET',
  });
};

/**
 * @description: 获取流程树 /api/app/mes/audit/list/flow/audit/category
 */

export const reqGetFlowConfigTreeReq = () => {
  return request({
    url: `/app/mes/audit/list/flow/audit/category`,
    method: 'GET',
  });
};
/**
 * @description: 流程绑定工艺 /api/app/mes/audit/flow/audit/bind/process
 */
export const reqFlowConfigBindProcessReq = (data: any) => {
  return request({
    url: `/app/mes/audit/flow/audit/bind/process`,
    method: 'PUT',
    data,
  });
};
/**
 * @description: 获取工艺list /api/app/mes/audit/flow/audit/process/list
 */
export const reqGetFlowConfigProcessListReq = (params: any) => {
  return request({
    url: `/app/mes/audit/flow/audit/process/list`,
    method: 'GET',
    params,
  });
};

// 启用流程模型
export const auditChangeState = (data: any) => {
  return request({
    url: `/app/mes/audit/changeState`,
    method: 'PUT',
    data,
  });
};
