import request from '../request';

/**
 * @description 根据Code 获取权限  /api/app/platform/menu/admin/tree
 * @param {number} rootMenuCode 菜单编码
 * @returns
 */
export const getPermissionMenuList = async (params: any) => {
  return await request({
    url: '/api/app/platform/menu/auth/all',
    method: 'get',
    params,
  });
};

/**
 * @description: 获取所有参数配置(可查锁屏时间) /api/app/platform/business/parameter/detailByCode
 * @param {string} code 参数编码
 */

export const getParameter = (code: string) => {
  return request({
    url: `/app/platform/business/parameter/detailByCode/${code}`,
    method: 'GET',
  });
};
