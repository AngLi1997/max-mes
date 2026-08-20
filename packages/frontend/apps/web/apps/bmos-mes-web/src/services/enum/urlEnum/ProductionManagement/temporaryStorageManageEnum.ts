import { OperationType } from '../../const';
import { log } from '../../type';

const TemporaryStorageManageEnum: Record<string, log> = {
  '120030008': {
    '/app/mes/storage/material/receiveMobile': {
      type: OperationType.edit,
      business: '物料接收',
    },
    '/app/mes/storage/material/sendBackMobile': {
      type: OperationType.edit,
      business: '物料入库',
    },
    '/app/mes/storage/material/outbound': {
      type: OperationType.edit,
      business: '物料出库',
    },
    '/app/mes/storage/material/move': {
      type: OperationType.edit,
      business: '物料移库',
    },
    '/app/mes/storage/material/check': {
      type: OperationType.edit,
      business: '物料盘点',
    },
    '/app/mes/storage/material/reserve': {
      type: OperationType.edit,
      business: '物料预定',
    },
    '/app/mes/storage/material/cancelReserve': {
      type: OperationType.edit,
      business: '物料取消预定',
    },
    '/app/mes/storage/material/splitPackage': {
      type: OperationType.edit,
      business: '拆包出库',
    },
    '/app/mes/storage/material/sendBackAndConsumeMobile': {
      type: OperationType.edit,
      business: '物料退库',
    },
    '/app/mes/storage/material/destroyAndConsumeMobile': {
      type: OperationType.edit,
      business: '物料销毁',
    },
    '/app/mes/storage/material/useAndConsumeMobile': {
      type: OperationType.edit,
      business: '物料使用',
    },
  },
};

export { TemporaryStorageManageEnum };
