import { BASE_URL } from '@/services/baseUrl';
import { OperationType } from '../../const';
import { log } from '../../type';
// 检验管理
const InspectionManagementEnum: Record<string, log> = {
  '170030001': {
    // 标本请验
    [`${BASE_URL}/sample-examination-info/inspection`]: {
      type: OperationType.edit,
      business: '标本请验',
    },
    [`${BASE_URL}/sample-examination-info/out/warehouse`]: {
      type: OperationType.edit,
      business: '标本出库',
    },
    [`${BASE_URL}/sample-examination-info/sync-lims`]: {
      type: OperationType.edit,
      business: '数据同步',
    },
  },
  // '170030002': { // 检验结果
  //   [`${BASE_URL}/examination/result/result-receive`]: {
  //     type: OperationType.relevance,
  //     business: '接收实验室检验结论',
  //   },
  // },
  // '170030003': { // 检验报告
  //   [`${BASE_URL}/examination-report/receive`]: {
  //     type: OperationType.relevance,
  //     business: '接收实验室报告',
  //   },
  // },
};

export default InspectionManagementEnum;
