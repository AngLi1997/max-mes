import { OperationType } from '../../const';
import { log } from '../../type';
// 称量任务
const WeighingTaskEnum: Record<string, log> = {
  '120030010': {
    '/app/mes/weigh/centre/task/programManual': {
      type: OperationType.add,
      business: '任务规划',
    },
    '/app/mes/weigh/centre/task/edit': {
      type: OperationType.edit,
      business: '编辑任务',
    },
    '/app/mes/weigh/centre/task/makeSure': {
      type: OperationType.edit,
      business: '确认任务',
    },
    '/app/mes/weigh/centre/task/send': {
      type: OperationType.edit,
      business: '下发任务',
    },
    '/app/mes/weigh/centre/task/cancel': {
      type: OperationType.edit,
      business: '取消任务',
    },
    '/app/mes/weigh/centre/task/programAuto': {
      type: OperationType.add,
      business: '自动规划',
    },
  },
};

export { WeighingTaskEnum };
