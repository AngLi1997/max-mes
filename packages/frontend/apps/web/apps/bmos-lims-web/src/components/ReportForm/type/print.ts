import { Range } from './sheet';
export interface Area {}

export interface AreaRangeItem {
  borderType?: string;
  style?: string;
  color?: string;
  rangeType?: string;
  range?: Array<Range>;
  mv?: string;
}
