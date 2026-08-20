import { OperationType } from '../const';
import { log } from '../type';
const CodeRuleEnum: Record<string, log> = {
  '100020001': {
    '/api/app/platform/codeRule/save': {
      type: OperationType.add,
      business: '新增编号规则',
    },
    '/api/app/platform/codeRuleVersion/save': {
      type: OperationType.add,
      business: '新增版本',
    },
    '/api/app/platform/codeRuleVersion/confirm': {
      type: OperationType.edit,
      business: '确认',
    },
    '/api/app/platform/codeRuleVersion/update': {
      type: OperationType.edit,
      business: '编辑',
    },
    '/api/app/platform/codeRuleVersion/delete': {
      type: OperationType.delete,
      business: '删除',
    },
    '/api/app/platform/codeRuleVersion/enabled': {
      type: OperationType.edit,
      business: '启用',
    },
    '/api/app/platform/codeRuleVersion/disabled': {
      type: OperationType.edit,
      business: '停用',
    },
  },
};

export default CodeRuleEnum;
