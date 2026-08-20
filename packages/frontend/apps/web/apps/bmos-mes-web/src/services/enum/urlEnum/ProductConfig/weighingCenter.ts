import { OperationType } from '../../const';
import { log } from '../../type';
// 称量中心
const WeighingCenterEnum: Record<string, log> = {
  '120020012': {
    '/app/mes/weigh/centre/category/create': {
      type: OperationType.add,
      business: '新增称量中心分类',
    },
    '/app/mes/weigh/centre/category/edit': {
      type: OperationType.edit,
      business: '编辑称量中心分类',
    },
    '/app/mes/weigh/centre/category/delete': {
      type: OperationType.delete,
      business: '删除称量中心分类',
    },
    '/app/mes/weigh/centre/create': {
      type: OperationType.add,
      business: '新增称量中心',
    },
    '/app/mes/weigh/centre/edit': {
      type: OperationType.edit,
      business: '编辑称量中心',
    },
    '/app/mes/weigh/centre/enable': {
      type: OperationType.edit,
      business: '启用称量中心',
    },
    '/app/mes/weigh/centre/disable': {
      type: OperationType.edit,
      business: '停用称量中心',
    },
    '/app/mes/weigh/centre/delete': {
      type: OperationType.delete,
      business: '删除称量中心',
    },
    '/app/mes/weigh/centre/bindStation': {
      type: OperationType.relevance,
      business: '称量中心绑定工位',
    },
    '/app/mes/resource/permission/save': {
      type: OperationType.edit,
      business: '称量中心数据授权',
    },
  },
};

export { WeighingCenterEnum };
