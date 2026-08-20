import { OperationType } from '../../const';
import { log } from '../../type';
// 产线管理
const ProductionLineManagementEnum: Record<string, log> = {
  '160030001': {
    '/app/platform/factory/line/module/save': {
      type: OperationType.add,
      business: '新增分类',
    },
    '/app/platform/factory/line/module/update': {
      type: OperationType.edit,
      business: '编辑分类',
    },
    '/app/platform/factory/line/module/delete': {
      type: OperationType.delete,
      business: '删除分类',
    },
    '/app/platform/factory/line/save': {
      type: OperationType.add,
      business: '新增产线',
    },
    '/app/platform/factory/line/enable': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.enable ? '启用产线' : '停用产线',
        };
      },
    },
    '/app/platform/factory/line/update': {
      type: OperationType.edit,
      business: '编辑产线',
    },
    '/app/platform/factory/line/delete': {
      type: OperationType.delete,
      business: '删除产线',
    },
    '/app/platform/factory/line/bind/room': {
      type: OperationType.edit,
      business: '产线绑定房间',
    },
    '/app/platform/factory/line/bind/station': {
      type: OperationType.edit,
      business: '产线绑定工位',
    },
  },
};

export default ProductionLineManagementEnum;
