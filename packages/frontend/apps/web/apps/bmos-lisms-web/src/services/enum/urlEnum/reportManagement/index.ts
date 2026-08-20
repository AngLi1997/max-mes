import { BASE_URL } from '@/services/baseUrl';
import { t } from '@bmos/i18n';
import { OperationType } from '../../const';
import { log } from '../../type';

// 报告管理
const ReportManagementEnum: Record<string, log> = {
  '210040001': {
    // 检验报告中心
    [`${BASE_URL}/report/audit`]: {
      type: OperationType.audit,
      business: t('审核'),
    },
    [`${BASE_URL}/report/preview`]: {
      type: OperationType.export,
      business: t('打印检测报告'),
    },
    [`${BASE_URL}/report/check-report`]: {
      type: OperationType.export,
      business: t('打印控制点记录'),
    },
  },
  '210040002': {
    // 检验报告签发
    [`${BASE_URL}/report/sign`]: {
      type: OperationType.audit,
      business: t('签发'),
    },
    [`${BASE_URL}/report/preview`]: {
      type: OperationType.export,
      business: t('打印检测报告'),
    },
    [`${BASE_URL}/report/resign`]: {
      type: OperationType.edit,
      business: t('再次签发'),
    },
    [`${BASE_URL}/report/sign-back`]: {
      type: OperationType.edit,
      business: t('撤销签发'),
    },
    [`${BASE_URL}/report/check-report`]: {
      type: OperationType.export,
      business: t('打印控制点记录'),
    },
  },
  '210040003': {
    [`${BASE_URL}/report/check`]: {
      type: OperationType.edit,
      business: t('检查'),
    },
    [`${BASE_URL}/report/preview`]: {
      type: OperationType.export,
      business: t('打印检测报告'),
    },
    [`${BASE_URL}/report/check-report`]: {
      type: OperationType.export,
      business: t('打印控制点记录'),
    },
  },
};

export default ReportManagementEnum;
