import request from '../../service';

// ems区域管理-房间管理

// 获取房间树
export const reqRoomModuleTreeList = () => {
  return request({
    url: '/app/platform/factory/room/module/tree/list',
    method: 'GET',
  });
};
// 获取房间树（包含房间信息）
export const reqRoomTreeRoom = () => {
  return request({
    url: '/app/platform/factory/room/tree/room',
    method: 'GET',
  });
};
// 新增房间树
export const reqRoomModuleSave = (data: any) => {
  return request({
    url: `/app/platform/factory/room/module/save`,
    method: 'POST',
    data,
  });
};

// 修改房间树
export const reqRoomModuleUpdate = (data: any) => {
  return request({
    url: `/app/platform/factory/room/module/update`,
    method: 'PUT',
    data,
  });
};

// 删除房间树
export const reqRoomModuleDelete = (id: any) => {
  return request({
    url: `/app/platform/factory/room/module/delete/${id}`,
    method: 'DELETE',
  });
};

// 获取房间列表分页
export const reqFactoryRoomPage = (params: any) => {
  return request({
    url: '/app/platform/factory/room/page',
    method: 'GET',
    params,
  });
};

// 表格-新建房间
export const reqFactoryRoomSave = (data: any) => {
  return request({
    url: '/app/platform/factory/room/save',
    method: 'POST',
    data,
  });
};
// 表格-编辑房间
export const reqFactoryRoomUpdate = (data: any) => {
  return request({
    url: '/app/platform/factory/room/update',
    method: 'PUT',
    data,
  });
};
// 启停房间
export const reqFactoryRoomEnable = (data: any) => {
  return request({
    url: `/app/platform/factory/room/enable`,
    method: 'PUT',
    data,
  });
};
// 表格-删除房间
export const reqFactoryRoomDelete = (id: any) => {
  return request({
    url: `/app/platform/factory/room/delete/${id}`,
    method: 'GET',
  });
};
// 获取房间详情(查看按钮)
export const reqFactoryRoomInfo = (id: any) => {
  return request({
    url: `/app/platform/factory/room/info/${id}`,
    method: 'GET',
  });
};
// 部门树(绑定权限)
export const getResourcePermissionTree = () => {
  return request({
    url: `/app/platform/dept/tree-all`,
    method: 'GET',
  });
};
// 查询该数据关联的部门
export const getResourcePermissionTreeDept = (params?: any) => {
  return request({
    // url: '/app/mes/resource/permission/list/dept',
    url: '/app/platform/resource/permission/list/dept',
    method: 'GET',
    params,
  });
};
// 保存数据权限
export const reqResourcePermissionSaveReq = (data: any) => {
  return request({
    url: `/app/platform/resource/permission/save`,
    method: 'POST',
    data,
  });
};

// 房间绑定工位
export const reqRoomBindStation = (data: any) => {
  return request({
    url: `/app/platform/factory/room/bind/station`,
    method: 'POST',
    data,
  });
};
// 获取打印机设备
export const reqGetPrintEquipment = () => {
  return request({
    url: '/app/platform/equipment/getPrintEquipment',
    method: 'GET',
  });
};

// 根据code查询二级列表数据 /app/platform/dict/list/dict/code
export const reqListDictCode = (params: any) => {
  return request({
    url: '/app/platform/dict/list/dict/code',
    method: 'GET',
    params,
  });
};

// 房间关联模型
export const reqRoomBindModel = (data: any) => {
  return request({
    url: `/app/platform/factory/room/bind/model`,
    method: 'POST',
    data,
  });
};

// 保存房间环境参数配置 /api/app/platform/factory/room/envProperty
export const reqSaveRoomEnvProperty = (data: any) => {
  return request({
    url: `/app/platform/factory/room/envProperty`,
    method: 'POST',
    data,
  });
};
// 房间绑定3D模型
export const reqRoomBind3DModel = (params: any) => {
  return request({
    url: `/app/platform/factory/room/3D/model`,
    method: 'PUT',
    params,
  });
};
// 获取楼栋树 '/app/platform/tenement/tree'
export const reqTenementTree = () => {
  return request({
    url: '/app/platform/tenement/tree',
    method: 'GET',
  });
};

// 获取根据楼栋id获取楼层列表 '/app/platform/tenement/floor/list
export const reqFloorList = (data: any) => {
  return request({
    url: '/app/platform/tenement/floor/list',
    method: 'POST',
    data,
  });
};
