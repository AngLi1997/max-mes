import { OperationType } from '../../const';
import { log } from '../../type';
import { BASE_URL } from '@/services/baseUrl';

// 总数据管理
const TotalDataManagementEnum: Record<string, log> = {
  '180030001': { // 免疫类型选择
    [`${BASE_URL}/titer/select`]: {
      type: OperationType.edit,
      business: '选择',
    },
  },
  '180030002': { // 检验结果汇总发布
    [`${BASE_URL}/summary/publish`]: {
      type: OperationType.edit,
      business: '发布',
    },
  },
  '180030003': { // 检验结果发布审核
    [`${BASE_URL}/summary/audit`]: {
      type: OperationType.audit,
      business: '审核',
    },
  },
};

export default TotalDataManagementEnum;
