import request from '@/utils/request/request.js';

// 查询称量中心列表数据 /api/app/mes/weigh/centre/execute/queryExecuteTaskPage
export const queryWeighCenterExecuteTaskPage = params =>
  request.get('/api/app/mes/weigh/centre/execute/queryExecuteTaskPage', params);

// 根据称量任务id查任务详情 /api/app/mes/weigh/centre/execute/queryTaskById
export const queryWeighCenterExecuteTaskById = params =>
  request.get('/api/app/mes/weigh/centre/execute/queryTaskById', params);

// 根据任务id查询任务下未称量的需求列表 /api/app/mes/weigh/centre/execute/queryPendingRequirementListByTaskId
export const queryWeighCenterExecutePendingRequirementListByTaskId = params =>
  request.get(
    '/api/app/mes/weigh/centre/execute/queryPendingRequirementListByTaskId',
    params,
  );

// 扫描物料件/设备号查询物料件信息 /api/app/mes/tag/scan/scanMaterialOrDeviceCode
export const weighCenterExecuteScanMaterialOrDeviceCode = params =>
  request.post('/api/app/mes/tag/scan/scanMaterialOrDeviceCode', params);

// 确认称量 /api/app/mes/weigh/centre/execute/makeSureWeigh
export const weighCenterExecuteMakeSureWeigh = params =>
  request.put('/api/app/mes/weigh/centre/execute/makeSureWeigh', params, {
    header: {
      'Bmos-MenuId': '121020001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '物料称量-消耗',
    },
  });

// 添加称量消耗物料件 /api/app/mes/weigh/centre/execute/addConsumeStorageMaterial
export const weighCenterExecuteAddConsumeStorageMaterial = params =>
  request.post(
    '/api/app/mes/weigh/centre/execute/addConsumeStorageMaterial',
    params,
    {
      header: {
        'Bmos-MenuId': '121020001',
        'Bmos-Operation': 1,
        'Bmos-Operation-Business': params.isResidual ? '余料称量-消耗' : '物料称量-消耗',
      },
    },
  );

// 根据工位ids获取秤具列表 /api/app/mes/ingredient/weigh/getBalanceListByStationId
export const weighCenterExecuteGetBalanceListByStationIdApi = params =>
  request.post(
    '/api/app/mes/ingredient/weigh/getBalanceListByStationId',
    params,
  );

// 根据设备id获取设备配置信息 /api/app/mes/equipment/getConfigByEquipmentId
export const weighCenterExecuteGetConfigByEquipmentIdApi = params =>
  request.get('/api/app/mes/equipment/getConfigByEquipmentId', params);

// 根据需求id查询需求详情 /api/app/mes/weigh/centre/execute/queryRequirementById
export const queryWeighCenterExecuteRequirementById = params =>
  request.get('/api/app/mes/weigh/centre/execute/queryRequirementById', params);

// 完成物料称量 /api/app/mes/weigh/centre/execute/finish
export const weighCenterExecuteFinish = params =>
  request.post('/api/app/mes/weigh/centre/execute/finish', params);

// 更换物料批次 /api/app/mes/weigh/centre/execute/changeBatch
export const weighCenterExecuteChangeBatch = params =>
  request.put('/api/app/mes/weigh/centre/execute/changeBatch', params);

// 扫描容器 /api/app/mes/tag/scan/scanWeighContainerCode
export const weighCenterExecuteScanWeighContainerCodeApi = params =>
  request.get('/api/app/mes/tag/scan/scanWeighContainerCode', params);

// 扫描货位 /api/app/mes/tag/scan/scanWeighPositionCode
export const weighCenterExecuteScanWeighPositionCodeApi = params =>
  request.get('/api/app/mes/tag/scan/scanWeighPositionCode', params);

// 称量打码 /api/app/mes/weigh/centre/execute/weighAndPrint
export const weighCenterExecuteWeighingApi = (params) => {
  let Business = '余料称量';
  if (params.weighProcess === 1) {
    Business = '物料称量';
  }
  delete params.weighProcess;
  return request.post(
    '/api/app/mes/weigh/centre/execute/weighAndPrint',
    params,
    {
      header: {
        'Bmos-MenuId': '121010001',
        'Bmos-Operation': 0,
        'Bmos-Operation-Business': Business,
      },
    },
  );
};

// 根据任务id查询称量结果 /api/app/mes/weigh/centre/execute/queryRecordResultByTaskId
export const weighCenterExecuteQueryRecordResultByTaskIdApi = params =>
  request.get(
    '/api/app/mes/weigh/centre/execute/queryRecordResultByTaskId',
    params,
  );

// 称量结果签名 /api/app/mes/weigh/centre/execute/sign
export const weighCenterExecuteSign = params =>
  request.post('/api/app/mes/weigh/centre/execute/sign', params);

// 更换称量人员 /api/app/mes/weigh/centre/execute/changeWeigher
export const weighCenterExecuteChangeWeigher = params =>
  request.put('/api/app/mes/weigh/centre/execute/changeWeigher', params);

// 根据权限码和工位列表获取校验签名列表 /api/app/mes/dSignature/getSingerListWithPermissionCodeAndStationIds
export const weighCenterExecuteGetSingerListWithPermissionCodeAndStationIds = params =>
  request.post(
    '/api/app/mes/dSignature/getSingerListWithPermissionCodeAndStationIds',
    params,
  );

// 扫描物料件/容器 /api/app/mes/tag/scan/weighCenterAddMaterial
export const reqScanMaterialApi = params => request.get(
  '/api/app/mes/tag/scan/weighCenterAddMaterial',
  params,
);
