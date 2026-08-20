import request from '../../service';

// dc 楼宇管理-楼宇管理

// 获取楼栋树 '/app/platform/tenement/tree'
export const reqTenementTree = () => {
  return request({
    url: '/app/platform/tenement/tree',
    method: 'GET',
  });
};
// 新增楼栋树
export const reqTenementSave = (data: any) => {
  return request({
    url: `/app/platform/tenement`,
    method: 'POST',
    data,
  });
};

// 修改楼栋树
export const reqTenementUpdate = (data: any) => {
  return request({
    url: `/app/platform/tenement`,
    method: 'PUT',
    data,
  });
};

// 删除楼栋
export const reqTenementDelete = (id: any) => {
  return request({
    url: `/app/platform/tenement?id=${id}`,
    method: 'DELETE',
  });
};
// 获取楼层列表分页 /api/app/platform/tenement/floor/page
export const reqTenementFloorPage = (params: any) => {
  return request({
    url: `/app/platform/tenement/floor/page`,
    method: 'GET',
    params,
  });
};

// 表格-新增楼层 /api/app/platform/tenement/floor
export const reqTenementFloorSave = (data: any) => {
  return request({
    url: `/app/platform/tenement/floor`,
    method: 'POST',
    data,
  });
};
// 表格-编辑楼层 /api/app/platform/tenement/floor
export const reqTenementFloorUpdate = (data: any) => {
  return request({
    url: `/app/platform/tenement/floor`,
    method: 'PUT',
    data,
  });
};
// 启停楼层 /api/app/platform/tenement/floor/enable
export const reqTenementFloorEnable = (data: any) => {
  return request({
    url: `/app/platform/tenement/floor/enable`,
    method: 'PUT',
    params: data,
  });
};
