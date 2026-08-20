import request from '../../service';

// ems区域管理-产线管理

// 获取产线树
export const reqLineModuleTreeList = () => {
  return request({
    url: '/app/platform/factory/line/module/tree/list',
    method: 'GET',
  });
};
// 新增产线树
export const reqLineModuleSave = (data: any) => {
  return request({
    url: `/app/platform/factory/line/module/save`,
    method: 'POST',
    data,
  });
};

// 修改产线树
export const reqLineModuleUpdate = (data: any) => {
  return request({
    url: `/app/platform/factory/line/module/update`,
    method: 'PUT',
    data,
  });
};

// 删除产线树
export const reqLineModuleDelete = (id: any) => {
  return request({
    url: `/app/platform/factory/line/module/delete/${id}`,
    method: 'DELETE',
  });
};
// 获取产线列表分页
export const reqFactoryLinePage = (params: any) => {
  return request({
    url: '/app/platform/factory/line/page',
    method: 'GET',
    params,
  });
};
// 表格-新增产线
export const reqFactoryLineSave = (data: any) => {
  return request({
    url: `/app/platform/factory/line/save`,
    method: 'POST',
    data,
  });
};
// 表格-编辑产线
export const reqFactoryLineUpdate = (data: any) => {
  return request({
    url: `/app/platform/factory/line/update`,
    method: 'PUT',
    data,
  });
};
// 启停产线
export const reqFactoryLineEnable = (data: any) => {
  return request({
    url: `/app/platform/factory/line/enable`,
    method: 'PUT',
    data,
  });
};
// 表格-删除产线
export const reqFactoryLineDelete = (id: any) => {
  return request({
    url: `/app/platform/factory/line/delete/${id}`,
    method: 'GET',
  });
};
// 获取产线详情(查看按钮)
export const reqFactoryLineInfo = (id: any) => {
  return request({
    url: `/app/platform/factory/line/info/${id}`,
    method: 'GET',
  });
};
// 产线绑定房间
export const reqLineBindRoom = (data: any) => {
  return request({
    url: `/app/platform/factory/line/bind/room`,
    method: 'POST',
    data,
  });
};
// 产线绑定工位
export const reqLineBindStation = (data: any) => {
  return request({
    url: `/app/platform/factory/line/bind/station`,
    method: 'POST',
    data,
  });
};
