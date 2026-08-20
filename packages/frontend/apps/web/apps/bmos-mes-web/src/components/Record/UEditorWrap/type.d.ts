export type ModeType = 1 | 0;

export type EditorContent = string | UENode;

export type UENode = typeof UE.uNode;

export interface PageBreak {
  pageHeaderHeight?: number;
  pageFooterHeight?: number;
  color?: string;
  pattern?: number; //0横板 | 1竖版
}

export interface PageBreakLine extends PageBreak {
  pageHeight: number;
}
