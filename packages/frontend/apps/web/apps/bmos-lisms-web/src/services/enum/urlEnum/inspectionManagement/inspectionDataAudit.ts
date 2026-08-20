import { BASE_URL } from '@/services/baseUrl';
import { OperationType } from '../../const';
import { log } from '../../type';
// 检验数据审核
export const InspectionDataAuditEnum: Record<string, log> = {
  '210030003': {
    [`${BASE_URL}/inspect/alldata/audit`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.audit,
          business: data.sampleBatchNo ? '批量审核' : '审核',
        };
      },
    },
  },
};
