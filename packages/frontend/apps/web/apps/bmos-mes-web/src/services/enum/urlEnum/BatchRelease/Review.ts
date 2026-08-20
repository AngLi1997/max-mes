import { OperationType } from '../../const';
import { log } from '../../type';
// 批签发审核
const BatchReleaseReviewEnum: Record<string, log> = {
  '120040005': {
    '/app/mes/audit/complete': {
      type: OperationType.edit,
      business: '审核通过',
    },
    '/app/mes/audit/complete/not/approve': {
      type: OperationType.edit,
      business: '审核不通过',
    },
    '/app/mes/lotRelease/manage/downloadByUrl': {
      type: OperationType.export,
      business: '下载',
    },
    '/app/mes/audit/back/to/prev': {
      type: OperationType.edit,
      business: '回退',
    },
  },
};

export { BatchReleaseReviewEnum };
