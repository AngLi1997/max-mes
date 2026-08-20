import request from '../request';
// 获取登录日志分页
export const getLoginLogList = async (params: any) => {
  return await request({
    url: '/api/app/platform/log/login/page',
    method: 'get',
    params,
  });
};
// 登录日志导出
export const LoginLogExport = async (params: any) => {
  return await request({
    url: '/api/app/platform/log/login/export',
    method: 'get',
    params,
    responseType: 'arraybuffer',
    headers: {
      'Bmos-MenuId': '111010001',
      'Bmos-Operation': 3,
      'Bmos-Operation-Business': encodeURIComponent(t('导出') || ''),
    },
  });
};
