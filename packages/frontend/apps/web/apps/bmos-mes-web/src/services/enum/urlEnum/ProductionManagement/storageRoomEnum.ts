import { OperationType } from '../../const';
import { log } from '../../type';

const StorageRoomEnum: Record<string, log> = {
  '120020010': {
    '/app/mes/storage/config/create': {
      type: OperationType.add,
      business: '新增存储区域',
    },
    '/app/mes/storage/config/edit': {
      type: OperationType.edit,
      business: '编辑存储区域',
    },
    '/app/mes/storage/config/delete': {
      type: OperationType.delete,
      business: '删除存储区域',
    },
    '/app/mes/material/position/create': {
      type: OperationType.add,
      business: '新增暂存货位',
    },
    '/app/mes/material/position/edit': {
      type: OperationType.edit,
      business: '编辑暂存货位',
    },
    '/app/mes/material/position/enable': {
      type: OperationType.edit,
      business: '启用暂存货位',
    },
    '/app/mes/material/position/disable': {
      type: OperationType.edit,
      business: '停用暂存货位',
    },
    '/app/mes/material/position/delete': {
      type: OperationType.delete,
      business: '删除暂存货位',
    },
    '/app/mes/resource/permission/save': {
      type: OperationType.edit,
      business: '暂存货位数据授权',
    },
  },
};

export { StorageRoomEnum };
