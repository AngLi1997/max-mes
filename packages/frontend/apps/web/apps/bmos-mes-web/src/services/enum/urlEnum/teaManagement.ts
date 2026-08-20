import { OperationType } from '../const';
import { log } from '../type';

const teaManagement: Record<string, log> = {
  '120030005':{
    '/app/mes/plan/team/save': {
      type: OperationType.add,
      business: '新增班组',
    },
    '/app/mes/plan/team/update':{
      type: OperationType.edit,
      business: '编辑班组',
    },
    '/app/mes/plan/team/enable':{
      type: OperationType.edit,
      business: '启用班组',
    },
    '/app/mes/plan/team/disable':{
      type: OperationType.edit,
      business: '停用班组',
    }
  }
}
export default teaManagement