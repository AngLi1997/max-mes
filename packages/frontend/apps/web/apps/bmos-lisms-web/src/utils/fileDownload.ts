import { fileStreamDownload } from '@bmos/utils';

export const getFileUrl = (path: string) => {
  return document.location.protocol + document.location.hostname + ':9000/bmos-product/' + path;
};

export const fileUrlDownload = (url: string, fileName?: string) => {
  let a: HTMLAnchorElement = document.createElement('a');
  a.style.display = 'none';
  if (fileName) {
    a.download = fileName;
  }
  a.href = url;
  a.click();
};

/**
 * 批量导出
 * @param {*} data fileDownload返回的buffer数据
 * @param {String} type 文件类型
 */
export const filesStreamDownload = (data: ArrayBuffer, type: string = '') => {
  const reader = new FileReader();
  const blob = new Blob([data], { type });
  reader.readAsDataURL(blob);
  reader.addEventListener(
    'load',
    function () {
      let a: HTMLAnchorElement = document.createElement('a');
      a.style.display = 'none';
      a.href = reader.result as string;
      a.click();
    },
    false,
  );
};

export const handleFileName = (name: string) => {
  const arr = name.split('/');
  arr.splice(0, 4);
  return '/' + arr.join('/');
};

const downloadFn = async (
  data: any,
  fileName: string,
  type: string = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
) => {
  try {
    const uint8Array = new Uint8Array(data);
    const decoder = new TextDecoder();
    const jsonString = decoder.decode(uint8Array);
    const error = JSON.parse(jsonString);
    return Promise.reject(error);
  } catch (error) {
    fileStreamDownload(data, fileName, type);
  }
};

/**
 * 导出文件 - 解析接口返回的文件名 -> 校验异常信息
 * @param {*} res 文件流/接口响应
 */
export const fileDownloadFlow = async (
  res: any,
  type: string = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
) => {
  let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
  // 文件名解码
  fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
  await downloadFn(res.data, fileName, type);
};

/**
 * PDF文件预览
 * @param {*} data fileDownload返回的buffer数据
 */
export const pdfPreview = (res: any) => {
  const { data } = res;
  try {
    const uint8Array = new Uint8Array(data);
    const decoder = new TextDecoder();
    const jsonString = decoder.decode(uint8Array);
    const error = JSON.parse(jsonString);
    return Promise.reject(error);
  } catch (error) {
    const blob = new Blob([data], { type: 'application/pdf' });
    window.open(URL.createObjectURL(blob));
  }
};
