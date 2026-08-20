import { commonModal } from '@/config';
import { Modal, message } from 'ant-design-vue';
import initPrint from '../classes/Print';
import { deleteAreaBorderInfo } from '../js/luckysheet';
import { Hooks, Manage, PrintArea } from '../type';
import { exportExcel } from '../utils/export';

interface AREA_STATUS_TYPE {
  SET_PRINT_AREA: boolean;
}

export const useManage: Hooks['useManage'] = (): Manage => {
  let editToolDom: HTMLElement, relaceDiv: HTMLElement; //替换隐藏工具栏
  const { Instance } = initPrint();
  const AREA_STATUS = reactive<AREA_STATUS_TYPE>({
    SET_PRINT_AREA: false,
  });

  const BTN_LOADING = reactive<Record<string, boolean>>({
    save: false, //保存模板
    export: false, //导出
    exportTm: false, //导出模板
    print: false, //打印
  });
  /**
   * 导出excel
   */
  const downloadExcel = () => {
    BTN_LOADING.export = true;
    exportExcel(luckysheet.getAllSheets(), '下载', true, Instance.currentRange)
      .then(res => {
        BTN_LOADING.export = false;
      })
      .catch(err => {
        BTN_LOADING.export = false;
      });
  };

  const getSheetDataFormStream = async (name: string = t('批签发模板')) => {
    return await exportExcel(
      luckysheet.getAllSheets(),
      name,
      false,
      Instance.currentRange,
    );
  };

  /**
   * 清楚当前文档所有打印区域
   */
  const clearAllArea = () => {
    Modal.confirm({
      title: commonModal.title,
      content: t('是否清除所有已设置打印区域') + '?',
      zIndex: 1003,
      onOk() {
        if (
          Instance.currentRange.length === 0 &&
          Instance.Cache.edit_cache.length === 0
        )
          return;
        const ranges: PrintArea = [
          ...Instance.currentRange,
          ...Instance.Cache.edit_cache,
        ].flat();
        const hasRanges = deleteAreaBorderInfo(ranges);
        if (hasRanges.length === 0) Instance.resetPrintAreaCache();
        Instance.setPrintArea([]);
        Instance.clearEditCache();
      },
    });
  };

  /**
   * 初始化打印区域
   * @param {*} arr
   */
  const initPrintArea = (area: PrintArea | string) => {
    if (!area) return;
    if (typeof area === 'string') {
      area = area.split('&&');
    }
    if (area.length === 0) return;
    Instance.setPrintArea(area);
  };

  /**
   * 校验遮罩
   */
  const vertifyDom = () => {
    if (!editToolDom) {
      editToolDom = document.querySelector('#luckysheet-wa-editor')!;
    }
    if (!relaceDiv) {
      const div = document.createElement('div');
      div.style.position = 'absolute';
      div.style.top = '0';
      div.style.left = '0';
      div.style.right = '0';
      div.style.bottom = '0';
      const { backgroundColor } = window.getComputedStyle(editToolDom);
      div.style.backgroundColor = backgroundColor;
      relaceDiv = div;
    }
  };

  /**
   * 隐藏工具栏
   * @param {*} type
   */
  const resetEditTool = (type: boolean) => {
    // edit.insertBefore() = target
    if (type) {
      editToolDom.appendChild(relaceDiv);
    } else {
      editToolDom.removeChild(relaceDiv);
    }
    AREA_STATUS.SET_PRINT_AREA = type;
  };

  const setPrintArea = () => {
    vertifyDom();
    resetEditTool(true);
    Instance.startSetArea();
  };

  const confirmEditPrintArea = () => {
    Instance.confirmArea();
    resetEditTool(false);
  };

  /**
   * 取消编辑打印区域
   */
  const cancelEditPrintArea = () => {
    Instance.cancelArea();
    resetEditTool(false);
  };
  const saveRangeToPrintArea = () => {
    const res = Instance.saveRangeToPrintArea();
    res && message.success(t('保存成功'));
  };
  const undoPreviousStep = () => {
    Instance.undoPreviousStep();
  };
  const redoPreviousStep = () => {
    Instance.redoPreviousStep();
  };

  const getPrintArea = () => {
    return Instance.currentRange;
  };

  return {
    BTN_LOADING,
    downloadExcel,
    initPrintArea,
    setPrintArea,
    AREA_STATUS,
    confirmEditPrintArea,
    cancelEditPrintArea,
    clearAllArea,
    Instance,
    getSheetDataFormStream,
    saveRangeToPrintArea,
    getPrintArea,
    undoPreviousStep,
    redoPreviousStep,
  };
};
