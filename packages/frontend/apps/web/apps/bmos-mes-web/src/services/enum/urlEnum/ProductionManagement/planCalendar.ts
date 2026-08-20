import { OperationType } from '../../const';
import { log } from '../../type';

const PlanCalendarEnum: Record<string, log> = {
  '120030012': {
    '/app/mes/production/changeCalendar': {
      type: OperationType.edit,
      business: '日历调整',
    },
  },
};

export { PlanCalendarEnum };
