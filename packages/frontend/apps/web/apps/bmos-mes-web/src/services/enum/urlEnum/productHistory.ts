import { InternalAxiosRequestConfig } from 'axios';
import { OperationType } from '../const';
import { log } from '../type';

const ProductHistory: Record<string, log> = {
  '120050001': {
    '/app/mes/operation/history/save': {
      type: OperationType.add,
      business: '新增分类',
      export(config: InternalAxiosRequestConfig) {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.export,
          business: data.type === 'view' ? '预览' : '打印',
        };
      },
    },
  },
};

export default ProductHistory;
