import { OperationType } from '../const';
import { log } from '../type';
// mes编号规则
const NoRulesHeadersEnum: Record<string, log> = {
  '120020009': {
    '/app/mes/plan/code/rule/update': {
      type: OperationType.edit,
      business: '编辑规则',
    },
    '/app/mes/plan/code/rule/save': {
      type: OperationType.edit,
      business: '批量配置规则',
    },
  },
};

export default NoRulesHeadersEnum;
