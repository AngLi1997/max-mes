import { dragField } from '../const';
import { AreaRangeItem, Range, sheetUtils } from '../type';
import { DragData } from '../type/drag';
/**
 * "A1:B1" 转换成{Row Column} 形式
 * @param {*} txtRange
 * @returns
 */
export const txtRangeToObject: sheetUtils['txtRangeToObject'] = (
  txtRange: string,
): Range => {
  if (typeof txtRange !== 'string') return txtRange;
  return window.luckysheet.getRangeByTxt(txtRange);
};

/**
 * {Row Column} 转换成 "A1:B1"形式
 * @param {*} objectRange
 * @returns
 */
export const objectRangeToTxt: sheetUtils['objectRangeToTxt'] = (
  objectRange: Range,
): string => {
  if (typeof objectRange !== 'object') return objectRange;
  return window.luckysheet.getTxtByRange(objectRange);
};

export const printAreaToRange = (area: Array<Range>) => {
  console.log(area, 'printAreaToRangeprintAreaToRange');
};

export const setBorder: sheetUtils['setBorder'] = (info: AreaRangeItem) => {
  let con = luckysheet.getConfig();
  if (con.borderInfo) {
    con.borderInfo.push(info);
  } else {
    con.borderInfo = [info];
  }
  luckysheet.setConfig(con);
  // luckysheet.refresh()
  luckysheet.luckysheetrefreshgrid();
};

export const dragToCell: sheetUtils['dragToCell'] = (
  ev: DragEvent,
  dragData: DragData,
) => {
  document.body.style.cursor = 'move';
  const target = ev.target as HTMLElement;
  target.style.cursor = 'move';
  const data = {
    b_field: true,
    ...dragData,
    label: `#{${dragData.label}}`
  };

  //向拖拽实例对象上添加数据
  ev.dataTransfer?.setData(dragField, JSON.stringify(data));
};
