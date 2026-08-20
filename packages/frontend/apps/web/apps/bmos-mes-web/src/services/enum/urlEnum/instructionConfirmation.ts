import { OperationType } from '../const';
import { log } from '../type';

const InstructionConfirmation: Record<string, log> = {
  '120030004': {
    '/app/mes/plan/instruction/team/confirm': {
      type: OperationType.edit,
      business: '确认指令单',
    },
  },
};

export default InstructionConfirmation;
