import request from '@/utils/request/request.js';

// 【物料管理】根据物料批次id查询当前批次的自定义字段信息
export const reqStorageMaterialBatchFieldList = materialBatchId => request.get(
  `/api/app/mes/storage/material/batch/field/list/${materialBatchId}`,
);
// 【生产物料】根据生产物料的自定义字段信息
export const reqMaterialFieldInfo = (materialId) => {
  return request.get(
    `/api/app/mes/material/field/info/${materialId}`,
  );
};

// 获取生产物料详情
export const reqProductMaterialDetail = materialId => request.get(
  '/api/app/mes/product/material/detail',
  {
    id: materialId,
  },
);

// 获取生产批次list
export const reqStorageMaterialBatchListByMaterialId = params => request.get(
  '/api/app/mes/storage/material/batch/listByMaterialId',
  params,
);

// 物料接收
export const reqStorageMaterialReceiveMobile = data => request.post(
  '/api/app/mes/storage/material/receiveMobile',
  data,
  {
    header: {
      'Bmos-MenuId': '121020004',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '物料接收',
    },
  },
);

// 获取生产批次详情
export const reqStorageMaterialMangeQueryBatchDetail = params => request.get(
  '/api/app/mes/storage/material/manage/queryBatchDetail',
  params,
);

// 打印物料件标签
export const reqStorageMaterialPrintStorageMaterialTagBatch = data => request.post(
  '/api/app/mes/storage/material/printStorageMaterialTagBatch',
  data,
);
