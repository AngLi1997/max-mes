import { OperationType } from '../../const';
import { log } from '../../type';
// 称量工单规划
const WeighingWorkOrderPlan: Record<string, log> = {
  '120030014': {
    '/app/mes/weigh/ticket/programManual': {
      type: OperationType.add,
      business: '工单手动规划',
    },
    '/app/mes/weigh/ticket/programAuto': {
      type: OperationType.add,
      business: '工单自动规划',
    },
    '/app/mes/weigh/ticket/edit': {
      type: OperationType.edit,
      business: '编辑称量工单',
    },
    '/app/mes/weigh/ticket/issue': {
      type: OperationType.edit,
      business: '下发称量工单',
    },
    '/app/mes/weigh/ticket/cancel': {
      type: OperationType.edit,
      business: '取消称量工单',
    },
  },
};

export { WeighingWorkOrderPlan };
