import { BMStateTagEnum, BMStateTagType } from '@bmos/components';

export enum StatusType {
  // 编辑
  EDIT = 830401,
  // 审核中
  PROCESSING = 830402,
  // 生效
  EFFECTIVE = 830403,
  // 作废
  SCRAPED = 830404,
}

export const StatusClassMap: Map<
  StatusType,
  {
    type: BMStateTagType;
    stateName: string;
  }
> = new Map([
  [
    StatusType.EDIT,
    {
      type: BMStateTagEnum.PRIMARY,
      stateName: t('编辑'),
    },
  ],
  [
    StatusType.PROCESSING,
    {
      type: BMStateTagEnum.WARNING,
      stateName: t('审核中'),
    },
  ],
  [
    StatusType.EFFECTIVE,
    {
      type: BMStateTagEnum.SUCCESS,
      stateName: t('生效'),
    },
  ],
  [
    StatusType.SCRAPED,
    {
      type: BMStateTagEnum.DEFAULT,
      stateName: t('作废'),
    },
  ],
]);
