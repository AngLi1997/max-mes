import request from '../../service';
//生产计划管理相关接口

// 分页接口
export const reqProductionListPage = (params: any) => {
  return request({
    url: '/app/mes/production/list/page',
    method: 'GET',
    params,
  });
};

// 获取启用模板列表
export const reqPlanTemplateList = () => {
  return request({
    url: '/app/mes/plan/template/list',
    method: 'GET',
  });
};

// 生成生产计划
export const reqProductionBuildPlan = (params: any) => {
  return request({
    url: '/app/mes/production/build/plan',
    method: 'GET',
    params,
  });
};

// 生成编码
export const reqProductionBuildBatchNo = (data: any) => {
  return request({
    url: '/app/mes/production/build/batch/no',
    method: 'POST',
    data,
  });
};

// 查询计划详情数据
export const reqProductionListPlanDetail = (params: any) => {
  return request({
    url: '/app/mes/production/list/plan/detail',
    method: 'GET',
    params,
  });
};

// 下发生产计划
export const reqProductionPlanIssue = (data: any) => {
  return request({
    url: '/app/mes/production/plan/issue',
    method: 'POST',
    data,
  });
};

// 计划作废
export const reqProductionPlanNullify = (id: any) => {
  return request({
    url: `/app/mes/production/plan/nullify/${id}`,
    method: 'PUT',
  });
};

// 生产计划日历
export const reqProductionCalendar = (params: any) => {
  return request({
    url: '/app/mes/production/calendar',
    method: 'GET',
    params,
  });
};

// 生产计划日历 多月份
export const reqProductionCalendarMonths = (params: any) => {
  return request({
    url: '/app/mes/production/calendar/months',
    method: 'GET',
    params,
  });
};

// 计划日历调整
export const reqProductionChangeCalendar = (data: any) => {
  return request({
    url: '/app/mes/production/changeCalendar',
    method: 'POST',
    data,
  });
};
