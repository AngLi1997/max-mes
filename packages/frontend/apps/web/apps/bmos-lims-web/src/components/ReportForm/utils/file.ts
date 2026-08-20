import Excel from 'exceljs';
/**
 * 获取打印区域
 * @param {*} data
 * @param {Object} fieldObj 需要返回哪些字段数据
 * @returns
 */
export const getConfig = async (data: any) => {
  let config: any = {};
  const workbook = new Excel.Workbook();
  await workbook.xlsx.load(data);
  workbook.eachSheet(worksheetItem => {
    if (
      worksheetItem.pageSetup?.printArea &&
      worksheetItem.pageSetup?.printArea?.length > 3
    ) {
      config.printArea = worksheetItem.pageSetup.printArea;
    }
  });
  return config;
};
