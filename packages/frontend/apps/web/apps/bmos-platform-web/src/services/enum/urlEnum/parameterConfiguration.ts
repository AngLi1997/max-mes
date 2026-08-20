import { OperationType } from '../const';
import { log } from '../type';
// 参数配置
const parameterConfigurationHeadersEnum: Record<string, log> = {
  '100010002': {
    '/api/app/platform/business/parameter/update': {
      type: OperationType.edit,
      business: '编辑',
    },
  },
};

export default parameterConfigurationHeadersEnum;
