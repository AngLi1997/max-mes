import { OperationType } from '../../const';
import { log } from '../../type';
//设备管理
const equipmentManagement: Record<string, log> = {
  '160010002': {
    '/app/platform/equipment/category/save': {
      type: OperationType.add,
      business: '新增设备分类',
    },
    '/app/platform/equipment/category/update': {
      type: OperationType.edit,
      business: '编辑设备分类',
    },
    '/app/platform/equipment/category/delete': {
      type: OperationType.delete,
      business: '删除设备分类',
    },
    '/app/platform/equipment/enable': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.enable ? '启用设备' : '停用设备',
        };
      },
    },
    '/app/platform/equipment/update': {
      type: OperationType.edit,
      business: '编辑设备',
    },
    '/app/platform/equipment/save': {
      type: OperationType.add,
      business: '新增设备',
    },
    '/app/platform/equipment/delete': {
      type: OperationType.delete,
      business: '删除设备',
    },
    '/app/platform/tag/instance/printBatch': {
      type: OperationType.export,
      business: '批量打印设备',
    },
  },
};
export default equipmentManagement;
