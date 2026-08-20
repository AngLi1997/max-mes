import { BASE_URL } from '@/services/baseUrl';
import { t } from '@bmos/i18n';
import { OperationType } from '../../const';
import { log } from '../../type';

// 标本交接
const SpecimenManagementEnum: Record<string, log> = {
  '180010001': {
    // 标本接收
    [`${BASE_URL}/sample/batch/accept`]: {
      type: OperationType.edit,
      business: t('标本接收'),
    },
  },
};

export default SpecimenManagementEnum;
