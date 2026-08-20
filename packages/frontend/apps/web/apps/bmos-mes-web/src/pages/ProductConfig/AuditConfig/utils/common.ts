import { BMStateTagEnum } from '@bmos/components';
import { VersionStatus } from '../enum';

export const VersionStatusClassMap = new Map([
  [VersionStatus.EDITING, BMStateTagEnum.PRIMARY],
  [VersionStatus.USING, BMStateTagEnum.SUCCESS],
  [VersionStatus.HISTORY, BMStateTagEnum.DEFAULT],
]);
