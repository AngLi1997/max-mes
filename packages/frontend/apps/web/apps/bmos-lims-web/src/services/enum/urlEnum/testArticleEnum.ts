import { OperationType } from '../const';
import { log } from '../type';

const TestArticleEnum: Record<string, log> = {
  '130010001':{
    '/app/lims/basic/category/save': {
      type: OperationType.add,
      business: '新增检品分类',
    },
    '/app/lims/basic/category/update': {
      type: OperationType.edit,
      business: '编辑检品分类',
    },
    '/app/lims/basic/category/delete': {
      type: OperationType.delete,
      business: '删除检品分类',
    },
    '/app/lims/basic/inspection/sync': {
      type: OperationType.relevance,
      business: '同步物料',
    },
    '/app/lims/basic/inspection/update': {
      type: OperationType.edit,
      business: '编辑检品信息',
    },
    '/app/lims/basic/inspection/delete': {
      type: OperationType.delete,
      business: '删除检品信息',
    }
  }
}
export default TestArticleEnum