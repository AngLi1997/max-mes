import { OperationType } from '../../const';
import { log } from '../../type';
// 数据集
const DataSetManageEnum: Record<string, log> = {
  '120070001': {
    '/app/mes/dataset/category/createCategory': {
      type: OperationType.add,
      business: '新增分类',
    },
    '/app/mes/dataset/category/editCategory': {
      type: OperationType.edit,
      business: '编辑分类',
    },
    '/app/mes/dataset/category/delete': {
      type: OperationType.delete,
      business: '删除分类',
    },
    '/app/mes/dataset/createDataset': {
      type: OperationType.add,
      business: '新增数据集',
    },
    '/app/mes/dataset/editDataset': {
      type: OperationType.edit,
      business: '编辑数据集',
    },
    '/app/mes/dataset/delete': {
      type: OperationType.delete,
      business: '删除数据集',
    },
  },
};

export { DataSetManageEnum };
