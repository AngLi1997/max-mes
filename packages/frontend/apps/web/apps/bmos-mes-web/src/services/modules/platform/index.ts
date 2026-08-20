import request from '../../service';

/**
 * @description 查询公式确认树列表  /app/mes/record/functionTree
 * @param params
 * @returns
 */
export const getExpressionFullList = (params?: any) => {
  return request({
    url: '/app/mes/record/functionTree',
    method: 'GET',
    params,
  });
};

/**
 * @description 查询参数配置  /api/app/platform/business/parameter/detailByCode/{code}
 * @param params
 * @returns
 */
export const getParameterDetailByCode = (code: string) => {
  return request({
    url: `/app/platform/business/parameter/detailByCode/${code}`,
    method: 'GET',
  });
};

// 查询修约方式
export const getRoundingList = (params: any) => {
  return request({
    url: '/app/platform/unit/list/rounding',
    method: 'GET',
    params,
  });
};

/**
 * @description 查询角色详情  /api/app/platform/role/detail/{id}
 * @param {string} id 角色id
 * @returns
 */
export const reqPlatformRoleDetail = (id: string) => {
  return request({
    url: `/app/platform/role/detail/${id}`,
    method: 'GET',
  });
};

/**
 * @description 根据code查询二级列表数据  /api/app/platform/dict/list/dict/code
 * @param {string} code 字典code
 * @returns
 */
export const reqPlatformDictListDictCode = (code: string) => {
  return request({
    url: `/app/platform/dict/list/dict/code`,
    method: 'GET',
    params: {
      code,
    },
  });
};

/**
 * @description 根据功能权限按钮id查询用户列表  /api/app/platform/user/listByMenuId
 * @param {string} menuId 权限码
 */
export const reqPlatformUserListByMenuId = async (menuId: string) => {
  return await request({
    url: '/app/platform/user/listByMenuId',
    method: 'get',
    params: {
      menuId,
    },
  });
};

/**
 * @description 查询用户列表  /api/app/platform/user/list
 */
export const getUserListApi = async (state: number) => {
  return await request({
    url: '/app/platform/user/list',
    method: 'get',
    params: {
      state,
    },
  });
};
