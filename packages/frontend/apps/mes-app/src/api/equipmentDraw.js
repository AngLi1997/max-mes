import request from '@/utils/request/request.js';

// 获取设备列表 /api/app/mes/equipment/picture/equipmentList
export const getEquipmentListApi = params => request.get(
  `/api/app/mes/equipment/picture/equipmentList`,
  params,
);

// 扫描code获取设备信息
export const getScanEquipmentCodeApi = params => request.get(
  `/api/app/mes/tag/scan/scanEquipmentCode`,
  params,
);

// /api/app/platform/dict/list/dict/code 根据code查询二级列表数据
export const dictListDictCode = params => request.get(
  `/api/app/platform/dict/list/dict/code`,
  params,
);

// 设备数采绘图:保存图片
export const savePictureEquipmentList = data => request.post(
  '/api/app/mes/equipment/picture/save',
  data,
);

// 设备数采绘图:区间计算
export const getPictureRange = params => request.get(
  '/api/app/mes/equipment/picture/range',
  params,
);
