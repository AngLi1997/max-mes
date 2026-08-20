import { InternalAxiosRequestConfig } from 'axios';
import { OperationType } from '../../const';
import { log } from '../../type';

const SendOutEnum: Record<string, log> = {
  '150020003': {
    '/app/ems/sendOut/sendout': {
      type: OperationType.edit,
      business: '批次发料',
      export: (config: InternalAxiosRequestConfig) => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sendOrderType === 'BATCH' ? '批次发料' : '货品发料',
        };
      },
    },
    '/app/ems/sendOut/cancel': {
      type: OperationType.edit,
      business: '取消发料',
    },
  },
};

export default SendOutEnum;
