import { OperationType } from '../const';
import { log } from '../type';

const RecordHeadersEnum: Record<string, log> = {
  '120020001': {
    '/app/mes/record/save/category': {
      type: OperationType.add,
      business: '新增分类',
    },
    '/app/mes/record/update/category': {
      type: OperationType.edit,
      business: '编辑分类',
    },
    '/app/mes/record/delete/category': {
      type: OperationType.delete,
      business: '删除分类',
    },
    '/app/mes/record/save/record': {
      type: OperationType.add,
      business: '新增记录',
    },
    '/app/mes/record/copy/version': {
      type: OperationType.add,
      business: '新增记录版本',
    },
    '/app/mes/record/item/singleSave': {
      type: OperationType.add,
      business: '新增记录项',
    },
    // '/app/mes/record/save/component': {
    //   type: OperationType.edit,
    //   business: '记录编辑',
    // },
    '/app/mes/record/item/singleEdit': {
      type: OperationType.edit,
      business: '记录编辑',
    },
    '/app/mes/record/save/formula': {
      type: OperationType.edit,
      business: '公式配置',
    },
    '/app/mes/record/save/product': {
      type: OperationType.relevance,
      business: '记录绑定产品',
    },
    '/app/mes/resource/permission/save': {
      type: OperationType.relevance,
      business: '数据权限',
    },
    '/app/mes/record/update/version': {
      type: OperationType.edit,
      business: '作废',
    },
    '/app/mes/record/audit/start/flow': {
      type: OperationType.audit,
      business: '发起审核',
    },
  },
};

export default RecordHeadersEnum;
