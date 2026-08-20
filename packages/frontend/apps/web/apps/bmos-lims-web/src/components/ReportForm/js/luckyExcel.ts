import LuckyExcel from 'luckyexcel';
import { FileArg, FileToExcel, SheetsJson } from '../type';
/**
 * File对象转luckysheet对象
 * @param {*} files
 * @param {*} type
 * @param {*} container
 * @returns
 */
export const fileToExcel: FileToExcel = async (
  files: File[] | File,
  callback,
) => {
  if (files == null || files.length == 0) {
    alert('No files wait for import');
    return;
  }
  // // ipe = files[0].lastModified

  // let name = files[0].name;

  // let suffixArr = name.split("."),
  // suffix = suffixArr[suffixArr.length - 1];

  // if (suffix != "xlsx") {
  //   alert("文件类型错误!");
  //   return;
  // }
  // window.luckysheet.destroy();
  // console.log(files[0],'files[0]files[0]files[0]');
  let target: FileArg = files;
  if (Array.isArray(files)) {
    files.some(item => {
      target = item;
      return item;
    });
  }
  LuckyExcel.transformExcelToLucky(
    target,
    function (exportJson: SheetsJson, luckysheetfile?: File) {
      if (exportJson.sheets == null || exportJson.sheets.length == 0) {
        alert(
          'Failed to read the content of the excel file, currently does not support xls files!',
        );
        return;
      }
      window.luckysheet.destroy();
      callback && callback(exportJson.sheets);
    },
  );
};

