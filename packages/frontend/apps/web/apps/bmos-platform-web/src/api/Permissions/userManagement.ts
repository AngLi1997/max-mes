import request from '../../utils/request';
// 查询表格
export const userPage = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/user/page',
    method: 'get',
    params,
  });
  return res;
};
// 添加用户
export const addUser = (data: any) => {
  return request({
    url: '/api/app/platform/user/save',
    method: 'post',
    data,
  });
};
// 编辑用户
export const editUser = (data: any) => {
  return request({
    url: '/api/app/platform/user/update',
    method: 'put',
    data,
  });
};

// 重置密码
export const rePassWord = (data: any) => {
  return request({
    url: '/api/app/platform/user/resetPwd',
    method: 'put',
    data,
  });
};

// 用户与角色关联-查找(绑定角色出现的弹框)
export const relateRoleData = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/user/relate-role-data',
    method: 'get',
    params,
  });
  return res;
};
// 用户与部门关联-查找(点分配部门出现的弹框)
export const relateDeptData = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/user/relate-dept-data',
    method: 'get',
    params,
  });
  return res;
};

//启停
export const startStop = (data: any) => {
  return request({
    url: '/api/app/platform/user/start',
    method: 'put',
    data,
  });
};

// 绑定角色保存框
export const bindRoleSave = (data: any) => {
  return request({
    url: '/api/app/platform/user/relate-role-save',
    method: 'post',
    data,
  });
};

//角色树查询(绑定角色弹框初始化) （懒加载需要）
export const getRoleTree = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/role/tree',
    method: 'get',
    params,
  });
  return res;
};
//角色树查询(绑定角色弹框初始化) （全量查）
export const getRoleTreeAll = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/role/tree-all',
    method: 'get',
    params,
  });
  return res;
};
// 角色树查询(绑定角色弹框初始化) （全量查）新
export const getRoleAll = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/role/role-tree-all',
    method: 'get',
    params,
  });
  return res;
};
// 角色树查询(绑定角色弹框初始化) （全量查）最新
export const getRoleAggregate = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/role/aggregate/tree',
    method: 'get',
    params,
  });
  return res;
};

// 部门内部管理的分配角色弹框
export const reqRoleDeptRoleTree = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/role/dept/role/tree',
    method: 'get',
    params,
  });
  return res;
};

// 校验用户名称是否存在
export const validateUser = (params: any) => {
  return request({
    url: '/api/app/platform/user/validate-user',
    method: 'get',
    params,
  });
};

// 下载模板
export const reqUserDownloadTemplate = () => {
  return request({
    url: `/api/app/platform/user/download/template`,
    method: 'GET',
    responseType: 'arraybuffer',
    original: true,
  });
};

// 导入人员
export const reqUserImport = (data: any) => {
  return request({
    url: `/api/app/platform/user/import/user`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};

// 导出人员
export const reqUserExport = async (params: any) => {
  return await request({
    url: `/api/app/platform/user/export/user`,
    method: 'GET',
    params,
    responseType: 'arraybuffer',
    original: true,
  });
};
