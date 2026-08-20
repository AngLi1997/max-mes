import { BASE_URL } from '@/services/baseUrl';
import { OperationType } from '../../const';
import { log } from '../../type';
// 检验项目
export const ExecutionManagementEnum: Record<string, log> = {
  '210030004': {
    [`${BASE_URL}/inspect/protein/read`]: {
      type: OperationType.edit,
      business: '读取',
    },
    [`${BASE_URL}/inspect/singledata/check`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sampleBatchNo === 'TO_CHECK' ? '批量发布' : '发布',
        };
      },
    },
    [`${BASE_URL}/inspect/singledata/cancelcheck`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sampleBatchNo === 'TO_CHECK' ? '批量核对' : '核对',
        };
      },
    },
  },
  '210030005': {
    [`${BASE_URL}/inspect/alt/read`]: {
      type: OperationType.edit,
      business: '读取',
    },
    [`${BASE_URL}/inspect/singledata/check`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sampleBatchNo === 'TO_CHECK' ? '批量发布' : '发布',
        };
      },
    },
    [`${BASE_URL}/inspect/singledata/cancelcheck`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sampleBatchNo === 'TO_CHECK' ? '批量核对' : '核对',
        };
      },
    },
  },
  '210030006': {
    [`${BASE_URL}/inspect/four-enzyme/file/read`]: {
      type: OperationType.edit,
      business: '文件读取',
    },
    [`${BASE_URL}/inspect/fourenzyme/read`]: {
      type: OperationType.edit,
      business: '读取',
    },
    [`${BASE_URL}/inspect/singledata/check`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sampleBatchNo === 'TO_CHECK' ? '批量发布' : '发布',
        };
      },
    },
    [`${BASE_URL}/inspect/singledata/cancelcheck`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sampleBatchNo === 'TO_CHECK' ? '批量核对' : '核对',
        };
      },
    },
  },
  '210030007': {
    [`${BASE_URL}/inspect/four-enzyme/file/read`]: {
      type: OperationType.edit,
      business: '文件读取',
    },
    [`${BASE_URL}/inspect/fourenzyme/read`]: {
      type: OperationType.edit,
      business: '读取',
    },
    [`${BASE_URL}/inspect/singledata/check`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sampleBatchNo === 'TO_CHECK' ? '批量发布' : '发布',
        };
      },
    },
    [`${BASE_URL}/inspect/singledata/cancelcheck`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sampleBatchNo === 'TO_CHECK' ? '批量核对' : '核对',
        };
      },
    },
  },
  '210030008': {
    [`${BASE_URL}/inspect/four-enzyme/file/read`]: {
      type: OperationType.edit,
      business: '文件读取',
    },
    [`${BASE_URL}/inspect/fourenzyme/read`]: {
      type: OperationType.edit,
      business: '读取',
    },
    [`${BASE_URL}/inspect/singledata/check`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sampleBatchNo === 'TO_CHECK' ? '批量发布' : '发布',
        };
      },
    },
    [`${BASE_URL}/inspect/singledata/cancelcheck`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sampleBatchNo === 'TO_CHECK' ? '批量核对' : '核对',
        };
      },
    },
  },
  '210030009': {
    [`${BASE_URL}/inspect/four-enzyme/file/read`]: {
      type: OperationType.edit,
      business: '文件读取',
    },
    [`${BASE_URL}/inspect/fourenzyme/read`]: {
      type: OperationType.edit,
      business: '读取',
    },
    [`${BASE_URL}/inspect/singledata/check`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sampleBatchNo === 'TO_CHECK' ? '批量发布' : '发布',
        };
      },
    },
    [`${BASE_URL}/inspect/singledata/cancelcheck`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sampleBatchNo === 'TO_CHECK' ? '批量核对' : '核对',
        };
      },
    },
  },
  '210030010': {
    [`${BASE_URL}/inspect/proteinelec/read`]: {
      type: OperationType.edit,
      business: '读取',
    },
    [`${BASE_URL}/inspect/singledata/check`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sampleBatchNo === 'TO_CHECK' ? '批量发布' : '发布',
        };
      },
    },
    [`${BASE_URL}/inspect/singledata/cancelcheck`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.sampleBatchNo === 'TO_CHECK' ? '批量核对' : '核对',
        };
      },
    },
    [`${BASE_URL}/report/file`]: {
      type: OperationType.export,
      business: '打印蛋白电泳检测报告',
    },
  },
};
