import request from '../../service';

/**
 * 生产物料相关接口
 */
// 获取生产物料分类树
export const postProductMaterialCategoryTreeApi = (data: any) => {
  return request({
    url: '/app/mes/product/material/category/tree',
    method: 'post',
    data,
  });
};

// 保存生产物料分类
export const postProductMaterialCategorySaveApi = (data: any) => {
  return request({
    url: '/app/mes/product/material/category/save',
    method: 'post',
    data,
  });
};

// 编辑生产物料分类
export const putProductMaterialCategoryUpdateApi = (data: any) => {
  return request({
    url: '/app/mes/product/material/category/update',
    method: 'put',
    data,
  });
};

// 删除生产物料分类
export const deleteProductMaterialCategoryApi = (id: string) => {
  return request({
    url: '/app/mes/product/material/category/delete/' + id,
    method: 'delete',
  });
};

// 保存生产物料
export const postProductMaterialSaveApi = (data: any) => {
  return request({
    url: '/app/mes/product/material/save',
    method: 'post',
    data,
  });
};

// 生产物料编辑
export const updateProductMaterialApi = (data: any) => {
  return request({
    url: '/app/mes/product/material/update',
    method: 'put',
    data,
  });
};

// 改变生产物料启停状态
export const putProductMaterialStatusApi = (data: any) => {
  return request({
    url: '/app/mes/product/material/changeStatus',
    method: 'put',
    data,
  });
};

// 删除生产物料
export const deleteProductMaterialApi = (id: string) => {
  return request({
    url: '/app/mes/product/material/delete/' + id,
    method: 'delete',
  });
};

// 获取生产物料列表
export const getProductMaterialPageApi = (params: any) => {
  return request({
    url: '/app/mes/product/material/page',
    method: 'get',
    params,
  });
};

// 获取生产物料详情
export const getProductMaterialDetailApi = (params: { id: string }) => {
  return request({
    url: '/app/mes/product/material/detail',
    method: 'get',
    params,
  });
};

// 查询能被关联的物料列表
export const getProductMaterialPrincipalListApi = (params: { materialCategoryId: string; filter: boolean }) => {
  return request({
    url: '/app/mes/product/material/principal/list',
    method: 'get',
    params,
  });
};
// 查询标准单位下拉框
export const getMesUnitListApi = () => {
  return request({
    url: '/app/mes/unit/list/down/box',
    method: 'get',
  });
};
// 查询拓展单位下拉框
export const getMesUnitExtendListApi = (unitId: string) => {
  return request({
    url: '/app/platform/unit/list/extendUnit',
    method: 'get',
    params: { unitId },
  });
};
// // 物料同步
export const postMaterialSyncApi = (data: any) => {
  return request({
    url: '/app/mes/product/material/sync',
    method: 'post',
    data,
  });
};

// 懒加载获取物料分类同步数据
export const getProductMaterialSyncTreeApi = (params: any) => {
  return request({
    url: '/app/mes/product/material/syncTree',
    method: 'get',
    params,
  });
};

// 获取平台完整的物料树
export const getPlatformMaterialTreeApi = (params: any) => {
  return request({
    url: '/app/mes/product/material/syncTree',
    method: 'get',
    params,
  });
};

// 获取同步分类全量树
export const getProductMaterialSyncTreeAllApi = () => {
  return request({
    url: '/app/mes/product/material/syncTreeAll',
    method: 'get',
  });
};

// 查询产品信息绑定批记录树结构
export const getRecordTreeApi = () => {
  return request({
    url: '/app/mes/record/list/record/tree',
    method: 'get',
  });
};

// 绑定批记录
export const postSaveBatchRecordApi = (data: any) => {
  return request({
    url: '/app/mes/product/material/save/batchRecord',
    method: 'post',
    data,
  });
};
// 获取产品已绑定的批记录
export const getBatchRecordIdsByIdApi = (params: any) => {
  return request({
    url: '/app/mes/product/material/bindRecordIds',
    method: 'get',
    params,
  });
};

// /api/app/mes/product/material/finishProductList
export const getMaterialFinishProductList = (params?: any) => {
  return request({
    url: '/app/mes/product/material/finishProductList',
    method: 'get',
    params,
  });
};

// 查询物料绑定拓展单位 /api/app/mes/unit/list/down/extend/bound
export const getMesUnitExtendListBoundApi = (params: { materialId: string }) => {
  return request({
    url: '/app/mes/unit/list/down/extend/bound',
    method: 'get',
    params,
  });
};

// 获取扩展信息详情 /api/app/platform/unit/watch/unit/extend
export const getUnitExtendDetailApi = (params: { id: string }) => {
  return request({
    url: '/app/platform/unit/watch/unit/extend',
    method: 'get',
    params,
  });
};

// 【生产物料】根据生产物料的自定义字段信息
export const reqMaterialFieldInfo = (materialId: string) => {
  return request({
    url: '/app/mes/material/field/info/' + materialId,
    method: 'get',
  });
};

// 【生产物料】获取生产物料的能够配置的自定义字段(新增的时候调用查扩展信息下拉框数据)
export const getMaterialFieldList = () => {
  return request({
    url: '/app/mes/material/field/list',
    method: 'get',
  });
};

// 追溯物料数据
export const getMaterialTraceData = (productPlanId: string) => {
  return request({
    url: '/app/mes/material/trace/data',
    method: 'get',
    params: {
      productPlanId,
    },
  });
};
