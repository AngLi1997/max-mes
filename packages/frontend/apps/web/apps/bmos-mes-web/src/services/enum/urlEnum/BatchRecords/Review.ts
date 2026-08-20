import { OperationType } from '../../const';
import { log } from '../../type';
// 批记录审核
const BatchRecordsReviewEnum: Record<string, log> = {
  '120080003': {
    '/app/mes/audit/complete': {
      type: OperationType.edit,
      business: '审核通过',
    },
    '/app/mes/audit/complete/not/approve': {
      type: OperationType.edit,
      business: '审核不通过',
    },
    '/app/mes/plan/archive/download': {
      type: OperationType.export,
      business: '下载批记录',
    },
    '/app/mes/audit/back/to/prev': {
      type: OperationType.edit,
      business: '回退',
    },
  },
};

export { BatchRecordsReviewEnum };
