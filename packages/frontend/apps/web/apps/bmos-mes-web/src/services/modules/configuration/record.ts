import request from '../../service';

// /api/app/mes/record/list/record
export const reqRecordList = (
  params: API.RecordListRecordReq = {
    pageNum: 1,
    pageSize: 10,
  },
) => {
  if (params.categoryId + '' === 'all') {
    params.categoryId = void 0;
  }
  return request({
    url: '/app/mes/record/list/record',
    method: 'GET',
    params,
  });
};

// 记录管理页查版本信息
export const reqRecordManageList = (
  params: API.RecordListRecordReq = {
    pageNum: 1,
    pageSize: 10,
  },
) => {
  if (params.categoryId + '' === 'all') {
    params.categoryId = void 0;
  }
  return request({
    url: '/app/mes/record/manage/list/record',
    method: 'GET',
    params,
  });
};

/**
 * @description: 根据多个版本id查询记录项 /api/app/mes/record/list/record/item
 * @param {versionId} 版本id
 * @returns {Promise<any>} 返回一个promise res
 */

export const reqRecordListItem = (data: API.ListRecordItemReq) => {
  return request<{ code: number }>({
    url: '/app/mes/record/list/record/item',
    method: 'POST',
    data,
  });
};

// /api/app/mes/record/list/category { categoryName: '' }
export const reqCategoryList = (params: { categoryName: string }) => {
  return request({
    url: '/app/mes/record/list/category',
    method: 'GET',
    params,
  });
};

// /api/app/mes/record/copy/version 复制已有得版本
export const recordCopyVersion = (data: API.RecordCopyVersionReq) => {
  return request({
    url: '/app/mes/record/copy/version',
    method: 'POST',
    data,
  });
};

// /api/app/mes/record/save/record 新增记录
export const recordSaveRecord = (data: API.RecordSaveRecordReq) => {
  return request({
    url: '/app/mes/record/save/record',
    method: 'POST',
    data,
  });
};

// /api/app/mes/record/save/category 新增分类
export const recordSaveCategory = (data: API.RecordSaveCategoryReq) => {
  return request({
    url: '/app/mes/record/save/category',
    method: 'POST',
    data,
  });
};

// /api/app/mes/record/delete/category 删除分类
export const recordDeleteCategory = (params: API.RecordDeleteCategoryRes) => {
  return request({
    url: '/app/mes/record/delete/category',
    method: 'GET',
    params,
  });
};

// /api/app/mes/record/update/category
export const recordUpdateCategory = (data: API.RecordUpdateCategoryReq) => {
  return request({
    url: '/app/mes/record/update/category',
    method: 'POST',
    data,
  });
};

// 下载格式校验文件
export const recordDownloadByUrl = (url: string) => {
  return request({
    url: `/app/mes/record/downloadByUrl`,
    method: 'POST',
    params: {
      url,
    },
    responseType: 'arraybuffer',
  });
};

/**
 * @description 获取所有版本 /api/app/mes/record/list/version?recordId
 * @param {API.RecordListVersionReq} recordId
 * @returns {Promise<any>}
 */
export const getVersionListByRecord = (recordId: API.RecordListVersionRes) => {
  return request({
    url: '/app/mes/record/list/version',
    method: 'GET',
    params: { recordId },
  });
};

/**
 * @description 更新版本状态 /api/app/mes/record/update/version
 * @param {API.RecordUpdateVersionReq} data
 * @returns {Promise<any>}
 */
export const recordUpdateVersion = (data: API.RecordUpdateVersionReq) => {
  return request({
    url: '/app/mes/record/update/version',
    method: 'POST',
    data,
  });
};

/**
 * @description: 根据记录项id查询组件 /api/app/mes/record/list/component
 * @param {API.RecordListComponentReq} params
 * @returns {Promise<any>} 返回一个promise
 */

export const reqRecordListComponentReq = (params: API.ListRecordItemRes) => {
  return request({
    url: '/app/mes/record/list/component',
    method: 'GET',
    params,
  });
};

/**
 * @description: 部门树 /api/app/mes/resource/permission/dept/tree
 * @param params
 * @returns
 */
export const getResourcePermissionTree = (params?: any) => {
  return request({
    url: '/app/mes/resource/permission/dept/tree',
    method: 'GET',
    params,
  });
};

/**
 * @description 部门树已选择 /api/app/mes/resource/permission/list/dept
 * @param params
 * @returns
 */
export const getResourcePermissionTreeDept = (params?: any) => {
  return request({
    url: '/app/mes/resource/permission/list/dept',
    method: 'GET',
    params,
  });
};

/**
 * @description 保存数据权限 // /api/app/mes/resource/permission/save
 * @param data
 * @returns
 */
export const resourcePermissionSave = (data: any) => {
  return request({
    url: '/app/mes/resource/permission/save',
    method: 'POST',
    data,
  });
};

/**
 * @description 查询历史 // /api/app/mes/record/list/record/log
 * @param params
 * @returns
 */
export const getRecordLog = (params: { versionId: string }) => {
  return request({
    url: '/app/mes/record/list/record/log',
    method: 'GET',
    params,
  });
};

/**
 * @description 查询产品树 /api/app/mes/product/material/productTree
 * @param params
 * @returns
 */
export const getRecordProductTree = (params: any = { categoryType: 2 }) => {
  return request({
    url: '/app/mes/product/material/productTree',
    method: 'GET',
    params,
  });
};

/**
 * @description 绑定产品 /api/app/mes/record/save/product
 * @param data
 * @returns
 */
export const recordSaveProduct = (data: API.RecordSaveProductReq) => {
  return request({
    url: '/app/mes/record/save/product',
    method: 'POST',
    data,
  });
};

/**
 * @description 查询公式树 /api/app/mes/record/expressionBindTree
 * @param params
 * @returns
 */
export const getExpressionBindTree = (params: any) => {
  return request({
    url: '/app/mes/record/expressionBindTree',
    method: 'GET',
    params,
  });
};

/**
 * @description 绑定公式 /api/app/mes/record/bindExpression
 * @param data
 * @returns
 */
export const recordBindExpression = (data: any) => {
  return request({
    url: '/app/mes/record/bindExpression',
    method: 'POST',
    data,
  });
};

/**
 * @description 获取已绑定的公式 /api/app/mes/record/boundExpressionIdList
 * @param params
 * @returns
 */
export const getBoundExpressionIdList = (params: { id: string }) => {
  return request({
    url: '/app/mes/record/boundExpressionIdList',
    method: 'get',
    params,
  });
};

/**
 * @description 查询记录是否可新增版本 /app/mes/record/checkout/save/record
 * @param params
 * @returns
 */
export const recordCheckoutSaveRecord = (params: { recordId: string }) => {
  return request({
    url: '/app/mes/record/checkout/save/record',
    method: 'get',
    params,
  });
};

/**
 * @description 查询记录是否可新增版本 /app/query/product/id
 * @param params
 * @returns
 */
export const recordQueryProductId = (params: { recordId: string }) => {
  return request({
    url: '/app/mes/record/query/product/id',
    method: 'GET',
    params,
  });
};

/**
 * @description 根据产品查询记录 /api/app/mes/record/query/list/record
 * @param {string} productId 产品id
 * @returns
 */
export const recordQueryListRecordByProductId = (productId: string) => {
  return request({
    url: '/app/mes/record/query/list/record',
    method: 'GET',
    params: { productId },
  });
};

/**
 * @description 根据记录查询版本 /api/app/mes/record/query/record/version
 * @param {string} recordId 记录id
 * @returns
 */
export const recordQueryVersionListByRecordId = (recordId: string) => {
  return request({
    url: '/app/mes/record/query/record/version',
    method: 'GET',
    params: { recordId },
  });
};
