import request from '../../utils/request';
// 查询表格
// export const userPage = async (params: any) => {
//   const res = await request({
//     url: '/api/app/platform/user/page',
//     method: 'get',
//     params,
//   });
//   return res
// };
// 用户登录(登录按钮调用)
export const userLogin = (data: any) => {
  return request({
    url: '/api/app/platform/user/login',
    method: 'post',
    data,
  });
};
// 修改密码(同用户管理重置密码)
export const rePassWord = (data: any) => {
  return request({
    url: '/api/app/platform/user/resetPassword',
    method: 'post',
    data,
  });
};
// 修改密码成功之后调编辑接口改用户状态
export const editUser = (data: any) => {
  return request({
    url: '/api/app/platform/user/update',
    method: 'put',
    data,
  });
};
