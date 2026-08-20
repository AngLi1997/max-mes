import request from '@/utils/request/request.js';

// 查询产出称量信息 /api/app/mes/output/weigh/getOutputWeighProcess
export const getOutputWeighProcessApi = params =>
  request.post('/api/app/mes/output/weigh/getOutputWeighProcess', params);

// 确认称量人员 /api/app/mes/output/weigh/makeSureWeigher
export const makeSureWeigherOutputApi = params =>
  request.put('/api/app/mes/output/weigh/makeSureWeigher', params);

// 获取产出批次中的中间品物料列表  /api/app/mes/output/weigh/getMiddleMaterialList
export const getMiddleMaterialListOutputApi = params =>
  request.get('/api/app/mes/output/weigh/getMiddleMaterialList', params);
// 根据物料id和批次编号查询批次信息 /api/app/mes/output/weigh/queryBatchInfo
export const queryBatchInfoOutputApi = params =>
  request.get('/api/app/mes/output/weigh/queryBatchInfo', params);

// 获取关联批次中的原辅包物料列表 /api/app/mes/output/weigh/getUnionOriginMaterialList
export const getUnionOriginMaterialListOutputApi = params =>
  request.get('/api/app/mes/output/weigh/getUnionOriginMaterialList', params);

// 更换称量人员 /api/app/mes/output/weigh/changeWeigher
export const changeWeigherOutputApi = params =>
  request.put('/api/app/mes/output/weigh/changeWeigher', params);

// 确认产出批次 /api/app/mes/output/weigh/makeSureBatch
export const makeSureBatchOutputApi = params =>
  request.put('/api/app/mes/output/weigh/makeSureBatch', params);

// 根据工位id获取秤具列表 /api/app/mes/output/weigh/getBalanceListByStationId
export const getOutputBalanceListByStationIdApi = params =>
  request.post('/api/app/mes/output/weigh/getBalanceListByStationId', params);

// 称量作废 /api/app/mes/output/weigh/scrap
export const scrapOutputApi = params =>
  request.put('/api/app/mes/output/weigh/scrap', params);

// 称量打码 /api/app/mes/output/weigh/weighAndPrint
export const weighAndPrintOutputApi = params =>
  request.post('/api/app/mes/output/weigh/weighAndPrint', params, {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 0,
      'Bmos-Operation-Business': params.size ? '中间品产出' : '产出称量',
    },
  });

// 签名 /api/app/mes/output/weigh/sign
export const signOutputApi = params => request.post('/api/app/mes/output/weigh/sign', params);

// 根据id获取皮重配置（带校验） /api/app/mes/tag/scan/scanTareWeighTag
export const queryTareWeighConfigByIdApi = params =>
  request.post('/api/app/mes/tag/scan/scanTareWeighTag', params);
