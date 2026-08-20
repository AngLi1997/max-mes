import request from '../request';
//签名追溯相关接口

// 查询表格
export const GetSignatureList = async (params: any) => {
  return await request({
    url: '/api/app/platform/signature/page',
    method: 'get',
    params,
  });
};

// 签名追溯导出
export const SignatureExport = async (params: any) => {
  return await request({
    url: '/api/app/platform/signature/export',
    method: 'get',
    params,
    responseType: 'arraybuffer',
    headers: {
      'Bmos-MenuId': '111020003',
      'Bmos-Operation': 3,
      'Bmos-Operation-Business': encodeURIComponent(t('导出') || ''),
    },
  });
};
