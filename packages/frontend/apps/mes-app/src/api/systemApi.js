import request from '@/utils/request/request.js';

export const postLogin = data =>
  request.post('/api/app/platform/user/login', data);
export const postLogout = () =>
  request.request({
    method: 'DELETE',
    url: '/api/app/platform/user/logout',
    options: { header: { terminalType: 1 } },
  });
export const postChangePwd = data =>
  request.request({
    method: 'PUT',
    url: '/api/app/platform/mobile/user/changePwd',
    data,
    options: { header: {
      'Bmos-MenuId': '100030001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '修改密码',
    } },
  });
export const getMenusApi = params =>
  request.get('/api/app/platform/menu/auth/tree', params);

// 平台接口校验签名V2
export const postVerifyPlatformSignatureV2Api = data =>
  request.post('/api/app/platform/signature/validate/v2', data);

// 获取app版本号
export const getAppVersionApi = () =>
  request.get('/api/app/platform/param/app/version');

// 根据功能权限id查询用户列表 /api/app/platform/user/listByMenuId
export const listByMenuIdApi = params =>
  request.get('/api/app/platform/user/listByMenuId', params);

// 根据启用状态查询用户列表
export const getUserListByStateApi = state =>
  request.get('/api/app/platform/user/list', { state });

// 获取打印机列表 /api/app/platform/equipment/app/list/equipment/info
export const listEquipmentInfoApi = params =>
  request.get('/api/app/platform/equipment/app/list/equipment/info', params);

// 获取所有的参数配置 /api/app/platform/business/parameter/all
export const getAllParameterApi = () =>
  request.get('/api/app/platform/business/parameter/all');

// 修改签名密码 /api/app/platform/signature/updateSignaturePassword
export const updateSignaturePasswordApi = data =>
  request.put('/api/app/platform/signature/updateSignaturePassword', data);

// 获取HUB鉴权信息 /api/app/platform/equipment/mqttAccredit
export const getMqttAccreditApi = () =>
  request.get('/api/app/platform/equipment/mqttAccredit');

// 获取多语言配置 /api/app/platform/i18n/config
export const getI18nConfigApi = lang =>
  request.get('/api/app/platform/i18n/config', '', { header: {
    'request-resource': 'frontend-app',
    'language': lang,
  } });
