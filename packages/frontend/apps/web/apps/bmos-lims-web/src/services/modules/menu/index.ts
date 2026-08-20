import request from '../../service';

export const getMenuList = async (params: any) => {
  const res = await request({
    url: '/app/platform/menu/auth/tree',
    method: 'get',
    params,
  });
  return res
};