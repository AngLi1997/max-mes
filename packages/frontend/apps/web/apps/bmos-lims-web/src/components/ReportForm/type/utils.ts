import { DragData } from './drag';
import { AreaRangeItem } from './print';
import { Range } from './sheet';

export interface sheetUtils {
  dragToCell: (ev: DragEvent, data: DragData) => void;
  setBorder: (info: AreaRangeItem) => void;
  objectRangeToTxt: (objectRange: Range) => string;
  txtRangeToObject: (txtRange: string) => Range;
}
