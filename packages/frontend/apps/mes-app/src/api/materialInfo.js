import request from '@/utils/request/request.js';

// 保存物料件组件值
export const reqStorageMaterialManageSaveMaterialComponentValue = data =>
  request.post('/api/app/mes/storage/material/manage/saveMaterialComponentValue', data, {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '物料件信息',
    },
  });
