import type { UploadChangeParam } from 'ant-design-vue';
import { FILE_STAUTS } from './enum';
export const fileUploadChange = (info: UploadChangeParam, model: any): void => {
  let resFileList = [...info.fileList];
  resFileList = resFileList.slice(-2);
  resFileList = resFileList.filter(file => {
    if (file.response) {
      file.url = file.response.url;
    }
    return file.status !== FILE_STAUTS.ERROR;
  });
  const file = resFileList.pop();

  model['fileList'] = file ? [file] : [];
  model['name'] = resFileList.pop()?.name;
};
