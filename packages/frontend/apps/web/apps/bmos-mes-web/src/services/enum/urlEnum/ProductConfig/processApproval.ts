import { OperationType } from '../../const';
import { log } from '../../type';
// 工艺审核
const ProcessApprovalEnum: Record<string, log> = {
  '120020007': {
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

export { ProcessApprovalEnum };
