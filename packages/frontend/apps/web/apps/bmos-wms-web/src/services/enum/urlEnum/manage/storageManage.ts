import { OperationType } from '../../const';
import { log } from '../../type';

const StorageManageEnum: Record<string, log> = {
  '150020001': {
    '/app/wms/inventory/inbound': {
      type: OperationType.add,
      business: '货品入库',
    },
    '/app/wms/inventory/outbound': {
      type: OperationType.edit,
      business: '货品出库',
    },
    '/app/wms/inventory/move': {
      type: OperationType.edit,
      business: '货品移库',
    },
    '/app/wms/inventory/check': {
      type: OperationType.edit,
      business: '货品盘点',
    },
  },
};

export default StorageManageEnum;
