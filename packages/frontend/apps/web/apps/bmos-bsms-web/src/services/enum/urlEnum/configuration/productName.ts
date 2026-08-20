import { OperationType } from '../../const';
import { log } from '../../type';
// 货品信息枚举
const ProductNameEnum: Record<string, log> = {
  '150010001': {
    '/app/wms/cargo/category/create': {
      type: OperationType.add,
      business: '新增货品分类',
    },
    '/app/wms/cargo/category/delete': {
      type: OperationType.delete,
      business: '删除货品分类',
    },
    '/app/wms/cargo/create': {
      type: OperationType.add,
      business: '新增货品',
    },
    '/app/wms/cargo/edit': {
      type: OperationType.edit,
      business: '编辑货品',
    },
    '/app/wms/cargo/sync': {
      type: OperationType.edit,
      business: '同步货品',
    },
    '/app/wms/cargo/enable': {
      type: OperationType.edit,
      business: '启用货品',
    },
    '/app/wms/cargo/disable': {
      type: OperationType.edit,
      business: '停用货品',
    },
    '/app/wms/cargo/delete': {
      type: OperationType.delete,
      business: '删除货品',
    },
  },
};

export default ProductNameEnum;
