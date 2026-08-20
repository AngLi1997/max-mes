import { sheetData } from '../luckysheet';

export interface Position {
  r: number;
  c: number;
}

export interface Cell extends Partial<Position> {
  v: string;
  tv?: string;
  m: string;
}

export interface RangeCell {
  row: number;
  column: number;
}

export interface Range {
  row: Array<number>;
  column: Array<number>;
}

export interface Authority {
  sheet: number;
  hintText: string;
}

export interface exportData {
  borderRange: string;
  markData: Array<Position>;
  sheet?: Array<sheetData>;
  sheetData?: sheetData;
}
