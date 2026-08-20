import request from '@/utils/request/request.js';
import { t } from '@/utils/useBmosI18n.js';

// 称量打码  /api/app/mes/free/weigh/weighAndPrint 物料称量
export const reqFreeWeightWeighAndPrint = data => request.post(
  '/api/app/mes/free/weigh/weighAndPrint',
  data,
  {
    header: {
      'Bmos-MenuId': '121020005',
      'Bmos-Operation': 0,
      'Bmos-Operation-Business': t('物料称量'),
    },
  },
);

// 获取所有秤具列表  /api/app/mes/free/weigh/getBalanceList
export const reqFreeWeighGetBalanceList = () => request.get(
  '/api/app/mes/free/weigh/getBalanceList',
);

// 查询称量历史分页  /api/app/mes/free/weigh/queryHistory
export const reqFreeWeighQueryHistory = params => request.get(
  '/api/app/mes/free/weigh/queryHistory',
  params,
);

// 获取单位信息  /api/app/mes/unit/getUnitById/{unit_id}
export const reqUnitGetUnitById = unit_id => request.get(
  `/api/app/mes/unit/getUnitById/${unit_id}`,
);
