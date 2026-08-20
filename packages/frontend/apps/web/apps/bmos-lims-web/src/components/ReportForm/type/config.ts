import { Hooks } from './hooks';
import { Cell } from './sheet';

export type ToolBarItem =
  | 'currencyFormat' //货币格式
  | 'percentageFormat' //百分比格式
  | 'numberDecrease' // '减少小数位数'
  | 'numberIncrease' // '增加小数位数
  | 'moreFormats' // '更多格式'
  | 'conditionalFormat' // '条件格式'
  | 'dataVerification' // '数据验证'
  | 'screenshot' // '截图'
  | 'print' // '打印'
  | 'image'
  | 'protection' //工作表保护
  | 'function' // '公式'
  | 'sortAndFilter' // '排序和筛选'
  | 'splitColumn' // '分列'
  | 'findAndReplace' // '查找替换'
  | 'pivotTable' //'数据透视表'
  | 'postil' //'批注'
  | 'link' // '插入链接'
  | 'chart' // '图表'（图标隐藏，但是如果配置了chart插件，右击仍然可以新建图表）
  | 'frozenMode';

export type ShowtoolbarConfig = {
  [key in ToolBarItem]?: boolean;
};

export interface CellRightClickConfig {
  copy: boolean; // 复制
  copyAs: boolean; // 复制为
  paste: boolean; // 粘贴
  insertRow: boolean; // 插入行
  insertColumn: boolean; // 插入列
  deleteRow: boolean; // 删除选中行
  deleteColumn: boolean; // 删除选中列
  deleteCell: boolean; // 删除单元格
  hideRow: boolean; // 隐藏选中行和显示选中行
  hideColumn: boolean; // 隐藏选中列和显示选中列
  rowHeight: boolean; // 行高
  columnWidth: boolean; // 列宽
  clear: boolean; // 清除内容
  matrix: boolean; // 矩阵操作选区
  sort: boolean; // 排序选区
  filter: boolean; // 筛选选区
  chart: boolean; // 图表生成
  image: boolean; // 插入图片
  link: boolean; // 插入链接
  data: boolean; // 数据验证
  cellFormat: boolean; // 设置单元格格式
}

export interface ShowsheetbarConfig {
  add: boolean; //新增sheet
  menu: boolean; //sheet管理菜单
}

export interface SheetRightClickConfig {
  delete: boolean; // 删除
  copy: boolean; // 复制
  rename: boolean; //重命名
  color: boolean; //更改颜色
  hide: boolean; //隐藏，取消隐藏
  move: boolean; //向左移，向右移
}

export interface SheetItemData {
  defaultRowHeight: number;
  name: string;
  data: Array<Array<Cell>>;
}

export type SheetData = Array<SheetItemData>;

export interface SheetOptions {
  container: string;
  lang?: string;
  title?: string;
  userName?: string;
  userImage?: string;
  column?: number;
  row?: number;
  enableAddRow?: boolean;
  enableAddBackTop?: boolean;
  cellRightClickConfig?: CellRightClickConfig;
  showsheetbarConfig?: ShowsheetbarConfig;
  sheetRightClickConfig?: SheetRightClickConfig;
  // plugins: ['chart'],
  data: Array<SheetItemData>;
  gridKey?: string;
  showtoolbar?: boolean;
  rowHeaderWidth?: number;
  columnHeaderHeight?: number;
  sheetFormulaBar?: boolean;
  hook?: Hooks;
}
