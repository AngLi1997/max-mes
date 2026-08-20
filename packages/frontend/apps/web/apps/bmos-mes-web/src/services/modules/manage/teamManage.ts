import request from '../../service';
// 班组配置接口

// 分页列表
export const planTeamPage = (params: any) => {
  return request({
    url: '/app/mes/plan/team/page',
    method: 'GET',
    params,
  });
};

// 查询全部列表
export const planTeamList = (params: any) => {
  return request({
    url: `/app/mes/plan/team/list`,
    method: 'GET',
    params,
  });
};

// 指令单保存-班组信息保存
export const planTeamSave = (data: any) => {
  return request({
    url: `/app/mes/plan/team/save`,
    method: 'POST',
    data,
  });
};

// 指令单保存-班组信息更新
export const planTeamUpdata = (data: any) => {
  return request({
    url: `/app/mes/plan/team/update`,
    method: 'PUT',
    data,
  });
};

// 班组启用
export const planTeamEnable = (id: any) => {
  return request({
    url: `/app/mes/plan/team/enable/${id}`,
    method: 'PUT',
  });
};

// 班组停用
export const planTeamDisable = (id: any) => {
  return request({
    url: `/app/mes/plan/team/disable/${id}`,
    method: 'PUT',
  });
};

// 查询部门树
export const platformQueryDeptUserTree = () => {
  return request({
    url: `/app/mes/platform/query/dept/user/tree`,
    method: 'GET',
  });
};

// 查询未分配的部门树
export const platformQueryDeptUserUnassigned = () => {
  return request({
    url: `/app/mes/platform/query/dept/user/unassigned`,
    method: 'GET',
  });
};

// 获取详情
export const getPlanTeamPeoPle = (id: string) => {
  return request({
    url: `/app/mes/plan/team/detail/${id}`,
    method: 'GET',
  });
};

//根据班组id获取产线列表 /api/app/mes/plan/team/listLinesByTeamId

export const getPlanTeamListByTeamId = (params: any) => {
  return request({
    url: `/app/mes/plan/team/listLinesByTeamId`,
    method: 'GET',
    params,
  });
};

//绑定产线 /api/app/mes/plan/team/boundProductionLine

export const postBoundProductionLine = (data: any) => {
  return request({
    url: `/app/mes/plan/team/boundProductionLine`,
    method: 'POST',
    data,
  });
};

//获取产线 /api/app/mes/process/product/line

export const getProcessProductLine = () => {
  return request({
    url: `/app/mes/process/product/line/tree`,
    method: 'GET',
  });
};

// 根据生产计划id获取班组列表  /api/app/mes/plan/team/listByProductPlanId
export const getPlanTeamListByProductPlanId = (params: any) => {
  return request({
    url: `/app/mes/plan/team/listByProductPlanId`,
    method: 'GET',
    params,
  });
};
