import { OperationType } from '../const';
import { log } from '../type';

const exceptionManagement: Record<string, log> = {
  '120090001': {
    '/app/mes/exception/save': {
      type: OperationType.add,
      business: '新增异常记录',
    },
    '/app/mes/exception/edit': {
      type: OperationType.edit,
      business: '编辑异常记录',
    },
    '/app/mes/exception/handle': {
      type: OperationType.edit,
      business: '处理异常记录',
    },
    '/app/mes/exception/cancel': {
      type: OperationType.edit,
      business: '作废异常记录',
    },
    '/app/mes/exception/reInvestigate': {
      type: OperationType.edit,
      business: '重新调查异常记录',
    },
  },
};
export default exceptionManagement;
