import request from '@/utils/request/request.js';
// 物料预定(移动端)
export const saveBeforeProductionConfirmApi = data =>
  request.post('/api/app/mes/plan/instruction/team/start/confirm', data);
// 物料预定(移动端)
export const getStorageMaterialReserveApi = params =>
  request.put('/api/app/mes/storage/material/reserve', params, {
    header: {
      'Bmos-MenuId': '121020002',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '物料预定',
    },
  });
// 查询复核人
export const getListBoundUserApi = params =>
  request.get('/api/app/mes/material/position/listBoundUser', params);

// 根据 货位id 和 menuId 查人员列表
export const getQueryPositionBoundUserListByPermissionCodeApi = params =>
  request.get('/api/app/mes/material/position/queryPositionBoundUserListByPermissionCode', params);
// 拆包出库
export const getStorageMaterialSplitPackageApi = params =>
  request.put('/api/app/mes/storage/material/splitPackage', params, {
    header: {
      'Bmos-MenuId': '121020002',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '拆包出库',
    },
  });
// 物料盘点
export const getStorageMaterialCheckApi = params =>
  request.put('/api/app/mes/storage/material/check', params, {
    header: {
      'Bmos-MenuId': '121020002',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '物料盘点',
    },
  });

// 获取生产批次 /api/app/mes/plan/info/queryNotTerminatedBatchListByProductIdAndProcessId
export const getProductionBatchListApi = params =>
  request.get(
    '/api/app/mes/plan/info/queryNotTerminatedBatchListByProductIdAndProcessId',
    params,
  );

// 使用并消耗
export const useAndConsumeMobileApi = data =>
  request.post('/api/app/mes/storage/material/useAndConsumeMobile', data, {
    header: {
      'Bmos-MenuId': '121020002',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '物料使用',
    },
  });
// 销毁并消耗
export const destroyAndConsumeMobileApi = data =>
  request.post('/api/app/mes/storage/material/destroyAndConsumeMobile', data, {
    header: {
      'Bmos-MenuId': '121020002',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '物料销毁',
    },
  });
// 退库并消耗
export const sendBackAndConsumeMobileApi = data =>
  request.post('/api/app/mes/storage/material/sendBackAndConsumeMobile', data, {
    header: {
      'Bmos-MenuId': '121020002',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '物料退库',
    },
  });
