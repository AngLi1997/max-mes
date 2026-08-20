import request from '@/utils/request/request.js';

// 获取当前选择的产出组件选择的配液单信息以及产出批次信息 /api/app/mes/mobile/preparation/produce/progress
export const getLiquidOutputProgressApi = params =>
  request.get('/api/app/mes/mobile/preparation/produce/progress', params);

// 获取当前生产批次下的配液单 /api/app/mes/mobile/preparation/produce/plan/list
export const getLiquidOutputPlanListApi = params =>
  request.get('/api/app/mes/mobile/preparation/produce/plan/list', params);

// 通过配液单查询当前配液单的配液计划组件中的产出中间品 /api/app/mes/mobile/preparation/produce/queryMaterial
export const getLiquidOutputMaterialApi = params =>
  request.get('/api/app/mes/mobile/preparation/produce/queryMaterial', params);

// 根据所输入的物料批次编号以及配方物料id查询物料批次信息  /api/app/mes/mobile/preparation/produce/queryMaterialBatch
export const queryLiquidOutputMaterialBatchApi = params =>
  request.get(
    '/api/app/mes/mobile/preparation/produce/queryMaterialBatch',
    params,
  );

// 配液产出确认 /api/app/mes/mobile/preparation/produce/confirm
export const confirmLiquidOutputApi = params =>
  request.put('/api/app/mes/mobile/preparation/produce/confirm', params);

// 配液产出结果 /api/app/mes/mobile/preparation/produce/queryProduce
export const queryLiquidOutputProduceApi = params =>
  request.get('/api/app/mes/mobile/preparation/produce/queryProduce', params);

// 配液产出签名 /api/app/mes/mobile/preparation/produce/sign
export const signLiquidOutputApi = params =>
  request.put('/api/app/mes/mobile/preparation/produce/sign', params);

// 配液产出更换操作人 /api/app/mes/mobile/preparation/produce/changeProducer
export const changeLiquidOutputProducerApi = params =>
  request.put('/api/app/mes/mobile/preparation/produce/changeProducer', params);

// 配液产出作废 /api/app/mes/mobile/preparation/produce/scrap
export const invalidLiquidOutputApi = params =>
  request.put('/api/app/mes/mobile/preparation/produce/scrap', params);

// 扫描容器码 /api/app/mes/tag/scan/scanPreparationProduceContainer
export const scanLiquidOutputPreparationProduceContainerApi = params =>
  request.get('/api/app/mes/tag/scan/scanPreparationProduceContainer', params);

// 扫描货位码 /api/app/mes/tag/scan/preparationCargoCode
export const scanLiquidOutputPreparationCargoCodeApi = params =>
  request.get('/api/app/mes/tag/scan/preparationCargoCode', params);

// 获取货位树 /api/app/mes/storage/config/queryTreeWithCargoPosition
export const queryLiquidOutputTreeWithCargoPositionApi = params =>
  request.get('/api/app/mes/storage/config/queryTreeWithCargoPosition', params);

// 配液产出 /api/app/mes/mobile/preparation/produce/handle
export const handleLiquidOutputApi = params =>
  request.put('/api/app/mes/mobile/preparation/produce/handle', params, {
    header: {
      'Bmos-MenuId': '121020001',
      'Bmos-Operation': 0,
      'Bmos-Operation-Business': '配液产出',
    },
  });
// 获取当前生产批次下的产出人员列表 /api/app/mes/mobile/preparation/produce/queryProducerList
export const queryLiquidOutputProducerListApi = params =>
  request.get('/api/app/mes/mobile/preparation/produce/queryProducerList', params);
