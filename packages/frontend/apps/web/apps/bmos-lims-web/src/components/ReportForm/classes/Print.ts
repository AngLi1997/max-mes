import { message } from 'ant-design-vue';
import { CONST_BORDER } from '../const';
import { authority } from '../js/config';
import { isOverlap } from '../js/luckysheet';
import { setBorder, txtRangeToObject } from '../js/luckysheetUtil';
import { singletonDecoration } from '../js/singletonDecoration';
import { AreaRangeItem, PrintArea, Range } from '../type';
import { cloneDeep } from '@bmos/utils';
interface Cache {
  prevLength: number;
  edit_cache: PrintArea;
  redo_cache: PrintArea;
}
type AreaType = Range | string;
class Print {
  Cache: Cache = reactive({
    prevLength: 0,
    edit_cache: [],
    redo_cache: [],
  });
  currentRange: PrintArea = [];
  constructor() {}
  /**
   * 返回上一步，撤销当前设置的一个打印区域
   */
  undoPreviousStep() {
    if (this.Cache.edit_cache.length > 0) {
      let con = luckysheet.getConfig();
      const deleteItem = con.borderInfo.pop();
      const item = this.Cache.edit_cache.pop()!;
      this.Cache.redo_cache.push(item); //撤销操作存入重做缓存
      luckysheet.setConfig(con);
      luckysheet.luckysheetrefreshgrid();
    }
  }

  /**
   * 打印区域设置-重做
   */
  redoPreviousStep() {
    if (this.Cache.redo_cache.length > 0) {
      const item = this.Cache.redo_cache.pop()!;
      this.setRangeBorder(item);
      this.Cache.edit_cache.push(item);
    }
  }
  /**
   * 设置打印区域边框
   * @param {*} arr:Array<Range>
   */
  setRangeBorder(array: Array<Range> | string) {
    let arr: Array<Range | string> = array;
    if (!Array.isArray(array)) {
      arr = [array];
    }
    if (arr[0]) {
      arr = arr.reduce((prev, cur) => {
        let c: Range = cur;
        if (typeof cur === 'string') {
          c = txtRangeToObject(cur);
        }
        prev.push(c);
        return prev;
      }, [] as Array<Range>);
    }

    const borderRange: AreaRangeItem = cloneDeep(CONST_BORDER);
    borderRange.range = arr as Array<Range>;
    setBorder(borderRange);
  }

  /**
   * 保存当前选区为打印区域
   */
  saveRangeToPrintArea() {
    const arr = luckysheet.getRangeAxis();
    if (arr.length === 0) return false;
    if (
      !isOverlap(arr, this.currentRange) ||
      !isOverlap(arr, this.Cache.edit_cache)
    ) {
      message.error(t('该选区有重叠，请重新选择'));
      return false;
    }
    this.setRangeBorder(arr);
    this.Cache.edit_cache.push(arr);
    return true;
  }

  /**
   * 确认修改打印区域
   */
  confirmEditPrintArea = () => {
    let con = luckysheet.getConfig();
    if (con.borderInfo) {
      con.borderInfo.splice(this.Cache.prevLength);
    }
    delete con.authority;
    luckysheet.setConfig(con);

    luckysheet.luckysheetrefreshgrid();
    const cache = this.Cache.edit_cache.reduce((prev, cur) => {
      if (Array.isArray(cur)) {
        prev.push(...cur);
      } else {
        prev.push(cur);
      }
      return prev;
    }, []);
    this.currentRange.push(...cache);
    this.resetPrintAreaCache();
  };

  resetPrintAreaCache = () => {
    this.Cache.edit_cache = [];
    this.Cache.redo_cache = [];
  };

  setPrintArea(rangs: PrintArea) {
    this.currentRange = rangs;
  }

  clearEditCache() {
    this.Cache.edit_cache = [];
  }

  startSetArea() {
    let con = luckysheet.getConfig();
    con.authority = authority;
    luckysheet.setConfig(con);
    luckysheet.luckysheetrefreshgrid();
    this.Cache.prevLength = con.borderInfo ? con.borderInfo.length : 0;

    if (this.currentRange.length > 0) {
      this.currentRange.forEach(item => this.setRangeBorder(item));
    }
  }
  confirmArea() {
    let con = luckysheet.getConfig();
    if (con.borderInfo) {
      con.borderInfo.splice(this.Cache.prevLength);
    }
    delete con.authority;
    luckysheet.setConfig(con);
    // luckysheet.refresh()
    luckysheet.luckysheetrefreshgrid();
    const cache = this.Cache.edit_cache.reduce((prev, cur) => {
      if (Array.isArray(cur)) {
        prev.push(...cur);
      } else {
        prev.push(cur);
      }
      return prev;
    }, [] as PrintArea);
    this.currentRange.push(...cache);
    this.resetPrintAreaCache();
  }
  cancelArea() {
    let con = luckysheet.getConfig();
    if (con.borderInfo) {
      con.borderInfo.splice(this.Cache.prevLength);
    }
    delete con.authority;
    luckysheet.setConfig(con);
    // luckysheet.refresh()
    luckysheet.luckysheetrefreshgrid();
    this.resetPrintAreaCache();
  }

  clear() {
    this.currentRange = [];
    this.resetPrintAreaCache();
  }
}

const initPrint = singletonDecoration<Print>(Print, (instance) => {
  return {
    destroy: () => {
      instance.clear();
    },
  };
});

export type PrintManage = Print;

export default initPrint;
