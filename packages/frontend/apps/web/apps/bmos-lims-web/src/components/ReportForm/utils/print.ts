// @ts-nocheck
import { message } from 'ant-design-vue';
import Excel from 'exceljs';
/**
 * 批量导出
 * @param {*} data fileDownload返回的buffer数据
 * @param {String} type 文件类型
 */
export const batchExport = (data, type) => {
  const reader = new FileReader();
  const blob = new Blob([data], { type });
  reader.readAsDataURL(blob);
  reader.addEventListener(
    'load',
    function () {
      let a = document.createElement('a');
      a.style.display = 'none';
      a.href = reader.result;
      a.click();
      document.body.appendChild(a);
    },
    false,
  );
};

/**
 * 获取打印区域
 * @param {*} data
 * @param {Object} fieldObj 需要返回哪些字段数据
 * @returns
 */
export const getConfig = async data => {
  let config = {};
  const wookbook = new Excel.Workbook();
  await wookbook.xlsx.load(data);
  wookbook.eachSheet(wooksheet => {
    if (wooksheet.pageSetup.printArea?.length > 3) {
      config.printArea = wooksheet.pageSetup.printArea;
    }
  });
  return config;
};

/**
 * 利用柯里化，解决打印函数个数未知
 * @param {*} num 预计接收的参数个数
 * @returns
 */
export const batchPrint = num => {
  return function func(...arg1) {
    if (arg1.length >= num) {
      printFunc(...arg1);
    } else {
      return function (...arg2) {
        return func.apply(null, [...arg1, ...arg2]);
      };
    }
  };
};

/**
 * 批量打印函数
 * @returns
 */
export const printFunc = async (...arg) => {
  // if(canvasArr.length===0)return
  const htmls = [].slice.call(arg);

  // const pdf = new jsPDF('', 'pt', 'a4')
  // canvasArr.forEach((canvas,index) => {
  //   canvasToPdf(pdf,canvas,index<canvasArr.length-1)
  // });
  let html = '';
  const dpi = getDPI();
  const errorRange = 0.25;
  const dpiheight = (29.7 / 2.54) * dpi;
  const dpiMargin = ((2 + errorRange) / 2.54) * dpi;
  if (!!window.ActiveXObject || 'ActiveXObject' in window) {
    remove_ie_header_and_footer();
  }
  for (let index = 0; index < htmls.length; index++) {
    // const div = `<div style="height:${dpiheight - dpiMargin * 2}px;page-break-after:always">${images[index]}</div>`
    const div = `<div style="min-height:${
      dpiheight - dpiMargin * 2
    }px;page-break-after:always;">${htmls[index]}</div>`;
    html += div;
    // await canvasToPdf(pdf,images[index],index<images.length-1)
  }
  htmlPrint(html);
  // pdf.autoPrint();
  // pdf.output('dataurlnewwindow');
};

/**
 * 打印样式设置
 */
const printStyle = `<style media="print">
  @page {
    size: A4 portrait;
    margin: 2cm 1.5cm 2cm;
  }
  table {
    border-collapse:collapse;
  }
</style>`;

/**
 * 通过iframe打印页面
 * @param {*} html
 * @returns
 */
export const htmlPrint = html => {
  if (!html) return message.error(t('没有需要打印得内容'));

  // let iframe = document.getElementById("print-iframe");
  let iframe;
  let doc = null;

  // if (!iframe) {
  iframe = document.createElement('iframe');

  iframe.setAttribute('id', 'print-iframe');

  iframe.setAttribute('style', 'display:none;');

  document.body.appendChild(iframe);
  doc = iframe.contentWindow.document;
  //这里可以自定义样式
  doc.write(printStyle); //解决出现页眉页脚和路径的问题
  // }
  doc = iframe.contentWindow.document;
  doc.write('<div>' + html + '</div>');
  doc.close();
  iframe.contentWindow.focus();
  iframe.contentWindow.print();
};

/**
 * 获取屏幕分辨率
 * @returns
 */
export const getDPI = () => {
  const div = document.createElement('div');
  div.style.cssText =
    'height: 1in; left: -100%; position: absolute; top: -100%; width: 1in;';
  window.document.body.appendChild(div);

  const devicePixelRatio = window.devicePixelRatio || 1,
    dpi = div.offsetWidth * devicePixelRatio;
  window.document.body.removeChild(div);
  return dpi;
};

/**
 * ie去掉页眉/页脚
 */
const remove_ie_header_and_footer = () => {
  let hkey_path =
    'HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\PageSetup\\';
  try {
    const RegWsh = new ActiveXObject('WScript.Shell');
    RegWsh.RegWrite(hkey_path + 'header', '');
    RegWsh.RegWrite(hkey_path + 'footer', '');
  } catch (e) {}
};

// 获取整个表格的全部范围
export const getAllSheetRange = () => {
  const luckysheetConfig: any = luckysheet.getSheet();
  return [
    {
      row: [0, luckysheetConfig.data[0].length - 1],
      column: [0, luckysheetConfig.data.length - 1]
    }
  ]
};
