import { OperationType } from '../../const';
import { log } from '../../type';

const MaterialManageEnum: Record<string, log> = {
  '120030007': {
    '/app/mes/storage/material/manage/addBatch': {
      type: OperationType.add,
      business: '新增物料批次',
    },
    '/app/mes/storage/material/manage/editBatch': {
      type: OperationType.edit,
      business: '编辑物料批次',
    },
    '/app/mes/storage/material/manage/add': {
      type: OperationType.add,
      business: '新增物料',
    },
  },
};

export { MaterialManageEnum };
