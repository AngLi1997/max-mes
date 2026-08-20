import { OperationType } from '../../const';
import { log } from '../../type';
// 批记录管理
const BatchRecordsManagementEnum: Record<string, log> = {
  '120080002': {
    '/app/mes/plan/archive/generate': {
      type: OperationType.add,
      business: '生成批记录',
    },
    '/app/mes/plan/archive/scrap': {
      type: OperationType.edit,
      business: '作废',
    },
    '/app/mes/plan/archive/audit': {
      type: OperationType.audit,
      business: '提交审核',
    },
    '/app/mes/plan/archive/download': {
      type: OperationType.export,
      business: '下载批记录',
    },
    '/app/mes/plan/archive/effective': {
      type: OperationType.edit,
      business: '确认生效',
    },
    '/app/mes/plan/archive/reGenerate': {
      type: OperationType.add,
      business: '重新生成',
    },
  },
};

export { BatchRecordsManagementEnum };
