import request from '../../service';

// 称量中心相关接口

// 获取称量中心分类树
export const reqWeighCentreCategoryTree = () => {
  return request({
    url: '/app/mes/weigh/centre/category/tree',
    method: 'GET',
  });
};

// 新增分类树
export const reqWeighCentreCategoryCreate = (data: any) => {
  return request({
    url: `/app/mes/weigh/centre/category/create`,
    method: 'POST',
    data,
  });
};

// 修改分类树
export const reqWeighCentreCategoryEdit = (data: any) => {
  return request({
    url: `/app/mes/weigh/centre/category/edit`,
    method: 'PUT',
    data,
  });
};

// 删除分类树
export const reqWeighCentreCategoryDelete = (params: any) => {
  return request({
    url: `/app/mes/weigh/centre/category/delete`,
    method: 'DELETE',
    params,
  });
};

// 获取称量中心列表分页
export const reqWeighCentreQueryPage = (params: any) => {
  return request({
    url: '/app/mes/weigh/centre/queryPage',
    method: 'GET',
    params,
  });
};

// 表格-新建称量中心
export const reqWeighCentreCreate = (data: any) => {
  return request({
    url: '/app/mes/weigh/centre/create',
    method: 'POST',
    data,
  });
};

// 表格-编辑称量中心
export const reqWeighCentreEdit = (data: any) => {
  return request({
    url: '/app/mes/weigh/centre/edit',
    method: 'PUT',
    data,
  });
};

// 启用称量中心
export const reqWeighCentreEnable = (params: any) => {
  return request({
    url: `/app/mes/weigh/centre/enable`,
    method: 'PUT',
    params,
  });
};

// 停用称量中心
export const reqWeighCentreDisable = (params: any) => {
  return request({
    url: `/app/mes/weigh/centre/disable`,
    method: 'PUT',
    params,
  });
};

// 表格-删除称量中心
export const reqWeighCentreDelete = (params: any) => {
  return request({
    url: `/app/mes/weigh/centre/delete`,
    method: 'DELETE',
    params
  });
};

// 获取称量中心详情
export const reqWeighCentreQueryInfo = (params: any) => {
  return request({
    url: `/app/mes/weigh/centre/queryInfo`,
    method: 'GET',
    params
  });
};

// 称量中心绑定工位
export const reqWeighCentreBindStation = (data: any) => {
  return request({
    url: `/app/mes/weigh/centre/bindStation`,
    method: 'PUT',
    data,
  });
};

// 工位树(包含工位信息)
export const reqEquipmentStationTree = () => {
  return request({
    url: '/app/platform/equipment/station/tree',
    method: 'GET',
  });
};

// 查询称量中心树
export const reqWeighingCenterTree = () => {
  return request({
    url: '/app/mes/weigh/centre/tree',
    method: 'GET',
  });
};
