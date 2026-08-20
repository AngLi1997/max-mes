import { OperationType } from '../../const';
import { log } from '../../type';
// 生产BOM审核
const FormulaApprovalEnum: Record<string, log> = {
  '120020005': {
    '/app/mes/audit/complete': {
      type: OperationType.edit,
      business: '审核通过',
    },
    '/app/mes/audit/complete/not/approve': {
      type: OperationType.edit,
      business: '审核不通过',
    },
    '/app/mes/audit/back/to/prev': {
      type: OperationType.edit,
      business: '回退',
    },
  },
};

export { FormulaApprovalEnum };
