import request from '../../service';

// ems设备管理-工厂建模

// 部门用户树
export const getDeptUserTree = () => {
  return request({
    url: '/app/platform/dept/user/tree',
    method: 'get',
  });
};
// 获取设备工厂模型树 (待删)
export const reqEquipmentModuleList = () => {
  return request({
    url: '/app/platform/equipment/module/list',
    // url: '/app/platform/factory/station/module/tree/list', //新
    method: 'GET',
  });
};
// 新增设备工厂模型树
export const reqEquipmentModuleSave = (data: any) => {
  return request({
    url: `/app/platform/equipment/module/save`,
    method: 'POST',
    data,
  });
};
// 修改设备工厂模型树
export const reqEquipmentModuleUpdate = (data: any) => {
  return request({
    url: `/app/platform/equipment/module/update`,
    method: 'PUT',
    data,
  });
};
// 删除工厂模型树
export const reqEquipmentModuleDelete = (id: any) => {
  return request({
    url: `/app/platform/equipment/module/delete/${id}`,
    method: 'DELETE',
  });
};

// 获取设备工位列表分页
export const reqEquipmentStationPage = (params: any) => {
  return request({
    url: '/app/platform/equipment/station/page',
    method: 'GET',
    params,
  });
};
// 获取设备工位详情(查看按钮)
export const reqEquipmentStationInfo = (id: any) => {
  return request({
    url: `/app/platform/equipment/station/info/${id}`,
    method: 'GET',
  });
};
// 新建设备工位
export const reqEquipmentStationSave = (data: any) => {
  return request({
    url: `/app/platform/equipment/station/save`,
    method: 'POST',
    data,
  });
};
// 启停设备工位
export const reqEquipmentStationEnable = (data: any) => {
  return request({
    url: `/app/platform/equipment/station/enable`,
    method: 'PUT',
    data,
  });
};
// 删除设备工位
export const reqEquipmentStationDelete = (id: any) => {
  return request({
    url: `/app/platform/equipment/station/delete/${id}`,
    method: 'GET',
  });
};

// 工位绑定设备
export const reqStationBindEquipment = (data: any) => {
  return request({
    url: `/app/platform/equipment/station/bind/equipment`,
    method: 'POST',
    data,
  });
};
// 工位绑定用户
export const reqStationBindUser = (data: any) => {
  return request({
    url: `/app/platform/equipment/station/bind/user`,
    method: 'POST',
    data,
  });
};
// 查设备分类树及下面的设备
export const reqEquipmentTree = () => {
  return request({
    url: '/app/platform/equipment/station/tree/station/equipment',
    method: 'GET',
  });
};
