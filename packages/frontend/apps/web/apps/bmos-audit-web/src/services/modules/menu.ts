import request from '../request';

export const getMenuList = async (params: any) => {
  return await request({
    url: '/api/app/platform/menu/auth/tree',
    method: 'get',
    params,
  });
};
