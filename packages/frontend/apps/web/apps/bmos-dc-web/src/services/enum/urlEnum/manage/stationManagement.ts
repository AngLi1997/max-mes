import { OperationType } from '../../const';
import { log } from '../../type';
// 工位管理
const StationManagementEnum: Record<string, log> = {
  '160030003': {
    '/app/platform/factory/station/module/save': {
      type: OperationType.add,
      business: '新增分类',
    },
    '/app/platform/factory/station/module/update': {
      type: OperationType.edit,
      business: '编辑分类',
    },
    '/app/platform/factory/station/module/delete': {
      type: OperationType.delete,
      business: '删除分类',
    },
    '/app/platform/equipment/station/save': {
      type: OperationType.add,
      business: '新增工位',
    },
    '/app/platform/equipment/station/enable': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.enable ? '启用工位' : '停用工位',
        };
      },
    },
    '/app/platform/equipment/station/update': {
      type: OperationType.edit,
      business: '编辑工位',
    },
    '/app/platform/equipment/station/delete': {
      type: OperationType.delete,
      business: '删除工位',
    },
    '/app/platform/equipment/station/bind/user': {
      type: OperationType.edit,
      business: '工位绑定人员',
    },
    '/app/platform/equipment/station/bind/equipment': {
      type: OperationType.edit,
      business: '工位绑定设备',
    },
  },
};

export default StationManagementEnum;
