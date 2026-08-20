import request from '../../service';
// 白俄
// 保存数据看板
export const queryBelarusDashboardConfigCreate = (data: any) => {
  return request({
    url: `/app/query/belarus/dashboard/config/create`,
    method: 'POST',
    data,
  });
};

// 修改数据看板
export const queryBelarusDashboardConfigUpdate = (data: any) => {
  return request({
    url: `/app/query/belarus/dashboard/config/update`,
    method: 'PUT',
    data,
  });
};

// 根据配置类型查看配置信息
export const queryBelarusDashboardConfigDetail = (params: any) => {
  return request({
    url: `/app/query/belarus/dashboard/config/detail`,
    method: 'GET',
    params,
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

export const recordRoundingList = (params?: any) => {
  return request({
    url: '/app/mes/record/list/rounding',
    method: 'get',
    params,
  });
};

// 展示数据看板
// 投浆情况总览
export const getDashboardDataPlasma = (params?: any) => {
  return request({
    url: '/app/query/belarus/dashboard/data/plasma',
    method: 'get',
    params,
  });
};
// 产品产量
export const getDashboardDataProduct = (params?: any) => {
  return request({
    url: '/app/query/belarus/dashboard/data/product',
    method: 'get',
    params,
  });
};
