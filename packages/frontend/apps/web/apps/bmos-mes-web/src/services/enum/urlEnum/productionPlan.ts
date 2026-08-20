import { OperationType } from '../const';
import { log } from '../type';
// 生产计划
const ProductionPlanHeadersEnum: Record<string, log> = {
  '120030001': {
    '/app/mes/plan/info/save': {
      type: OperationType.add,
      business: '新建生产指令单',
    },
    '/app/mes/plan/info/batchSave': {
      type: OperationType.add,
      business: '批量创建指令单',
    },
    '/app/mes/plan/info/update': {
      type: OperationType.edit,
      business: '编辑指令单',
    },
    '/app/mes/plan/info/approve': {
      type: OperationType.audit,
      business: '提交审核',
    },
    '/app/mes/plan/info/discard': {
      type: OperationType.edit,
      business: '作废指令单',
    },
  },
};

export default ProductionPlanHeadersEnum;
