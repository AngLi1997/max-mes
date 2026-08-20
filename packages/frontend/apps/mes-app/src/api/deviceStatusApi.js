import request from '@/utils/request/request.js';
// 获取当前工位下的设备分页信息 /api/app/platform/equipment/app/page
export const getEquipmentAppPage = params =>
  request.get('/api/app/platform/equipment/app/page', params);

// 生产前确认中的设备分页信息
export const getEquipmentAppByLinePage = params =>
  request.get('/api/app/platform/equipment/app/byLinePage', params);

// 根据设备id获取设备状态信息 /api/app/platform/equipment/app/info/{id}
export const getEquipmentAppInfo = params =>
  request.get(`/api/app/platform/equipment/app/info/${params}`);

// 设备故障 /api/app/platform/equipment/app/fault
export const putEquipmentAppFault = data =>
  request.put('/api/app/platform/equipment/app/fault', data, {
    header: {
      'Bmos-MenuId': '121030001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '状态变更-故障操作',
    },
  });

// 设备属性状态变更 /api/app/platform/equipment/app/operate/property
export const putEquipmentAppOperateProperty = data =>
  request.put('/api/app/platform/equipment/app/operate/property', data, {
    header: {
      'Bmos-MenuId': '121030001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': `状态变更-${data.name}操作`,
    },
  });

// 设备释放操作 /api/app/platform/equipment/app/release
export const putEquipmentAppRelease = data =>
  request.put('/api/app/platform/equipment/app/release', data, {
    header: {
      'Bmos-MenuId': '121030001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '状态变更-释放操作',
    },
  });

// 设备恢复操作 /api/app/platform/equipment/app/recover
export const putEquipmentAppRecover = data =>
  request.put('/api/app/platform/equipment/app/recover', data, {
    header: {
      'Bmos-MenuId': '121030001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '状态变更-恢复操作',
    },
  });

// 设备占用操作 /api/app/platform/equipment/app/apply
export const putEquipmentAppApply = data =>
  request.put('/api/app/platform/equipment/app/apply', data, {
    header: {
      'Bmos-MenuId': '121030001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '状态变更-占用操作',
    },
  });

// 获取所有未生产或生产的生产批次的简单信息  /api/app/mes/plan/info/batch/list
export const getInfoBatchList = () =>
  request.get('/api/app/mes/plan/info/batch/list');

// 获取当前设备绑定的所有工位基础信息 /api/app/platform/equipment/app/all/station
export const getEquipmentAppAllStation = params =>
  request.get('/api/app/platform/equipment/app/all/station', params);

// 扫描设备编号查询设备信息  /api/app/mes/tag/scan/scanDeviceCode
export const postScanScanDeviceCode = data =>
  request.post('/api/app/mes/tag/scan/scanDeviceCode', data);

// 获取设备类型树
export const getEquipmentTagTree = params =>
  request.get('/api/app/platform/equipment/tag/tree', params);

// 获取设备的使用日志模板
export const getUseLogTemplate = id =>
  request.get(`/api/app/platform/equipment/${id}/useLogTemplate`);

// 保存操作日志
export const saveEquipmentLog = data =>
  request.post('/api/app/platform/equipment/log', data);

// 填报操作日志
export const fillEquipmentLog = data =>
  request.post('/api/app/platform/equipment/log/fill', data);

// 查询未填报日志
export const incompleteEquipmentLog = params =>
  request.get(`/api/app/platform/equipment/log/incomplete`, params);

