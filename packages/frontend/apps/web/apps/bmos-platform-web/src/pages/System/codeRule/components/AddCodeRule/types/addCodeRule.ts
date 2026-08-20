import { AddRuleDataStatus } from '../../../types';

export interface AddCodeRuleProps {
  currentStatus?: AddRuleDataStatus;
  selectDictId?: string;
  codeObj?:Record<string,any>;
}
