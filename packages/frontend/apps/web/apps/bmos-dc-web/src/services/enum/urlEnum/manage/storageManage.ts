import { OperationType } from '../../const';
import { log } from '../../type';

const StorageManageEnum: Record<string, log> = {
  '150020001': {
    '/app/dc/inventory/inbound': {
      type: OperationType.add,
      business: '货品入库',
    },
    '/app/dc/inventory/outbound': {
      type: OperationType.edit,
      business: '货品出库',
    },
    '/app/dc/inventory/move': {
      type: OperationType.edit,
      business: '货品移库',
    },
    '/app/dc/inventory/check': {
      type: OperationType.edit,
      business: '货品盘点',
    },
  },
};

export default StorageManageEnum;
