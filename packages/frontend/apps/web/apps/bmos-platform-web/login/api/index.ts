import { t } from '@bmos/i18n';
import axios from 'axios';
import request from '../../src/utils/request';
// 查询表格
// export const userPage = async (params: any) => {
//   const res = await request({
//     url: '/api/app/platform/user/page',
//     method: 'get',
//     params,
//   });
//   return res.data;
// };
// 用户登录(登录按钮调用)
export const userLogin = (data: any) => {
  data = { ...data, type: 0 };
  return request({
    url: '/api/app/platform/user/login',
    method: 'post',
    data,
  });
};
// 修改密码(同用户管理重置密码)
export const rePassWord = (data: any, token = '') => {
  const config = {
    headers: {
      token,
      'bmos-access-token': token,
    },
  };
  return axios.post('/api/app/platform/user/resetPassword', data, config);
};
// 登录页未激活时修改密码
export const changePassWord = (data: any, token = '') => {
  const config = {
    headers: {
      token,
      'bmos-access-token': token,
      'Bmos-MenuId': '100',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': encodeURIComponent(t('修改密码') || ''),
    },
  };
  return axios.put('/api/app/platform/user/activeUser', data, config);
};
// 登录页密码过期时修改密码
export const expireUserChangePwd = (data: any, token = '') => {
  const config = {
    headers: {
      token,
      'bmos-access-token': token,
      'Bmos-MenuId': '100',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': encodeURIComponent(t('修改密码') || ''),
    },
  };
  return axios.put('/api/app/platform/user/expireUserChangePwd', data, config);
};

// 修改密码成功之后调编辑接口改用户状态
export const editUser = (data: any) => {
  return request({
    url: '/api/app/platform/user/update',
    method: 'put',
    data,
  });
};
// 登出
export const logout = () => {
  return request(
    {
      url: '/api/app/platform/user/logout',
      method: 'delete',
      headers: {
        terminalType: 0,
      },
    },
    // {
    //   headers: {
    //     'terminalType': 0,
    //   },
    // },
  );
};
// 查平台的激活
export const determinePlatformActived = (data: any) => {
  return request({
    url: '/api/app/platform/user/actived',
    method: 'post',
    data,
  });
};
// 查mes的激活
export const determineMesActived = (data: any) => {
  return request({
    url: '/api/app/mes/user/actived',
    method: 'post',
    data,
  });
};
// 查lims的激活
export const determineLimsActived = (data: any) => {
  return request({
    url: '/api/app/lims/active/actived',
    method: 'post',
    data,
  });
};
// 查wms的激活
export const determineWmsActived = (data: any) => {
  return request({
    url: '/api/app/wms/user/actived',
    method: 'post',
    data,
  });
};

// 查bsms的激活
export const determineBsmsActived = (data: any) => {
  return request({
    url: '/api/bmos-plasma/license/activated',
    method: 'post',
    data,
  });
};

// 查bims的激活
export const determineBimsActived = (data: any) => {
  return request({
    url: '/api/bmos-lims/license/activated',
    method: 'post',
    data,
  });
};

// 查集中化lims的激活
export const determineLismsActived = (data: any) => {
  return request({
    url: '/api/centralized-lims/license/activated',
    method: 'post',
    data,
  });
};

// 激活平台
export const platformActived = (data: any) => {
  return request({
    url: '/api/app/platform/user/active',
    method: 'post',
    data,
  });
};
// 激活mes
export const mesActived = (data: any) => {
  return request({
    url: '/api/app/mes/user/active',
    method: 'post',
    data,
  });
};
// 激活lims
export const limsActived = (data: any) => {
  return request({
    url: '/api/app/lims/active/active',
    method: 'post',
    data,
  });
};
// 激活wms
export const wmsActived = (data: any) => {
  return request({
    url: '/api/app/wms/user/active',
    method: 'post',
    data,
  });
};

// 激活bsms
export const bsmsActived = (data: any) => {
  return request({
    url: '/api/bmos-plasma/license/active',
    method: 'post',
    data,
  });
};

// 激活bims
export const bimsActived = (data: any) => {
  return request({
    url: '/api/bmos-lims/license/active',
    method: 'post',
    data,
  });
};

// 激活集中化lims
export const lismsActived = (data: any) => {
  return request({
    url: '/api/centralized-lims/license/active',
    method: 'post',
    data,
  });
};

// 查系统版本号
export const getSystemVersion = (params: any) => {
  return request({
    url: '/api/app/platform/param/app/version',
    method: 'get',
    params,
  });
};
// 获取i18n
export const getI18nConfig = () => {
  return request({
    url: '/api/app/platform/i18n/config',
    method: 'GET',
    headers: {
      'request-resource': 'frontend-web',
    },
  });
};
