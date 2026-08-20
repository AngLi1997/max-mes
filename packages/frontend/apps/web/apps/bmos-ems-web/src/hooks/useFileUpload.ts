import { message, UploadProps } from 'ant-design-vue';
import { fileUpload } from '../services';
export const customRequest: UploadProps['customRequest'] = (options: any) => {
  const formData = new FormData();
  formData.append('file', options.file);

  fileUpload(formData)
    .then((res: any) => {
      if (res.status === 200 && res.data.code === 0) {
        options.onSuccess(res.data.data as any);
      } else {
        message.error(res.data.message);
        options.onError(res);
      }
    })
    .catch((error: any) => {
      message.error(error.message);
      options.onError(error);
    });
};
