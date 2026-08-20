export const getFileUrl = (path: string) => {
  return (
    document.location.protocol +
    document.location.hostname +
    ':9000/bmos-product/' +
    path
  );
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
export const fileStreamDownload = (data: ArrayBuffer, type: string = '') => {
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
