import request from '@/utils/request/request.js';

// 查询配料称量信息 /api/app/mes/ingredient/weigh/getIngredientWeighProcess
export const getIngredientWeighProcessApi = params =>
  request.post(
    '/api/app/mes/ingredient/weigh/getIngredientWeighProcess',
    params,
  );

// 获取未完成的配料单列表 /api/app/mes/ingredient/weigh/queryPendingIngredientPlanList
export const queryPendingIngredientPlanListApi = params =>
  request.get(
    '/api/app/mes/ingredient/weigh/queryPendingIngredientPlanList',
    params,
  );

// 根据工位id获取秤具列表  /api/app/mes/ingredient/weigh/getBalanceListByStationId
export const getBalanceListByStationIdApi = params =>
  request.post(
    '/api/app/mes/ingredient/weigh/getBalanceListByStationId',
    params,
  );

// 查询配料单详情 /api/app/mes/ingredient/weigh/queryIngredientPlanById
export const queryIngredientPlanByIdApi = params =>
  request.get('/api/app/mes/ingredient/weigh/queryIngredientPlanById', params);

// 扫描物料件号查询物料件信息  /api/app/mes/tag/scan/scanWeighMaterialCode
export const scanWeighMaterialCodeApi = params =>
  request.post('/api/app/mes/tag/scan/scanWeighMaterialCode', params);

// 确认称量 /api/app/mes/ingredient/weigh/makeSureWeigh
export const makeSureWeighApi = params =>
  request.put('/api/app/mes/ingredient/weigh/makeSureWeigh', params, {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '配料称量-消耗',
    },
  });

// 根据批次id查询称量批次详细信息 /api/app/mes/ingredient/weigh/queryWeighDetailByPlanIdAndBatchId
export const queryWeighDetailByPlanIdAndBatchIdApi = params =>
  request.get(
    '/api/app/mes/ingredient/weigh/queryWeighDetailByPlanIdAndBatchId',
    params,
  );

// 称量打码 /api/app/mes/ingredient/weigh/weighAndPrint
export const weighAndPrintApi = (params) => {
  let Business = '配料称量';
  if (params.weighProcess === 2) {
    Business = '余料称量';
  }
  delete params.weighProcess;
  return request.post('/api/app/mes/ingredient/weigh/weighAndPrint', params, {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 0,
      'Bmos-Operation-Business': Business,
    },
  });
};

// 添加称量消耗物料件 /api/app/mes/ingredient/weigh/addConsumeStorageMaterial
export const addConsumeStorageMaterialApi = params =>
  request.post(
    '/api/app/mes/ingredient/weigh/addConsumeStorageMaterial',
    params,
  );

// 完成称量 /api/app/mes/ingredient/weigh/finish
export const weighFinishApi = params =>
  request.post('/api/app/mes/ingredient/weigh/finish', params);

// 签名 /api/app/mes/ingredient/weigh/sign
export const weighSignApi = params =>
  request.post('/api/app/mes/ingredient/weigh/sign', params);

// 根据配料计划id查询称量结果列表 /api/app/mes/ingredient/weigh/queryResult
export const weighQueryResultApi = params =>
  request.post('/api/app/mes/ingredient/weigh/queryResult', params);

// 更换称量人员 /api/app/mes/ingredient/weigh/changeWeigher
export const weighChangeWeigherApi = params =>
  request.put('/api/app/mes/ingredient/weigh/changeWeigher', params);

// 扫描容器 /api/app/mes/tag/scan/scanWeighContainerCode
export const scanWeighContainerCodeApi = params =>
  request.get('/api/app/mes/tag/scan/scanWeighContainerCode', params);

// 扫描货位 /api/app/mes/tag/scan/scanWeighPositionCode
export const scanWeighPositionCodeApi = params =>
  request.get('/api/app/mes/tag/scan/scanWeighPositionCode', params);

// 根据权限码和组件获取校验签名列表 /api/app/mes/dSignature/getSignerListWithPermissionCodeAndComponent
export const postSingerListWithPermissionCodeAndComponentApi = params =>
  request.post(
    '/api/app/mes/dSignature/getSignerListWithPermissionCodeAndComponent',
    params,
  );

// 根据设备id获取设备配置信息 /api/app/mes/equipment/getConfigByEquipmentId
export const getConfigByEquipmentIdApi = params =>
  request.get('/api/app/mes/equipment/getConfigByEquipmentId', params);
