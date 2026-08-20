import { t } from '@bmos/i18n';

export enum CHECK_STATUS {
  // CREATE = 'create', // 创建请验单
  CONFIRM = 'confirm', // 待确认
  TAKE = 'take', // 待取样
  INSPECT = 'inspect', // 待检验
  REPORT = 'report', // 报告待生成
  AUDIT_REPORT = 'audit_report', // 报告待审核
  SIGN = 'sign', // 报告待签发
  COMPLETE = 'complete', // 已完成
  CANCEL = 'cancel', // 已取消
  ALREADY_TERMINATION = 'terminate', // 已终止
  ALREADY_SIGN = 'already_sign', // 已签发
}

export const checkStatusMap = {
  [CHECK_STATUS.CONFIRM]: t('待确认'),
  [CHECK_STATUS.TAKE]: t('待取样'),
  [CHECK_STATUS.INSPECT]: t('待检验'),
  [CHECK_STATUS.REPORT]: t('报告待生成'),
  [CHECK_STATUS.AUDIT_REPORT]: t('报告待审核'),
  [CHECK_STATUS.SIGN]: t('报告待签发'),
  [CHECK_STATUS.COMPLETE]: t('已完成'),
  [CHECK_STATUS.CANCEL]: t('已取消'),
  [CHECK_STATUS.ALREADY_TERMINATION]: t('已终止'),
  [CHECK_STATUS.ALREADY_SIGN]: t('报告已签发'),
}