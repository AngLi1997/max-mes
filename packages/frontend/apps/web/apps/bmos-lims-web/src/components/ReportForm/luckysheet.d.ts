import { Range } from './type';

interface sheetData {
  celldata: Array<any>;
  images: Array<any>;
  luckysheet_selection_range: Array<any>;
  data: Array<any>;
  name?: string;
  config: {
    authority: Record<string, any>;
  };
}
export default interface luckysheet {
  getRangeByTxt: (rang: string) => import('./type').Range;
  getConfig: () => Record<string, any>;
  setConfig: (config: Record<string, any>) => void;
  luckysheetrefreshgrid: () => void;
  getTxtByRange: (range: import('./type').Range) => string;
  exitEditMode: () => void;
  getSheet: () => sheetData;
  destroy: () => void;
  create: (config) => void;
  getRange: () => import('./type').Range[];
  getRangeHtml: (option?: any) => string;
  setCellFormat: (
    row: number,
    column: number,
    type: string,
    config: import('./type').AreaRangeItem,
  ) => void;
  setCellValue: (
    row: number,
    column: number,
    config: import('./type').Cell,
  ) => void;
  getAllSheets: () => Array<sheetData>;
  getSheetData: () => sheetData['data'];
  setColumnWidth: (config: Record<number, number>) => void;
  getRangeAxis: () => Array<Range>;
  transToData: (any) => any;
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
  setRangeShow: (
    range: Array<Range>,
    setting?: {
      show?: boolean;
      order?: number;
      success?: () => void;
    },
  ) => void;
  clearRange: (
    setting?: {
      range?: Array<Range>;
      order?: number;
      success?: () => void;
    },
  ) => void;
  scroll: (setting?: {
    scrollLeft?: number;
    scrollTop?: number;
    targetRow?: number;
    targetColumn?: number;
    success?: () => void;
  }) => void;
}
