import { OperationType } from '../const';
import { log } from '../type';
// 字典管理
const DictManagementHeadersEnum: Record<string, log> = {
  '100020009': {
    '/api/app/platform/dict/save/dict': {
      type: OperationType.add,
      business: '新建字典',
    },
    '/api/app/platform/dict/update/dict': {
      type: OperationType.edit,
      business: '编辑字典',
    },
    '/api/app/platform/dict/delete/dict': {
      type: OperationType.delete,
      business: '删除字典',
    },
    '/api/app/platform/dict/save/dict/detail': {
      type: OperationType.add,
      business: '新增数据',
    },
    '/api/app/platform/dict/update/dict/detail': {
      type: OperationType.edit,
      business: '编辑数据',
    },
    '/api/app/platform/dict/delete/dict/detail': {
      type: OperationType.delete,
      business: '删除数据',
    },
  },
};

export default DictManagementHeadersEnum;
