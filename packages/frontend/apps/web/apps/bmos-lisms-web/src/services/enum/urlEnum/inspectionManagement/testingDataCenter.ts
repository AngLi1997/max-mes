import { BASE_URL } from '@/services/baseUrl';
import { OperationType } from '../../const';
import { log } from '../../type';
// 检验数据中心
export const TestingDataCenterEnum: Record<string, log> = {
  '210030002': {
    [`${BASE_URL}/inspect/alldata/publish`]: {
      type: OperationType.edit,
      business: '发布',
    },
    [`${BASE_URL}/inspect/alldata/check`]: {
      type: OperationType.edit,
      business: '批量发布',
    },
    [`${BASE_URL}/report/file`]: {
      type: OperationType.export,
      business: '打印检测记录单',
    },
  },
};
