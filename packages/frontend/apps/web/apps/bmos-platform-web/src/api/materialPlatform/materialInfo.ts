import request from '@/utils/request';

// 物料分类树（全量）查询
export const getMaterialCategoryTreeApi = () => {
  return request({
    url: '/api/app/platform/material/category/tree',
    method: 'get',
    params: {},
  });
};

// 保存物料分类
export const postMaterialCategorySaveApi = (data: API.CategorySave) => {
  return request({
    url: '/api/app/platform/material/category/save',
    method: 'post',
    data,
  });
};

// 编辑物料分类
export const putMaterialCategoryUpdateApi = (data: API.CategoryUpdate) => {
  return request({
    url: '/api/app/platform/material/category/update',
    method: 'put',
    data,
  });
};

// 删除物料分类
export const deleteMaterialCategoryApi = (id: string) => {
  return request({
    url: '/api/app/platform/material/category/delete/' + id,
    method: 'delete',
  });
};

// 保存物料
export const postMaterialSaveApi = (data: any) => {
  return request({
    url: '/api/app/platform/material/save',
    method: 'post',
    data,
  });
};

// 更新物料
export const updateMaterialApi = (data: any) => {
  return request({
    url: '/api/app/platform/material/update',
    method: 'post',
    data,
  });
};

// 改变物料启停状态
export const putMaterialStatusApi = (data: API.MaterialChangeStatus) => {
  return request({
    url: '/api/app/platform/material/changeStatus',
    method: 'put',
    data,
  });
};

// 删除物料
export const deleteMaterialApi = (id: string) => {
  return request({
    url: '/api/app/platform/material/delete/' + id,
    method: 'delete',
  });
};

// 获取物料列表
export const getMaterialPageApi = (params: API.GetPageUsingGET) => {
  return request({
    url: '/api/app/platform/material/page',
    method: 'get',
    params,
  });
};

// 获取物料详情
export const getMaterialDetailApi = (params: { id: string }) => {
  return request({
    url: '/api/app/platform/material/detail',
    method: 'get',
    params,
  });
};

// 查询能被关联的物料列表
export const getMaterialPrincipalListApi = (params: { materialCategoryId: string }) => {
  return request({
    url: '/api/app/platform/material/principal/list',
    method: 'get',
    params,
  });
};
// 查询标准单位下拉框
export const getUnitListApi = () => {
  return request({
    url: '/api/app/platform/unit/list/down/box',
    method: 'get',
  });
};
// 物料下发
export const postMaterialIssueApi = (data: any) => {
  return request({
    url: '/api/app/platform/material/issue',
    method: 'post',
    data,
  });
};

// 懒加载获取物料分类下发数据
export const getMaterialCategoryIssueTreeApi = (params: { parentId?: string; keyword?: string }) => {
  return request({
    url: '/api/app/platform/material/category/issueTree',
    method: 'get',
    params,
  });
};

// 获取平台完整的物料树
export const getMaterialTreeApi = () => {
  return request({
    url: '/api/app/platform/material/tree',
    method: 'get',
  });
};
// 下发业务列表

export const getMaterialIssueBusinesseApi = () => {
  return request({
    url: '/api/app/platform/material/issueBusinesses',
    method: 'get',
  });
};

// 物料绑定拓展单位
// /api/app/platform/material/extendUnit/extendUnit/bind
export const postMaterialExtendUnitBindApi = (data: any) => {
  return request({
    url: '/api/app/platform/material/extendUnit/extendUnit/bind',
    method: 'post',
    data,
  });
};

// 物料绑定的拓展单位列表
// /api/app/platform/material/extendUnit/extendUnit/list
export const getMaterialExtendUnitListApi = (params: { materialId: string }) => {
  return request({
    url: '/api/app/platform/material/extendUnit/extendUnit/list',
    method: 'get',
    params,
  });
};

// 查询扩展单位下拉框
// /api/app/platform/unit/list/extendUnit
export const getExtendUnitListApi = (params: { unitId: string }) => {
  return request({
    url: '/api/app/platform/unit/list/extendUnit',
    method: 'get',
    params,
  });
};

// 下载物料模板
export const reqMaterialImportTemplate = () => {
  return request({
    url: `/api/app/platform/material/import/template`,
    method: 'GET',
    responseType: 'arraybuffer',
    original: true,
  });
};

// 导入物料
export const reqMaterialImport = (data: any) => {
  return request({
    url: `/api/app/platform/material/import/material`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};

// 导出物料
export const reqMaterialExport = async (params: any) => {
  return await request({
    url: `/api/app/platform/material/export/material`,
    method: 'GET',
    params,
    responseType: 'arraybuffer',
    original: true,
  });
};
