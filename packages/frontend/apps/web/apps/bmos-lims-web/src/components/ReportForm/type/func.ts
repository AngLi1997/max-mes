import { DragData } from './drag';
import { Area } from './print';
import { Cell, exportData, Position, Range, RangeCell } from './sheet';
export interface Func {
  setCellMarkValue: (position: Position, obj: DragData) => void;
  handleCoord: (range: Array<Range>) => Position[];
  getCoord: (rows: Range['row'], columns: Range['column']) => Array<Position>;
  setCoordArr: (arr: Array<Position>) => Array<Range>;
  saveTemplate: <T>(printArea: Array<Area>) => exportData & { config?: any };
  loadExcel: (
    evt: InputEvent & { target: HTMLInputElement },
    type: boolean,
  ) => void;
  printTemplate: (area: Array<Range>) => Promise<void>;
  isOverlap: (
    target: Range | Array<Range>,
    array: Array<Array<Range>> | Array<string>,
  ) => boolean;
  setPrintAreaCellBorder: (
    target: RangeCell,
    borderType: string,
    color: string,
    style: string,
  ) => void;
  is_cell: (arr: Array<Range>) => boolean;
  // find(content [,setting])
  // 参数：
  // {String} [content]: 要查找的内容
  // {PlainObject} [setting]: 可选参数
  // {Boolean} [isRegularExpression]: 是否正则表达式匹配；默认为 false
  // {Boolean} [isWholeWord]: 是否整词匹配；默认为 false
  // {Boolean} [isCaseSensitive]: 是否区分大小写匹配；默认为 false
  // {Number} [order]: 工作表下标；默认值为当前工作表下标
  // {String} [type]: 单元格属性；默认值为"m"
  find: (
    content: string,
    setting?: {
      isRegularExpression?: boolean;
      isWholeWord?: boolean;
      isCaseSensitive?: boolean;
      order?: number;
      type?: string;
    },
  ) => Array<Cell>;
}

interface SingletonTeturn {
  [p: string]: any;
  destroy: () => void;
}

interface SingletonFunctionReturn<T> extends SingletonTeturn {
  Instance: T;
}

export type singletonCallback<T> = (instance: T) => SingletonTeturn;

export type singletonFunction<T> = () => SingletonFunctionReturn<T>;
