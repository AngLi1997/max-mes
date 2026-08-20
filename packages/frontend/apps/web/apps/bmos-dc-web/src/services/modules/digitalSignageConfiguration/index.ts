import request from '../../service';
/**
 * @description: 查询产品数 /api/app/mes/product/material/productTree
 * @param {API.StepConfigListReq} params
 */

export const reqProductMaterialProductTreeReq = (categoryType = 2) => {
  return request({
    url: `/app/mes/product/material/productTree?categoryType=${categoryType}`,
    method: 'GET',
  });
};

// 产品下拉列表改变时获取对应生产工艺下拉列表
export const getPlanProcessList = (params: any) => {
  return request({
    // url: `/app/mes/process/list`,
    url: `/app/mes/process/instruction/process/list`, //0726改成加了数据权限的
    method: 'GET',
    params,
  });
};

// 查询所有看板配置
export const queryDashboardListAll = () => {
  return request({
    url: `/app/query/dashboard/listAll`,
    method: 'GET',
  });
};

// 查询所有看板配置
export const queryDashboardCreate = (data: any) => {
  return request({
    url: `/app/query/dashboard/create`,
    method: 'POST',
    data,
  });
};

// 查询所有看板配置
export const queryDashboardUpdate = (data: any) => {
  return request({
    url: `/app/query/dashboard/update`,
    method: 'PUT',
    data,
  });
};

// 根据id查询看板配置
export const getDashboardInstanceById = (params: any) => {
  return request({
    url: `/app/query/dashboard/getDashboardInstanceById`,
    method: 'GET',
    params,
  });
};

// 根据id删除看板配置
export const dashboardDeleteById = (params: any) => {
  return request({
    url: `/app/query/dashboard/deleteById`,
    method: 'DELETE',
    params,
  });
};

// 获取看板数据
export const getDashboardViewDataById = (params: any) => {
  return request({
    url: `/app/query/dashboard/viewData`,
    method: 'GET',
    params,
  });
};

// 根据工艺id查询数据集列表
export const queryDatasetListByProcessIdApi = (params: any) => {
  return request({
    url: '/app/mes/dataset/queryDatasetListByProcessId',
    method: 'GET',
    params,
  });
};

// 查询数据集详情
export const queryDatasetDetailApi = (params: any) => {
  return request({
    url: '/app/mes/dataset/queryDatasetDetail',
    method: 'GET',
    params,
  });
};

// 获取所有冷链车数据
export const getColdLinkCarData = () => {
  return request({
    url: '/app/query/chain/cold-link-car/data',
    method: 'GET',
  });
};
