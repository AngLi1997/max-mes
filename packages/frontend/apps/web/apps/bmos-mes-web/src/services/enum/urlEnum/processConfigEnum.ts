import { InternalAxiosRequestConfig } from 'axios';
import { OperationType } from '../const';
import { log } from '../type';

const ProcessConfigHeadersEnum: Record<string, log> = {
  '120020006': {
    '/app/mes/process/save': {
      type: OperationType.add,
      business: '新增工艺',
    },
    '/app/mes/process/modify': {
      type: OperationType.edit,
      business: '编辑工艺',
    },
    '/app/mes/process/version/save': {
      type: OperationType.add,
      business: '新增工艺版本',
    },
    '/app/mes/process/version/copy': {
      type: OperationType.add,
      business: '复制工艺',
    },
    '/app/mes/process/version/changeState': {
      type: OperationType.edit,
      business: '启用工艺版本',
      export: (config: InternalAxiosRequestConfig) => {
        const data = JSON.parse(config.data);
        const stateMap: any = {
          confirm: '确认工艺版本',
          invalid: '停用工艺版本',
          valid: '立即生效当前工艺版本',
        };
        return {
          type: OperationType.edit,
          business: stateMap[data.actionState],
        };
      },
    },
    '/app/mes/resource/permission/save': {
      type: OperationType.relevance,
      business: '数据权限',
    },
    '/app/mes/process/version/audit': {
      type: OperationType.audit,
      business: '发起审核',
    },
  },
};

export default ProcessConfigHeadersEnum;
