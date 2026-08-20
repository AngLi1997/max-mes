import { OperationType } from '../const';
import { log } from '../type';

const BatchReleaseAbstract: Record<string, log> = {
  '120050010': {
    '/app/mes/lotSummary/create': {
      type: OperationType.add,
      business: '新增批次摘要',
    },
    '/app/mes/lotSummary/delete': {
      type: OperationType.edit,
      business: '删除批次摘要',
    },
    '/app/mes/lotSummary/edit': {
      type: OperationType.edit,
      business: '编辑异常记录',
    },
  },
};
export default BatchReleaseAbstract;
