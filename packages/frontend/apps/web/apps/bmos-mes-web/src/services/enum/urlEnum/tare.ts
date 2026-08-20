import { OperationType } from '../const';
import { log } from '../type';

const TareHeadersEnum: Record<string, log> = {
  '120020015': {
    '/app/mes/tareWeigh/config/create': {
      type: OperationType.add,
      business: '新增皮重',
    },
    '/app/mes/tareWeigh/config/edit': {
      type: OperationType.edit,
      business: '编辑皮重',
    },
    '/app/mes/tareWeigh/config/delete': {
      type: OperationType.delete,
      business: '删除皮重',
    },
    '/app/platform/tag/instance/printBatch': {
      type: OperationType.edit,
      business: '打印标签',
    },
  },
};
export default TareHeadersEnum;
