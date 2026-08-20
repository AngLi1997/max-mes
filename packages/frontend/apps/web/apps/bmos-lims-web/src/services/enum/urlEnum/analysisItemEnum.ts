import { OperationType } from '../const';
import { log } from '../type';

const AnalysisItemEnum: Record<string, log> = {
  '130010002':{
    '/app/lims/experiment/analyze/save': {
      type: OperationType.add,
      business: '新增分析项',
    },
    '/app/lims/experiment/analyze/update': {
      type: OperationType.edit,
      business: '编辑分析项',
    },
    '/app/lims/experiment/analyze/delete': {
      type: OperationType.delete,
      business: '删除分析项',
    },
  }
}
export default AnalysisItemEnum