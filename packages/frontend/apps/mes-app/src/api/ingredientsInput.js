import request from '@/utils/request/request.js';

// 根据配料计划id查询待投料列表 /api/app/mes/ingredient/input/queryInputListByPlanId
export const reqMesQueryInputListByPlanIdApi = params =>
  request.get('/api/app/mes/ingredient/input/queryInputListByPlanId', params);

// 投料 /api/app/mes/ingredient/input/input
export const reqMesIngredientInputInputApi = data =>
  request.post('/api/app/mes/ingredient/input/input', data, {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '配料投入',
    },
  });

// 获取未投料的配料单列表 /api/app/mes/ingredient/input/queryPendingInputPlanList
export const reqMesQueryPendingInputPlanListApi = params =>
  request.get('/api/app/mes/ingredient/input/queryPendingInputPlanList', params);

// 扫描物料件号查询物料件信息（校验配料单信息）/api/app/mes/tag/scan/scanWeighMaterialCodeWithIngredientPlanId
export const reqMesScanWeighMaterialCodeWithIngredientPlanIdApi = data =>
  request.post('/api/app/mes/tag/scan/scanWeighMaterialCodeWithIngredientPlanId', data);

// 扫描设备编号查询设备信息 /api/app/mes/tag/scan/scanDeviceCode
export const reqMesScanDeviceCodeApi = data =>
  request.post('/api/app/mes/tag/scan/scanDeviceCode', data);

// 获取组件唯一实例id /api/app/mes/ingredient/input/instance
export const reqMesIngredientInputInstanceApi = params =>
  request.get('/api/app/mes/ingredient/input/instance', params);
