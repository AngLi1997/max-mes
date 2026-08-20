import request from '@/utils/request';

/**
 * @description: 参数信息分页 /api/app/platform/business/parameter/page
 * @param {any} params 入参
 */
export const reqBusinessParameterGET = (params?: any) => {
  return request({
    url: '/api/app/platform/business/parameter/page',
    method: 'GET',
    params,
  });
};

/**
 * @description: 参数信息详情 /api/app/platform/business/parameter/detailByCode/{id}
 * @param {any} params 入参
 */
export const reqBusinessParameterDetailGET = (code: string) => {
  return request({
    url: `/api/app/platform/business/parameter/detailByCode/${code}`,
    method: 'GET',
  });
};

/**
 * @description: 参数信息刷新 /api/app/platform/business/parameter/refresh
 * @param {any} params 入参
 */
export const reqBusinessParameterRefreshPUT = () => {
  return request({
    url: `/api/app/platform/business/parameter/refresh`,
    method: 'PUT',
  });
};

/**
 * @description: 参数信息更新 /api/app/platform/business/parameter/update
 * @param {any} params 入参
 */
export const reqBusinessParameterUpdatePUT = (data: any) => {
  return request({
    url: `/api/app/platform/business/parameter/update`,
    method: 'PUT',
    data
  });
};
