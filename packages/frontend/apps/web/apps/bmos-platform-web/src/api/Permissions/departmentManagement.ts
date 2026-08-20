import request from '../../utils/request';
// 查询所有可分配的人员(分配人员按钮的弹框展示树内容)
export const assignPerson = (params: any) => {
  return request({
    url: '/api/app/platform/dept/assign-person',
    method: 'get',
    params,
  });
};

// 部门删除
export const deleteDepartment = (params: any) => {
  return request({
    url: '/api/app/platform/dept/delete',
    method: 'delete',
    params,
  });
};
// 部门与用户关联-查询（点左边树回显右边部门信息及人员信息表格）
export const relateUserData = async (params: any) => {
  const res = await request({
    url: '/api/app/platform/dept/relate-user-data',
    method: 'get',
    params,
  });
  return res;
};

// 部门关联用户-单个删除
export const relateUserDel = (data: any) => {
  return request({
    url: '/api/app/platform/dept/remove/user',
    method: 'delete',
    data,
  });
};
// 部门关联用户-全部删除
export const relateUserDelAll = (params: any) => {
  return request({
    url: '/api/app/platform/dept/relate-user-delAll',
    method: 'delete',
    params,
  });
};
// 部门与用户关联-保存（分配人员弹框确定按钮）旧
export const relateUserSave = (data: any) => {
  return request({
    url: '/api/app/platform/dept/relate-user-save',
    method: 'post',
    data,
  });
};
// 部门与用户关联-保存（分配人员弹框确定按钮）新
export const relateDeptSave = (data: any) => {
  return request({
    url: '/api/app/platform/user/relate-dept-save',
    method: 'post',
    data,
  });
};
// 部门保存（新增弹框确定按钮）
export const addDepartment = (data: any) => {
  return request({
    url: '/api/app/platform/dept/save',
    method: 'post',
    data,
  });
};
// 部门树查询（懒加载）
export const departmentTree = (params: any) => {
  return request({
    url: '/api/app/platform/dept/tree',
    method: 'get',
    params,
  });
};

// 部门树查询（全量查询）
export const departmentTreeAll = (params?: any) => {
  return request({
    // url: "/api/app/platform/dept/tree-all",
    url: '/api/app/platform/dept/tree-all',
    method: 'get',
    params,
  });
};

// 部门内部管理左侧树接口
export const reqDeptIntervalTree = (params?: any) => {
  return request({
    url: '/api/app/platform/dept/interval/tree',
    method: 'get',
    params,
  });
};

// 部门编辑（编辑弹框确定按钮）
export const editDepartment = (data: any) => {
  return request({
    url: '/api/app/platform/dept/update',
    method: 'put',
    data,
  });
};

// 校验部门名称是否存在
export const validateDept = (params: any) => {
  return request({
    url: '/api/app/platform/dept/validate-dept',
    method: 'get',
    params,
  });
};

// 工位树(包含工位信息)
export const reqEquipmentStationTree = () => {
  return request({
    url: '/api/app/platform/equipment/station/tree',
    method: 'GET',
  });
};

// 用户绑定工位（弹框保存）
export const reqEquipmentStationUserBindStation = (data: any) => {
  return request({
    url: '/api/app/platform/equipment/station/user/bind/station',
    method: 'POST',
    data,
  });
};
// 获取用户已绑定的工位
export const reqEquipmentStationUserStationList = (params: any) => {
  return request({
    url: `/api/app/platform/equipment/station/user/station/list`,
    method: 'GET',
    params,
  });
};

// 根据用户id查询用户信息
export const reqUserIdGetUser = (id: any) => {
  return request({
    url: `/api/app/platform/user/info/${id}`,
    method: 'GET',
  });
};

// 部门管理右上角分配角色保存
export const reqDeptBindRole = (data: any) => {
  return request({
    url: '/api/app/platform/dept/bind/role',
    method: 'POST',
    data,
  });
};
// 部门内部管理绑定角色保存
export const reqUserDeptUserBindRole = (params: any) => {
  return request({
    url: '/api/app/platform/user/dept/user/bind/role',
    method: 'GET',
    params,
  });
};

// 获取当前部门所拥有的角色(返的id集合回显树勾选)
export const reqDeptDeptRole = (params: any) => {
  return request({
    url: `/api/app/platform/dept/dept/role`,
    method: 'GET',
    params,
  });
};
//绑定工位的树数据（含俩树）
export const reqFactoryLineUserLine = (params: any) => {
  return request({
    url: `/api/app/platform/factory/line/user/line`,
    method: 'GET',
    params,
  });
};
