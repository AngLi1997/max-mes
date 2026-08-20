import { OperationType } from '../../const';
import { log } from '../../type';
// 货品配置枚举
const ProductConfigurationEnum: Record<string, log> = {
  '150010002': {
    '/app/wms/storage/config/create': {
      type: OperationType.add,
      business: '新增存储区域',
    },
    '/app/wms/storage/config/edit': {
      type: OperationType.edit,
      business: '编辑存储区域',
    },
    '/app/wms/storage/config/delete': {
      type: OperationType.delete,
      business: '删除存储区域',
    },
    '/app/wms/material/position/create': {
      type: OperationType.add,
      business: '新增货位',
    },
    '/app/wms/material/position/edit': {
      type: OperationType.edit,
      business: '编辑货位',
    },
    '/app/wms/material/position/enable': {
      type: OperationType.edit,
      business: '启用货位',
    },
    '/app/wms/material/position/disable': {
      type: OperationType.edit,
      business: '停用货位',
    },
    '/app/wms/material/position/delete': {
      type: OperationType.delete,
      business: '删除货位',
    },
    '/app/wms/resource/permission/save': {
      type: OperationType.edit,
      business: '货位数据授权',
    },
  },
};

export default ProductConfigurationEnum;
