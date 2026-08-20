import request from '../../service';
// wms货品信息相关接口...............
/**
 * 生产物料相关接口
 */
// 查询全部货品分类树
export const postCargoCategoryTreeApi = (params: any) => {
  return request({
    url: '/app/wms/cargo/category/queryTree',
    method: 'get',
    params,
  });
};

// 新增货品分类
export const postCargoCategorySaveApi = (data: any) => {
  return request({
    url: '/app/wms/cargo/category/create',
    method: 'post',
    data,
  });
};

// 编辑生产物料分类
export const putCargoCategoryUpdateApi = (data: any) => {
  return request({
    url: '/app/wms/product/material/category/update',
    method: 'put',
    data,
  });
};
// 删除货品分类
export const deleteCargoCategoryApi = (id: any) => {
  return request({
    url: `/app/wms/cargo/category/delete?id=${id}`,
    method: 'delete',
  });
};

// 新增货品
export const postCargoSaveApi = (data: any) => {
  return request({
    url: '/app/wms/cargo/create',
    method: 'post',
    data,
  });
};

// 编辑货品
export const updateCargoApi = (data: any) => {
  return request({
    url: '/app/wms/cargo/edit',
    method: 'put',
    data,
  });
};
// 启用货品
export const putCargoEnableStatusApi = (params: any) => {
  return request({
    url: '/app/wms/cargo/enable',
    method: 'put',
    params,
  });
};
// 停用货品
export const putCargoDisenableStatusApi = (params: any) => {
  return request({
    url: '/app/wms/cargo/disable',
    method: 'put',
    params,
  });
};
// 删除货品
export const deleteCargoApi = (id: string) => {
  return request({
    url: `/app/wms/cargo/delete?id=${id}`,
    method: 'delete',
  });
};

// 获取生产物料列表
export const getCargoPageApi = (params: any) => {
  return request({
    url: '/app/wms/cargo/queryPage',
    method: 'get',
    params,
  });
};

// 根据id查询货品详情信息
export const getCargoDetailApi = (params: { id: string }) => {
  return request({
    url: '/app/wms/cargo/queryInfo',
    method: 'get',
    params,
  });
};

// 查询能被关联的物料列表 (params: { categoryId: string; filter: boolean })
export const getCargoPrincipalListApi = (params: { categoryId: string }) => {
  return request({
    url: '/app/wms/cargo/queryNotMemberListByCategoryId',
    method: 'get',
    params,
  });
};
// 查询标准单位下拉框
// export const getWmsUnitListApi = () => {
//   return request({
//     url: '/app/wms/unit/getAllUnit',(标准单位及扩展单位一起返的)
//     method: 'get',
//   });
// };
export const getWmsUnitListApi = () => {
  return request({
    url: '/app/wms/unit/getBaseUnitList',
    method: 'get',
  });
};

// 查询拓展单位下拉框
export const getMesUnitExtendListApi = (unitId: string) => {
  return request({
    // url: '/app/wms/unit/list/down/extend',
    url: '/app/wms/unit/getExtUnitListByBaseId',
    method: 'get',
    params: { unitId },
  });
};
// 同步货品
export const postCargoSyncApi = (data: any) => {
  return request({
    url: '/app/wms/cargo/sync',
    method: 'post',
    data,
  });
};

// 懒加载获取物料分类同步数据
export const getCargoSyncTreeApi = (params: any) => {
  return request({
    url: '/app/wms/cargo/syncTree',
    method: 'get',
    params,
  });
};

// 获取同步分类全量树
export const getCargoSyncTreeAllApi = () => {
  return request({
    url: '/app/wms/cargo/syncTreeAll',
    method: 'get',
  });
};

// 查询产品信息绑定批记录树结构
export const getRecordTreeApi = () => {
  return request({
    url: '/app/wms/record/list/record/tree',
    method: 'get',
  });
};

// 绑定批记录
export const postSaveBatchRecordApi = (data: any) => {
  return request({
    url: '/app/wms/product/material/save/batchRecord',
    method: 'post',
    data,
  });
};
// 获取产品已绑定的批记录
export const getBatchRecordIdsByIdApi = (params: any) => {
  return request({
    url: '/app/wms/product/material/bindRecordIds',
    method: 'get',
    params,
  });
};

// /api/app/wms/product/material/finishProductList
export const getMaterialFinishProductList = (params?: any) => {
  return request({
    url: '/app/wms/product/material/finishProductList',
    method: 'get',
    params,
  });
};

// 查询物料绑定拓展单位 /api/app/wms/unit/list/down/extend/bound
export const getMesUnitExtendListBoundApi = (params: { materialId: string }) => {
  return request({
    url: '/app/wms/unit/list/down/extend/bound',
    method: 'get',
    params,
  });
};
