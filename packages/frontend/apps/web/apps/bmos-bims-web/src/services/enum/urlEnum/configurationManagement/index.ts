import { OperationType } from '../../const';
import { log } from '../../type';
import { BASE_URL } from '@/services/baseUrl';

// 配置管理
const ConfigurationManagementEnum: Record<string, log> = {
  '180060001': { // 总发布校验配置
    [`${BASE_URL}/validator/update`]: {
      type: OperationType.edit,
      business: '总发布筛选配置启用/禁用',
    },
  },
};

export default ConfigurationManagementEnum;
