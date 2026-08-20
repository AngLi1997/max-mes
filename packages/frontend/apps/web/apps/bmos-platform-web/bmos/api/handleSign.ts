import request from '../../src/utils/request';

/* 
  @description: 获取当前登录人的签名地址
*/
export const reqUserSignInfo = () => {
  return request({
    url: '/api/app/platform/user/sign/info',
    method: 'get',
  });
};

/* 
  @description: 签名保存接口
*/
export const reqUserSignSave = (data: any) => {
  return request({
    url: '/api/app/platform/user/sign/save',
    method: 'post',
    data,
  });
};