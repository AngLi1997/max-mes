import LuckyExcel from 'luckyexcel';
import { CONST_BORDER } from '../const';
import {
  AreaRangeItem,
  exportData,
  Func,
  Position,
  PrintArea,
  Range,
  SheetsJson,
} from '../type';
import { initConfig } from './config';
import initMark from './mark';
import { borderType } from './Enum';
import { txtRangeToObject } from './luckysheetUtil';
import { batchPrint, getAllSheetRange, printFunc } from '../utils/print';
//拖拽各自记录类

const { markArrOperation, destroy } = initMark();
/**
 * 调用setCellValue设置单元格自定义隐藏值
 * @param {Array | Object} position
 * @param {String} check 子表单数据渲染方向
 */
export const setCellMarkValue: Func['setCellMarkValue'] = (position, obj) => {
  // update_r = position.r;
  // update_c = position.c;

  luckysheet.setCellValue(position.r, position.c, {
    m: obj.label,
    v: obj.label,
    tv: obj.value,
  });
  //每设置一个便记录一次
  markArrOperation.addMarkArr(position);
};

/**
 * 处理坐标区间
 * @param {*} range
 * @returns
 */
export const handleCoord: Func['handleCoord'] = range => {
  const rangeArr: Position[] = [];
  range.forEach(item => {
    rangeArr.push(...getCoord(item.row, item.column));
  });
  return rangeArr;
};

/**
 * 获取区间内单元格坐标
 * @param {*} rows
 * @param {*} columns
 * @returns
 */
const getCoord: Func['getCoord'] = (rows, columns) => {
  const coord = [];
  const rowSet = new Set(rows);
  const columnSet = new Set(columns);

  rows = [...rowSet];
  
  columns = [...columnSet];

  if (rowSet.size === 1) {
    if (columnSet.size === 1) {
      coord.push({ r: rows[0], c: columns[0] });
    } else {
      for (let index = columns[0]; index <= columns[1]; index++) {
        coord.push({ r: rows[0], c: index });
      }
    }
  } else {
    if (columnSet.size === 1) {
      for (let r = rows[0]; r <= rows[1]; r++) {
        coord.push({ r, c: columns[0] });
      }
    } else {
      for (let c = columns[0]; c <= columns[1]; c++) {
        for (let r = rows[0]; r <= rows[0]; r++) {
          coord.push({ r, c });
        }
      }
    }
  }

  return coord;
};

/**
 * 根据坐标生成坐标区间
 * @param {*} arr
 * @returns 坐标区间数组
 */
export const setCoordArr: Func['setCoordArr'] = arr => {
  if (arr.length === 0) return [];
  const column = new Set<number>();
  const row = new Set<number>();
  arr.forEach(item => {
    column.add(item.c);
    row.add(item.r);
  });
  let rows: Range['row'] = [...row].sort((a, b) => a - b);
  let columns: Range['column'] = [...column].sort((a, b) => a - b);
  if (rows.length === 1) {
    const tt = rows[0];
    rows.push(tt);
  } else {
    const tt = rows[0];
    const tt1 = rows.pop()!;
    rows = [tt, tt1];
  }
  if (columns.length === 1) {
    const tt = columns[0];
    columns.push(tt);
  } else {
    const tt = columns[0];
    const tt1 = columns.pop()!;
    columns = [tt, tt1];
  }
  return [
    {
      row: rows,
      column: columns,
    },
  ];
};

/**
 * 保存当前模板
 * @param {*} id 模板id
 */
export const saveTemplate: Func['saveTemplate'] = printArea => {
  luckysheet.exitEditMode();
  const excel = JSON.parse(JSON.stringify(luckysheet.getSheet())); //防止修改原配置文件，导致出错
  delete excel.celldata;
  delete excel.images;
  excel.luckysheet_selection_range = [];
  const markData = [...markArrOperation.values()];
  const data: exportData & { config?: any } = {
    borderRange: JSON.stringify(printArea),
    sheetData: excel.data,
    // name:currentFileName,
    markData,
  };

  delete excel.data, (data['config'] = excel);
  return data;
};

/**
 * 上传excel
 */
export const loadExcel: Func['loadExcel'] = (evt, type = true) => {
  const files = evt.target?.files;
  // fileToExcel(files,true,'luckysheet')
  if (files == null || files.length == 0) {
    alert('No files wait for import');
    return;
  }
  // ipe = files[0].lastModified

  let name = files[0].name;

  let suffixArr = name.split('.'),
    suffix = suffixArr[suffixArr.length - 1];

  if (suffix != 'xlsx') {
    alert('文件类型错误!');
    return;
  }

  LuckyExcel.transformExcelToLucky(
    files[0],
    function (exportJson: SheetsJson, luckysheetfile: File) {
      if (exportJson.sheets == null || exportJson.sheets.length == 0) {
        alert(
          'Failed to read the content of the excel file, currently does not support xls files!',
        );
        return;
      }

      // jsonData.value = exportJson;
      window.luckysheet.destroy();
      exportJson.sheets[0].name = exportJson.info.name;
      // setFileName(exportJson.info.name)
      const findName = suffixArr.slice(0, suffixArr.length - 1).join('');
      const prev = localStorage.getItem(findName);
      let dd: Array<Range> | null = null;
      if (prev) {
        dd = JSON.parse(prev); //查询是否有设置打印区域
      }

      if (dd) {
        const rangeArr: Array<AreaRangeItem> = [];
        dd.forEach(item => {
          const rangeItem: AreaRangeItem = CONST_BORDER;
          rangeItem.range?.push(item);
          rangeArr.push(rangeItem);
        });
        //   allRange.value = rangeArr
        exportJson.sheets[0].config.borderInfo.push(...rangeArr); //加入边框配置属性中
      }
      const sheetOptions = initConfig(type);
      destroy();
      window.luckysheet.create({
        ...sheetOptions,
        container: 'luckysheet', //luckysheet is the container id
        showinfobar: false,
        data: exportJson.sheets,
        title: exportJson.info.name,
        userInfo: exportJson.info.name.creator,
      });
    },
  );
};

/**
 * 打印报表
 */
export const printTemplate: Func['printTemplate'] = (area = []) => {
  return new Promise((resolve, reject) => {
    let range = area;
    if (area?.length === 0 || !area) {
      // range = luckysheet.getRange();
      if (!range || range.length === 0) {
        const newAllSheetArea = getAllSheetRange();
        const h = luckysheet.getRangeHtml({
          range: newAllSheetArea,
        });
        const div = `<div style="page-break-after:always;">${h}</div>`
        printFunc(div)
        resolve();
        return;
      }
      reject();
      return;
      // if (!is_cell(range)) {
      //   message.error(t('当前文件没有设置打印区域,请选择需要打印的区域'));
      //   reject();
      //   return;
      // }
      // const h = luckysheet.getRangeHtml();

      // const div = `<div style="page-break-after:always;">${h}</div>`
      // const op = window.open() as Window;
      // op.document.body.innerHTML = div
      // htmlPrint(h)
      // resolve();
      // return;
    }
    // htmlPrint(range)
    const ranges = range.flat();

    let func = batchPrint(ranges.length)

    ranges.forEach(item=>{

      const image = luckysheet.getRangeHtml({range:item})
      // @ts-ignore
      func = func(image)
    })
    setTimeout(()=>{
      resolve()
    },100)
  });
};

/**
 * 判断打印区域是否有重叠
 * @param {*} target
 * @param {*} arr
 * @returns
 */
export const isOverlap: Func['isOverlap'] = (target, array = []) => {
  let targets: any = target;
  if (!Array.isArray(target)) {
    targets = [target];
  }
  const arr = array.flat();

  return arr.every(item => {
    let par = item;
    if (typeof item === 'string') {
      par = txtRangeToObject(item);
    }

    // @ts-ignore
    const cl = par.column;
    // @ts-ignore
    const rr = par.row;
    return targets.every((t: Range) => {
      let tar = t;
      if (typeof t === 'string') {
        tar = txtRangeToObject(t);
      }

      // 判断是否有交叉
      // 通过判断四条边的位置，以此判断是否有交叉
      if (
        cl[0] > tar.column[1] ||
        rr[0] > tar.row[1] ||
        cl[1] < tar.column[0] ||
        rr[1] < tar.row[0]
      ) {
        return true;
      }
      return false;
    });
  });
};

export const deleteAreaBorderInfo = (arr: PrintArea): PrintArea => {
  let ranges = arr;
  let con = luckysheet.getConfig();

  const len = con.borderInfo.length - 1;
  for (let index = len; index >= 0; index--) {
    const cur = con.borderInfo[index];

    if (
      cur &&
      cur.borderType === 'border-outside' &&
      cur.rangeType === 'range'
    ) {
      const i = ranges.findIndex(item => {
        const p = txtRangeToObject(item as string);

        const rStr = p.row.toString();
        const cStr = p.column.toString();
        return cur.range.some((it: Range) => {
          const rowStr = it.row.toString();
          const colStr = it.column.toString();

          if (rStr === rowStr && cStr === colStr) {
            return true;
          }
          return false;
        });
      });

      if (i > -1) {
        con.borderInfo.splice(index, 1);
        ranges.splice(i, 1);
        index++;
      }
    }

    if (ranges.length === 0) {
      luckysheet.setConfig(con);
      luckysheet.luckysheetrefreshgrid();
      break;
    }
  }
  return ranges;
};

/**
 * 设置四周单元格边框
 * @param {*} range
 */
const setPrintAreaAroundBorder = (
  range: Range,
  color = '#ff0000',
  style = '1',
) => {
  const row = range.row;
  const column = range.column;
  let bd = 0;
  //设置顶部/底部
  for (let t = 0; t < 2; t++) {
    for (let i = column[0]; i <= column[1]; i++) {
      setPrintAreaCellBorder(
        {
          row: row[t],
          column: i,
        },
        borderType[bd],
        color,
        style,
      );
    }
    bd++;
  }
  //设置左边/右边
  for (let t = 0; t < 2; t++) {
    bd++;
    for (let i = column[0]; i <= column[1]; i++) {
      setPrintAreaCellBorder(
        {
          row: i,
          column: column[t],
        },
        borderType[bd],
        color,
        style,
      );
    }
  }
};

/**
 * 设置单元格边框代表打印区域
 * @param {*} target
 * @param {*} borderType
 * @param {*} color
 */
const setPrintAreaCellBorder: Func['setPrintAreaCellBorder'] = (
  target,
  borderType,
  color,
  style = '1',
) => {
  window.luckysheet.setCellFormat(target.row, target.column, 'bd', {
    borderType: borderType,
    style: style,
    color: color,
  });
};

/**
 * 判断选区是否是单个单元格
 * @param {*} arr
 */
const is_cell: Func['is_cell'] = arr => {
  if (arr.length > 1) return true;
  return arr.every(item => {
    const row = item.row;
    const column = item.column;
    return !(row[0] === row[1] && column[0] === column[1]);
  });
};
