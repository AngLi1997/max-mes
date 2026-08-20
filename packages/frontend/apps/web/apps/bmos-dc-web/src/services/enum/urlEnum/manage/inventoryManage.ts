import { OperationType } from '../../const';
import { log } from '../../type';

const InventoryManageEnum: Record<string, log> = {
  '150020002': {
    '/app/dc/inventory/addInventoryBatch': {
      type: OperationType.add,
      business: '新增批次',
    },
    '/app/dc/inventory/editInventoryBatch': {
      type: OperationType.edit,
      business: '编辑批次',
    },
    '/app/dc/inventory/addInventory': {
      type: OperationType.add,
      business: '新增货品件',
    },
  },
};

export default InventoryManageEnum;
