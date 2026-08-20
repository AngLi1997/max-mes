import { BASE_URL } from '@/services/baseUrl';
import { t } from '@bmos/i18n';
import { OperationType } from '../../const';
import { log } from '../../type';

// 标本管理
const SpecimenManagementEnum: Record<string, log> = {
  '210020001': {
    // 标本接收
    [`${BASE_URL}/sample/receive/apply`]: {
      type: OperationType.edit,
      business: t('标本接收'),
    },
    [`${BASE_URL}/sample/download`]: {
      type: OperationType.export,
      business: t('打印标本清单'),
    },
    [`${BASE_URL}/sample/report/file`]: {
      type: OperationType.export,
      business: t('打印送检交接记录'),
    },
    [`${BASE_URL}/sample/export`]: {
      type: OperationType.export,
      business: t('导出'),
    },
    [`${BASE_URL}/sample/station/receive`]: {
      type: OperationType.edit,
      business: t('再次接收'),
    },
  },
  '210020002': {
    //接收审核
    [`${BASE_URL}/sample/receive/audit`]: {
      type: OperationType.audit,
      business: t('接收审核'),
    },
  },
  '210020003': {
    // 标本拒收
    [`${BASE_URL}/sample/reject/apply`]: {
      type: OperationType.edit,
      business: t('标本拒收'),
    },
  },
  '210020004': {
    // 拒收审核
    [`${BASE_URL}/sample/reject/audit`]: {
      type: OperationType.audit,
      business: t('拒收审核'),
    },
  },
};

export default SpecimenManagementEnum;
