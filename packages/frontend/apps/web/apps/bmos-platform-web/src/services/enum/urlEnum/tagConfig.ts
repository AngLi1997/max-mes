import { OperationType } from '../const';
import { log } from '../type';

const TagConfig: Record<string, log> = {
  '100020007': {
    '/api/app/platform/tag/instance/create': {
      type: OperationType.add,
      business: '新增标签',
    },
    '/api/app/platform/tag/instance/edit': {
      type: OperationType.edit,
      business: '编辑标签',
    },
    '/api/app/platform/tag/instance/delete': {
      type: OperationType.delete,
      business: '删除标签',
    },
    '/api/app/platform/tag/instance/enable': {
      type: OperationType.edit,
      business: '启用标签',
    },
    '/api/app/platform/tag/instance/disable': {
      type: OperationType.edit,
      business: '停用标签',
    },
  },
};

export default TagConfig;
