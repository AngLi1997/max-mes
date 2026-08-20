import { OperationType } from '../../const';
import { log } from '../../type';
// 房间管理
const RoomManagementEnum: Record<string, log> = {
  '160030002': {
    '/app/platform/factory/room/module/save': {
      type: OperationType.add,
      business: '新增分类',
    },
    '/app/platform/factory/room/module/update': {
      type: OperationType.edit,
      business: '编辑分类',
    },
    '/app/platform/factory/room/module/delete': {
      type: OperationType.delete,
      business: '删除分类',
    },
    '/app/platform/factory/room/save': {
      type: OperationType.add,
      business: '新增房间',
    },
    '/app/platform/factory/room/enable': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.enable ? '启用房间' : '停用房间',
        };
      },
    },
    '/app/platform/factory/room/update': {
      type: OperationType.edit,
      business: '编辑房间',
    },
    '/app/platform/factory/room/delete': {
      type: OperationType.delete,
      business: '删除房间',
    },
    '/app/platform/factory/room/bind/station': {
      type: OperationType.edit,
      business: '房间绑定工位',
    },
    '/app/platform/resource/permission/save': {
      type: OperationType.edit,
      business: '保存数据权限',
    },
  },
};

export default RoomManagementEnum;
