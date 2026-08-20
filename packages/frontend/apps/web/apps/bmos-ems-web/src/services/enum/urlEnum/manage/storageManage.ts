import { OperationType } from '../../const';
import { log } from '../../type';

const StorageManageEnum: Record<string, log> = {
  '150020001': {
    '/app/ems/inventory/inbound': {
      type: OperationType.add,
      business: '货品入库',
    },
    '/app/ems/inventory/outbound': {
      type: OperationType.edit,
      business: '货品出库',
    },
    '/app/ems/inventory/move': {
      type: OperationType.edit,
      business: '货品移库',
    },
    '/app/ems/inventory/check': {
      type: OperationType.edit,
      business: '货品盘点',
    },
  },
};

export default StorageManageEnum;
