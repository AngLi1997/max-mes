import { t } from '@bmos/i18n';
import { DetailsType } from '../../../types';

export const typeMap = new Map([
  [DetailsType.CONSTANT, t('常量')],
  [DetailsType.PARAMETER, t('参数')],
  [DetailsType.DATE, t('日期')],
  [DetailsType.SEQUENCE, t('流水号')],
]);