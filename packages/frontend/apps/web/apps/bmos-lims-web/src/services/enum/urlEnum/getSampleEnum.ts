import { OperationType } from '../const';
import { log } from '../type';

// 取样
const ExperimentalPackageEnum: Record<string, log> = {
  '130020003':{
    '/app/lims/experiment/package/save': {
      type: OperationType.add,
      business: '新增实验包',
    },
    '/app/lims/experiment/package/update': {
      type: OperationType.edit,
      business: '编辑实验包',
    },
    '/app/lims/experiment/package/delete': {
      type: OperationType.delete,
      business: '删除实验包',
    },
  }
}
export default ExperimentalPackageEnum