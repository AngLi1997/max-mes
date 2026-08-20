import request from '../../utils/request';
// 查询角色
export const getRoleList = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/role/get-role',
    method: 'get',
    params,
  });
  return res;
};

//角色树查询
export const getRoleTree = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/role/tree',
    method: 'get',
    params,
  });
  return res;
};

//新增角色类型
export const addRoleType = async (data: any) => {
  const res = await request({
    url: '/api/app/platform/role/save-type',
    method: 'post',
    data,
  });
  return res;
};

//删除角色
export const deleteRole = async (data: any) => {
  const res = await request({
    url: '/api/app/platform/role/delete-role' + `?id=${data.id}`,
    method: 'delete',
  });
  return res;
};

//新增角色
export const postRoleSave = async (data: any) => {
  const res = await request({
    url: '/api/app/platform/role/save-role',
    method: 'post',
    data,
  });
  return res;
};

//编辑角色
export const postRoleUpdate = async (data: any) => {
  const res = await request({
    url: '/api/app/platform/role/update-role',
    method: 'post',
    data,
  });
  return res;
};

//新增角色类型
export const postRoleType = async (data: any) => {
  const res = await request({
    url: '/api/app/platform/role/save-type',
    method: 'post',
    data,
  });
  return res;
};

//编辑角色类型
export const postUpdateType = async (data: any) => {
  const res = await request({
    url: '/api/app/platform/role/update-type',
    method: 'put',
    data,
  });
  return res;
};

//删除角色类型
export const deleteRoleType = async (data: any) => {
  const res = await request({
    url: '/api/app/platform/role/delete-type' + `?id=${data}`,
    method: 'delete',
  });
  return res;
};

//菜单分配全量查询
export const getMenuAll = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/menu/tree-all',
    params,
    method: 'get',
  });
  return res;
};

//角色与菜单关联-查询
export const postMenu = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/role/menu/id',
    method: 'get',
    params,
  });
  return res;
};

//角色与菜单关联-保存
export const postMenuSave = async (data: any) => {
  const res = await request({
    url: '/api/app/platform/role/menu/save',
    method: 'post',
    data,
  });
  return res;
};

//菜单功能查询
export const getMenuFunction = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/menu/function',
    method: 'get',
    params,
  });
  return res;
};

//查询部门树里未分配的人员
export const getUser = async () => {
  const res = await request({
    url: '/api/app/platform/dept/tree-unassigned',
    method: 'get',
  });
  return res;
};

//查询部门树里已分配的人员
export const getUserMessage = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/dept/tree-assigned',
    method: 'get',
    params,
  });
  return res;
};

//查询部门树
export const getDepartmentTree = async () => {
  const res = await request({
    url: '/api/app/platform/dept/tree-all',
    method: 'get',
  });
  return res;
};

//
export const getDepRoleTree = async () => {
  const res = await request({
    url: '/api/app/platform/role/tree-all',
    method: 'get',
  });
  return res;
};

//校验角色名称是否存在
export const getRoleName = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/role/validate-role',
    method: 'get',
    params,
  });
  return res;
};

//校验角色类型名称是否存在
export const getRoleTypeName = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/role/validate-roleType',
    method: 'get',
    params,
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

// 人员分配保存
export const postUserSave = async (data: any) => {
  const res = await request({
    url: '/api/app/platform/role/relate-user-save',
    method: 'post',
    data,
  });
  return res;
};

// /api/app/platform/role/relate-user-data
export const getRelateUserData = (params: any) => {
  return request({
    url: '/api/app/platform/role/relate-user-data',
    method: 'get',
    params,
  });
};

// /api/app/platform/menu/auth/tree
export const getTreePermission = (params: any) => {
  return request({
    url: '/api/app/platform/menu/auth/tree',
    method: 'get',
    params,
  });
};

//菜单分配弹框的tabs和左侧菜单列表总数居
export const getTreePermissionManage = (params: any) => {
  return request({
    url: '/api/app/platform/menu/auth/menu/tree',
    method: 'get',
    params,
  });
};

// /api/app/platform/dept/user/tree 部门用户树
export const getDeptUserTree = () => {
  return request({
    url: '/api/app/platform/dept/user/tree',
    method: 'get',
  });
};

// 角色绑定部门
export const reqDeptRoleBindDept = (data: any) => {
  return request({
    url: '/api/app/platform/dept/role/bind/dept',
    method: 'POST',
    data,
  });
};

// 回显角色绑定的部门
export const reqDeptRoleDeptList = (params: any) => {
  return request({
    url: '/api/app/platform/dept/role/dept/list',
    method: 'get',
    params,
  });
};

//权限授权弹框的tabs和左侧菜单列表总数据
export const reqMenuAuthMenuTreeList = () => {
  return request({
    url: '/api/app/platform/menu/auth/menu/tree/list',
    method: 'get',
  });
};

// 获取当前角色的拥有的权限授权的菜单(回显左侧)
export const reqRoleAuthMenuId = (params: any) => {
  return request({
    url: '/api/app/platform/role/auth/menu/id',
    method: 'get',
    params,
  });
};

// 角色管理根据菜单获取功能（包含角色拥有的功能（回显右侧））
export const reqMenuRoleFunction = (params: any) => {
  return request({
    url: '/api/app/platform/menu/role/function',
    method: 'get',
    params,
  });
};

// 权限授权保存按钮
export const reqRoleAtuhMenuSave = async (data: any) => {
  const res = await request({
    url: '/api/app/platform/role/auth/menu/save',
    method: 'post',
    data,
  });
  return res;
};
