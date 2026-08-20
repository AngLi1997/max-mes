export const getFileUrl = (path: string) => {
  return document.location.protocol + '//' + document.location.hostname + ':9000/bmos-product/' + path;
};

export const fileUrlDownload = async (url: string, fileName?: string) => {
  try {
    const response = await fetch(url);
    const blob = await response.blob();
    const a: HTMLAnchorElement = document.createElement('a');
    a.style.display = 'none';
    const newUrl = window.URL.createObjectURL(blob);
    a.href = newUrl;
    if (fileName !== undefined) {
      a.download = fileName;
    } else {
      a.download = 'downloaded_file'; // 如果未指定文件名，使用默认文件名
    }
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(newUrl);
    document.body.removeChild(a);
    return Promise.resolve();
  } catch (error) {
    return Promise.reject(error);
  }
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
