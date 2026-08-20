// 配液相关
import request from '@/utils/request/request.js';

// 获取配液单实例
export const getPreparationApi = params => request.get(
  '/api/app/mes/liquid/preparation/plan/instance',
  params,
);

// 获取已添加批次列表
export const getBoundMaterialBatchApi = params => request.get(
  '/api/app/mes/liquid/preparation/plan/boundMaterialBatch',
  params,
);

// 获取可添加与已添加的批次列表
export const getAvailableBoundMaterialBatchApi = params => request.get(
  '/api/app/mes/liquid/preparation/plan/availableBoundMaterialBatch',
  params,
);

// 配液计划添加批次
export const boundMaterialBatchApi = data => request.post('/api/app/mes/liquid/preparation/plan/boundMaterialBatch', data);

// 配液量计算
export const calculateApi = data => request.post('/api/app/mes/liquid/preparation/plan/calculate', data);

// 完成配料计划
export const completeApi = data => request.post('/api/app/mes/liquid/preparation/plan/complete', data, {
  header: {
    'Bmos-MenuId': '121010001',
    'Bmos-Operation': 0,
    'Bmos-Operation-Business': '完成配液计划',
  },
});

// 根据物料批次id查询详情
export const getBatchDetailApi = params => request.get(
  '/api/app/mes/storage/material/batch/batchDetail',
  params,
);
