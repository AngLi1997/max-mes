import { fileDownload } from '@/services';
import { debounce } from '@bmos/utils';
import { message } from 'ant-design-vue';
import initPrint from '../classes/Print';
import { authority, initConfig } from '../js/config';
import { fileToExcel } from '../js/luckyExcel';
import initMark from '../js/mark';
import { Hooks, PrintArea, ReportPropsType } from '../type';
import { handleFileName } from '../utils';
import { getConfig } from '../utils/file';
import { getData } from '../utils/sheetData';

export const useSheet: Hooks['useSheet'] = (
  Manage: any,
  props: ReportPropsType,
) => {
  const { Instance, destroy: printDestroy } = initPrint();
  const { initPrintArea } = Manage;
  const { destroy, markArrOperation } = initMark();
  const { request } = props;

  const STATUS = reactive({
    export: true,
    LOADING: false,
  });

  const luckysheetDestory = () => {
    window.luckysheet.destroy();
  };

  /**
   * 加载报表 处于由luckyexcel转化而来得到数据
   * @param {*} data
   * @param {*} defaultConfig
   */
  const initFile = (
    data: Array<any> | Record<string, any>,
    defaultConfig: Record<string, any> = {},
  ) => {
    const sheetOptions = initConfig(props.edit);
    // config = JSON.parse(config)
    if (!props.edit) {
      if (data instanceof Array) {
        data.forEach(item => {
          if (item.config) {
            item.config.authority = authority;
          } else {
            item['config'] = {
              authority,
            };
          }
        });
      } else {
        if (data.config) {
          data.config.authority = authority;
        } else {
          data['config'] = {
            authority,
          };
        }
      }
    }
    let printArea = defaultConfig;
    if (defaultConfig instanceof Object) {
      printArea = defaultConfig.printArea || [];
    }

    initPrintArea(printArea as PrintArea);

    luckysheetDestory();
    window.luckysheet.create({
      ...sheetOptions,
      container: 'luckysheet', //luckysheet is the container id
      showinfobar: false,
      data: data,
    });
  };

  const loadFileToExcel = (files: File[] | File) => {
    fileToExcel(files, async data => {
      const defaultConfig = await getConfig(files);
      initFile(data, defaultConfig);
    });
  };

  /**
   * 加载报表-处理后端返回的数据
   * @param {Object} data
   */
  const init = (data: any) => {
    const sheetOptions = initConfig(props.edit);
    if (!data) {
      throw new Error('data is must be need');
    }
    const { config, sheetData, borderRange, markData } = data;

    const range = borderRange
      ? JSON.parse(borderRange || props.defaultBorderRange)
      : [];

    // config = JSON.parse(config)
    if (!props.edit) {
      if (config.config) {
        config.config.authority = authority;
      } else {
        config['config'] = {
          authority,
        };
      }
    }
    const sheet = {
      ...config,
      data: sheetData,
      name: config?.name,
    };
    luckysheetDestory();
    window.luckysheet.create({
      ...sheetOptions,
      container: 'luckysheet', //luckysheet is the container id
      showinfobar: false,
      data: [sheet],
    });

    if (markData && markData.length !== 0) {
      markArrOperation.addMarkArr(markData);
    }
    initPrintArea(range);
    STATUS.export = markData.length === 0;
  };

  const setColumnWidth = (width: number | Record<number, number>) => {
    let data: Record<number, number> = {};
    if (typeof width === 'number') {
      const sheetData = luckysheet.getSheetData();
      const cols = sheetData[0].length;
      for (let index = 0; index < cols; index++) {
        data[index] = 100;
      }
      luckysheet.setColumnWidth(data);
    } else {
      data = width;
      luckysheet.setColumnWidth(data);
    }
  };
  const templateSheet = (id: KEY) => {
    return new Promise<void>((resolve, reject) => {});
  };

  const clearSheet = () => {
    let sheetOptions = initConfig(props.edit);
    luckysheetDestory();
    window.luckysheet.create({
      ...sheetOptions,
      container: 'luckysheet', //luckysheet is the container id
      showinfobar: false,
    });
  };

  const loadDataById = async (id: KEY) => {
    // const sheetOptions = initConfig(props.edit)
    try {
      const res = await getData(id);
      if (!res) {
        clearSheet();
        return;
      }
      init(res);
    } catch (error: any) {
      // console.log(error, error.message);
      // error.message && message.error(error.message);
    } finally {
      STATUS.LOADING = false;
    }

    // luckysheet.create({
    //     ...sheetOptions,
    //     // data:[{
    //     //   defaultRowHeight:30,
    //     //   config:{
    //     //     authority:authority
    //     //   }
    //     // }]
    // });
  };

  /**
   * 获取所有数据
   */
  const getSheetData = () => {
    const sheet = luckysheet.getAllSheets();
    const data = {
      borderRange: JSON.stringify(Instance.currentRange),
      markData: [...markArrOperation.values()],
      sheet: sheet,
    };
    return data;
  };

  const getDataByRequest = async () => {
    try {
      const res = await props.request?.();
      if (!res) {
        return clearSheet();
      }
      init(res);
    } catch (error) {
    } finally {
      STATUS.LOADING = false;
    }
  };

  const loadSheetData = debounce(() => {
    STATUS.LOADING = true;
    if (props.id) {
      return loadDataById(props.id);
    } else if (props.url) {
      return loadDataByUrl(props.url);
    } else if (props.request) {
      return getDataByRequest();
    }
    STATUS.LOADING = false;
  });

  const loadDataByUrl = async (url: string) => {
    try {
      const path = handleFileName(url);
      const res = await fileDownload(path);
      fileToExcel(res, async data => {
        const config = await getConfig(res);
        initFile(data, config);
      });
    } catch (error: any) {
      // error.message && message.error(error.message);
    } finally {
      STATUS.LOADING = false;
    }
  };

  const find = (
    content: string,
    setting?: {
      isRegularExpression?: boolean;
      isWholeWord?: boolean;
      isCaseSensitive?: boolean;
      order?: number;
      type?: string;
    },
  ) => {
    try {
      const result = luckysheet.find(content, {
        ...setting,
      });
      return result;
    } catch (error) {
      return [];
    }
    
  };

  watch(
    () => [props.id, props.request, props.url],
    () => {
      loadSheetData();
    },
    { immediate: true },
  );

  onUnmounted(() => {
    destroy();
    printDestroy();
    window.luckysheet.destroy();
  });

  return {
    sheetDestory: luckysheetDestory,
    getSheetData,
    loadDataById,
    setColumnWidth,
    initFile,
    init,
    luckysheetDestory,
    loadSheetData,
    STATUS,
    loadFileToExcel,
    loadDataByUrl,
    find,
  };
};
