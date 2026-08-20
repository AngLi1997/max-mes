import { OperationType } from '../../const';
import { log } from '../../type';
// 生产计划模板
const PlanTemplateEnum: Record<string, log> = {
  '120020014': {
    '/app/mes/plan/template/save': {
      type: OperationType.add,
      business: '新增模板',
    },
    '/app/mes/plan/template/edit': {
      type: OperationType.edit,
      business: '编辑模板',
    },
    '/app/mes/plan/template/changeState': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.state ? '启用模板' : '停用模板',
        };
      },
    },
    '/app/mes/plan/template/delete': {
      type: OperationType.delete,
      business: '删除模板',
    },
  },
};

export { PlanTemplateEnum };
