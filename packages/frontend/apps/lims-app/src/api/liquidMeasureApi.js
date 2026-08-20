import request from '@/utils/request/request.js';

// 获取配液量取组件实例 /api/app/mes/liquid/preparation/measure/instance
export const getLiquidMeasureInstanceApi = params =>
  request.get('/api/app/mes/liquid/preparation/measure/instance', params);

// 获取未量取的配液单列表 /api/app/mes/liquid/preparation/measure/plan/list
export const getLiquidMeasurePlanListApi = params =>
  request.get('/api/app/mes/liquid/preparation/measure/plan/list', params);

// 配液单详情 /api/app/mes/liquid/preparation/measure/plan/detail
export const getLiquidMeasurePlanDetailApi = params =>
  request.get('/api/app/mes/liquid/preparation/measure/plan/detail', params);

// 量取结果 /api/app/mes/liquid/preparation/measure/result
export const getLiquidMeasureResultApi = params =>
  request.get('/api/app/mes/liquid/preparation/measure/result', params);

// 扫描配液量取物料 /api/app/mes/tag/scan/scanStorageMaterial
export const scanStorageMaterialApi = params =>
  request.get('/api/app/mes/tag/scan/scanStorageMaterial', params);

// 确认量取 /api/app/mes/liquid/preparation/measure/confirmMeasure
export const confirmMeasureApi = params =>
  request.post(
    '/api/app/mes/liquid/preparation/measure/confirmMeasure',
    params,
    {
      header: {
        'Bmos-MenuId': '121020001',
        'Bmos-Operation': 1,
        'Bmos-Operation-Business': '配液量取-消耗',
      },
    },
  );

// 添加量取物料件 /api/app/mes/liquid/preparation/measure/addConsumeStorageMaterial
export const addMeasureConsumeStorageMaterialApi = params =>
  request.post(
    '/api/app/mes/liquid/preparation/measure/addConsumeStorageMaterial',
    params,
    {
      header: {
        'Bmos-MenuId': '121020001',
        'Bmos-Operation': 1,
        'Bmos-Operation-Business': '配液量取-消耗',
      },
    },
  );

// 查询配液量取批次详细信息 /api/app/mes/liquid/preparation/measure/queryMeasureBatchDetail
export const queryMeasureBatchDetailApi = params =>
  request.get(
    '/api/app/mes/liquid/preparation/measure/queryMeasureBatchDetail',
    params,
  );

// 完成量取 /api/app/mes/liquid/preparation/measure/complete
export const completeMeasureApi = params =>
  request.post('/api/app/mes/liquid/preparation/measure/complete', params);

// 量取打码 /api/app/mes/liquid/preparation/measure/measureAndPrint
export const measureAndPrintApi = params =>
  request.post(
    '/api/app/mes/liquid/preparation/measure/measureAndPrint',
    params,
    {
      header: {
        'Bmos-MenuId': '121020001',
        'Bmos-Operation': 0,
        'Bmos-Operation-Business': params.measureStage === 'MEASURING' ? '余液量取' : '配液量取',
      },
    },
  );

// 更换量取人员 /api/app/mes/liquid/preparation/measure/changeMeasurer
export const changeMeasurerApi = params =>
  request.put('/api/app/mes/liquid/preparation/measure/changeMeasurer', params);

// 配液量取签名 /api/app/mes/liquid/preparation/measure/sign
export const measureSignApi = params =>
  request.post('/api/app/mes/liquid/preparation/measure/sign', params);
