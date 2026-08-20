import { OperationType } from '../const';
import { log } from '../type';

const instructionListDecomposition: Record<string, log> = {
  '120030003': {
    '/app/mes/plan/instruction/send': {
      type: OperationType.edit,
      business: '指令单下发',
    },
    '/app/mes/plan/instruction/generate':{
      type: OperationType.edit,
      business: '指令单生成',
    },
    '/app/mes/plan/instruction/save':{
      type: OperationType.edit,
      business: '指令单分解',
    },
    '/app/mes/plan/instruction/update':{
      type: OperationType.edit,
      business: '指令单分解',
    }
  },
}

export default instructionListDecomposition;
