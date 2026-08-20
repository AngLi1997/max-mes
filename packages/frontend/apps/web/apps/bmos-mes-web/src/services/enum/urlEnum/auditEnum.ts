import { OperationType } from '../const';
import { log } from '../type';

const AuditEnum: Record<string, log> = {
  '120020008': {
    '/app/mes/audit/save/flow/audit': {
      type: OperationType.add,
      business: '新增流程模型',
      export: (config: any) => {
        const data = JSON.parse(config.data);
        return data.logParams;
      },
    },
    '/app/mes/audit/deploy/flow/audit': {
      type: OperationType.edit,
      business: '发布流程',
    },
    '/app/mes/audit/flow/audit/bind/process': {
      type: OperationType.relevance,
      business: '流程绑定工艺',
    },
    '/app/mes/audit/delete/flow/audit': {
      type: OperationType.edit,
      business: '删除',
    },
    '/app/mes/audit/changeState': {
      type: OperationType.edit,
      business: '启用',
      export: (config: any) => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.enable ? '启用流程版本' : '停用流程版本',
        };
      },
    },
  },
};

export default AuditEnum;
