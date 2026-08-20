import { BASE_URL } from '@/services/baseUrl';
import { OperationType } from '../../const';
import { log } from '../../type';
// 检疫期管理
const QuarantineManagementEnum: Record<string, log> = {
  '170050002': {
    // 检疫期核查数据
    [`${BASE_URL}/quarantine/report/save`]: {
      type: OperationType.edit,
      business: '保存检疫期报告',
    },
    [`${BASE_URL}/quarantine/report/submit`]: {
      type: OperationType.add,
      business: '创建报告',
    },
  },
  '170050003': {
    // 检疫期报告送审
    [`${BASE_URL}/quarantine/report/submit-audit`]: {
      type: OperationType.edit,
      business: '检疫期报告送审',
    },
    [`${BASE_URL}/quarantine/report/cancel`]: {
      type: OperationType.edit,
      business: '检疫期报告撤销',
    },
  },
  '170050004': {
    // 检疫期报告审核
    [`${BASE_URL}/quarantine/report/audit`]: {
      type: OperationType.audit,
      business: '检疫期报告审核',
    },
  },
};

export default QuarantineManagementEnum;
