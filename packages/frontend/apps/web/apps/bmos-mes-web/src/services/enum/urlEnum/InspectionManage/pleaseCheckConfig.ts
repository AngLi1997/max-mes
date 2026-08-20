import { OperationType } from '../../const';
import { log } from '../../type';
// 请验单配置
const PleaseCheckConfigEnum: Record<string, log> = {
  '120100001': {
    '/app/mes/inspect/config/save': {
      type: OperationType.add,
      business: '新增请验单',
    },
    '/app/mes/inspect/config/update': {
      type: OperationType.edit,
      business: '编辑请验单',
    },
    '/app/mes/inspect/config/disable': {
      type: OperationType.edit,
      business: '停用请验单',
    },
    '/app/mes/inspect/config/enable': {
      type: OperationType.edit,
      business: '启用请验单',
    },
    '/app/mes/inspect/config/bind/material': {
      type: OperationType.edit,
      business: '请验单绑定物料',
    },
    '/app/mes/inspect/config/delete': {
      type: OperationType.delete,
      business: '删除请验单',
    },
  },
};

export { PleaseCheckConfigEnum };
