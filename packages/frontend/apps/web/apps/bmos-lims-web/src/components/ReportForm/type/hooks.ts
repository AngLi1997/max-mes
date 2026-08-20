import { PrintManage } from '../classes/Print';
import { SheetData, SheetItemData, SheetOptions } from './config';
import { ReportPropsType } from './report';
import { Cell, Position, Range, exportData } from './sheet';
export type OperateType = 'datachange';

export interface Operate {
  data: SheetItemData['data'];
  curdata: SheetItemData['data'];
  range: Range[];
  type: OperateType;
}

export interface SheetHooks {
  cellDragStop?: (a: any, position: Position, f: any, d: any, e: any) => void;
  workbookCreateAfter?: () => void;
  rangeSelect?: () => void;
  rangePullAfter?: () => void;
  updated?: () => void;
  beforeUpdated?: (operate: Operate) => void;
}

export type PrintArea = Array<string>;

export interface Manage {
  BTN_LOADING: Record<string, boolean>;
  downloadExcel: () => void;
  initPrintArea: (arr: PrintArea) => void;
  setPrintArea: () => void;
  AREA_STATUS: Record<string, boolean>;
  confirmEditPrintArea: () => void;
  cancelEditPrintArea: () => void;
  clearAllArea: () => void;
  saveRangeToPrintArea?: () => void;
  Instance: PrintManage;
  getSheetDataFormStream: (name:string) => Promise<string | boolean>;
  getPrintArea:()=>PrintArea
  undoPreviousStep:()=>void
  redoPreviousStep:()=>void
}

export interface Sheet {
  sheetDestory: () => void;
  getSheetData: () => exportData;
  loadDataById: (id: KEY) => void;
  setColumnWidth: (width: number | Record<string, number>) => void;
  initFile: (data: SheetData, config: SheetOptions) => void;
  luckysheetDestory: () => void;
  init: (data: exportData) => void;
  loadSheetData: () => void;
  STATUS: {
    export: boolean;
  };
  loadFileToExcel:(files: File[] | File)=>void
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

export interface Print {
  currentRange: Ref<Array<Range>>;
}

export interface Hooks {
  useManage: () => Manage;
  usePrint: () => Print;
  useSheet: (manage: Manage, props: ReportPropsType) => Sheet;
}
