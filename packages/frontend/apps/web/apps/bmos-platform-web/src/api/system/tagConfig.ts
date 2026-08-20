import request from '@/utils/request';
// 标签管理树 /api/app/platform/tag/type/listAll
export const reqPlatformTagTypeGET = () => {
  return request({
    url: '/api/app/platform/tag/type/listAll',
    method: 'GET',
  });
};

//标签管理列表 /api/app/platform/tag/instance/queryPage
export const reqPlatformTagInstanceQueryPageGET = (params?: any) => {
  return request({
    url: '/api/app/platform/tag/instance/queryPage',
    method: 'GET',
    params,
  });
};

//标签管理启用 /api/app/platform/tag/instance/enable
export const reqPlatformTagInstanceEnablePOST = (params?: any) => {
  return request({
    url: '/api/app/platform/tag/instance/enable',
    method: 'PUT',
    params,
  });
};

//标签管理停用 /api/app/platform/tag/instance/disable
export const reqPlatformTagInstanceDisablePOST = (params?: any) => {
  return request({
    url: '/api/app/platform/tag/instance/disable',
    method: 'PUT',
    params,
  });
};

//标签业务场景树 /api/app/platform/tag/scene/listByTypeId
export const reqPlatformTagSceneListByTypeIdGET = (params?: any) => {
  return request({
    url: '/api/app/platform/tag/scene/listByTypeId',
    method: 'GET',
    params,
  });
};

//标签样式 /api/app/platform/tag/define/listAll
export const reqPlatformTagDefineListAllTypeIdGET = () => {
  return request({
    url: '/api/app/platform/tag/define/listAll',
    method: 'GET',
  });
};

//标签样式分类 /api/app/platform/tag/define/info
export const reqPlatformInfoGET = (params?: any) => {
  return request({
    url: '/api/app/platform/tag/scene/info',
    method: 'GET',
    params,
  });
};

//新建标签 /api/app/platform/tag/instance/create
export const reqPlatformTagInstanceCreatePOST = (data?: any) => {
  return request({
    url: '/api/app/platform/tag/instance/create',
    method: 'POST',
    data,
  });
};

//查询标签详情 /api/app/platform/tag/instance/info、

export const reqPlatformTagInstanceInfoGET = (params?: any) => {
  return request({
    url: '/api/app/platform/tag/instance/info',
    method: 'GET',
    params,
  });
};

//编辑标签 /api/app/platform/tag/instance/edit
export const reqPlatformTagInstanceEditPOST = (data?: any) => {
  return request({
    url: '/api/app/platform/tag/instance/edit',
    method: 'PUT',
    data,
  });
};

//删除标签 /api/app/platform/tag/instance/delete

export const reqPlatformTagInstanceDeletePOST = (params?: any) => {
  return request({
    url: '/api/app/platform/tag/instance/delete',
    method: 'DELETE',
    params,
  });
};
