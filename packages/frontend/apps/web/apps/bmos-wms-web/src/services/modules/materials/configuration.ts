import request from '../../service';

// wms货位配置相关接口

//暂存间配置树
export const storageQueryAllTree = () => {
  return request({
    url: `/app/wms/storage/config/queryTree`,
    method: 'GET',
  });
};
//新增暂存间树 /api/app/wms/storage/config/create
export const storageConfigCreate = (params: any) => {
  return request({
    url: `/app/wms/storage/config/create`,
    method: 'POST',
    data: params,
  });
};
//删除暂存间树 /api/app/wms/storage/config/delete

export const storageConfigDelete = (params: any) => {
  return request({
    url: `/app/wms/storage/config/delete`,
    method: 'delete',
    params,
  });
};
//编辑暂存间树 /api/app/wms/storage/config/edit

export const storageConfigEdit = (data: any) => {
  return request({
    url: `/app/wms/storage/config/edit`,
    method: 'PUT',
    data,
  });
};
//暂存间配置列表
export const storageQueryList = (params: any) => {
  return request({
    url: `/app/wms/material/position/page`,
    method: 'GET',
    params,
  });
};

//暂存间配置启用 /api/app/wms/material/position/enable

export const storageConfigEnable = (params: any) => {
  return request({
    url: `/app/wms/material/position/enable`,
    method: 'PUT',
    params,
  });
};

//暂存间配置停用 /api/app/wms/material/position/disable

export const storageConfigDisable = (params: any) => {
  return request({
    url: `/app/wms/material/position/disable`,
    method: 'PUT',
    params,
  });
};

//暂存间配置删除 /api/app/wms/material/position/delete

export const storageConfigDeleteById = (params: any) => {
  return request({
    url: `/app/wms/material/position/delete`,
    method: 'DELETE',
    params,
  });
};

//暂存间配置新增 /api/app/wms/material/position/create

export const storageConfigCreateById = (params: any) => {
  return request({
    url: `/app/wms/material/position/create`,
    method: 'POST',
    data: params,
  });
};

//暂存间配置编辑 /api/app/wms/material/position/edit

export const storageConfigEditById = (params: any) => {
  return request({
    url: `/app/wms/material/position/edit`,
    method: 'POST',
    data: params,
  });
};

//暂存间配置部门权限 /api/app/wms/material/position/listDataPermission

export const storageConfigListDataPermission = (params: any) => {
  return request({
    url: `/app/wms/material/position/listDataPermission`,
    method: 'GET',
    params,
  });
};
// 部门权限弹框相关的接口............
/**
 * @description: 部门树 /api/app/wms/resource/permission/dept/tree
 * @param params
 * @returns
 */
export const getResourcePermissionTree = (params?: any) => {
  return request({
    url: '/app/wms/resource/permission/dept/tree',
    method: 'GET',
    params,
  });
};

/**
 * @description 部门树已选择 /api/app/wms/resource/permission/list/dept
 * @param params
 * @returns
 */
export const getResourcePermissionTreeDept = (params?: any) => {
  return request({
    url: '/app/wms/resource/permission/list/dept',
    method: 'GET',
    params,
  });
};
/**
 * @description 保存数据权限 // /api/app/wms/resource/permission/save
 * @param data
 * @returns
 */
export const resourcePermissionSave = (data: any) => {
  return request({
    url: '/app/wms/resource/permission/save',
    method: 'POST',
    data,
  });
};
