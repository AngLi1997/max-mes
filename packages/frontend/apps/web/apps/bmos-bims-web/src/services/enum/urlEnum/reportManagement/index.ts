import { OperationType } from '../../const';
import { log } from '../../type';
import { BASE_URL } from '@/services/baseUrl';

// 报告管理
const ReportManagementEnum: Record<string, log> = {
  '180040001': { // 检验报告管理
    [`${BASE_URL}/report/create`]: {
      type: OperationType.add,
      business: '创建检验报告',
    },
  },
  '180040002': { // 检验报告审核
    [`${BASE_URL}/report/audit`]: {
      type: OperationType.audit,
      business: '审核',
    },
  },
};

export default ReportManagementEnum;
