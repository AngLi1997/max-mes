import { BASE_URL } from '@/services/baseUrl';
import { OperationType } from '../../const';
import { log } from '../../type';
// 质保管理
const QualityAssuranceManagementEnum: Record<string, log> = {
  '170060001': {
    // 标本请验审核
    [`${BASE_URL}/quality-guarantee/examination/audit`]: {
      type: OperationType.audit,
      business: '标本请验审核',
    },
  },
  '170060002': {
    // 放行单管理
    [`${BASE_URL}/quality-guarantee/release-note`]: {
      type: OperationType.add,
      business: '创建放行单',
    },
  },
  '170060003': {
    // 放行单审核
    [`${BASE_URL}/quality-guarantee/note/audit`]: {
      type: OperationType.audit,
      business: '放行单审核',
    },
  },
  '170060006': {
    // 科研调用质保审核
    [`${BASE_URL}/outbound-process/process`]: {
      type: OperationType.audit,
      business: '科研调用质保审核',
    },
  },
  '170060007': {
    // 销毁出库初审
    [`${BASE_URL}/outbound-process/process`]: {
      type: OperationType.audit,
      business: '销毁出库初审',
    },
  },
  '170060008': {
    // 销毁出库复审
    [`${BASE_URL}/outbound-process/process`]: {
      type: OperationType.audit,
      business: '销毁出库复审',
    },
  },
};

export default QualityAssuranceManagementEnum;
