import request from '../../service';

/**
 * @description: 工艺版本列表查询接口 /api/app/mes/process/version/page
 * @param {API.ProcessVersionPageReq} params 默认参数 pageNum: 1, pageSize: 10
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcessVersionList = (params: API.ProcessVersionPageReq) => {
  return request({
    url: '/app/mes/process/version/page',
    method: 'GET',
    params,
  });
};

/**
 * @description: 工艺列表查询接口 /api/app/mes/process/page
 * @param {API.MesProcessPageReq} params 默认参数 pageNum: 1, pageSize: 10
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcessList = (
  params: API.MesProcessPageReq = {
    pageNum: 1,
    pageSize: 10,
  },
) => {
  return request({
    url: '/app/mes/process/page',
    method: 'GET',
    params,
  });
};

// 查询工艺大屏显示配置数据
export const reqAllProcessGetDashboardConfig = (params: any) => {
  return request({
    url: `/app/mes/process/getDashboardConfig`,
    method: 'GET',
    params,
  });
};

// 保存工艺大屏显示配置数据
export const reqAllProcessSaveDashboardConfig = (data: any) => {
  return request({
    url: `/app/mes/process/saveDashboardConfig`,
    method: 'POST',
    data,
  });
};

// 保存工艺大屏显示配置数据
export const reqQueryBelarusDashboardDataCompleteBatch = (params: any) => {
  return request({
    url: '/app/query/belarus/dashboard/data/complete/batch',
    method: 'GET',
    params,
  });
};

// 查询正在生产的生产批次的工艺内的工序顺序&生产进度看板
export const reqQueryBelarusDashboardDataProductProgress = (params: any) => {
  return request({
    url: '/app/query/belarus/dashboard/data/product/progress',
    method: 'GET',
    params,
  });
};

// 查询正在生产的生产批次的工艺内的工序顺序&生产进度看板
export const reqQueryBelarusDashboardDataProcessProgress = (params: any) => {
  return request({
    url: '/app/query/belarus/dashboard/data/process/progress',
    method: 'GET',
    params,
  });
};

// 根据模型ids查询工序信息 /app/query/belarus/dashboard/data/procedure
export const reqQueryBelarusDashboardDataProcedure = (params: any) => {
  return request({
    url: '/app/query/belarus/dashboard/data/procedure',
    method: 'GET',
    params,
  });
};
