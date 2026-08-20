import { OperationType } from '../const';
import { log } from '../type';

const InspectionItemEnum: Record<string, log> = {
  '130010003':{
    '/app/lims/experiment/inspect/save': {
      type: OperationType.add,
      business: '新增检验项目',
    },
    '/app/lims/experiment/inspect/update': {
      type: OperationType.edit,
      business: '编辑检验项目',
    },
    '/app/lims/experiment/inspect/delete': {
      type: OperationType.delete,
      business: '删除检验项目',
    },
  }
}
export default InspectionItemEnum