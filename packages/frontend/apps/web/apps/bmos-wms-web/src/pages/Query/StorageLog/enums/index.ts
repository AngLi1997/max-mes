import { t } from '@bmos/i18n';

/**
 * 操作类型枚举
 [INBOUND:入库,
 OUTBOUND:出库,
 PLUS:盘增,
 MINUS:盘减,
 SEND_BACK:退库
 */
export type OperationType = 'INBOUND' | 'OUTBOUND' | 'PLUS' | 'MINUS' | 'SEND_BACK';

export const OperationTypeMap: Map<OperationType, string> = new Map([
  ['INBOUND', t('入库')],
  ['OUTBOUND', t('出库')],
  ['PLUS', t('盘增')],
  ['MINUS', t('盘减')],
  ['SEND_BACK', t('退库')],
]);
