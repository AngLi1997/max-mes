import request from '../../service';

//暂存间配置树
export const storageQueryAllTree = () => {
  return request({
    url: `/app/mes/storage/config/queryTree`,
    method: 'GET',
  });
};
//新增暂存间树 /api/app/mes/storage/config/create
export const storageConfigCreate = (params: any) => {
  return request({
    url: `/app/mes/storage/config/create`,
    method: 'POST',
    data: params,
  });
};
//删除暂存间树 /api/app/mes/storage/config/delete

export const storageConfigDelete = (params: any) => {
  return request({
    url: `/app/mes/storage/config/delete`,
    method: 'delete',
    params,
  });
};
//编辑暂存间树 /api/app/mes/storage/config/edit

export const storageConfigEdit = (data: any) => {
  return request({
    url: `/app/mes/storage/config/edit`,
    method: 'PUT',
    data,
  });
};
//暂存间配置列表
export const storageQueryList = (params: any) => {
  return request({
    url: `/app/mes/material/position/page`,
    method: 'GET',
    params,
  });
};

//暂存间配置启用 /api/app/mes/material/position/enable

export const storageConfigEnable = (params: any) => {
  return request({
    url: `/app/mes/material/position/enable`,
    method: 'PUT',
    params,
  });
};

//暂存间配置停用 /api/app/mes/material/position/disable

export const storageConfigDisable = (params: any) => {
  return request({
    url: `/app/mes/material/position/disable`,
    method: 'PUT',
    params,
  });
};

//暂存间配置删除 /api/app/mes/material/position/delete

export const storageConfigDeleteById = (params: any) => {
  return request({
    url: `/app/mes/material/position/delete`,
    method: 'DELETE',
    params,
  });
};

//暂存间配置新增 /api/app/mes/material/position/create

export const storageConfigCreateById = (params: any) => {
  return request({
    url: `/app/mes/material/position/create`,
    method: 'POST',
    data: params,
  });
};

//暂存间配置编辑 /api/app/mes/material/position/edit

export const storageConfigEditById = (params: any) => {
  return request({
    url: `/app/mes/material/position/edit`,
    method: 'POST',
    data: params,
  });
};

//暂存间配置部门权限 /api/app/mes/material/position/listDataPermission

export const storageConfigListDataPermission = (params: any) => {
  return request({
    url: `/app/mes/material/position/listDataPermission`,
    method: 'GET',
    params,
  });
};
