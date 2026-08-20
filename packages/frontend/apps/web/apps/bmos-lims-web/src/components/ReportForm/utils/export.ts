// @ts-nocheck
// import { createCellPos } from './translateNumToLetter'
import Excel from 'exceljs';
import FileSaver from 'file-saver';

import { commonFileUpload } from '@/services';
import { message } from 'ant-design-vue';

/**
 * excel 导出入口
 * @param {*} luckysheet excel对下给你
 * @param {*} value 文件名称
 * @returns
 */
const exportExcel = (
  luckysheet: any,
  value = '批签发文件',
  type = true,
  defineConfig: any = {},
): Promise<boolean | string> => {
  try {
    if (window.luckysheet?.exitEditMode) window.luckysheet.exitEditMode();
  } catch (error) {
    throw { message: 'get data failed' };
  }
  // // 参数为luckysheet.getluckysheetfile()获取的对象
  // // 1.创建工作簿，可以为工作簿添加属性
  const workbook = new Excel.Workbook();
  // // 2.创建表格，第二个参数可以配置创建什么样的工作表

  if (!Array.isArray(luckysheet)) {
    luckysheet = [luckysheet];
  }

  luckysheet.forEach(function (table: any) {
    if (table.data.length === 0) return true;
    const worksheet = workbook.addWorksheet(table.name);
    const merge = (table.config && table.config.merge) || {};
    const borderInfo = (table.config && table.config.borderInfo) || {};
    const rowlen =
      (table.config && table.config.rowlen) || table.defaultRowHeight;
    // 3.设置单元格合并,设置单元格边框,设置单元格样式,设置值
    const newTableData = table.data.map((item) => {
      return item.map((ll) => {
        if (ll && !(ll.ct && ll.ct.s)) {
          ll.m = ll.m, 
          ll.v = ll.m, 
          ll.ct = {
            fa: "@",
            t: "s",
          };
        }
        return ll;
      })
  });
    setStyleAndValue(newTableData, worksheet);
    setMerge(merge, worksheet);
    setBorder(borderInfo, worksheet);
    setRowHeight(rowlen, worksheet, table.data.length);
    setPrintArea(defineConfig.printArea, worksheet);
    return true;
  });

  // 4.写入 buffer
  return new Promise((resolve, reject) => {
    workbook.xlsx
      .writeBuffer()
      .then(data => {
        if (type) {
          const blob = new Blob([data], {
            type: 'application/vnd.ms-excel;charset=utf-8',
          });
          try {
            FileSaver.saveAs(blob, `${value}.xlsx`);
            resolve(true);
          } catch (error) {
            reject(error);
          }
        } else {
          // const type = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'

          const blob = new Blob([data], {
            type: 'ArrayBuffer',
          });
          const formData = new FormData();
          let file = new File([blob], `${value}.xlsx`, {
            type: 'ArrayBuffer',
          });

          formData.append('file', file);

          commonFileUpload(formData)
            .then(res => {
              resolve(res.data);
            })
            .catch(err => {
              reject(err);
            });
        }
      })
      .catch(err => {
        throw err;
      });
  });
  // const buffer =''
  // return buffer;
};

/**
 * 合并单元格
 * @param {*} luckyMerge
 * @param {*} worksheet
 */
const setMerge = (luckyMerge = {}, worksheet: any) => {
  const mergearr = Object.values(luckyMerge);
  mergearr.forEach(function (elem: any) {
    // elem格式：{r: 0, c: 0, rs: 1, cs: 2}
    // 按开始行，开始列，结束行，结束列合并（相当于 K10:M12）
    worksheet.mergeCells(
      elem.r + 1,
      elem.c + 1,
      elem.r + elem.rs,
      elem.c + elem.cs,
    );
  });
};

/**
 * 设置单元格border
 * @param {*} luckyBorderInfo
 * @param {*} worksheet
 * @returns
 */
const setBorder = (luckyBorderInfo: any, worksheet: any) => {
  if (!Array.isArray(luckyBorderInfo)) return;
  // console.log('luckyBorderInfo', luckyBorderInfo)
  luckyBorderInfo.forEach(function (elem) {
    // 现在只兼容到borderType 为range的情况
    // console.log('ele', elem)
    if (elem.rangeType === 'range') {
      let border = borderConvert(elem.borderType, elem.style, elem.color);
      let rang = elem.range[0];
      // console.log('range', rang)
      let row = rang.row;
      let column = rang.column;
      for (let i = row[0] + 1; i < row[1] + 2; i++) {
        for (let y = column[0] + 1; y < column[1] + 2; y++) {
          worksheet.getCell(i, y).border = border;
        }
      }
    }
    if (elem.rangeType === 'cell') {
      // col_index: 2
      // row_index: 1
      // b: {
      //   color: '#d0d4e3'
      //   style: 1
      // }
      const { col_index, row_index } = elem.value;
      const borderData = Object.assign({}, elem.value);
      delete borderData.col_index;
      delete borderData.row_index;
      let border = addborderToCell(borderData, row_index, col_index);
      // console.log('bordre', border, borderData)
      worksheet.getCell(row_index + 1, col_index + 1).border = border;
    }
    // console.log(rang.column_focus + 1, rang.row_focus + 1)
    // worksheet.getCell(rang.row_focus + 1, rang.column_focus + 1).border = border
  });
};

/**
 * 设置单元格样式/值
 * @param {*} cellArr
 * @param {*} worksheet
 * @returns
 */
const setStyleAndValue = (cellArr, worksheet) => {
  if (!Array.isArray(cellArr)) return;
  cellArr.forEach(function (row, rowid) {
    row.every(function (cell, columnid) {
      if (!cell) return true;
      if (!cell.bg) cell.bg = '';
      let fill = fillConvert(cell.bg);
      if (!cell.fc) cell.fc = '';
      let font = fontConvert(
        cell.ff,
        cell.fc,
        cell.bl,
        cell.it,
        cell.fs,
        cell.cl,
        cell.ul,
      );

      let alignment = alignmentConvert(cell.vt, cell.ht, cell.tb, cell.tr);
      let value = '';

      if (cell.f) {
        value = { formula: cell.f, result: cell.v };
      } else if (!cell.v && cell.ct && cell.ct.s) {
        // xls转为xlsx之后，内部存在不同的格式，都会进到富文本里，即值不存在与cell.v，而是存在于cell.ct.s之后
        // value = cell.ct.s[0].v
        cell.ct.s.forEach(arr => {
          value += arr.v;
        });
        // 如果格式不是自动	或者 纯文本，那么就是富文本
      } else {
        value = cell.v;
      }

      //  style 填入到_value中可以实现填充色
      let letter = createCellPos(columnid);
      let target = worksheet.getCell(letter + (rowid + 1));

      for (const key in fill) {
        target.fill = fill;
        break;
      }
      target.font = font;
      target.alignment = alignment;
      target.value = value;

      return true;
    });
  });
};

/**
 * 填充设置
 * @param {*} bg
 * @returns
 */
const fillConvert = bg => {
  if (!bg) {
    return {};
  }
  // const bgc = bg.replace('#', '')
  let fill = {
    type: 'pattern',
    pattern: 'solid',
    fgColor: { argb: bg.replace('#', '') },
  };
  return fill;
};

/**
 * 字体样式属性
 * @param {*} ff
 * @param {*} fc
 * @param {*} bl
 * @param {*} it
 * @param {*} fs
 * @param {*} cl
 * @param {*} ul
 * @returns
 */
const fontConvert = (
  ff = 0,
  fc = '#000000',
  bl = 0,
  it = 0,
  fs = 10,
  cl = 0,
  ul = 0,
) => {
  // luckysheet：ff(样式), fc(颜色), bl(粗体), it(斜体), fs(大小), cl(删除线), ul(下划线)
  const luckyToExcel = {
    0: '微软雅黑',
    1: '宋体（Song）',
    2: '黑体（ST Heiti）',
    3: '楷体（ST Kaiti）',
    4: '仿宋（ST FangSong）',
    5: '新宋体（ST Song）',
    6: '华文新魏',
    7: '华文行楷',
    8: '华文隶书',
    9: 'Arial',
    10: 'Times New Roman ',
    11: 'Tahoma ',
    12: 'Verdana',
    num2bl: function (num) {
      return num === 0 ? false : true;
    },
  };
  // 出现Bug，导入的时候ff为luckyToExcel的val
  let font = {
    name: typeof ff === 'number' ? luckyToExcel[ff] : ff,
    family: 1,
    size: fs,
    color: { argb: fc.replace('#', '') },
    bold: luckyToExcel.num2bl(Number(bl)),
    italic: luckyToExcel.num2bl(Number(it)),
    underline: luckyToExcel.num2bl(Number(ul)),
    strike: luckyToExcel.num2bl(Number(cl)),
  };

  return font;
};

/**
 * 内容位置处理
 * @param {*} vt
 * @param {*} ht
 * @param {*} tb
 * @param {*} tr
 * @returns
 */
const alignmentConvert = (
  vt = 'default',
  ht = 'default',
  tb = 'default',
  tr = 'default',
) => {
  // luckysheet:vt(垂直), ht(水平), tb(换行), tr(旋转)
  const luckyToExcel = {
    vertical: {
      0: 'middle',
      1: 'top',
      2: 'bottom',
      default: 'top',
    },
    horizontal: {
      0: 'center',
      1: 'left',
      2: 'right',
      default: 'left',
    },
    wrapText: {
      0: false,
      1: false,
      2: true,
      default: false,
    },
    textRotation: {
      0: 0,
      1: 45,
      2: -45,
      3: 'vertical',
      4: 90,
      5: -90,
      default: 0,
    },
  };

  let alignment = {
    vertical: luckyToExcel.vertical[vt],
    horizontal: luckyToExcel.horizontal[ht],
    wrapText: luckyToExcel.wrapText[tb],
    textRotation: luckyToExcel.textRotation[tr],
  };
  return alignment;
};

/**
 * 设置边框
 * @param {*} borderType
 * @param {*} style
 * @param {*} color
 * @returns
 */
const borderConvert = (borderType, style = 1, color = '#000') => {
  // 对应luckysheet的config中borderinfo的的参数
  if (!borderType) {
    return {};
  }
  const luckyToExcel = {
    type: {
      'border-all': 'all',
      'border-top': 'top',
      'border-right': 'right',
      'border-bottom': 'bottom',
      'border-left': 'left',
    },
    style: {
      0: 'none',
      1: 'thin',
      2: 'hair',
      3: 'dotted',
      4: 'dashDot', // 'Dashed',
      5: 'dashDot',
      6: 'dashDotDot',
      7: 'double',
      8: 'medium',
      9: 'mediumDashed',
      10: 'mediumDashDot',
      11: 'mediumDashDotDot',
      12: 'slantDashDot',
      13: 'thick',
    },
  };
  let template = {
    style: luckyToExcel.style[style],
    color: { argb: color.replace('#', '') },
  };
  let border = {};
  if (luckyToExcel.type[borderType] === 'all') {
    border['top'] = template;
    border['right'] = template;
    border['bottom'] = template;
    border['left'] = template;
  } else {
    border[luckyToExcel.type[borderType]] = template;
  }
  // console.log('border', border)
  return border;
};

/**
 * 单元格border
 * @param {*} borders
 * @param {*} row_index
 * @param {*} col_index
 * @returns
 */
const addborderToCell = (borders, row_index, col_index) => {
  let border = {};
  const luckyExcel = {
    type: {
      l: 'left',
      r: 'right',
      b: 'bottom',
      t: 'top',
    },
    style: {
      0: 'none',
      1: 'thin',
      2: 'hair',
      3: 'dotted',
      4: 'dashDot', // 'Dashed',
      5: 'dashDot',
      6: 'dashDotDot',
      7: 'double',
      8: 'medium',
      9: 'mediumDashed',
      10: 'mediumDashDot',
      11: 'mediumDashDotDot',
      12: 'slantDashDot',
      13: 'thick',
    },
  };
  // console.log('borders', borders)
  for (const bor in borders) {
    // console.log(bor)
    if (borders[bor].color.indexOf('rgb') === -1) {
      border[luckyExcel.type[bor]] = {
        style: luckyExcel.style[borders[bor].style],
        color: { argb: borders[bor].color.replace('#', '') },
      };
    } else {
      border[luckyExcel.type[bor]] = {
        style: luckyExcel.style[borders[bor].style],
        color: { argb: borders[bor].color },
      };
    }
  }

  return border;
};

const createCellPos = n => {
  let ordA = 'A'.charCodeAt(0);

  let ordZ = 'Z'.charCodeAt(0);
  let len = ordZ - ordA + 1;
  let s = '';
  while (n >= 0) {
    s = String.fromCharCode((n % len) + ordA) + s;

    n = Math.floor(n / len) - 1;
  }
  return s;
};

/**
 * 设置行高
 * @param {*} rowlen luckysheet行高属性配置
 * @param {*} worksheet
 */
const setRowHeight = (rowlen, worksheet, num) => {
  if (typeof rowlen !== 'object') {
    for (let key = 0; key < num; key++) {
      const row = worksheet.getRow(key * 1 + 1);
      row.height = rowlen;
    }
  } else {
    for (const key in rowlen) {
      if (Object.hasOwnProperty.call(rowlen, key)) {
        const row = worksheet.getRow(key * 1 + 1);

        row.height = rowlen[key];
      }
    }
  }
};

/**
 * 设置打印区域
 * @param {*} area 在线配置的打印区域
 * @param {*} worksheet 工作表对象
 */
const setPrintArea = (area, worksheet) => {
  if (!area) return;
  if (!Array.isArray(area)) {
    // // 设置工作表的打印区域
    worksheet.pageSetup.printArea = area;
    return;
  }
  // if(area.length===0){
  //   return
  // }
  const areas = area.join('&&');
  // // 通过使用 `&&` 分隔打印区域来设置多个打印区域
  worksheet.pageSetup.printArea = areas;
  // worksheet.pageSetup.printArea = areas;
  // 使用 A4 横向的页面设置创建新工作表
  // const worksheet =  workbook.addWorksheet('sheet', {
  //   pageSetup:{paperSize: 9, orientation:'landscape'}
  // });

  // // 使用适合页面的pageSetup设置创建一个新的工作表编写器
  // const worksheetWriter = workbookWriter.addWorksheet('sheet', {
  //   pageSetup:{fitToPage: true, fitToHeight: 5, fitToWidth: 7}
  // });

  // // 之后调整页面设置配置
  // worksheet.pageSetup.margins = {
  //   left: 0.7, right: 0.7,
  //   top: 0.75, bottom: 0.75,
  //   header: 0.3, footer: 0.3
  // };

  // // 在每个打印页面上重复特定的行
  // worksheet.pageSetup.printTitlesRow = '1:3';

  // // 在每个打印页面上重复特定列
  // worksheet.pageSetup.printTitlesColumn = 'A:C';
};

export { exportExcel };
