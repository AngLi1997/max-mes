import request from '../../service';

// 生产计划模板相关接口

// 获取生产计划模板列表分页
export const reqPlanTemplatePage = (params: any) => {
  return request({
    url: '/app/mes/plan/template/page',
    method: 'GET',
    params,
  });
};

// 新增模板
export const reqPlanTemplateSave = (data: any) => {
  return request({
    url: '/app/mes/plan/template/save',
    method: 'POST',
    data,
  });
};

// 编辑模板
export const reqPlanTemplateEdit = (data: any) => {
  return request({
    url: '/app/mes/plan/template/edit',
    method: 'POST',
    data,
  });
};

// 获取模板详情
export const reqPlanTemplateDetail = (params: any) => {
  return request({
    url: '/app/mes/plan/template/detail',
    method: 'GET',
    params,
  });
};

// 删除模板
export const reqPlanTemplateDelete = (params: any) => {
  return request({
    url: `/app/mes/plan/template/delete`,
    method: 'DELETE',
    params,
  });
};

// 启停模板
export const reqPlanTemplateChangeState = (data: any) => {
  return request({
    url: '/app/mes/plan/template/changeState',
    method: 'POST',
    data,
  });
};

// 查只有生效版本号及有数据权限的工艺树
export const getEffectiveProcessListTreeReq = (params: any) => {
  return request({
    url: `/app/mes//process/list/tree`,
    method: 'GET',
    params,
  });
};
