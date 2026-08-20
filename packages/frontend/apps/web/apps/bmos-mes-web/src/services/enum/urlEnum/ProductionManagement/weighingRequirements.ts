import { OperationType } from '../../const';
import { log } from '../../type';
// 生产批次配料
const WeighingRequirements: Record<string, log> = {
  '120030013': {
    '/app/mes/weigh/ticket/requirement/group/create': {
      type: OperationType.add,
      business: '新增批次配料',
    },
    '/app/mes/weigh/ticket/requirement/group/edit': {
      type: OperationType.edit,
      business: '编辑批次配料',
    },
    '/app/mes/weigh/ticket/requirement/group/makeSure': {
      type: OperationType.edit,
      business: '确认批次配料',
    },
    '/app/mes/weigh/ticket/requirement/group/saveRequirement': {
      type: OperationType.edit,
      business: '保存称量工单组配料信息',
    },
    '/app/mes/weigh/ticket/requirement/group/cancel': {
      type: OperationType.edit,
      business: '取消批次配料',
    },
  },
};

export { WeighingRequirements };
