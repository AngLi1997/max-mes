import request from '../../service';
// 生产BOM配置对应接口

// 生产BOM分页
export const reqFormulaList = (params?: any) => {
  return request({
    url: '/app/mes/product/formula/page',
    method: 'get',
    params,
  });
};

// 生产BOM-版本分页
export const reqFormulaVersionList = (params?: any) => {
  return request({
    url: '/app/mes/product/formula/version/page',
    method: 'get',
    params,
  });
};

// 修改生产BOM-版本信息启停状态
export const reqFormulaVersionChangeState = (data: any) => {
  return request({
    url: '/app/mes/product/formula/version/changeState',
    method: 'PUT',
    data,
  });
};

// 生产BOM审核分页
export const reqFormulaApproveList = (params: any) => {
  return request({
    url: '/app/mes/product/formula/audit/page',
    method: 'get',
    params,
  });
};

// 提交审核
export const reqFormulaVersionApprove = (data: any) => {
  return request({
    url: '/app/mes/product/formula/audit/submit',
    method: 'PUT',
    data,
  });
};

// 版本详情(拿跳转路由页面的数据)
export const reqFormulaVersionDetail = (params: any) => {
  return request({
    url: '/app/mes/product/formula/version/detail',
    method: 'get',
    params,
  });
};

// 上方表单产品改变(或称物料改变)时查对应可选单位  (查询物料绑定拓展单位)
export const reqFormulaExtendUnit = (params: any) => {
  return request({
    url: '/app/mes/unit/list/down/extend/bound',
    method: 'get',
    params,
  });
};

// 新增生产BOM(保存整个页面数据)
export const reqFormulaAddSave = (data: any) => {
  return request({
    url: '/app/mes/product/formula/save',
    method: 'POST',
    data,
  });
};

// 新增生产BOM版本(保存整个页面数据)
export const reqFormulaVersionAddSave = (data: any) => {
  return request({
    url: '/app/mes/product/formula/version/save',
    method: 'POST',
    data,
  });
};
// 编辑生产BOM版本
export const reqFormulaVersionEditSave = (data: any) => {
  return request({
    url: '/app/mes/product/formula/version/edit',
    method: 'POST',
    data,
  });
};

/**
 * @description: 启用的产品生产BOM列表 /api/app/mes/product/formula/enableList
 * @param {string} productId 产品id
 */
export const reqProductFormulaEnableList = (productId: string) => {
  return request({
    url: '/app/mes/product/formula/enableList',
    method: 'get',
    params: {
      productId,
    },
  });
};

/**
 * @description: 产品生产BOM物料列表 /api/app/mes/product/formula/material/list
 * @param {string} versionId 生产BOM版本id
 */
export const reqProductFormulaMaterialList = (versionId: string) => {
  return request({
    url: '/app/mes/product/formula/material/list',
    method: 'get',
    params: {
      versionId,
    },
  });
};

/**
 * @description: 新产品生产BOM物料列表 /api/app/mes/product/formula/material/list
 * @param {string} versionId 生产BOM版本id
 */
export const reqProductFormulaModelMaterialList = (versionId: string, procedureModelId: string) => {
  return request({
    url: '/app/mes/product/formula/model/material/list',
    method: 'get',
    params: {
      versionId,
      procedureModelId,
    },
  });
};

/**
 * @description: 获取工序绑定物料列表 /api/app/mes/product/formula/material/listByProcedureId
 * @param {string} procedureId 工序id
 */
export const reqProductFormulaMaterialListByProcedureId = (
  procedureModelId: string,
  categoryType?: any,
  stepModelId?: any,
) => {
  return request({
    url: '/app/mes/product/formula/material/listByProcedureId',
    method: 'get',
    params: {
      procedureModelId,
      categoryType,
      stepModelId,
    },
  });
};
