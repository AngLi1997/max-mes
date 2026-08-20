import request from '../../utils/request';
// 查询菜单
export const getMenuList = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/menu/admin/tree',
    method: 'get',
    params,
  });
  return res;
};

export const getPermissionMenuList = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/menu/auth/all',
    method: 'get',
    params,
  });
  return res;
};

// 弹窗查询角色
export const getRoleList = async (params?: any) => {
  const res = await request({
    url: '/api/app/platform/role/tree-all',
    method: 'get',
    params,
  });
  return res;
};

// 选中左侧菜单 右侧渲染角色树
export const getRoleTreeAll = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/role/aggregate/tree',
    method: 'get',
    params,
  });
  return res;
};

export const getRoleAddTreeAll = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/role/auth/role/tree',
    method: 'get',
    params,
  });
  return res;
};

export const getPerrmissionRoleTreeAll = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/role/auth/role/tree',
    method: 'get',
    params,
  });
  return res;
};
export const getPerrmissionRoleTreeAll2 = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/menu/role/tree;',
    method: 'get',
    params,
  });
  return res;
};

//默认选中
export const getRoleTree = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/menu/relate-role-data',
    method: 'get',
    params,
  });
  return res;
};

//默认选中
export const getPermissionRoleTree = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/role/auth/list',
    method: 'get',
    params,
  });
  return res;
};

// 保存
export const postMenuSave = async (data: any) => {
  const res = await request({
    url: '/api/app/platform/menu/role/save',
    method: 'post',
    data,
  });
  return res;
};

export const postPermissionMenuSave = async (data: any) => {
  const res = await request({
    url: '/api/app/platform/menu/auth/role/save',
    method: 'post',
    data,
  });
  return res;
};

//终端类型查询
export const getTerminalType = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/menu/admin/root/list',
    method: 'get',
    params,
  });
  return res;
};
// 权限授权用admin/tree可查全部功能按钮
export const getPermissionMenuList2 = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/menu/admin/tree',
    method: 'get',
    params,
  });
  return res;
};
