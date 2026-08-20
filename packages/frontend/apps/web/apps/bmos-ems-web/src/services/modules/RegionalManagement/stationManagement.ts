import request from '../../service';

// ems区域管理-工位管理(部分接口在原工厂建模里)

// 获取工位树
export const reqStationModuleTreeList = () => {
  return request({
    url: '/app/platform/factory/station/module/tree/list',
    method: 'GET',
  });
};
// 工位树(包含工位信息)
export const reqEquipmentStationTree = () => {
  return request({
    url: '/app/platform/equipment/station/tree',
    method: 'GET',
  });
};

// 查设备分类树及下面的设备
export const reqEquipmentStationTreeEquipment = () => {
  return request({
    url: '/app/platform/equipment/station/tree/equipment',
    method: 'GET',
  });
};
// 新增工位树
export const reqStationModuleSave = (data: any) => {
  return request({
    url: `/app/platform/factory/station/module/save`,
    method: 'POST',
    data,
  });
};

// 修改工位树
export const reqStationModuleUpdate = (data: any) => {
  return request({
    url: `/app/platform/factory/station/module/update`,
    method: 'PUT',
    data,
  });
};

// 删除工位树
export const reqStationModuleDelete = (id: any) => {
  return request({
    url: `/app/platform/factory/station/module/delete/${id}`,
    method: 'DELETE',
  });
};

// 编辑表格-工位
export const reqEquipmentStationUpdate = (data: any) => {
  return request({
    url: `/app/platform/equipment/station/update`,
    method: 'PUT',
    data,
  });
};
