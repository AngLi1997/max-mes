import { OperationType } from '../../const';
import { log } from '../../type';

const InventoryManageEnum: Record<string, log> = {
  '150020002': {
    '/app/wms/inventory/addInventoryBatch': {
      type: OperationType.add,
      business: '新增批次',
    },
    '/app/wms/inventory/editInventoryBatch': {
      type: OperationType.edit,
      business: '编辑批次',
    },
    '/app/wms/inventory/addInventory': {
      type: OperationType.add,
      business: '新增货品件',
    },
  },
};

export default InventoryManageEnum;
