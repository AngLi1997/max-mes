import request from '@/utils/request';

/**
 * @description: 公式分类树 /api/app/platform/expression/category/tree
 * @param {API.GetCategoryTreeUsingGET} params 入参
 */
export const reqCategoryTreeUsingGET = (params?: API.GetCategoryTreeUsingGET) => {
  return request({
    url: '/api/app/platform/expression/category/tree',
    method: 'GET',
    params,
  });
};

/**
 * @description: 公式分类更新 /api/app/platform/expression/category/update
 * @param {any} data 入参
 */
export const reqCategoryTreeUpdate = (data: any) => {
  return request({
    url: '/api/app/platform/expression/category/update',
    method: 'PUT',
    data,
  });
};

/**
 * @description: 公式分类保存 /api/app/platform/expression/category/save
 * @param {any} data 入参
 */
export const reqCategoryTreeSave = (data: any) => {
  return request({
    url: '/api/app/platform/expression/category/save',
    method: 'POST',
    data,
  });
};

/**
 * @description: 公式分类删除 /api/app/platform/expression/category/delete/{id}
 * @param {any} data 入参
 */
export const reqCategoryTreeDelete = (id: string) => {
  return request({
    url: '/api/app/platform/expression/category/delete/' + id,
    method: 'DELETE',
  });
};

/**
 * @description: 公式列表 /api/app/platform/expression/page
 * @param {API.PageUsingGET} params 入参
 */
export const reqPageUsingGET = (params?: any) => {
  return request({
    url: '/api/app/platform/expression/page',
    method: 'GET',
    params,
  });
};

/**
 * @description: 公式解析 /api/app/platform/expression/parse
 * @param {string} expression 入参
 */
export const reqExpressionParseGET = (expression: string) => {
  return request({
    url: `/api/app/platform/expression/parse`,
    method: 'POST',
    data: {
      expression,
    },
  });
};

/**
 * @description: 公式更新 /api/app/platform/expression/update
 * @param {API.ExpressionUpdate} data 入参
 */
export const reqExpressionUpdate = (data: API.ExpressionUpdate) => {
  return request({
    url: '/api/app/platform/expression/update',
    method: 'PUT',
    data,
  });
};

/**
 * @description: 公式保存 /api/app/platform/expression/save
 * @param {API.ExpressionSave} data 入参
 */
export const reqExpressionSave = (data: API.ExpressionSave) => {
  return request({
    url: '/api/app/platform/expression/save',
    method: 'POST',
    data,
  });
};

/**
 * @description: 公式删除 /api/app/platform/expression/delete/{id}
 * @param {any} data 入参
 */
export const reqExpressionDelete = (id: string) => {
  return request({
    url: `/api/app/platform/expression/delete/${id}`,
    method: 'DELETE',
  });
};

/**
 * @description: 公式确认 /api/app/platform/expression/confirm/{id}
 * @param {string} id 入参
 */
export const reqExpressionConfirm = (id: string) => {
  return request({
    url: `/api/app/platform/expression/confirm/${id}`,
    method: 'PUT',
  });
};
/**
 * @description: 计算校验 /api/app/platform/expression/calculate
 * @param {any} data 入参
 */
export const reqExpressionCalculate = (data: any) => {
  return request({
    url: '/api/app/platform/expression/calculate',
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

// 公式验证通过
export const expressionVerify = (id: string) => {
  return request({
    url: `/api/app/platform/expression/verify/${id}`,
    method: 'PUT',
  });
};

// 获取记录树
export const getRecordTree = (params: { id: string }) => {
  return request({
    url: '/api/app/platform/expression/recordTree',
    method: 'GET',
    params,
  });
};

// 获取绑定的记录id列表
export const getBoundRecordIdList = (params: { id: string }) => {
  return request({
    url: '/api/app/platform/expression/boundRecordIdList',
    method: 'GET',
    params,
  });
};

// 公式绑定记录
export const bindRecord = (data: any) => {
  return request({
    url: '/api/app/platform/expression/bindRecord',
    method: 'POST',
    data,
  });
};
