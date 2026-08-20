import request from '@/utils/request/request.js';
// 生产投料相关接口

// 获取该组件已投料的物料列表
export const getChargeRecycleMaterialChargeList = params =>
  request.get('/api/app/mes/chargeRecycle/material/chargeList', params);

// 获取组件信息id及已投料及已回收列表
export const getChargeRecycleMaterialList = params =>
  request.get('/api/app/mes/chargeRecycle/material/list', params);

// 投料
export const postChargeRecycleCharge = data =>
  request.post('/api/app/mes/chargeRecycle/charge', data, {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '生产投料',
    },
  });

// 回收
export const postChargeRecycleRecycle = data =>
  request.post('/api/app/mes/chargeRecycle/recycle', data, {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '物料回收',
    },
  });

// 扫描物料件或设备信息
export const postScanScanMaterialOrDevice = data =>
  request.post('/api/app/mes/tag/scan/scanMaterialOrDevice', data);

// 扫描设备编号查询物料件信息
export const postScanScanScanDeviceCode = data =>
  request.post('/api/app/mes/tag/scan/scanDeviceCode', data);

// 扫描投料回收组件设备信息
export const postScanScanScanChargeRecycleDeviceCode = data =>
  request.post('/api/app/mes/tag/scan/scanChargeRecycleDeviceCode', data);

// 扫描设备编号查询设备信息
export const postScaScanDeviceCodeAndValidateStationIds = data =>
  request.post('/api/app/mes/tag/scan/scanDeviceCodeAndValidateStationIds', data);

// 扫描投料回收容器
export const postScanScanScanChargeRecycleContainer = data =>
  request.post('/api/app/mes/tag/scan/scanChargeRecycleContainer', data);

// 打印回收物料件标签
export const postPrintRecoveryMaterialParts = data =>
  request.post('/api/app/mes/tag/print/STORAGE_MATERIAL', data); // 路径待换
