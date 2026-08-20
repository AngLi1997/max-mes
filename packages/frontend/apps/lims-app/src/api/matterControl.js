import request from '@/utils/request/request.js';
// 暂存间数据树 /api/app/mes/storage/config/queryTreeWithCargoPosition

export const getStorageConfigTreeApi = params => request.get('/api/app/mes/storage/config/queryTreeWithCargoPosition', params);

// 暂存间物料批次 /api/app/mes/storage/material/batch/page
export const getStorageMaterialBatchPageApi = params => request.get('/api/app/mes/storage/material/batch/page', params);
// 暂存间物料件 /api/app/mes/storage/material/page
export const getStorageMaterialPageApi = params => request.get('/api/app/mes/storage/material/page', params);

// 物料详情 /api/app/mes/storage/material/infoByNo
export const getStorageMaterialInfoByNo = params => request.get('/api/app/mes/storage/material/infoByNo', params);

// 单位求和 /api/app/mes/unit/calcSumAdapt
export const postMesUnitCalcSumAdapt = data => request.post('/api/app/mes/unit/calcSumAdapt', data);

// 移库 /api/app/mes/storage/material/moveMobile
export const putStorageMaterialMoveMobile = data => request.put('/api/app/mes/storage/material/moveMobile', data, {
  header: {
    'Bmos-MenuId': '121020002',
    'Bmos-Operation': 1,
    'Bmos-Operation-Business': '物料移库',
  },
});

// 出库 /api/app/mes/storage/material/outboundMobile
export const putStorageMaterialOutboundMobile = data => request.put('/api/app/mes/storage/material/outboundMobile', data, {
  header: {
    'Bmos-MenuId': '121020002',
    'Bmos-Operation': 1,
    'Bmos-Operation-Business': '物料出库',
  },
});

// 退库 /api/app/mes/storage/material/sendBackMobile
export const postStorageMaterialSendBackMobile = data => request.post('/api/app/mes/storage/material/sendBackMobile', data, {
  header: {
    'Bmos-MenuId': '121020002',
    'Bmos-Operation': 1,
    'Bmos-Operation-Business': '物料入库',
  },
});
// 取消预定 /api/app/mes/storage/material/cancelReserve
export const putStorageMaterialCancelReserve = data => request.put('/api/app/mes/storage/material/cancelReserve', data, {
  header: {
    'Bmos-MenuId': '121020002',
    'Bmos-Operation': 1,
    'Bmos-Operation-Business': '物料取消预定',
  },
});

// 打印 /api/app/mes/storage/material/printStorageMaterialTag
export const getStorageMaterialPrintTag = data => request.post('/api/app/mes/storage/material/printStorageMaterialTag', data);
