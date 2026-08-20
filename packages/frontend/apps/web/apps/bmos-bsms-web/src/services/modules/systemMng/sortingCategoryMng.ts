import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------分拣类别管理---------------

/**
 * @description: 分页查询 /sorting/page
 */
export const getSortingCategoryList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询血浆分拣类型树 /immunity/plasma/tree
 */
export const getSortingCategoryTree = () => {
  return request({
    url: `${BASE_URL}/immunity/plasma/tree`,
    method: 'GET',
  });
};

/**
 * @description: 查询标本分拣类型树 /immunity/sample/tree
 */
export const getSampleSortingCategoryTree = () => {
  return request({
    url: `${BASE_URL}/immunity/sample/tree`,
    method: 'GET',
  });
};

/**
 * @description: 分拣类型下拉列表 /immunity/list
 */
export const getSortingCategoryOptions = (params: any) => {
  return request({
    url: `${BASE_URL}/immunity/list`,
    method: 'GET',
    params
  });
};

/**
 * @description: 根据id查询 /sorting/{id}
 */
export const getSortingCategoryById = (id: string) => {
  return request({
    url: `${BASE_URL}/sorting/${id}`,
    method: 'GET',
  });
};

/**
 * @description: 新增 /sorting/create
 */
export const createSortingCategory = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting/create`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 编辑 /sorting/update
 */
export const updateSortingCategory = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting/update`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 删除 /sorting/{id}
 */
export const deleteSortingCategory = (id: string) => {
  return request({
    url: `${BASE_URL}/sorting/${id}`,
    method: 'DELETE',
  });
};

/**
 * @description: 启用/禁用 /sorting/enableOrDisable
 */
export const enableOrDisableSortingCategory = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting/enableOrDisable`,
    method: 'PUT',
    data,
  });
};


/**
 * @description: 下拉列表 /sorting/list
 */
export const getSortingCategoryListOptions = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting/list`,
    method: 'POST',
    data,
  });
}