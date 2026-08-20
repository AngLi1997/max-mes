import { BASE_URL } from '@/services/baseUrl';
import { OperationType } from '../../const';
import { log } from '../../type';
// 任务中心
export const TaskCenterEnum: Record<string, log> = {
  '210030001': {
    [`${BASE_URL}/inspect/task/edit`]: {
      type: OperationType.edit,
      business: '检验项目编辑',
    },
  },
};
