import { dragField } from '../const';
import {
  CellRightClickConfig,
  Position,
  SheetData,
  SheetHooks,
  SheetOptions,
  SheetRightClickConfig,
  ShowsheetbarConfig,
  ShowtoolbarConfig,
} from '../type';
import { DragData } from '../type/drag';
import { handleCoord } from './luckysheet';
import initMark from './mark';
import { cellMarkValue } from './markCell';
const { markArrOperation } = initMark();

//匹配是否是拖拽的内容/^#\{(.+?\.){2,}.+?\}$/
const regmv = /^#\{.+\}$/;
const regtv2 = /^#\{.+(\..+){3,4}\}$/;

// 工作表保护设置
export const authority = {
  sheet: 1,
  hintText: '当前为预览模式，不可编辑',
};

// 工具栏配置
const showtoolbarConfig: ShowtoolbarConfig = {
  currencyFormat: false, //货币格式
  percentageFormat: false, //百分比格式
  numberDecrease: false, // '减少小数位数'
  numberIncrease: false, // '增加小数位数
  moreFormats: false, // '更多格式'
  conditionalFormat: false, // '条件格式'
  dataVerification: false, // '数据验证'
  screenshot: false, // '截图'
  print: false, // '打印'
  image: false,
  protection: false, //工作表保护
  function: false, // '公式'
  sortAndFilter: false, // '排序和筛选'
  splitColumn: false, // '分列'
  findAndReplace: false, // '查找替换'
  pivotTable: false, //'数据透视表'
  postil: false, //'批注'
  link: false, // '插入链接'
  chart: false, // '图表'（图标隐藏，但是如果配置了chart插件，右击仍然可以新建图表）
  frozenMode: false,
};

//鼠标右键菜单配置
const cellRightClickConfig: CellRightClickConfig = {
  copy: false, // 复制
  copyAs: false, // 复制为
  paste: false, // 粘贴
  insertRow: false, // 插入行
  insertColumn: false, // 插入列
  deleteRow: false, // 删除选中行
  deleteColumn: false, // 删除选中列
  deleteCell: false, // 删除单元格
  hideRow: false, // 隐藏选中行和显示选中行
  hideColumn: false, // 隐藏选中列和显示选中列
  rowHeight: false, // 行高
  columnWidth: false, // 列宽
  clear: false, // 清除内容
  matrix: false, // 矩阵操作选区
  sort: false, // 排序选区
  filter: false, // 筛选选区
  chart: false, // 图表生成
  image: false, // 插入图片
  link: false, // 插入链接
  data: false, // 数据验证
  cellFormat: false, // 设置单元格格式
};

//底部sheet页按钮配置
const showsheetbarConfig: ShowsheetbarConfig = {
  add: false, //新增sheet
  menu: false, //sheet管理菜单
};

//sheet右键菜单
const sheetRightClickConfig: SheetRightClickConfig = {
  delete: false, // 删除
  copy: false, // 复制
  rename: false, //重命名
  color: false, //更改颜色
  hide: false, //隐藏，取消隐藏
  move: false, //向左移，向右移
};

//luckysheet钩子函数配置
const hooks: SheetHooks = {
  cellDragStop(a, position, f, d, e) {
    const data: DragData & { b_field: boolean } = JSON.parse(
      e.dataTransfer.getData(dragField),
    );

    //是否是对应数据，否则不做任何处理
    if (data.b_field) {
      //设置单元格隐藏数据
      cellMarkValue.setCellMarkValue(position, data);
    }
  },
  workbookCreateAfter() {
    const lucky_dom = document.querySelector('.luckysheet') as HTMLElement;
    if (!lucky_dom) return;
    lucky_dom.style.width = '100%';
    lucky_dom.style.height = '100%';
  },
  //自定义钩子
  beforeUpdated(operate) {
    if (operate.type === 'datachange') {
      const beforeData = operate.data;
      const currentData = operate.curdata;

      const Coord = handleCoord(operate.range); //处理发生变化的单元格坐标
      const adds: Position[] = [];
      const deletes = Coord.reduce((prev, cur) => {
        //删除标记
        const bData = beforeData[cur.r][cur.c]; //历史值
        const cData = currentData[cur.r][cur.c]; //当前值
        // 删除回退
        const is_true =
          bData &&
          bData?.v !== cData?.v &&
          bData.tv &&
          regmv.test(bData?.v) &&
          (!cData.v || !regmv.test(cData?.v)) &&
          regtv2.test(cData?.tv!);
        if (is_true) {
          cData?.tv && delete cData?.tv;
          prev.push({ r: cur.r, c: cur.c });
        }
        //回退新增
        if (regmv.test(cData?.m) && cData.tv) {
          adds.push({ r: cur.r, c: cur.c });
        }
        return prev;
      }, [] as Position[]);

      adds.length > 0 && markArrOperation.addMarkArr(adds);

      deletes.length > 0 && cellMarkValue.deleteCellMarkValue(deletes);
    }
  },
};

const defaultData: SheetData = [
  {
    defaultRowHeight: 50,
    name: '批签发模板',
    data: [],
  },
];

// 工作表配置
const sheetOptions: SheetOptions = {
  container: 'luckysheet',
  lang: 'zh',
  title: 'Bmos-excel',
  userName: 'Bmos-excel',
  userImage: 'logo',
  column: 8,
  row: 10,
  enableAddRow: false,
  enableAddBackTop: false,
  cellRightClickConfig,
  showsheetbarConfig,
  sheetRightClickConfig,
  // plugins: ['chart'],
  data: defaultData,
  gridKey: `${new Date().getTime()}`,
};

/**
 * 初始化配置项
 * @param {*} type
 * @param {*} target
 * @returns
 */
export const initConfig = (type: boolean = true): SheetOptions => {
  if (type) {
    return {
      ...JSON.parse(JSON.stringify(sheetOptions)),
      hook: hooks,
      showtoolbarConfig,
    };
  } else {
    return {
      ...JSON.parse(JSON.stringify(sheetOptions)),
      hook: { workbookCreateAfter: hooks.workbookCreateAfter },
      showtoolbar: false,
      rowHeaderWidth: 0,
      columnHeaderHeight: 0,
      sheetFormulaBar: false,
    };
  }
};
