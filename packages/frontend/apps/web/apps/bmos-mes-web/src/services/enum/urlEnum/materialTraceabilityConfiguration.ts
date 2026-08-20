import { OperationType } from '../const';
import { log } from '../type';

const MaterialTraceabilityConfigurationEnum: Record<string, log> = {
  '120020016': {
    '/app/mes/material/trace/template/create': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.add,
          business: data.copy ? '复制物料追溯模版' : '新增物料追溯模版',
        };
      },
    },
    '/app/mes/material/trace/template/edit': {
      type: OperationType.edit,
      business: '编辑物料追溯模版',
    },
    '/app/mes/material/trace/template/enable': {
      type: OperationType.edit,
      business: '启用物料追溯模版',
    },
    '/app/mes/material/trace/template/disable': {
      type: OperationType.edit,
      business: '停用物料追溯模版',
    },
    '/app/mes/material/trace/template/delete': {
      type: OperationType.delete,
      business: '删除物料追溯模版',
    },
  },
};
export default MaterialTraceabilityConfigurationEnum;
