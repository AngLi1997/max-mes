import request from '../../service';
// 计划审核页
// 计划审核页分页列表
export const productionPlanApprovalPage = (params: any) => {
  return request({
    url: '/app/mes/plan/info/audit/page',
    method: 'GET',
    params,
  });
};
// 审核通过
export const PlanAuditComplete = (data: any) => {
  return request({
    url: '/app/mes/audit/complete',
    method: 'POST',
    data,
  });
};
// 审核不通过
export const PlanAuditNoComplete = (data: any) => {
  return request({
    url: '/app/mes/audit/complete/not/approve',
    method: 'POST',
    data,
  });
};
// 审核回退
export const PlanAuditBack = (data: any) => {
  return request({
    url: '/app/mes/audit/back/to/prev',
    method: 'POST',
    data,
  });
};
