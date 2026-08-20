import { OperationType } from '../const';
import { log } from '../type';
// 公式配置
const formulaConfigurationHeadersEnum: Record<string, log> = {
  '100020006': {
    '/api/app/platform/expression/category/save': {
      type: OperationType.add,
      business: '新增分类',
    },
    '/api/app/platform/expression/category/update': {
      type: OperationType.edit,
      business: '编辑分类',
    },
    '/api/app/platform/expression/category/delete': {
      type: OperationType.delete,
      business: '删除分类',
    },
    '/api/app/platform/expression/save': {
      type: OperationType.add,
      business: '新建公式',
    },
    '/api/app/platform/expression/update': {
      type: OperationType.edit,
      business: '编辑公式',
    },
    '/api/app/platform/expression/confirm': {
      type: OperationType.edit,
      business: '确认',
    },
    // 按钮为删除按钮
    '/api/app/platform/expression/delete': {
      type: OperationType.delete,
      business: '删除',
    },
  },
};

export default formulaConfigurationHeadersEnum;
