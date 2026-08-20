import { message, UploadProps } from 'ant-design-vue';
const tenMB = 10 * 1024 * 1024;
export const beforeUpload: UploadProps['beforeUpload'] = file => {
  return new Promise((resolve, reject) => {
    if (file.size > tenMB) {
      message.error(t('上传文件大小不能超过10MB'));
      reject();
    }
    resolve();
  });
};
