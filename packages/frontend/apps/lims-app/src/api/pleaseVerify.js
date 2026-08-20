// 请验相关
import request from '@/utils/request/request.js';

// 分页查询请验单
export const getInspectPageApi = params => request.get(
  '/api/app/mes/inspect/page',
  params,
);

// 发起请验
export const startInspectApi = data => request.post(
  '/api/app/mes/inspect/initiate',
  data,
  {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 0,
      'Bmos-Operation-Business': '发起请验',
    },
  },
);

// 获取请验单详情
export const getInspectDetailApi = params => request.get(
  `/api/app/mes/inspect/info`,
  params,
);

// 获取请验结果
export const getInspectResultApi = params => request.get(
  `/api/app/mes/inspect/program/result`,
  params,
);

// 重新发起请验
export const reStartInspectApi = data => request.post(
  '/api/app/mes/inspect/retry/initiate',
  data,
  {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '重新发起请验',
    },
  },
);

// 根据配方物料id查询请验单配置数据以及请验单信息
export const getInspectConfigApi = formulaMaterialId => request.get(
  `/api/app/mes/inspect/config/query/${formulaMaterialId}`,
);

// 获取生产信息
export const getProductionInfoApi = id => request.get(
  `/api/app/mes/plan/info/detail/${id}`,
);

// 获取工序绑定物料列表
export const getProcessBindMaterialListApi = params => request.get(
  '/api/app/mes/product/formula/material/listByProcedureId',
  params,
);
