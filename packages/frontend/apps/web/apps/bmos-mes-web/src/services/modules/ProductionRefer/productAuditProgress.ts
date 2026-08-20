import request from '../../service';

/**
 * @description 生产审核进度:批次分页 /api/app/mes/plan/info/productionAuditProgressPage
 */
export const reqPlanInfoProductionAuditProgressPage = (params: any) => {
  return request({
    url: '/app/mes/plan/info/productionAuditProgressPage',
    method: 'get',
    params,
  });
};

/**
 * @description 生产审核进度:批次详情 /api/app/mes/plan/info/auditProgressDetail
 */
export const reqPlanInfoAuditProgressDetail = (params: any) => {
  return request({
    url: '/app/mes/plan/info/auditProgressDetail',
    method: 'get',
    params,
  });
};
