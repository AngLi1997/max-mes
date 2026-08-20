import { OperationType } from '../../const';
import { log } from '../../type';

const PlanManagementEnum: Record<string, log> = {
  '120030011': {
    '/app/mes/production/plan/issue': {
      type: OperationType.add,
      business: '下发生产计划',
    },
    '/app/mes/production/plan/nullify': {
      type: OperationType.edit,
      business: '计划作废',
    },
    '/app/mes/production/changeCalendar': {
      type: OperationType.edit,
      business: '日历调整',
    },
  },
};

export { PlanManagementEnum };
