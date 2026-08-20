import { BMStateTagEnum, BMStateTagType } from '@bmos/components';
// 审核状态
export enum AuditStatus {
  // 待提交
  PENDING_SUBMISSION = 'PENDING_SUBMISSION',
  // 已提交
  SUBMITTED = 'SUBMITTED',
  // 审核中
  UNDER_AUDIT = 'UNDER_AUDIT',
  // 审核完成
  AUDIT_COMPLETED = 'AUDIT_COMPLETED',
}
export const AuditStatusClassMap: Map<
  AuditStatus,
  {
    type: BMStateTagType;
    stateName: string;
  }
> = new Map([
  [
    AuditStatus.PENDING_SUBMISSION,
    {
      type: BMStateTagEnum.PRIMARY,
      stateName: t('待提交'),
    },
  ],
  [
    AuditStatus.SUBMITTED,
    {
      type: BMStateTagEnum.CONFIRM,
      stateName: t('已提交'),
    },
  ],
  [
    AuditStatus.UNDER_AUDIT,
    {
      type: BMStateTagEnum.WARNING,
      stateName: t('审核中'),
    },
  ],
  [
    AuditStatus.AUDIT_COMPLETED,
    {
      type: BMStateTagEnum.SUCCESS,
      stateName: t('审核完成'),
    },
  ],
]);
