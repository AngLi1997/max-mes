import request from '../request';
// 获取左侧菜单树
export const getMenuTreeList = async (params: any) => {
  return await request({
    // url: '/api/app/platform/menu/admin/tree',
    url: '/api/app/platform/menu/admin/tree/operation',
    method: 'get',
    params,
  });
};
// 获取右边操作日志列表(平台操作日志)
export const getPlatformOperationLogpage = async (params: any) => {
  return await request({
    url: '/api/app/platform/log/operation/page',
    method: 'get',
    params,
  });
};
// 获取右边操作日志列表(mes操作日志)
export const getMesOperationLogpage = async (params: any) => {
  return await request({
    url: '/api/app/mes/log/page',
    method: 'get',
    params,
  });
};
// 获取右边操作日志列表(lims操作日志)
export const getLimsOperationLogpage = async (params: any) => {
  return await request({
    url: '/api/app/lims/log/page',
    method: 'get',
    params,
  });
};
// 获取右边操作日志列表(wms操作日志)
export const getWmsOperationLogpage = async (params: any) => {
  return await request({
    url: '/api/app/wms/log/page',
    method: 'get',
    params,
  });
};

// 获取右边操作日志列表(bsms操作日志)
export const getBsmsOperationLogpage = async (data: any) => {
  return await request({
    url: '/api/bmos-plasma/log/page',
    method: 'post',
    data,
  });
};

// 获取右边操作日志列表(bims操作日志)
export const getBimsOperationLogpage = async (data: any) => {
  return await request({
    url: '/api/bmos-lims/log/page',
    method: 'post',
    data,
  });
};

// 获取右边操作日志列表(lisms操作日志)
export const getLismsOperationLogpage = async (data: any) => {
  return await request({
    url: '/api/centralized-lims/log/page',
    method: 'post',
    data,
  });
};

// 在调用操作日志导出接口前先调一下这个
export const reqLogExportSave = async (data: any) => {
  return await request({
    url: '/api/app/platform/log/export/save',
    method: 'post',
    data,
    responseType: 'arraybuffer',
    headers: {
      'Bmos-MenuId': '111010002',
      'Bmos-Operation': 3,
      'Bmos-Operation-Business': encodeURIComponent(t('导出') || ''),
    },
  });
};

// 平台操作日志导出
export const OperationLogPlatformExport = async (params: any) => {
  return await request({
    url: '/api/app/platform/log/operation/export',
    method: 'get',
    params,
    responseType: 'arraybuffer',
    headers: {
      'Bmos-MenuId': '111010002',
      'Bmos-Operation': 3,
      'Bmos-Operation-Business': encodeURIComponent(t('导出') || ''),
    },
  });
};

// MES操作日志导出
export const OperationLogMesExport = async (params: any) => {
  return await request({
    url: '/api/app/mes/log/export',
    method: 'get',
    params,
    responseType: 'arraybuffer',
    headers: {
      'Bmos-MenuId': '111010002',
      'Bmos-Operation': 3,
      'Bmos-Operation-Business': encodeURIComponent(t('导出') || ''),
    },
  });
};

// LIMS操作日志导出
export const OperationLogLimsExport = async (params: any) => {
  return await request({
    url: '/api/app/lims/log/export',
    method: 'get',
    params,
    responseType: 'arraybuffer',
    headers: {
      'Bmos-MenuId': '111010002',
      'Bmos-Operation': 3,
      'Bmos-Operation-Business': encodeURIComponent(t('导出') || ''),
    },
  });
};

// WMS操作日志导出
export const OperationLogWmsExport = async (params: any) => {
  return await request({
    url: '/api/app/wms/log/export',
    method: 'get',
    params,
    responseType: 'arraybuffer',
    headers: {
      'Bmos-MenuId': '111010002',
      'Bmos-Operation': 3,
      'Bmos-Operation-Business': encodeURIComponent(t('导出') || ''),
    },
  });
};

// BSMS操作日志导出
export const OperationLogBsmsExport = async (params: any) => {
  return await request({
    url: '/api/bmos-plasma/log/operation/export',
    method: 'get',
    params,
    responseType: 'arraybuffer',
    headers: {
      'Bmos-MenuId': '111010002',
      'Bmos-Operation': 3,
      'Bmos-Operation-Business': encodeURIComponent(t('导出') || ''),
    },
  });
};

// BIMS操作日志导出
export const OperationLogBimsExport = async (params: any) => {
  return await request({
    url: '/api/bmos-lims/log/operation/export',
    method: 'get',
    params,
    responseType: 'arraybuffer',
    headers: {
      'Bmos-MenuId': '111010002',
      'Bmos-Operation': 3,
      'Bmos-Operation-Business': encodeURIComponent(t('导出') || ''),
    },
  });
};

// LISMS操作日志导出
export const OperationLogLismsExport = async (params: any) => {
  return await request({
    url: '/api/centralized-lims/log/operation/export',
    method: 'get',
    params,
    responseType: 'arraybuffer',
  });
};

// 平台操作日志详情
export const reqPlatformLogOperationDetailInfo = async (params: any) => {
  return await request({
    url: '/api/app/platform/log/operation/detail/info',
    method: 'get',
    params,
  });
};

// lims操作日志详情
export const reqLimsLogDetail = async (params: any) => {
  return await request({
    url: '/api/app/lims/log/detail',
    method: 'get',
    params,
  });
};
// wms操作日志详情
export const reqWmsLogDetail = async (params: any) => {
  return await request({
    url: '/api/app/wms/log/detail',
    method: 'get',
    params,
  });
};
// lisms操作日志详情
export const reqLismsLogDetail = async (params: any) => {
  return await request({
    url: '/api/centralized-lims/log/detail',
    method: 'get',
    params,
  });
};
