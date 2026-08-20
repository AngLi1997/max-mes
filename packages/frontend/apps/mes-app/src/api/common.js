import request from '@/utils/request/request.js';

// 根据Code 获取权限  /api/app/platform/menu/auth/tree
export const reqPlatformMenuAdminTreeApi = params => request.get(
  '/api/app/platform/menu/auth/all',
  params,
);

// 校验工位权限 /api/app/mes/execute/business/checkStation
export const reqCheckStationApi = params => request.get(
  '/api/app/mes/execute/business/checkStation',
  params,
);

// 物料件标签打印 /api/app/mes/storage/material/printStorageMaterialTag
export const reqPrintStorageMaterialTagApi = params => request.post(
  '/api/app/mes/storage/material/printStorageMaterialTag',
  params,
);

// 电子天平发送心跳 /api/app/mes/equipment/heartbeat/sendHeartBeat
export const reqSendHeartBeatApi = params => request.post(
  '/api/app/mes/equipment/heartbeat/sendHeartBeat',
  params,
);

// 根据权限码和计划id 获取人员列表
export const reqUserListByAuthCodeAndPlanIdApi = params => request.get(
  '/api/app/mes/dSignature/getUserListByPermissionCodeAndPlanId',
  params,
);

// 释放称具
export const reqEquipmentAppRelease = params => request.put(
  '/api/app/platform/equipment/app/release',
  params,
);

// 扫描物料件/容器（公共校验）/api/app/mes/tag/scan/scanStorageMaterialWithCommonValidate
export const reqScanStorageMaterialWithCommonValidateApi = params => request.get(
  '/api/app/mes/tag/scan/scanStorageMaterialWithCommonValidate',
  params,
);
