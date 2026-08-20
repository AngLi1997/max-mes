import request from '@/utils/request';

/**
 * @description: 字典配置列表 /api/app/platform/dict/list/dict
 * @param {any} params 入参
 */
export const reqPlatformDictListGET = (
  params?: any,
) => {
  return request({
    url: '/api/app/platform/dict/list/dict',
    method: 'GET',
    params,
  });
};

/**
 * @description: 字典配置版本列表 /api/app/platform/dict/list/dict/detail
 * @param {any} params 入参
 */
export const reqPlatformDictListDetailGET = (
  params?: any,
) => {
  return request({
    url: '/api/app/platform/dict/list/dict/detail',
    method: 'GET',
    params,
  });
};
// 字典点编辑时回显跳转页面的表格数据
export const reqPlatformDictListWatchGET = (
  params?: any,
) => {
  return request({
    url: '/api/app/platform/dict/watch/dict',
    method: 'GET',
    params,
  });
};



/**
 * @description: 字典配置下拉列表 /api/app/platform/dict/list/dict/down
 * @param {any} params 入参
 */
export const reqPlatformDictListDownGET = (
  dictId: string,
) => {
  return request({
    url: `/api/app/platform/dict/list/dict/down?dictId=${dictId}`,
    method: 'GET',
  });
};

/**
 * @description: 保存字典 /api/app/platform/dict/save/dict
 * @param {any} params 入参
 */
export const reqPlatformDictSavePOST = (
  data: any,
) => {
  return request({
    url: `/api/app/platform/dict/save/dict`,
    method: 'POST',
    data
  });
};

/**
 * @description: 编辑字典 /api/app/platform/dict/update/dict
 * @param {any} params 入参
 */
export const reqPlatformDictUpdatePOST = (
  data: any,
) => {
  return request({
    url: `/api/app/platform/dict/update/dict`,
    method: 'POST',
    data
  });
};

/**
 * @description: 删除字典 /api/app/platform/dict/delete/dict
 * @param {any} params 入参
 */
export const reqPlatformDictDeletePOST = (
  id: string,
) => {
  return request({
    url: `/api/app/platform/dict/delete/dict?id=${id}`,
    method: 'GET',
  });
};

/**
 * @description: 添加字典数据 /api/app/platform/dict/save/dict/detail
 * @param {any} params 入参
 */
export const reqPlatformSaveDictDetailPOST = (
  data: any,
) => {
  return request({
    url: `/api/app/platform/dict/save/dict/detail`,
    method: 'POST',
    data
  });
};

/**
 * @description: 编辑字典数据 /api/app/platform/dict/update/dict/detail
 * @param {any} params 入参
 */
export const reqPlatformDictUpdateDetailPOST = (
  data: any,
) => {
  return request({
    url: `/api/app/platform/dict/update/dict/detail`,
    method: 'POST',
    data
  });
};
/**
 * @description: 删除字典数据 /api/app/platform/dict/delete/dict/detail
 * @param {any} params 入参
 */
export const reqPlatformDictDetailDeletePOST = (
  id: string,
) => {
  return request({
    url: `/api/app/platform/dict/delete/dict/detail?id=${id}`,
    method: 'GET',
  });
};

/**
 * @description: 查血字典下拉 /api/app/platform/dict/list/dict/down
 * @param {any} params 入参
 */
export const reqPlatformDictListDownGet = () => {
  return request({
    url: `/api/app/platform/dict/list/dict/down`,
    method: 'GET',
  });
};

/**
 * @description: 根据code查询二级列表数据 /api/app/platform/dict/list/dict/code
 * @param {any} params 入参
 */
export const reqPlatformDictListDictDow = (
  params?: any
) => {
  return request({
    url: `/api/app/platform/dict/list/dict/code`,
    method: 'GET',
    params: params
  });
};